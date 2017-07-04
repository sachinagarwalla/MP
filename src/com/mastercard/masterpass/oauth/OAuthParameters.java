package com.mastercard.masterpass.oauth;

import java.util.Map;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;

/**
 * Oauth Parameters.
 */
public class OAuthParameters {

    private final Map<String, SortedSet<String>> baseParameters;

    /**
     * Initializes parameters containers.
     */
    public OAuthParameters() {
        baseParameters = new TreeMap<>();
    }

    /**
     * Return the parameters used to calculate the OAuth signature.
     *
     * @return a map of key/value pairs to use in the signature base string
     */
    public Map<String, SortedSet<String>> getBaseParameters() {
        return baseParameters;
    }

    /**
     * Adds a parameter to be used when generating the OAuth signature.
     *
     * @param key   The key used to reference this parameter.  This key will also be
     *              used to reference the value in the request url and in the http
     *              authorization header.
     * @param value the value of the parameter
     */
    public void addParameter(String key, String value) {
        put(key, value);
    }

    /**
     * Adds the key/value pair to the input map.
     *
     * @param key   the key to add to the map
     * @param value the value to add to the map
     */
    protected void put(String key, String value) {
        SortedSet<String> temp;
        if (this.baseParameters.containsKey(key)) {
            temp = new TreeSet<>(this.baseParameters.get(key));
            temp.add(value);

            this.baseParameters.put(key, temp);

        } else {
            temp = new TreeSet<>();
            temp.add(value);
            this.baseParameters.put(key, temp);
        }
    }

}
