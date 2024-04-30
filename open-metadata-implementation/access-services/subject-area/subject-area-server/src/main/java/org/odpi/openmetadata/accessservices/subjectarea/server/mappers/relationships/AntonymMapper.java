/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.subjectarea.server.mappers.relationships;

import org.odpi.openmetadata.accessservices.subjectarea.properties.enums.TermRelationshipStatus;
import org.odpi.openmetadata.accessservices.subjectarea.properties.relationships.Antonym;
import org.odpi.openmetadata.accessservices.subjectarea.server.mappers.SubjectAreaMapper;
import org.odpi.openmetadata.commonservices.generichandlers.*;
import org.odpi.openmetadata.accessservices.subjectarea.utilities.SubjectAreaUtils;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataProperty;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EnumPropertyValue;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProperties;


/**
 * Mapping methods to map between the antonym and the equivalent omrs Relationship.
 */
@SubjectAreaMapper
public class AntonymMapper extends RelationshipMapper<Antonym> {
    public static final String ANTONYM = "Antonym";

    public AntonymMapper(OpenMetadataAPIGenericHandler genericHandler){
        super(genericHandler);
    }


    /**
     * Map the supplied relationship to omrs InstanceProperties.
     *
     * @param antonym            supplied relationship
     * @param instanceProperties equivalent instance properties to the relationship
     */
    @Override
    protected void mapRelationshipToInstanceProperties(Antonym antonym, InstanceProperties instanceProperties) {
        if (antonym.getDescription() != null) {
            SubjectAreaUtils.setStringPropertyInInstanceProperties(instanceProperties, antonym.getDescription(), OpenMetadataProperty.DESCRIPTION.name);
        }
        if (antonym.getExpression() != null) {
            SubjectAreaUtils.setStringPropertyInInstanceProperties(instanceProperties, antonym.getExpression(), OpenMetadataProperty.EXPRESSION.name);
        }
        if (antonym.getSteward() != null) {
            SubjectAreaUtils.setStringPropertyInInstanceProperties(instanceProperties, antonym.getSteward(), OpenMetadataProperty.STEWARD.name);
        }
        if (antonym.getSource() != null) {
            SubjectAreaUtils.setStringPropertyInInstanceProperties(instanceProperties, antonym.getSource(), OpenMetadataProperty.SOURCE.name);
        }
        if (antonym.getStatus() != null) {
            SubjectAreaUtils.setStatusPropertyInInstanceProperties(instanceProperties, antonym.getStatus(), OpenMetadataProperty.TERM_RELATIONSHIP_STATUS.name);
        }
    }

    /**
     * Map a primitive omrs property to the antonym object.
     *
     * @param antonym      the omas relationship to be updated
     * @param propertyName the omrs property name
     * @param value        the omrs primitive property value
     * @return true if the propertyName was recognised and mapped to the relationship, otherwise false
     */
    @Override
    protected boolean mapPrimitiveToRelationship(Antonym antonym, String propertyName, Object value) {
        String stringValue = (String) value;
        boolean foundProperty = false;
        if (propertyName.equals("description")) {
            antonym.setDescription(stringValue);
            foundProperty = true;
        }
        if (propertyName.equals(OpenMetadataProperty.EXPRESSION.name)) {
            antonym.setExpression(stringValue);
            foundProperty = true;
        }
        if (propertyName.equals(OpenMetadataProperty.STEWARD.name)) {
            antonym.setSteward(stringValue);
            foundProperty = true;
        }
        if (propertyName.equals(OpenMetadataProperty.SOURCE.name)) {
            antonym.setSource(stringValue);
            foundProperty = true;
        }
        return foundProperty;
    }

    @Override
    protected boolean mapEnumToRelationship(Antonym antonym, String propertyName, EnumPropertyValue enumPropertyValue) {
        boolean foundProperty = false;
        if (propertyName.equals(OpenMetadataProperty.TERM_RELATIONSHIP_STATUS.name)) {
            TermRelationshipStatus status = TermRelationshipStatus.valueOf(enumPropertyValue.getSymbolicName());
            antonym.setStatus(status);
            foundProperty = true;
        }
        return foundProperty;
    }

    @Override
    public String getTypeName() {
        return ANTONYM;
    }

    @Override
    protected Antonym getRelationshipInstance() {
        return new Antonym();
    }

}
