/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.samples.archiveutilities.clinicaltrialtemplates;


import org.odpi.openmetadata.adapters.connectors.datastore.csvfile.CSVFileStoreProvider;
import org.odpi.openmetadata.archiveutilities.openconnectors.OpenConnectorArchiveWriter;
import org.odpi.openmetadata.frameworks.connectors.ConnectorProvider;
import org.odpi.openmetadata.frameworks.governanceaction.mapper.OpenMetadataProperty;
import org.odpi.openmetadata.frameworks.governanceaction.mapper.OpenMetadataType;
import org.odpi.openmetadata.frameworks.governanceaction.refdata.FileType;
import org.odpi.openmetadata.repositoryservices.connectors.stores.archivestore.properties.OpenMetadataArchive;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Classification;
import org.odpi.openmetadata.samples.archiveutilities.combo.CocoBaseArchiveWriter;
import org.odpi.openmetadata.samples.archiveutilities.governanceprogram.CocoGovernanceProgramArchiveWriter;
import org.odpi.openmetadata.samples.archiveutilities.governanceprogram.CocoGovernanceZoneDefinition;
import org.odpi.openmetadata.samples.archiveutilities.governanceprogram.LicenseTypeDefinition;
import org.odpi.openmetadata.samples.archiveutilities.organization.ScopeDefinition;

import java.util.*;


/**
 * CocoClinicalTrialsArchiveWriter creates a physical open metadata archive file containing the clinical trials templates
 * needed by Coco Pharmaceuticals.
 */
public class CocoClinicalTrialsArchiveWriter extends CocoBaseArchiveWriter
{
    private static final String archiveFileName = "CocoClinicalTrialsTemplatesArchive.omarchive";

    /*
     * This is the header information for the archive.
     */
    private static final String                  archiveGUID        = "74a786b2-d6d7-401d-b8c1-7d798f752c55";
    private static final String                  archiveName        = "Coco Pharmaceuticals Clinical Trials Templates";
    private static final String                  archiveDescription = "Templates for new assets relating to a clinical trial.";

    private static final String clinicalTrialsSubjectArea = "SubjectArea:ClinicalTrial:TeddyBearDropFoot";

    /**
     * Default constructor initializes the archive.
     */
    public CocoClinicalTrialsArchiveWriter()
    {
        super(archiveGUID,
              archiveName,
              archiveDescription,
              new Date(),
              archiveFileName,
              new OpenMetadataArchive[]{ new CocoGovernanceProgramArchiveWriter().getOpenMetadataArchive(),
                                         new OpenConnectorArchiveWriter().getOpenMetadataArchive()});
    }


    /**
     * Add the content to the archive builder.
     */
    public void getArchiveContent()
    {
        writeGlossary();
        writeWeeklyMeasurementsTemplate();
    }



    private void writeGlossary()
    {
        final String methodName = "writeGlossary";

        String glossaryGUID = archiveHelper.addGlossary("Glossary:TeddyBearDropFootTerminology",
                                                        "Teddy Bear Drop Foot Terminology",
                                                        "This glossary describes terminology invented for the fictitious study into Teddy Bear Drop Foot that is being used to demonstrate aspects of open governance without risk to real patient data. ",
                                                        "English",
                                                        "Used with the Teddy Bear Drop Foot Demonstration Study.",
                                                        null,
                                                        ScopeDefinition.ALL_COCO.getPreferredValue());

        archiveHelper.addSubjectAreaClassification(glossaryGUID, clinicalTrialsSubjectArea);

        Map<String, String> categoryLookup = new HashMap<>();
        for (GlossaryCategoryDefinition glossaryCategoryDefinition : GlossaryCategoryDefinition.values())
        {
            String glossaryCategoryGUID = archiveHelper.addGlossaryCategory(glossaryGUID,
                                                                            glossaryCategoryDefinition.getQualifiedName(),
                                                                            glossaryCategoryDefinition.getName(),
                                                                            glossaryCategoryDefinition.getDescription(),
                                                                            null);

            categoryLookup.put(glossaryCategoryDefinition.getName(), glossaryCategoryGUID);
        }

        for (GlossaryTermDefinition glossaryTermDefinition : GlossaryTermDefinition.values())
        {
            String glossaryTermGUID = archiveHelper.addTerm(glossaryGUID,
                                                            null,
                                                            false,
                                                            glossaryTermDefinition.getQualifiedName(),
                                                            glossaryTermDefinition.getName(),
                                                            glossaryTermDefinition.getSummary(),
                                                            glossaryTermDefinition.getDescription(),
                                                            null,
                                                            glossaryTermDefinition.getAbbreviation(),
                                                            glossaryTermDefinition.getUsage(),
                                                            false,
                                                            false,
                                                            false,
                                                            null,
                                                            null,
                                                            null,
                                                            null);

            List<Classification> classificationList = new ArrayList<>();

            classificationList.add(archiveHelper.getTemplateSubstituteClassification());

            String substituteGlossaryTermGUID = archiveHelper.addTerm(glossaryGUID,
                                                                      null,
                                                                      false,
                                                                      glossaryTermDefinition.getTemplateSubstituteQualifiedName(),
                                                                      glossaryTermDefinition.getTemplateSubstituteName(),
                                                                      glossaryTermDefinition.getTemplateSubstituteSummary(),
                                                                      null,
                                                                      null,
                                                                      null,
                                                                      glossaryTermDefinition.getTemplateSubstituteUsage(),
                                                                      false,
                                                                      false,
                                                                      false,
                                                                      null,
                                                                      null,
                                                                      null,
                                                                      classificationList);


            archiveHelper.addSourcedFromRelationship(substituteGlossaryTermGUID,
                                                     1,
                                                     glossaryTermGUID,
                                                     methodName);

            if (glossaryTermDefinition.getCategory() != null)
            {
                archiveHelper.addTermToCategory(categoryLookup.get(glossaryTermDefinition.getCategory().getName()),
                                                glossaryTermGUID);
            }
        }
    }


    private void writeWeeklyMeasurementsTemplate()
    {
        final String methodName = "writeWeeklyMeasurementsTemplate";
        Map<String, Object>  extendedProperties = new HashMap<>();

        extendedProperties.put(OpenMetadataProperty.PATH_NAME.name, "{{pathName}}");
        extendedProperties.put(OpenMetadataProperty.FILE_NAME.name, "{{fileName}}");
        extendedProperties.put(OpenMetadataProperty.DEPLOYED_IMPLEMENTATION_TYPE.name, FileType.CSV_FILE.getDeployedImplementationType().getDeployedImplementationType());
        extendedProperties.put(OpenMetadataProperty.FILE_TYPE.name, FileType.CSV_FILE.getFileTypeName());
        extendedProperties.put(OpenMetadataProperty.FILE_EXTENSION.name, "csv");
        extendedProperties.put(OpenMetadataType.DELIMITER_CHARACTER_PROPERTY_NAME, ",");
        extendedProperties.put(OpenMetadataType.QUOTE_CHARACTER_PROPERTY_NAME, "\"");

        List<Classification> classifications = new ArrayList<>();

        List<String> zones = new ArrayList<>();
        zones.add(CocoGovernanceZoneDefinition.LANDING_AREA.getZoneName());
        zones.add(CocoGovernanceZoneDefinition.QUARANTINE.getZoneName());
        zones.add("{{clinicalTrialId}}");
        classifications.add(archiveHelper.getAssetZoneMembershipClassification(zones));

        Map<String, String> otherOriginValues = new HashMap<>();
        otherOriginValues.put("contact", "{{contactName}}");
        otherOriginValues.put("dept", "{{contactDept}}");

        classifications.add(archiveHelper.getAssetOriginClassification("{{hospitalName}}",
                                                                       OpenMetadataProperty.NAME.name,
                                                                       null,
                                                                       null,
                                                                       otherOriginValues));

        Map<String, String> placeholderProperties = new HashMap<>();

        placeholderProperties.put("clinicalTrialId", "Add the identifier of the clinical trial.");
        placeholderProperties.put("clinicalTrialName", "Add the display name of the clinical trial.");
        placeholderProperties.put("hospitalName", "Add the name of the hospital that sent the measurements.");
        placeholderProperties.put("contactName", "Add the name of the person at the hospital who is responsible for supplying the measurements.");
        placeholderProperties.put("contactDept", "Add the hospital department where the contact name works.");
        placeholderProperties.put("receivedDate", "Add the date that the measurements were received.");
        placeholderProperties.put("pathName", "Add the full name (absolute file name) of the file and its enclosing directory structure.");
        placeholderProperties.put("fileName", "Add the name of the file without directory information.");

        classifications.add(archiveHelper.getTemplateClassification("Weekly teddy bear measurements for drop foot clinical trial",
                                                                    "This template supports the cataloguing of weekly measurement files. " +
                                                                            "Use it to catalog the files as they come into the landing area.",
                                                                    "2.4",
                                                                    null,
                                                                    placeholderProperties,
                                                                    null,
                                                                    methodName));

        String assetGUID = archiveHelper.addAsset(FileType.CSV_FILE.getAssetSubTypeName(),
                                                  "CSVFile:ClinicalTrial:{{clinicalTrialId}}:WeeklyMeasurement:{{hospitalName}}:{{dateReceived}}:{{pathName}}",
                                                  "{{hospitalName}} teddy bear measurements received on {{dateReceived}} for {{clinicalTrialName}}",
                                                  "V1.0",
                                                  "Dated measurements of patient's progression presented in a tabular format" +
                                                          " with columns of PatientId, Date, AngleLeft and AngleRight.",
                                                  null,
                                                  extendedProperties,
                                                  classifications);

        String endpointGUID = archiveHelper.addEndpoint(assetGUID,
                                                        FileType.CSV_FILE.getAssetSubTypeName(),
                                                        "CSVFile:ClinicalTrial:{{clinicalTrialId}}:WeeklyMeasurement:{{hospitalName}}:{{dateReceived}}:{{pathName}}_endpoint",
                                                        null,
                                                        null,
                                                        "{{pathName}}",
                                                        null,
                                                        null);

        ConnectorProvider connectorProvider = new CSVFileStoreProvider();
        String            connectorTypeGUID = connectorProvider.getConnectorType().getGUID();

        String connectionGUID = archiveHelper.addConnection("CSVFile:ClinicalTrial:{{clinicalTrialId}}:WeeklyMeasurement:{{hospitalName}}:{{dateReceived}}:{{pathName}}_connection",
                                                            null,
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
                                                            FileType.CSV_FILE.getAssetSubTypeName());

        archiveHelper.addConnectionForAsset(assetGUID, null, connectionGUID);

        String licenseTypeGUID = archiveHelper.getGUID(LicenseTypeDefinition.CLINICAL_TRIAL_LICENSE.getQualifiedName());

        Map<String, String> entitlements = new HashMap<>();
        entitlements.put("research", "true");
        entitlements.put("marketing", "false");
        Map<String, String> restrictions = new HashMap<>();
        restrictions.put("copying", "in-house-only");
        Map<String, String> obligations = new HashMap<>();
        obligations.put("retention", "20 years");
        archiveHelper.addLicense(assetGUID,
                                 "ClinicalTrial:{{clinicalTrialId}}:WeeklyMeasurement:{{hospitalName}}",
                                 null,
                                 null,
                                 null,
                                 "{{hospitalName}}",
                                 "Organization",
                                 "name",
                                 "tanyatidie",
                                 "UserIdentity",
                                 "userId",
                                 "tessatube",
                                 "UserIdentity",
                                 "userId",
                                 entitlements,
                                 restrictions,
                                 obligations,
                                 null,
                                 licenseTypeGUID);

        String topLevelSchemaTypeGUID = archiveHelper.addTopLevelSchemaType(assetGUID,
                                                                            FileType.CSV_FILE.getAssetSubTypeName(),
                                                                            OpenMetadataType.TABULAR_SCHEMA_TYPE_TYPE_NAME,
                                                                            "CSVFile:ClinicalTrial:{{clinicalTrialId}}:WeeklyMeasurement:{{hospitalName}}:{{dateReceived}}:{{pathName}}_schemaType",
                                                                            null,
                                                                            null,
                                                                            null);

        String schemaAttributeGUID = archiveHelper.addSchemaAttribute(assetGUID,
                                                                      FileType.CSV_FILE.getAssetSubTypeName(),
                                                                      OpenMetadataType.TABULAR_COLUMN_TYPE_NAME,
                                                                      OpenMetadataType.PRIMITIVE_SCHEMA_TYPE_TYPE_NAME,
                                                                      "CSVFile:ClinicalTrial:{{clinicalTrialId}}:WeeklyMeasurement:{{hospitalName}}:{{dateReceived}}:{{pathName}}_patientId_column",
                                                                      "PatientId",
                                                                      null,
                                                                      "string",
                                                                      0,
                                                                      0,
                                                                      null,
                                                                      null);

        archiveHelper.addAttributeForSchemaType(topLevelSchemaTypeGUID, schemaAttributeGUID);

        String glossaryTermGUID = archiveHelper.getGUID(GlossaryTermDefinition.PATIENT_IDENTIFIER.getTemplateSubstituteQualifiedName());
        archiveHelper.addSemanticAssignment(schemaAttributeGUID, glossaryTermGUID);

        schemaAttributeGUID = archiveHelper.addSchemaAttribute(assetGUID,
                                                               FileType.CSV_FILE.getAssetSubTypeName(),
                                                               OpenMetadataType.TABULAR_COLUMN_TYPE_NAME,
                                                               OpenMetadataType.PRIMITIVE_SCHEMA_TYPE_TYPE_NAME,
                                                               "CSVFile:ClinicalTrial:{{clinicalTrialId}}:WeeklyMeasurement:{{hospitalName}}:{{dateReceived}}:{{pathName}}_date_column",
                                                               "Date",
                                                               null,
                                                               "date",
                                                               0,
                                                               1,
                                                               "YYYY-MM-DD",
                                                               null);

        archiveHelper.addAttributeForSchemaType(topLevelSchemaTypeGUID, schemaAttributeGUID);

        glossaryTermGUID = archiveHelper.getGUID(GlossaryTermDefinition.MEASUREMENT_DATE.getTemplateSubstituteQualifiedName());
        archiveHelper.addSemanticAssignment(schemaAttributeGUID, glossaryTermGUID);

        schemaAttributeGUID = archiveHelper.addSchemaAttribute(assetGUID,
                                                               FileType.CSV_FILE.getAssetSubTypeName(),
                                                               OpenMetadataType.TABULAR_COLUMN_TYPE_NAME,
                                                               OpenMetadataType.PRIMITIVE_SCHEMA_TYPE_TYPE_NAME,
                                                               "CSVFile:ClinicalTrial:{{clinicalTrialId}}:WeeklyMeasurement:{{hospitalName}}:{{dateReceived}}:{{pathName}}_angleLeft_column",
                                                               "AngleLeft",
                                                               null,
                                                               "integer",
                                                               0,
                                                               2,
                                                               null,
                                                               null);

        archiveHelper.addAttributeForSchemaType(topLevelSchemaTypeGUID, schemaAttributeGUID);

        glossaryTermGUID = archiveHelper.getGUID(GlossaryTermDefinition.ANGLE_LEFT.getTemplateSubstituteQualifiedName());
        archiveHelper.addSemanticAssignment(schemaAttributeGUID, glossaryTermGUID);

        schemaAttributeGUID = archiveHelper.addSchemaAttribute(assetGUID,
                                                               FileType.CSV_FILE.getAssetSubTypeName(),
                                                               OpenMetadataType.TABULAR_COLUMN_TYPE_NAME,
                                                               OpenMetadataType.PRIMITIVE_SCHEMA_TYPE_TYPE_NAME,
                                                               "CSVFile:ClinicalTrial:{{clinicalTrialId}}:WeeklyMeasurement:{{hospitalName}}:{{dateReceived}}:{{pathName}}_angleRight_column",
                                                               "AngleRight",
                                                               null,
                                                               "integer",
                                                               0,
                                                               3,
                                                               null,
                                                               null);

        archiveHelper.addAttributeForSchemaType(topLevelSchemaTypeGUID, schemaAttributeGUID);

        glossaryTermGUID = archiveHelper.getGUID(GlossaryTermDefinition.ANGLE_RIGHT.getTemplateSubstituteQualifiedName());
        archiveHelper.addSemanticAssignment(schemaAttributeGUID, glossaryTermGUID);
    }
}
