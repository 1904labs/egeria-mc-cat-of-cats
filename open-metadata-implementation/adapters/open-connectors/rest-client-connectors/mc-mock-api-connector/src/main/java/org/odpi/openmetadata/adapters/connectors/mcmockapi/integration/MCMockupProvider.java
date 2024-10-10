/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.adapters.connectors.mcmockapi.integration;

import org.odpi.openmetadata.frameworks.auditlog.AuditLogReportingComponent;
import org.odpi.openmetadata.frameworks.auditlog.ComponentDevelopmentStatus;
import org.odpi.openmetadata.frameworks.connectors.controls.SupportedTechnologyType;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.ConnectorType;
import org.odpi.openmetadata.frameworks.openmetadata.refdata.DeployedImplementationType;
import org.odpi.openmetadata.frameworks.openmetadata.refdata.DeployedImplementationTypeDefinition;
//import org.odpi.openmetadata.adapters.connectors.apacheatlas.controls.AtlasDeployedImplementationType;
import org.odpi.openmetadata.frameworks.integration.controls.CatalogTargetType;
import org.odpi.openmetadata.frameworks.integration.connectors.IntegrationConnectorProvider;

import java.util.ArrayList;
import java.util.List;

/**
 * MCMockupProvider is the connector provider for the MC Mock API integration connector
 */
public class MCMockupProvider extends IntegrationConnectorProvider
{
    /*
     * Unique identifier of the connector for the audit log.
     */
    private static final int    connectorComponentId   = 1904;

    /*
     * Unique identifier for the connector type.
     */
    private static final String connectorTypeGUID      = "b936d718-9f5a-460b-a879-88c750b7cf3e";

    /*
     * Descriptive information about the connector for the connector type and audit log.
     */
    private static final String connectorQualifiedName = "Egeria:IntegrationConnector:Catalog:MCMockAPI";
    private static final String connectorDisplayName   = "MC Mock API Integration Connector";
    private static final String connectorDescription   = "Connector extracts data assets and glossary terms from the MC Mock API";
    private static final String connectorWikiPage      = "no";

    /*
     * Class of the connector.
     */
    private static final String connectorClassName       = "org.odpi.openmetadata.adapters.connectors.mcmockapi.integration.MCMockupConnector";


    public static final String OPEN_METADATA_TYPES_POLICY_CONFIGURATION_PROPERTY = "openMetadataTypesPolicy";

    /**
     * Static literal for setting openMetadataTypesPolicy to "ON_DEMAND".
     */
    public static final String ON_DEMAND_OPEN_METADATA_TYPES_CONFIGURATION_PROPERTY_VALUE = "ON_DEMAND";

    /**
     * Static literal for setting openMetadataTypesPolicy to "ALL".
     */
    public static final String ALL_OPEN_METADATA_TYPES_CONFIGURATION_PROPERTY_VALUE       = "ALL";

    /**
     * If openMetadataTypesPolicy is set to ALL, it is possible to skip the definition of particular open metadata
     * types in Apache Atlas by listing these types in the ignoreOpenMetadataTypes property.  For example,
     * to avoid adding the types for the Anchors and LatestChange classifications, set ignoreOpenMetadataTypes to
     * "[Anchors, LatestChange]".
     */
    public static final String IGNORE_OPEN_METADATA_TYPES_CONFIGURATION_PROPERTY = "ignoreOpenMetadataTypes";

    /**
     * Static literal for setting ignoreOpenMetadataTypes to "*" which means the same as if it is not set.
     */
    public static final String IGNORE_ALL_OPEN_METADATA_TYPES_CONFIGURATION_PROPERTY_VALUE = "*";

    /**
     * The configuration property name used to supply the qualified name of an Egeria glossary to synchronize with
     * Apache Atlas.  If this value is null, all Egeria originated glossaries are copied to Apache Atlas.
     */
    public static final String EGERIA_GLOSSARY_QUALIFIED_NAME_CONFIGURATION_PROPERTY = "egeriaGlossaryQualifiedName";

    /**
     * The configuration property name used to supply the name of the Atlas Glossary to copy into the open metadata
     * ecosystem.  If this value is null, all Apache Atlas originated glossaries are copied into the open metadata ecosystem.
     */
    public static final String MC_Mockup_GLOSSARY_NAME_CONFIGURATION_PROPERTY = "MCMockupGlossaryName";

    /**
     * The name of the configuration property to control how this connector maps open metadata informal tags to
     * Apache Atlas.  If this value is null, or set to an invalid value, informal tags are added as related entities.  Otherwise, it can be
     * set to LABELS (mapping informal tags to labels on the related entity), CLASSIFICATIONS (mapping informal tags to classifications)
     * or ENTITIES (mapping informal tags to related entities).
     * <p>
     *     To turn off mapping of informal tags, specify ENTITIES in this property and list AttachedInformalTag in the IgnoreRelationshipList.
     * </p>
     */
    public static final String INFORMAL_TAGS_MAPPING_POLICY_CONFIGURATION_PROPERTY   = "informalTagsMappingPolicy";

    /**
     * Static literal for setting informalTagsMappingPolicy to "LABELS".
     */
    public static final String INFORMAL_TAGS_MAP_TO_LABELS_CONFIGURATION_PROPERTY_VALUE          = "LABELS";

    /**
     * Static literal for setting informalTagsMappingPolicy to "CLASSIFICATIONS".
     */
    public static final String INFORMAL_TAGS_MAP_TO_CLASSIFICATIONS_CONFIGURATION_PROPERTY_VALUE = "CLASSIFICATIONS";

    /**
     * Static literal for setting informalTagsMappingPolicy to "ENTITIES".
     */
    public static final String INFORMAL_TAGS_MAP_TO_ENTITIES_CONFIGURATION_PROPERTY_VALUE        = "ENTITIES";

    /**
     * Static literal for setting informalTagsMappingPolicy to "NONE".
     */
    public static final String INFORMAL_TAGS_NO_MAPPING_CONFIGURATION_PROPERTY_VALUE             = "NONE";

    /**
     * The classificationReferenceSetName configuration property is used to supply the qualified name of a valid values set that lists the names and descriptions
     * of classifications to exchange with Apache Atlas.  This is used to establish a standard set of classifications in an
     * Apache Atlas server - or across multiple Apache Atlas servers.
     */
    public static final String CLASSIFICATION_REFERENCE_SET_NAME_CONFIGURATION_PROPERTY     = "classificationReferenceSetName";

    /**
     * The classificationReferenceSetPolicy configuration property determines the direction of the exchange of classifications from
     * Apache Atlas and the classification reference set.
     */
    public static final String CLASSIFICATION_REFERENCE_SET_POLICY_CONFIGURATION_PROPERTY   = "classificationReferenceSetPolicy";

    /**
     * Static literal for setting classificationReferenceSetPolicy to "TO_MC_MOCKUP".
     */
    public static final String CLASSIFICATION_REFERENCE_SET_TO_MC_MOCKUP_CONFIGURATION_PROPERTY_VALUE = "TO_MC_MOCKUP";

    /**
     * Static literal for setting classificationReferenceSetPolicy to "FROM_MC_MOCKUP".
     */
    public static final String CLASSIFICATION_REFERENCE_SET_FROM_MC_MOCKUP_CONFIGURATION_PROPERTY_VALUE = "FROM_MC_MOCKUP";

    /**
     * Static literal for setting classificationReferenceSetPolicy to "BOTH_WAYS".
     */
    public static final String CLASSIFICATION_REFERENCE_SET_BOTH_WAYS_CONFIGURATION_PROPERTY_VALUE  = "BOTH_WAYS";


    /**
     * The relatedClassificationPolicy configuration property determines if a classification added to an open metadata entity that is synchronized in
     * Apache Atlas should be added to the Apache Atlas entity as business metadata.
     */
    public static final String RELATED_CLASSIFICATION_POLICY_CONFIGURATION_PROPERTY   = "relatedClassificationPolicy";

    /**
     * The relatedEntityPolicy configuration property determines if an entity related to an open metadata entity that is synchronized in
     * Apache Atlas should be added to the Apache Atlas entity as a related entity.
     */
    public static final String RELATED_ENTITY_POLICY_CONFIGURATION_PROPERTY   = "relatedEntityPolicy";

    /**
     * Static literal for setting relatedClassificationPolicy or relatedEntityPolicy to "ALL".
     */
    public static final String ALL_RELATED_ELEMENT_CONFIGURATION_PROPERTY_VALUE      = "ALL";

    /**
     * Static literal for setting relatedClassificationPolicy or relatedEntityPolicy to "SELECTED".
     */
    public static final String SELECTED_RELATED_ELEMENT_CONFIGURATION_PROPERTY_VALUE = "SELECTED";

    /**
     * Static literal for setting relatedClassificationPolicy or relatedEntityPolicy to "NONE".
     */
    public static final String NONE_RELATED_ELEMENT_CONFIGURATION_PROPERTY_VALUE     = "NONE";

    /**
     * The relatedClassificationIgnoreList configuration property is used to list the classification names that should not be copied from the
     * open metadata ecosystem to Apache Atlas if relatedClassificationPolicy==SELECTED.
     * An example could be "[Anchors, LatestChange]" to ignore the Anchors and LatestChange classifications.
     */
    public static final String RELATED_CLASSIFICATION_IGNORE_LIST_CONFIGURATION_PROPERTY     = "relatedClassificationIgnoreList";

    /**
     * The relatedRelationshipIgnoreList configuration property used to list the relationship type names that should not be copied from the
     * open metadata ecosystem to Apache Atlas if relatedEntityPolicy==SELECTED.
     * An example could be "[License, Certification]" to ignore the License and Certification relationships.
     */
    public static final String RELATED_RELATIONSHIP_IGNORE_LIST_CONFIGURATION_PROPERTY     = "relatedRelationshipIgnoreList";

    /**
     * Symbolic name for the catalog target (MC Mock API).
     */
    static public final String CATALOG_TARGET_NAME                             = "MCMockAPI";

    /**
     * Constructor used to initialize the ConnectorProvider with the Java class name of the specific
     * store implementation.
     */
    public MCMockupProvider()
    {
        super();

        /*
         * Set up the class name of the connector that this provider creates.
         */
        super.setConnectorClassName(connectorClassName);

        /*
         * Set up the connector type that should be included in a connection used to configure this connector.
         */
        ConnectorType connectorType = new ConnectorType();
        connectorType.setType(ConnectorType.getConnectorTypeType());
        connectorType.setGUID(connectorTypeGUID);
        connectorType.setQualifiedName(connectorQualifiedName);
        connectorType.setDisplayName(connectorDisplayName);
        connectorType.setDescription(connectorDescription);
        connectorType.setConnectorProviderClassName(this.getClass().getName());
        connectorType.setSupportedAssetTypeName(supportedAssetTypeName);
        connectorType.setSupportedDeployedImplementationType(DeployedImplementationType.CATALOG_INTEGRATION_CONNECTOR.getDeployedImplementationType());

        List<String> recognizedConfigurationProperties = new ArrayList<>();
        recognizedConfigurationProperties.add(OPEN_METADATA_TYPES_POLICY_CONFIGURATION_PROPERTY);
        recognizedConfigurationProperties.add(IGNORE_OPEN_METADATA_TYPES_CONFIGURATION_PROPERTY);
        recognizedConfigurationProperties.add(EGERIA_GLOSSARY_QUALIFIED_NAME_CONFIGURATION_PROPERTY);
        recognizedConfigurationProperties.add(MC_Mockup_GLOSSARY_NAME_CONFIGURATION_PROPERTY);
        recognizedConfigurationProperties.add(INFORMAL_TAGS_MAPPING_POLICY_CONFIGURATION_PROPERTY);
        recognizedConfigurationProperties.add(CLASSIFICATION_REFERENCE_SET_NAME_CONFIGURATION_PROPERTY);
        recognizedConfigurationProperties.add(CLASSIFICATION_REFERENCE_SET_POLICY_CONFIGURATION_PROPERTY);
        recognizedConfigurationProperties.add(RELATED_CLASSIFICATION_POLICY_CONFIGURATION_PROPERTY);
        recognizedConfigurationProperties.add(RELATED_CLASSIFICATION_IGNORE_LIST_CONFIGURATION_PROPERTY);
        recognizedConfigurationProperties.add(RELATED_ENTITY_POLICY_CONFIGURATION_PROPERTY);
        recognizedConfigurationProperties.add(RELATED_RELATIONSHIP_IGNORE_LIST_CONFIGURATION_PROPERTY);
        connectorType.setRecognizedConfigurationProperties(recognizedConfigurationProperties);

        super.connectorTypeBean = connectorType;

        /*
         * Set up the component description used in the connector's audit log messages.
         */
        AuditLogReportingComponent componentDescription = new AuditLogReportingComponent();

        componentDescription.setComponentId(connectorComponentId);
        componentDescription.setComponentDevelopmentStatus(ComponentDevelopmentStatus.TECHNICAL_PREVIEW);
        componentDescription.setComponentName(connectorDisplayName);
        componentDescription.setComponentDescription(connectorDescription);
        componentDescription.setComponentWikiURL(connectorWikiPage);

        super.setConnectorComponentDescription(componentDescription);

        CatalogTargetType catalogTargetType = new CatalogTargetType();

        catalogTargetType.setName(CATALOG_TARGET_NAME);
        catalogTargetType.setTypeName("MC Mock API"); //not sure if this matters or is correct
        catalogTargetType.setDeployedImplementationType("MC Mock API"); //not sure if this matters or is correct

        super.catalogTargets = new ArrayList<>();
        super.catalogTargets.add(catalogTargetType);

        // just commenting this out for now. don't think it matters?
        //super.supportedTechnologyTypes = SupportedTechnologyType.getSupportedTechnologyTypes(new DeployedImplementationTypeDefinition[]{AtlasDeployedImplementationType.APACHE_ATLAS_SERVER});
    }
}
