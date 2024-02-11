/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.governanceservers.integrationdaemonservices.properties;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * IntegrationConnectorStatus describes the status of an integration connector.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public enum IntegrationConnectorStatus implements Serializable
{
    INITIALIZED       (0,  "Initialized",          "The connector is set up and waiting to start."),
    RUNNING           (1,  "Running",              "The connector is running."),
    STOPPED           (2,  "Stopped",              "The connector has been stopped (either because it is configured to stop, or the integration daemon is shutting down)."),
    INITIALIZE_FAILED (3,  "Initialize Failed",    "The integration daemon is not able to initialize the connector because the metadata server is not available."),
    CONFIG_FAILED     (4,  "Configuration Failed", "The integration daemon is not able to initialize the connector because the class name or related configuration is invalid."),
    SHUTDOWN          (5,  "Shutdown",             "The integration daemon is no longer calling the connector because it has been shutdown or replaced."),
    FAILED            (99, "Connector Failed",     "The connector threw an exception and will not be called until it is restarted."),
    ;

    private static final long     serialVersionUID = 1L;

    private final int    ordinal;
    private final String name;
    private final String description;


    /**
     * Typical Constructor
     *
     * @param ordinal index
     * @param name short description
     * @param description longer definition
     */
    IntegrationConnectorStatus(int ordinal, String name, String description)
    {
        /*
         * Save the values supplied
         */
        this.ordinal = ordinal;
        this.name = name;
        this.description = description;
    }


    /**
     * Return the code for this enum instance
     *
     * @return int  code
     */
    public int getOrdinal()
    {
        return ordinal;
    }


    /**
     * Return the default name for this enum instance.
     *
     * @return String default name
     */
    public String getName()
    {
        return name;
    }


    /**
     * Return the default description for this enum instance.
     *
     * @return String default description
     */
    public String getDescription()
    {
        return description;
    }


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "IntegrationConnectorStatus{" +
                "ordinal=" + ordinal +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
