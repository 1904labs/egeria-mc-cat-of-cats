/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.archiveutilities.openconnectors.unitycatalog;

import org.odpi.openmetadata.adapters.connectors.apachekafka.control.KafkaPlaceholderProperty;
import org.odpi.openmetadata.adapters.connectors.apachekafka.integration.KafkaTopicIntegrationProvider;
import org.odpi.openmetadata.adapters.connectors.apachekafka.resource.ApacheKafkaAdminProvider;
import org.odpi.openmetadata.adapters.connectors.apachekafka.survey.SurveyApacheKafkaServerProvider;
import org.odpi.openmetadata.adapters.connectors.datastore.datafolder.DataFolderProvider;
import org.odpi.openmetadata.adapters.connectors.integration.openapis.OpenAPIMonitorIntegrationProvider;
import org.odpi.openmetadata.adapters.connectors.unitycatalog.controls.UnityCatalogDeployedImplementationType;
import org.odpi.openmetadata.adapters.connectors.unitycatalog.controls.UnityCatalogPlaceholderProperty;
import org.odpi.openmetadata.adapters.connectors.unitycatalog.controls.UnityCatalogTemplateType;
import org.odpi.openmetadata.adapters.connectors.unitycatalog.resource.OSSUnityCatalogResourceProvider;
import org.odpi.openmetadata.adapters.connectors.unitycatalog.sync.OSSUnityCatalogInsideCatalogSyncProvider;
import org.odpi.openmetadata.adapters.connectors.unitycatalog.sync.OSSUnityCatalogServerSyncProvider;
import org.odpi.openmetadata.adapters.eventbus.topic.kafka.KafkaOpenMetadataTopicProvider;
import org.odpi.openmetadata.archiveutilities.openconnectors.ContentPackDefinition;
import org.odpi.openmetadata.archiveutilities.openconnectors.GovernanceEngineDefinition;
import org.odpi.openmetadata.archiveutilities.openconnectors.IntegrationGroupDefinition;
import org.odpi.openmetadata.archiveutilities.openconnectors.RequestTypeDefinition;
import org.odpi.openmetadata.archiveutilities.openconnectors.base.ContentPackBaseArchiveWriter;
import org.odpi.openmetadata.archiveutilities.openconnectors.core.CorePackArchiveWriter;
import org.odpi.openmetadata.frameworks.openmetadata.controls.PlaceholderProperty;
import org.odpi.openmetadata.frameworks.openmetadata.controls.PlaceholderPropertyType;
import org.odpi.openmetadata.frameworks.openmetadata.mapper.PropertyFacetValidValues;
import org.odpi.openmetadata.frameworks.openmetadata.refdata.DeployedImplementationType;
import org.odpi.openmetadata.frameworks.openmetadata.refdata.DeployedImplementationTypeDefinition;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataProperty;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;
import org.odpi.openmetadata.repositoryservices.connectors.stores.archivestore.properties.OpenMetadataArchive;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Classification;

import java.util.*;

/**
 * CorePackArchiveWriter creates an open metadata archive that includes the connector type
 * information for all open connectors supplied by the egeria project.
 */
public class UnityCatalogPackArchiveWriter extends ContentPackBaseArchiveWriter
{
    /**
     * Default constructor initializes the archive.
     */
    public UnityCatalogPackArchiveWriter()
    {
        super(ContentPackDefinition.UNITY_CATALOG_CONTENT_PACK.getArchiveGUID(),
              ContentPackDefinition.UNITY_CATALOG_CONTENT_PACK.getArchiveName(),
              ContentPackDefinition.UNITY_CATALOG_CONTENT_PACK.getArchiveDescription(),
              new Date(),
              ContentPackDefinition.UNITY_CATALOG_CONTENT_PACK.getArchiveFileName(),
              new OpenMetadataArchive[]{new CorePackArchiveWriter().getOpenMetadataArchive()});
    }


    /**
     * Implemented by subclass to add the content.
     */
    @Override
    public void getArchiveContent()
    {
        /*
         * Add valid metadata values for deployedImplementationType.  The GUIDs are saved in a look-up map
         * to make it easy to link other elements to these valid values later.
         */
        for (UnityCatalogDeployedImplementationType deployedImplementationType : UnityCatalogDeployedImplementationType.values())
        {
            this.addDeployedImplementationType(deployedImplementationType.getDeployedImplementationType(),
                                               deployedImplementationType.getAssociatedTypeName(),
                                               deployedImplementationType.getQualifiedName(),
                                               deployedImplementationType.getCategory(),
                                               deployedImplementationType.getDescription(),
                                               deployedImplementationType.getWikiLink());
        }

        /*
         * Integration Connector Types may need to link to deployedImplementationType valid value element.
         * This information is in the connector provider.
         */
        archiveHelper.addConnectorType(null, new OSSUnityCatalogResourceProvider());
        archiveHelper.addConnectorType(null, new OSSUnityCatalogServerSyncProvider());
        archiveHelper.addConnectorType(null, new OSSUnityCatalogInsideCatalogSyncProvider());

        archiveHelper.addConnectorType(null, new OpenAPIMonitorIntegrationProvider());

        archiveHelper.addConnectorType(null, new ApacheKafkaAdminProvider());
        archiveHelper.addConnectorType(null, new KafkaTopicIntegrationProvider());
        archiveHelper.addConnectorType(null, new SurveyApacheKafkaServerProvider());

        /*
         * Add catalog templates
         */
        this.addSoftwareServerCatalogTemplates(ContentPackDefinition.UNITY_CATALOG_CONTENT_PACK);
        this.addUCCatalogCatalogTemplate();
        this.addUCSchemaCatalogTemplate();
        this.addUCVolumeCatalogTemplate();
        this.addUCTableCatalogTemplate();
        this.addUCFunctionCatalogTemplate();

        /*
         * Create the default integration group.
         */
        super.addIntegrationGroups(ContentPackDefinition.UNITY_CATALOG_CONTENT_PACK);
        super.addIntegrationConnectors(ContentPackDefinition.UNITY_CATALOG_CONTENT_PACK, IntegrationGroupDefinition.DEFAULT);

        /*
         * Create the default governance engines
         */
        super.createGovernanceEngines(ContentPackDefinition.UNITY_CATALOG_CONTENT_PACK);

        /*
         * Register the governance services that are going to be in the default governance engines.
         */
        super.createGovernanceServices(ContentPackDefinition.UNITY_CATALOG_CONTENT_PACK);

        /*
         * Connect the governance engines to the governance services using the request types.
         */
        super.createRequestTypes(ContentPackDefinition.UNITY_CATALOG_CONTENT_PACK);

        /*
         * Create a sample process
         */
        this.createAndSurveyServerGovernanceActionProcess("UnityCatalogServer",
                                                          UnityCatalogDeployedImplementationType.OSS_UNITY_CATALOG_SERVER.getDeployedImplementationType(),
                                                          RequestTypeDefinition.CREATE_UC_SERVER,
                                                          GovernanceEngineDefinition.UNITY_CATALOG_GOVERNANCE_ENGINE,
                                                          RequestTypeDefinition.SURVEY_UC_SERVER,
                                                          GovernanceEngineDefinition.UNITY_CATALOG_SURVEY_ENGINE);
        this.createAndCatalogServerGovernanceActionProcess("UnityCatalogServer",
                                                           UnityCatalogDeployedImplementationType.OSS_UNITY_CATALOG_SERVER.getDeployedImplementationType(),
                                                           RequestTypeDefinition.CREATE_UC_SERVER,
                                                           GovernanceEngineDefinition.UNITY_CATALOG_GOVERNANCE_ENGINE,
                                                           RequestTypeDefinition.CATALOG_UC_SERVER,
                                                           GovernanceEngineDefinition.UNITY_CATALOG_GOVERNANCE_ENGINE);

        /*
         * Saving the GUIDs means tha the guids in the archive are stable between runs of the archive writer.
         */
        archiveHelper.saveGUIDs();
        archiveHelper.saveUsedGUIDs();
    }


    private void addUCCatalogCatalogTemplate()
    {
        final String methodName = "addUCCatalogCatalogTemplate";
        final String guid       = UnityCatalogTemplateType.UC_CATALOG_TEMPLATE.getDefaultTemplateGUID();

        DeployedImplementationTypeDefinition deployedImplementationType = UnityCatalogDeployedImplementationType.OSS_UC_CATALOG;

        String               qualifiedName      = deployedImplementationType.getDeployedImplementationType() + ":" + PlaceholderProperty.SERVER_NETWORK_ADDRESS.getPlaceholder() + ":" + UnityCatalogPlaceholderProperty.CATALOG_NAME.getPlaceholder();
        List<Classification> classifications    = new ArrayList<>();

        classifications.add(archiveHelper.getTemplateClassification(deployedImplementationType.getDeployedImplementationType() + " template",
                                                                    UnityCatalogTemplateType.UC_CATALOG_TEMPLATE.getTemplateDescription(),
                                                                    "V1.0",
                                                                    null, methodName));

        archiveHelper.setGUID(qualifiedName, guid);
        String capabilityGUID = archiveHelper.addSoftwareCapability(deployedImplementationType.getAssociatedTypeName(),
                                                                    qualifiedName,
                                                                    UnityCatalogPlaceholderProperty.CATALOG_NAME.getPlaceholder(),
                                                                    PlaceholderProperty.DESCRIPTION.getPlaceholder(),
                                                                    deployedImplementationType.getDeployedImplementationType(),
                                                                    PlaceholderProperty.VERSION_IDENTIFIER.getPlaceholder(),
                                                                    null,
                                                                    null,
                                                                    null,
                                                                    null,
                                                                    classifications,
                                                                    null,
                                                                    deployedImplementationType.getAssociatedTypeName(),
                                                                    OpenMetadataType.SOFTWARE_CAPABILITY.typeName);
        assert(guid.equals(capabilityGUID));

        String deployedImplementationTypeGUID = archiveHelper.getGUID(deployedImplementationType.getQualifiedName());

        archiveHelper.addCatalogTemplateRelationship(deployedImplementationTypeGUID, capabilityGUID);

        archiveHelper.addPlaceholderProperties(capabilityGUID,
                                               deployedImplementationType.getAssociatedTypeName(),
                                               OpenMetadataType.SOFTWARE_CAPABILITY.typeName,
                                               UnityCatalogPlaceholderProperty.getCatalogPlaceholderPropertyTypes());
    }


    private void addUCSchemaCatalogTemplate()
    {
        final String methodName = "addUCSchemaCatalogTemplate";
        final String guid       = UnityCatalogTemplateType.UC_SCHEMA_TEMPLATE.getDefaultTemplateGUID();

        DeployedImplementationTypeDefinition deployedImplementationType = UnityCatalogDeployedImplementationType.OSS_UC_SCHEMA;
        String                               fullName                   = UnityCatalogPlaceholderProperty.CATALOG_NAME.getPlaceholder() + "."
                                                                        + UnityCatalogPlaceholderProperty.SCHEMA_NAME.getPlaceholder();
        String                               qualifiedName              = deployedImplementationType.getDeployedImplementationType() + ":"
                                                                        + PlaceholderProperty.SERVER_NETWORK_ADDRESS.getPlaceholder() + ":"
                                                                        + fullName;

        Map<String, Object>  extendedProperties = new HashMap<>();
        List<Classification> classifications    = new ArrayList<>();

        extendedProperties.put(OpenMetadataProperty.DEPLOYED_IMPLEMENTATION_TYPE.name, deployedImplementationType.getDeployedImplementationType());
        extendedProperties.put(OpenMetadataProperty.RESOURCE_NAME.name, fullName);

        classifications.add(archiveHelper.getTemplateClassification(deployedImplementationType.getDeployedImplementationType() + " template",
                                                                    UnityCatalogTemplateType.UC_SCHEMA_TEMPLATE.getTemplateDescription(),
                                                                    "V1.0",
                                                                    null,
                                                                    methodName));

        archiveHelper.setGUID(qualifiedName, guid);
        String assetGUID = archiveHelper.addAsset(deployedImplementationType.getAssociatedTypeName(),
                                                  qualifiedName,
                                                  UnityCatalogPlaceholderProperty.SCHEMA_NAME.getPlaceholder(),
                                                  PlaceholderProperty.VERSION_IDENTIFIER.getPlaceholder(),
                                                  PlaceholderProperty.DESCRIPTION.getPlaceholder(),
                                                  null,
                                                  extendedProperties,
                                                  classifications);
        assert(guid.equals(assetGUID));

        String deployedImplementationTypeGUID = archiveHelper.getGUID(deployedImplementationType.getQualifiedName());

        archiveHelper.addCatalogTemplateRelationship(deployedImplementationTypeGUID, assetGUID);

        archiveHelper.addPlaceholderProperties(assetGUID,
                                               deployedImplementationType.getAssociatedTypeName(),
                                               OpenMetadataType.ASSET.typeName,
                                               UnityCatalogPlaceholderProperty.getSchemaPlaceholderPropertyTypes());
    }


    private void addUCVolumeCatalogTemplate()
    {
        final String methodName = "addUCVolumeCatalogTemplate";
        final String guid       = UnityCatalogTemplateType.UC_VOLUME_TEMPLATE.getDefaultTemplateGUID();

        DeployedImplementationTypeDefinition deployedImplementationType = UnityCatalogDeployedImplementationType.OSS_UC_VOLUME;
        String                     fullName                             = UnityCatalogPlaceholderProperty.CATALOG_NAME.getPlaceholder() + "."
                                                                        + UnityCatalogPlaceholderProperty.SCHEMA_NAME.getPlaceholder() + "."
                                                                        + UnityCatalogPlaceholderProperty.VOLUME_NAME.getPlaceholder();
        String                     qualifiedName                        = deployedImplementationType.getDeployedImplementationType() + ":"
                                                                        + PlaceholderProperty.SERVER_NETWORK_ADDRESS.getPlaceholder() + ":"
                                                                        + fullName;
        String connectorTypeGUID = new DataFolderProvider().getConnectorType().getGUID();

        Map<String, Object>  extendedProperties   = new HashMap<>();
        Map<String, String>  facetProperties      = new HashMap<>();
        List<Classification> classifications      = new ArrayList<>();

        extendedProperties.put(OpenMetadataProperty.DEPLOYED_IMPLEMENTATION_TYPE.name, deployedImplementationType.getDeployedImplementationType());
        extendedProperties.put(OpenMetadataProperty.PATH_NAME.name, UnityCatalogPlaceholderProperty.STORAGE_LOCATION.getPlaceholder());
        extendedProperties.put(OpenMetadataProperty.RESOURCE_NAME.name, fullName);

        facetProperties.put(UnityCatalogPlaceholderProperty.VOLUME_TYPE.getName(), UnityCatalogPlaceholderProperty.VOLUME_TYPE.getPlaceholder());
        facetProperties.put(UnityCatalogPlaceholderProperty.STORAGE_LOCATION.getName(), UnityCatalogPlaceholderProperty.STORAGE_LOCATION.getPlaceholder());

        classifications.add(archiveHelper.getTemplateClassification(deployedImplementationType.getDeployedImplementationType() + " template",
                                                                    UnityCatalogTemplateType.UC_VOLUME_TEMPLATE.getTemplateDescription(),
                                                                    "V1.0",
                                                                    null,
                                                                    methodName));

        archiveHelper.setGUID(qualifiedName, guid);
        String assetGUID = archiveHelper.addAsset(deployedImplementationType.getAssociatedTypeName(),
                                                  qualifiedName,
                                                  UnityCatalogPlaceholderProperty.VOLUME_NAME.getPlaceholder(),
                                                  PlaceholderProperty.VERSION_IDENTIFIER.getPlaceholder(),
                                                  PlaceholderProperty.DESCRIPTION.getPlaceholder(),
                                                  null,
                                                  extendedProperties,
                                                  classifications);
        assert(guid.equals(assetGUID));

        archiveHelper.addPropertyFacet(assetGUID,
                                       deployedImplementationType.getAssociatedTypeName(),
                                       OpenMetadataType.ASSET.typeName,
                                       qualifiedName,
                                       PropertyFacetValidValues.UNITY_CATALOG_SOURCE_VALUE,
                                       PropertyFacetValidValues.UNITY_CATALOG_SCHEMA_VERSION_VALUE,
                                       PropertyFacetValidValues.VENDOR_PROPERTIES_DESCRIPTION_VALUE,
                                       facetProperties);

        String endpointGUID = archiveHelper.addEndpoint(assetGUID,
                                                        deployedImplementationType.getAssociatedTypeName(),
                                                        OpenMetadataType.ASSET.typeName,
                                                        qualifiedName + ":Endpoint",
                                                        fullName + " endpoint",
                                                        null,
                                                        UnityCatalogPlaceholderProperty.STORAGE_LOCATION.getPlaceholder(),
                                                        null,
                                                        null);

        String connectionGUID = archiveHelper.addConnection(qualifiedName + ":Connection",
                                                            fullName + " connection",
                                                            null,
                                                            null,
                                                            null,
                                                            null,
                                                            null,
                                                            null,
                                                            null,
                                                            connectorTypeGUID,
                                                            endpointGUID,
                                                            assetGUID,
                                                            deployedImplementationType.getAssociatedTypeName(),
                                                            OpenMetadataType.ASSET.typeName);

        archiveHelper.addConnectionForAsset(assetGUID, null, connectionGUID);

        String deployedImplementationTypeGUID = archiveHelper.getGUID(deployedImplementationType.getQualifiedName());

        archiveHelper.addCatalogTemplateRelationship(deployedImplementationTypeGUID, assetGUID);

        archiveHelper.addPlaceholderProperties(assetGUID,
                                               deployedImplementationType.getAssociatedTypeName(),
                                               OpenMetadataType.ASSET.typeName,
                                               UnityCatalogPlaceholderProperty.getVolumePlaceholderPropertyTypes());
    }


    private void addUCTableCatalogTemplate()
    {
        final String methodName = "addUCTableCatalogTemplate";
        final String guid       = UnityCatalogTemplateType.UC_TABLE_TEMPLATE.getDefaultTemplateGUID();

        DeployedImplementationTypeDefinition deployedImplementationType = UnityCatalogDeployedImplementationType.OSS_UC_TABLE;
        String                               fullName                   = UnityCatalogPlaceholderProperty.CATALOG_NAME.getPlaceholder() + "."
                                                                        + UnityCatalogPlaceholderProperty.SCHEMA_NAME.getPlaceholder() + "."
                                                                        + UnityCatalogPlaceholderProperty.TABLE_NAME.getPlaceholder();
        String                               qualifiedName              = deployedImplementationType.getDeployedImplementationType() + ":"
                                                                        + PlaceholderProperty.SERVER_NETWORK_ADDRESS.getPlaceholder() + ":"
                                                                        + fullName;

        Map<String, Object>  extendedProperties   = new HashMap<>();
        Map<String, Object>  folderProperties     = new HashMap<>();
        Map<String, String>  facetProperties      = new HashMap<>();
        List<Classification> classifications      = new ArrayList<>();

        extendedProperties.put(OpenMetadataProperty.DEPLOYED_IMPLEMENTATION_TYPE.name, deployedImplementationType.getDeployedImplementationType());
        extendedProperties.put(OpenMetadataProperty.RESOURCE_NAME.name, fullName);

        folderProperties.put(OpenMetadataProperty.PATH_NAME.name, UnityCatalogPlaceholderProperty.STORAGE_LOCATION.getPlaceholder());

        facetProperties.put(UnityCatalogPlaceholderProperty.STORAGE_LOCATION.getName(), UnityCatalogPlaceholderProperty.STORAGE_LOCATION.getPlaceholder());
        facetProperties.put(UnityCatalogPlaceholderProperty.TABLE_TYPE.getName(), UnityCatalogPlaceholderProperty.TABLE_TYPE.getPlaceholder());

        classifications.add(archiveHelper.getTemplateClassification(deployedImplementationType.getDeployedImplementationType() + " template",
                                                                    UnityCatalogTemplateType.UC_TABLE_TEMPLATE.getTemplateDescription(),
                                                                    "V1.0",
                                                                    null,
                                                                    methodName));

        classifications.add(archiveHelper.getDataAssetEncodingClassification(UnityCatalogPlaceholderProperty.DATA_SOURCE_FORMAT.getPlaceholder(),
                                                                             null,
                                                                             null,
                                                                             null));

        archiveHelper.setGUID(qualifiedName, guid);
        String assetGUID = archiveHelper.addAsset(deployedImplementationType.getAssociatedTypeName(),
                                                  qualifiedName,
                                                  UnityCatalogPlaceholderProperty.TABLE_NAME.getPlaceholder(),
                                                  PlaceholderProperty.VERSION_IDENTIFIER.getPlaceholder(),
                                                  PlaceholderProperty.DESCRIPTION.getPlaceholder(),
                                                  null,
                                                  extendedProperties,
                                                  classifications);
        assert(guid.equals(assetGUID));

        extendedProperties   = new HashMap<>();
        extendedProperties.put("file:///" + OpenMetadataProperty.RESOURCE_NAME.name, UnityCatalogPlaceholderProperty.STORAGE_LOCATION.getPlaceholder());
        String folderGUID = archiveHelper.addAnchoredAsset(OpenMetadataType.DATA_FOLDER.typeName,
                                                           assetGUID,
                                                           deployedImplementationType.getAssociatedTypeName(),
                                                           OpenMetadataType.ASSET.typeName,
                                                           qualifiedName + "_storageLocation",
                                                           fullName + "_storageLocation",
                                                           PlaceholderProperty.VERSION_IDENTIFIER.getPlaceholder(),
                                                           "Location of files for table " + fullName,
                                                           null,
                                                           folderProperties,
                                                           classifications);

        archiveHelper.addDataContentForDataSet(folderGUID, assetGUID);

        archiveHelper.addPropertyFacet(assetGUID,
                                       deployedImplementationType.getAssociatedTypeName(),
                                       OpenMetadataType.ASSET.typeName,
                                       qualifiedName,
                                       PropertyFacetValidValues.UNITY_CATALOG_SOURCE_VALUE,
                                       PropertyFacetValidValues.UNITY_CATALOG_SCHEMA_VERSION_VALUE,
                                       PropertyFacetValidValues.VENDOR_PROPERTIES_DESCRIPTION_VALUE,
                                       facetProperties);

        String deployedImplementationTypeGUID = archiveHelper.getGUID(deployedImplementationType.getQualifiedName());

        archiveHelper.addCatalogTemplateRelationship(deployedImplementationTypeGUID, assetGUID);

        archiveHelper.addPlaceholderProperties(assetGUID,
                                               deployedImplementationType.getAssociatedTypeName(),
                                               OpenMetadataType.ASSET.typeName,
                                               UnityCatalogPlaceholderProperty.getTablePlaceholderPropertyTypes());
    }


    private void addUCFunctionCatalogTemplate()
    {
        final String methodName = "addUCFunctionCatalogTemplate";
        final String guid       = UnityCatalogTemplateType.UC_FUNCTION_TEMPLATE.getDefaultTemplateGUID();

        DeployedImplementationTypeDefinition deployedImplementationType = UnityCatalogDeployedImplementationType.OSS_UC_FUNCTION;
        String                     fullName                             = UnityCatalogPlaceholderProperty.CATALOG_NAME.getPlaceholder() + "."
                                                                        + UnityCatalogPlaceholderProperty.SCHEMA_NAME.getPlaceholder() + "."
                                                                        + UnityCatalogPlaceholderProperty.FUNCTION_NAME.getPlaceholder();
        String                     qualifiedName                        = deployedImplementationType.getDeployedImplementationType() + ":"
                                                                        + PlaceholderProperty.SERVER_NETWORK_ADDRESS.getPlaceholder() + ":"
                                                                        + fullName;

        Map<String, Object>  extendedProperties = new HashMap<>();
        List<Classification> classifications    = new ArrayList<>();

        extendedProperties.put(OpenMetadataProperty.DEPLOYED_IMPLEMENTATION_TYPE.name, deployedImplementationType.getDeployedImplementationType());
        extendedProperties.put(OpenMetadataProperty.RESOURCE_NAME.name, fullName);

        classifications.add(archiveHelper.getTemplateClassification(deployedImplementationType.getDeployedImplementationType() + " template",
                                                                    UnityCatalogTemplateType.UC_FUNCTION_TEMPLATE.getTemplateDescription(),
                                                                    "V1.0",
                                                                    null,
                                                                    methodName));

        archiveHelper.setGUID(qualifiedName, guid);
        String assetGUID = archiveHelper.addAsset(deployedImplementationType.getAssociatedTypeName(),
                                                  qualifiedName,
                                                  UnityCatalogPlaceholderProperty.FUNCTION_NAME.getPlaceholder(),
                                                  PlaceholderProperty.VERSION_IDENTIFIER.getPlaceholder(),
                                                  PlaceholderProperty.DESCRIPTION.getPlaceholder(),
                                                  null,
                                                  extendedProperties,
                                                  classifications);
        assert(guid.equals(assetGUID));

        String deployedImplementationTypeGUID = archiveHelper.getGUID(deployedImplementationType.getQualifiedName());

        archiveHelper.addCatalogTemplateRelationship(deployedImplementationTypeGUID, assetGUID);

        archiveHelper.addPlaceholderProperties(assetGUID,
                                               deployedImplementationType.getAssociatedTypeName(),
                                               OpenMetadataType.ASSET.typeName,
                                               UnityCatalogPlaceholderProperty.getFunctionPlaceholderPropertyTypes());
    }


    /**
     * Main program to initiate the archive writer for the connector types for data store connectors supported by
     * the Egeria project.
     *
     * @param args ignored
     */
    public static void main(String[] args)
    {
        try
        {
            UnityCatalogPackArchiveWriter archiveWriter = new UnityCatalogPackArchiveWriter();
            archiveWriter.writeOpenMetadataArchive();
        }
        catch (Exception error)
        {
            System.err.println("Exception: " + error);
            System.exit(-1);
        }
    }
}