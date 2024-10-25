/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.adapters.connectors.mcmockapi.integration;

//import org.odpi.openmetadata.accessservices.assetmanager.metadataelements.ValidValueElement;
//import org.odpi.openmetadata.adapters.connectors.apacheatlas.integration.ffdc.AtlasIntegrationAuditCode;
//import org.odpi.openmetadata.adapters.connectors.apacheatlas.integration.ffdc.AtlasIntegrationErrorCode;
import org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectorCheckedException;
//import org.odpi.openmetadata.frameworks.openmetadata.enums.PermittedSynchronization;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.integrationservices.catalog.connector.CatalogIntegratorConnector;
import org.odpi.openmetadata.integrationservices.catalog.connector.CatalogIntegratorContext;
import org.odpi.openmetadata.integrationservices.catalog.connector.DataAssetExchangeService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.DataAssetProperties;
import org.odpi.openmetadata.frameworks.governanceaction.properties.ExternalIdentifierProperties;

import java.util.List;
import java.util.HashMap;
import java.util.Map;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class MCMockupConnector extends CatalogIntegratorConnector {
//public class MCMockupConnector {
    private String                                         targetRootURL                             = "http://mc-mock-api:8080/";
    //private String                                         targetRootURL                             = "http://localhost:8080/";
    private CatalogIntegratorContext                       myContext                                 = null;
    //protected final DataAssetExchangeService   dataAssetExchangeService                              = null;
    protected DataAssetExchangeService dataAssetExchangeService                                      = null;

    // not sure where this will get called. ApacheAtlasIntegrationConnector doesn't seem to have an explicit constructor


/*
    public MCMockupConnector()
    {
        this.targetRootURL = "http://localhost:8080/";
    }

 */


    /* ==============================================================================
     * Standard methods that trigger activity.
     */

    /**
     * Requests that the connector does a comparison of the metadata in the third party technology and open metadata repositories.
     * Refresh is called when the integration connector first starts and then at intervals defined in the connector's configuration
     * as well as any external REST API calls to explicitly refresh the connector.
     *
     * @throws ConnectorCheckedException there is a problem with the connector.  It is not able to refresh the metadata.
     */
    @Override
    public void refresh() throws ConnectorCheckedException
    {
        final String methodName = "refresh";


        myContext = super.getContext();
        try {
            dataAssetExchangeService = myContext.getDataAssetExchangeService();
        }
        catch (UserNotAuthorizedException e)
        {
            e.printStackTrace();
        }


        // hit the mock REST API and get info for all databases
        // create externalIdentifierProperties and DataAssetProperties from mock metadata
        // call DataAssetExchangeService to create the corresponding asset in Egeria

        // JSON string
        String resourceInfo = getResourceInfoFromMockAPI();

        ObjectMapper mapper = new ObjectMapper();
        try {
            JsonNode node = mapper.readTree(resourceInfo);
            // json array of resources returned by the mock API
            JsonNode hits = node.get("hits");
            int len = hits.size();
            for (int i = 0; i < len; i++) {
                JsonNode resource = hits.get(i);

                ExternalIdentifierProperties externalIdentifierProperties = new ExternalIdentifierProperties();
                DataAssetProperties dataAssetProperties = new DataAssetProperties();

                String dateString = resource.get("lastModifiedDate").asText();
                SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
                Date lastModifiedDate = formatter.parse(dateString);
                String resourceName = resource.get("name").asText();
                Map<String, String> additionalProperties = new HashMap<>();
                Map<String, Object> extendedProperties = new HashMap<>();
                JsonNode tablesNode = resource.get("tables");

                // just plopping everything in the tables list into an additional property
                if (!tablesNode.isEmpty()) {
                    System.out.println("TablesNode: ");
                    additionalProperties.put("Tables", tablesNode.toString());
                }

                // extended properties. should be searchable
                extendedProperties.put("schema", resource.get("schema").asText());
                extendedProperties.put("catalog", resource.get("catalog").asText());
                extendedProperties.put("verified", resource.get("verified").asText());
                extendedProperties.put("verifiedBy", resource.get("verifiedBy").asText());
                extendedProperties.put("verifiedDate", resource.get("verifiedDate").asText());
                extendedProperties.put("subjectMatterExperts", resource.get("subjectMatterExperts").toString());
                extendedProperties.put("externalResourceId", resource.get("id").asText());
                extendedProperties.put("businessDescription", resource.get("businessDescription").asText());
                extendedProperties.put("externalInstanceUpdateTime", lastModifiedDate.toString());

                // not searchable at the moment
                additionalProperties.put("Data Owner", resource.get("dataOwner").toString());
                additionalProperties.put("Data Steward", resource.get("dataSteward").asText());
                additionalProperties.put("Tags", resource.get("tags").toString());
                additionalProperties.put("Data Domain", resource.get("compositeDataDomain").asText());
                additionalProperties.put("External Identifier Name", "MC Informatica Data Catalog ID");


                externalIdentifierProperties.setExternalIdentifier(resource.get("id").asText());
                externalIdentifierProperties.setExternalInstanceLastUpdateTime(lastModifiedDate);
                externalIdentifierProperties.setExternalIdentifierName("MC Informatica Data Catalog ID");

                dataAssetProperties.setQualifiedName(myContext.getMetadataSourceQualifiedName() + ":MC-Mock-API:" + resource.get("id").asText());
                dataAssetProperties.setDisplayName(resourceName);
                dataAssetProperties.setName(resourceName);
                dataAssetProperties.setDisplayDescription(resource.get("businessDescription").asText());
                dataAssetProperties.setResourceDescription(resource.get("resourceType").asText());
                //doesn't like this for some reason
                //dataAssetProperties.setDeployedImplementationType(resource.get("resourceType").asText());


                //dataAssetProperties.setTypeName("DataAsset");
                dataAssetProperties.setTypeName("DemoAsset");
                dataAssetProperties.setAdditionalProperties(additionalProperties);
                dataAssetProperties.setExtendedProperties(extendedProperties);
                System.out.println("Data Asset Properties: ");
                System.out.println(dataAssetProperties.toString());


                String egeriaDatabaseGUID = dataAssetExchangeService.createDataAsset(true,
                        externalIdentifierProperties,
                        dataAssetProperties);
                System.out.println("Asset GUID: ");
                System.out.println(egeriaDatabaseGUID);



                /*
                auditLog.logMessage(methodName,
                        AtlasIntegrationAuditCode.CREATING_EGERIA_ENTITY.getMessageDefinition(connectorName,
                                egeriaDatabaseTypeName,
                                egeriaDatabaseGUID,
                                resource.get("resourceType").asText(),
                                resource.get("id").asText()));

                 */


            }
        }
        //catch (JsonProcessingException e) {
        catch (Exception e) {
            e.printStackTrace();
            return;
        }

    }

    /**
     * Hits the Mock API and retrieves all resource info
     *
     * @throws Exception of some kind if it doesn't work?
     */
    public String getResourceInfoFromMockAPI()
    {
        HttpClient client = HttpClient.newBuilder().build();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(this.targetRootURL + "resource-descriptions?resource_name=*"))
                .build();
        System.out.println("HTTP Request: ");
        System.out.println(request.uri());
        System.out.println("Request headers:");
        System.out.println(request.headers());

        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            System.out.println("Response status code: " + response.statusCode());
            System.out.println("response body: ");
            System.out.println(response.body());
            return response.body();
        }
        catch (Exception e)
        {
            System.out.println("Error sending the request to the API.");
            e.printStackTrace();
            System.out.println("Exception message: " + e.getMessage());
            System.out.println("Exception class: " + e.getClass().getName());
            System.out.println("Exception cause: " + e.getCause());
        }

        return "Todo";
    }

    //this should only get called when I'm testing
    public static void main(String[] args) {
        MCMockupConnector conn = new MCMockupConnector();
        //conn.getResourceInfoFromMockAPI();
        try {
            conn.refresh();
        }
        catch (Exception e) {
            e.printStackTrace();
        }


    }


}
