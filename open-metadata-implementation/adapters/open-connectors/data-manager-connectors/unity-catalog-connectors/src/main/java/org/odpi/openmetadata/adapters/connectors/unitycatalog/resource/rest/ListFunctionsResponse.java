/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.adapters.connectors.unitycatalog.resource.rest;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.adapters.connectors.unitycatalog.properties.FunctionInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * Return a list of functions
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class ListFunctionsResponse
{
    private List<FunctionInfo> functions = new ArrayList<>();
    private String            nextPageToken = null;


    /**
     * Constructor
     */
    public ListFunctionsResponse()
    {
    }


    /**
     * Return the list of functions.
     *
     * @return list
     */
    public List<FunctionInfo> getFunctions()
    {
        return functions;
    }


    /**
     * Return the list of functions.
     *
     * @param functions list
     */
    public void setFunctions(List<FunctionInfo> functions)
    {
        this.functions = functions;
    }


    /**
     * Return the opaque token to retrieve the next page of results. Absent if there are no more pages. page_token should be set to this value for the next request (for the next page of results).
     *
     * @return token
     */
    public String getNextPageToken()
    {
        return nextPageToken;
    }


    /**
     * Set up the opaque token to retrieve the next page of results. Absent if there are no more pages. page_token should be set to this value for the next request (for the next page of results).
     *
     * @param nextPageToken token
     */
    public void setNextPageToken(String nextPageToken)
    {
        this.nextPageToken = nextPageToken;
    }


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "ListFunctionsResponse{" +
                "functions=" + functions +
                ", nextPageToken='" + nextPageToken + '\'' +
                '}';
    }


    /**
     * Compare the values of the supplied object with those stored in the current object.
     *
     * @param objectToCompare supplied object
     * @return boolean result of comparison
     */
    @Override
    public boolean equals(Object objectToCompare)
    {
        if (this == objectToCompare) return true;
        if (objectToCompare == null || getClass() != objectToCompare.getClass()) return false;
        ListFunctionsResponse that = (ListFunctionsResponse) objectToCompare;
        return Objects.equals(functions, that.functions) && Objects.equals(nextPageToken, that.nextPageToken);
    }


    /**
     * Return hash code based on properties.
     *
     * @return int
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(functions, nextPageToken);
    }
}
