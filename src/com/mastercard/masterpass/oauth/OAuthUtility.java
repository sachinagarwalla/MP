package com.mastercard.masterpass.oauth;

import org.apache.commons.lang3.StringUtils;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;

/**
 * Oauth utility class.
 */
public class OAuthUtility {

    public static final String UTF_8 = "UTF-8";
    public static final String AMP = "&";
    public static final String EQUALS = "=";
    public static final String QUESTION_MARK = "?";
    public static final String EMPTY_STRING = "";

    public static String generateSignatureBaseString(String requestURL, String requestMethod,
                                                     Map<String, SortedSet<String>> baseParameters) {
        URI requestUri = parseUrl(requestURL);
        return encode(requestMethod.toUpperCase()) + AMP + encode(normalizeUrl(requestUri)) + AMP
                + encode(normalizeParameters(requestURL, baseParameters));
    }

    private static URI parseUrl(String httpsURL) {
        // validate the request url
        if (StringUtils.isEmpty(httpsURL)) {
            throw new RuntimeException("Request Url cannot be empty");
        }
        // parse the url into its constituent parts.
        URI uri;
        try {
            uri = new URI(httpsURL);
        } catch (URISyntaxException uriSyntaxException) {
            throw new RuntimeException("Exception during url parsing :: " + uriSyntaxException);
        }
        return uri;
    }

    /**
     * Formats the input string for inclusion in a url. Account for the
     * differences in how OAuth encodes certain characters (such as the space
     * character).
     *
     * @param stringToEncode the string to encode
     * @return the url-encoded string
     */
    public static String encode(String stringToEncode) {

        String encoded;
        try {
            encoded = URLEncoder.encode(stringToEncode, UTF_8);
        } catch (UnsupportedEncodingException unsupportedEncodingException) {
            System.out.println("Exception during url parsing :: " + unsupportedEncodingException);
            throw new RuntimeException(unsupportedEncodingException);
        }
        encoded = encoded.replace("*", "%2A");
        encoded = encoded.replace("%7E", "~");
        encoded = encoded.replace("+", "%20");
        encoded = encoded.replace("%5B", "");
        encoded = encoded.replace("%5D", "");
        return encoded;
    }

    /**
     * Calculates the normalized request url, as per section 9.1.2 of the OAuth
     * Spec. This removes the querystring from the url and the port (if it is
     * the standard http or https port).
     *
     * @param uri the request url to normalize (not <code>null</code>)
     * @return the normalized request url, as per the rules in the link above
     */
    public static String normalizeUrl(URI uri) {
        String authority = uri.getAuthority();
        String scheme = uri.getScheme();
        if (authority == null || scheme == null) {
            throw new RuntimeException("Invalid Request Url");
        }
        authority = authority.toLowerCase();
        scheme = scheme.toLowerCase();

        // if this url contains the standard port, remove it
        if ((scheme.equals("http") && uri.getPort() == 80) || (scheme.equals("https") && uri.getPort() == 443)) {
            int index = authority.lastIndexOf(":");
            if (index >= 0) {
                authority = authority.substring(0, index);
            }
        }
        // piece together the url without the querystring
        return scheme + "://" + authority + uri.getRawPath();
    }

    /**
     * Calculates the normalized request parameters string to use in the base
     * string, as per section 9.1.1 of the OAuth Spec.
     *
     * @param hostUrl           the request url to normalize (not <code>null</code>)
     * @param requestParameters key/value pairs of parameters in the request
     * @return the parameters normalized to a string
     */
    public static String normalizeParameters(String hostUrl, Map<String, SortedSet<String>> requestParameters) {

        TreeMap<String, SortedSet<String>> alphaParams = new TreeMap<>(requestParameters);
        if (hostUrl.indexOf(QUESTION_MARK) > 0) {
            Map<String, SortedSet<String>> queryParameters = parseQuerystring(
                    hostUrl.substring(hostUrl.indexOf(QUESTION_MARK) + 1));
            alphaParams.putAll(queryParameters);
        }

        // piece together the base string, encoding each key and value
        StringBuilder paramString = new StringBuilder();
        for (Map.Entry<String, SortedSet<String>> e : alphaParams.entrySet()) {
            if ((e.getValue()).size() == 0) {
                continue;
            }
            if (paramString.length() > 0) {
                paramString.append(AMP);
            }
            paramString.append(encode(e.getKey())).append(EQUALS).append(encode(e.getValue().toString()));
        }
        return paramString.toString();
    }

    /**
     * Parse a querystring into a map of key/value pairs.
     *
     * @param queryString the string to parse (without the '?')
     * @return key/value pairs mapping to the items in the querystring
     */
    public static Map<String, SortedSet<String>> parseQuerystring(String queryString) {

        Map<String, SortedSet<String>> map = new TreeMap<>();
        if ((queryString == null) || (queryString.equals(EMPTY_STRING))) {
            return map;
        }
        String[] params = new String[20];
        if (queryString.contains(AMP))
            params = queryString.split(AMP);
        else if (queryString.length() > 0)
            params = new String[]{queryString};

        for (String param : params) {
                try {
                    String[] keyValuePair = param.split(EQUALS, 0);
                    String name = URLDecoder.decode(keyValuePair[0], UTF_8);
                    if (StringUtils.isEmpty(name)) {
                        continue;
                    }
                    String value = keyValuePair.length > 1 ? URLDecoder.decode(keyValuePair[1], UTF_8) : EMPTY_STRING;
                    if (map.containsKey(name)) {
                        SortedSet<String> tempSet = map.get(name);
                        tempSet.add(value);
                    } else {
                        SortedSet<String> tmpSet = new TreeSet<>();
                        tmpSet.add(value);
                        map.put(name, tmpSet);
                    }
                } catch (UnsupportedEncodingException unsupportedEncodingException) {
                    throw new RuntimeException("Exception during parsing :: " + unsupportedEncodingException);
                }
            }
        return map;
    }

    public static String nonce() {
        return Long.toString(System.nanoTime());
    }

    public static String timestamp() {
        return Long.toString(System.currentTimeMillis() / 1000);
    }
}