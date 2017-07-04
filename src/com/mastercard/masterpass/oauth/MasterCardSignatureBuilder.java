package com.mastercard.masterpass.oauth;

import static com.mastercard.masterpass.oauth.constants.OauthConstants.ERR_MSG_CONSUMER_KEY;
import static com.mastercard.masterpass.oauth.constants.OauthConstants.ERR_MSG_PRIVATE_KEY;
import static com.mastercard.masterpass.oauth.constants.OauthConstants.OAUTH;
import static com.mastercard.masterpass.oauth.constants.OauthConstants.OAUTH_BODY_HASH;
import static com.mastercard.masterpass.oauth.constants.OauthConstants.OAUTH_CONSUMER_KEY;
import static com.mastercard.masterpass.oauth.constants.OauthConstants.OAUTH_NONCE;
import static com.mastercard.masterpass.oauth.constants.OauthConstants.OAUTH_SIGNATURE;
import static com.mastercard.masterpass.oauth.constants.OauthConstants.OAUTH_SIGNATURE_METHOD;
import static com.mastercard.masterpass.oauth.constants.OauthConstants.OAUTH_TIMESTAMP;
import static com.mastercard.masterpass.oauth.constants.OauthConstants.OAUTH_VERSION_KEY;
import static com.mastercard.masterpass.oauth.constants.OauthConstants.OAUTH_VERSION_VALUE;
import static com.mastercard.masterpass.oauth.constants.OauthConstants.REALM_KEY;
import static com.mastercard.masterpass.oauth.constants.OauthConstants.REALM_VALUE;
import static com.mastercard.masterpass.oauth.constants.OauthConstants.SIGNATURE_METHOD;

import java.security.PrivateKey;
import java.util.Map;
import java.util.SortedSet;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;

/**
 * Created by Shrikant Belan on 5/16/2017.
 */
public class MasterCardSignatureBuilder {

    /**
     * Create Oauth header required by Mastercard API.
     *
     * @param privateKey     the private key retrieved from the .p12 file.
     * @param consumerKey    the consumer key from developer zone.
     * @param requestUrl     the resource request url.
     * @param httpMethod     the http method.
     * @param requestHeaders the request headers.
     * @param requestBody    the request body.
     * @return the generated authorization header.
     */
    public static String generateSignature(PrivateKey privateKey, String consumerKey, String requestUrl,
                                           String httpMethod, Map<String, String> requestHeaders,
                                           String requestBody) {

        if (privateKey == null) {
            System.out.println(ERR_MSG_PRIVATE_KEY);
            throw new RuntimeException(ERR_MSG_PRIVATE_KEY);
        }

        if (StringUtils.isEmpty(consumerKey)) {
            System.out.println(ERR_MSG_CONSUMER_KEY);
            throw new RuntimeException(ERR_MSG_CONSUMER_KEY);
        }

        OAuthSigner rsaSigner = new OAuthSigner();
        rsaSigner.setPrivateKey(privateKey);

        OAuthParameters oauthParams = new OAuthParameters();
        oauthParams.addParameter(OAUTH_CONSUMER_KEY, consumerKey);
        oauthParams.addParameter(OAUTH_NONCE, OAuthUtility.nonce());
        oauthParams.addParameter(OAUTH_TIMESTAMP, OAuthUtility.timestamp());
        oauthParams.addParameter(OAUTH_SIGNATURE_METHOD, SIGNATURE_METHOD);
        oauthParams.addParameter(OAUTH_VERSION_KEY, OAUTH_VERSION_VALUE);

        // for merchant chekout and precheckout calls auth_token require in header,
        // so added condition to add all request present headers during base string generation
        if (!requestHeaders.isEmpty()) {
           /* requestHeaders.entrySet().forEach(entry -> {
                oauthParams.addParameter(entry.getKey(), entry.getValue());
            });*/
            
            for(Map.Entry<String, String> entry : requestHeaders.entrySet()){
            	oauthParams.addParameter(entry.getKey(), entry.getValue());
            }
        }

        if (StringUtils.isNotEmpty(requestBody)) {
            String encodedHash = Base64.encodeBase64String(DigestUtils.sha1(requestBody));
            oauthParams.addParameter(OAUTH_BODY_HASH, encodedHash);
        }

        String baseString = OAuthUtility.generateSignatureBaseString(requestUrl, httpMethod, oauthParams.getBaseParameters());
        String signature = rsaSigner.generateSignature(baseString);
        oauthParams.addParameter(OAUTH_SIGNATURE, signature);
        oauthParams.addParameter(REALM_KEY, REALM_VALUE);
        return buildAuthHeaderString(oauthParams);
    }

    private static String buildAuthHeaderString(OAuthParameters params) {
        StringBuilder builder = new StringBuilder();
        builder.append(OAUTH).append(" ");
        for (Map.Entry<String, SortedSet<String>> param : params.getBaseParameters().entrySet()) {
            builder.append(param.getKey()).append("=\"").append(OAuthUtility.encode(param.getValue().toString())).append("\",");
        }
        builder.setLength(Math.max(builder.length() - 1, 0));
        return builder.toString();
    }
}
