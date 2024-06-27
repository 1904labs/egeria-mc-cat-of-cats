/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.frameworks.governanceaction.controls;


import java.util.ArrayList;
import java.util.List;

/**
 * PlaceholderProperty provides some standard definitions for placeholder properties used to pass properties
 * to services that use templates.
 */
public enum PlaceholderProperty
{
    /**
     * The host IP address or domain name.
     */
    HOST_IDENTIFIER ("hostIdentifier", "The host IP address or domain name.", "string", "coconet.com"),

    /**
     * The number of the port to use to connect to a service.
     */
    PORT_NUMBER ("portNumber", "The number of the port to use to connect to a service.", "string", "1234"),

    /**
     * The userId to store in the userId attribute of the connection.
     */
    CONNECTION_USER_ID ("connectionUserId",
                        "The userId to store in the userId attribute of the connection.",
                        "string",
                        "user1"),

    /**
     * The password to store in the clearPassword attribute of the connection.
     */
    CONNECTION_PASSWORD ("connectionPassword",
                         "The password to store in the clearPassword attribute of the connection.",
                         "string",
                         "secret"),

    /**
     * The name of the server being catalogued.
     */
    SERVER_NAME ("serverName", "The name of the server being catalogued.", "string", "myServer"),

    /**
     * The name of the schema being catalogued.
     */
    SCHEMA_NAME ("schemaName", "The name of the schema being catalogued.", "string", "MyServer.schema"),

    /**
     * The display name is used to identify the element.
     */
    DISPLAY_NAME("displayName",
                 "The display name is used to identify the element.  It does not need to be unique, but it should help someone know what the element is about.",
                 "string",
                 "myDataSet"),

    /**
     * The description of the element to help a consumer understand its content and purpose.
     */
    DESCRIPTION ("description",
                 "The description of the element to help a consumer understand its content and purpose.",
                 "string",
                 "This file contains a week's worth of patient data for the Teddy Bear Drop Foot clinical trial."),

    /**
     * The description of the element to help a consumer understand its content and purpose.
     */
    VERSION_IDENTIFIER ("versionIdentifier",
                 "The string identifier for the element.  This is typically of the form Vx.y.z where x is the major version number, y is the minor version number, and z is an option patch identifier.",
                 "string",
                 "V1.0"),

    ;

    public final String           name;
    public final String           description;
    public final String           dataType;
    public final String           example;


    /**
     * Create a specific Enum constant.
     *
     * @param name name of the placeholder property
     * @param description description of the placeholder property
     * @param dataType type of value of the placeholder property
     * @param example example of the placeholder property
     */
    PlaceholderProperty(String name,
                        String description,
                        String dataType,
                        String example)
    {
        this.name        = name;
        this.description = description;
        this.dataType    = dataType;
        this.example     = example;
    }


    /**
     * Return the name of the placeholder property.
     *
     * @return string name
     */
    public String getName()
    {
        return name;
    }

    /**
     * Return the placeholder to use when building templates.
     *
     * @return placeholder property
     */
    public String getPlaceholder()
    {
        return "{{" + name + "}}";
    }

    /**
     * Return the description of the placeholder property.
     *
     * @return text
     */
    public String getDescription()
    {
        return description;
    }


    /**
     * Return the data type for the placeholder property.
     *
     * @return data type name
     */
    public String getDataType()
    {
        return dataType;
    }


    /**
     * Return an example of the placeholder property to help users understand how to set it up.
     *
     * @return example
     */
    public String getExample()
    {
        return example;
    }


    /**
     * Retrieve all the defined placeholder properties
     *
     * @return list of placeholder property types
     */
    public static List<PlaceholderPropertyType> getPlaceholderPropertyTypes()
    {
        List<PlaceholderPropertyType> placeholderPropertyTypes = new ArrayList<>();

        for (PlaceholderProperty placeholderProperty : PlaceholderProperty.values())
        {
            placeholderPropertyTypes.add(placeholderProperty.getPlaceholderType());
        }

        return placeholderPropertyTypes;
    }



    /**
     * Return a summary of this enum to use in a service provider.
     *
     * @return placeholder property type
     */
    public PlaceholderPropertyType getPlaceholderType()
    {
        PlaceholderPropertyType placeholderPropertyType = new PlaceholderPropertyType();

        placeholderPropertyType.setName(name);
        placeholderPropertyType.setDescription(description);
        placeholderPropertyType.setDataType(dataType);
        placeholderPropertyType.setExample(example);
        placeholderPropertyType.setRequired(true);

        return placeholderPropertyType;
    }



    /**
     * Retrieve all the defined placeholder properties for directories (file folder)
     *
     * @return list of placeholder property types
     */
    public static List<PlaceholderPropertyType> getDataSetPlaceholderPropertyTypes()
    {
        List<PlaceholderPropertyType> placeholderPropertyTypes = new ArrayList<>();

        placeholderPropertyTypes.add(DISPLAY_NAME.getPlaceholderType());
        placeholderPropertyTypes.add(DESCRIPTION.getPlaceholderType());
        placeholderPropertyTypes.add(VERSION_IDENTIFIER.getPlaceholderType());

        return placeholderPropertyTypes;
    }



    /**
     * Output of this enum class and main value.
     *
     * @return string showing enum value
     */
    @Override
    public String toString()
    {
        return "PlaceholderProperty{ name=" + name + "}";
    }
}
