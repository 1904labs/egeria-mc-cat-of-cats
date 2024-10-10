/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.adapters.connectors.mcmockapi.integration;

//import org.odpi.openmetadata.accessservices.assetmanager.metadataelements.ValidValueElement;
//import org.odpi.openmetadata.adapters.connectors.apacheatlas.integration.ffdc.AtlasIntegrationAuditCode;
//import org.odpi.openmetadata.adapters.connectors.apacheatlas.integration.ffdc.AtlasIntegrationErrorCode;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.odpi.openmetadata.adapters.connectors.apacheatlas.integration.ffdc.AtlasIntegrationAuditCode;
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
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class MCMockupConnector extends CatalogIntegratorConnector {
//public class MCMockupConnector {
    private String                                         targetRootURL                             = "http://localhost:8080/";
    private CatalogIntegratorContext                       myContext                                 = null;
    //protected final DataAssetExchangeService   dataAssetExchangeService                              = null;
    protected DataAssetExchangeService dataAssetExchangeService                                      = null;

    // not sure where this will get called. ApacheAtlasIntegrationConnector doesn't seem to have an explicit constructor

    /*
    public MCMockupConnector(CatalogIntegratorContext myContext)
    {
        this.targetRootURL = "https://localhost:8080/";
        this.myContext = myContext;
        try {
            this.dataAssetExchangeService = myContext.getDataAssetExchangeService(); //not sure why it doesn't like this
        }
        //catch (UserNotAuthorizedException e)
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
     */


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
    //public void refresh() throws Exception
    {
        final String methodName = "refresh";


        myContext = super.getContext();
        try {
            dataAssetExchangeService = myContext.getDataAssetExchangeService();
        }
        catch (UserNotAuthorizedException e)
        //catch (Exception e)
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
                String resourceName = resource.get("resourceName").asText();
                Map<String, String> additionalProperties = new HashMap<>();
                JsonNode columnsNode = resource.get("columns");
                if (!columnsNode.isEmpty()) {
                    ArrayList<String> columns = new ArrayList<String>();
                    for (JsonNode element : columnsNode) {
                        String value = element.asText();
                        columns.add(value);
                    }
                    additionalProperties.put("Columns", columns.toString());
                }

                additionalProperties.put("Data Owner", resource.get("dataOwner").asText());
                additionalProperties.put("Data Steward", resource.get("dataSteward").asText());
                additionalProperties.put("Data Domain", resource.get("compositeDataDomain").asText());
                additionalProperties.put("External Resource ID", resource.get("id").asText());
                additionalProperties.put("External Instance Last Update Time", lastModifiedDate.toString());
                additionalProperties.put("External Identifier Name", "MC Informatica Data Catalog ID");


                externalIdentifierProperties.setExternalIdentifier(resource.get("id").asText());
                externalIdentifierProperties.setExternalInstanceLastUpdateTime(lastModifiedDate);
                externalIdentifierProperties.setExternalIdentifierName("MC Informatica Data Catalog ID");

                dataAssetProperties.setQualifiedName(myContext.getMetadataSourceQualifiedName() + ":MC-Mock-API:" + resource.get("id").asText());
                dataAssetProperties.setDisplayName(resourceName);
                dataAssetProperties.setName(resourceName);
                dataAssetProperties.setDisplayDescription(resource.get("businessDescription").asText());
                dataAssetProperties.setResourceDescription(resource.get("resourceType").asText());

                //this is ORACLE in the resource coming from the Mock API
                //but i think Egeria is looking for something like String
                //dataAssetProperties.setTypeName(resource.get("resourceType").asText());
                dataAssetProperties.setTypeName("DataAsset");
                dataAssetProperties.setAdditionalProperties(additionalProperties);
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
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            System.out.println("response body: ");
            System.out.println(response.body());
            return response.body();
        }
        catch (Exception e)
        {
            System.out.println("Error sending the request to the API.");
            e.printStackTrace();
        }

        return "Todo";
    }

    //this should only get called when I'm testing
    public static void main(String[] args) {
        System.out.println("main method");
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
