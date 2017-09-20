package com.crunchify.controller;

import java.security.KeyStore;
import java.security.PrivateKey;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.crunchify.common.Connector;
import com.crunchify.model.PostbackPOJO;
import com.google.gson.Gson;
import com.mastercard.masterpass.oauth.MasterCardSignatureBuilder;

@Controller
public class MasterPassController {
	
	@Autowired
	private Connector connector;

	private static final String consumerKey = "Z0Kx59E6M6u3f_-70fkzN3Qi22VLYJs8ydtvfnI4e52ba9b5!3e9c9b44604141dd917a28e1758ede9f0000000000000000";
			//"Z0Kx59E6M6u3f_-70fkzN3Qi22VLYJs8ydtvfnI4e52ba9b5!3e9c9b44604141dd917a28e1758ede9f0000000000000000";
	//l2vaSek4q-qV76Wf21VnHIBbPhOxH7qPqSPABE5pd15080c4!c751b121576c4984a651c611fccd5e2d0000000000000000
    //private static final String requestUrl = "https://sandbox.api.mastercard.com/masterpass/paymentdata/d9458d37929f2d0016a4ba923428af4b78b75ac8?cartId=6a7804b7-6e6f-4ec3-ac1c-f7951704082c&checkoutId=e60e6400d7ce41e1979bc8930819bca3";

    
	@RequestMapping("/mp/payment")
	public ModelAndView callPaymentAPI(@RequestParam(value="oauth_token") String oauth_token,@RequestParam(value="oauth_verifier") String oauth_verifier,
			@RequestParam(value="checkout_resource_url") String checkout_resource_url, @RequestParam(value="checkoutId") String checkoutId) {
		
		
		System.setProperty("org.slf4j.simpleLogger.defaultLogLevel", "debug"); 
		System.out.println("In MasterPassController callPaymentAPI");
		System.out.println("oauth_verifier " + oauth_verifier);
		System.out.println("checkoutId " + checkoutId);
		String checkoutid ="e60e6400d7ce41e1979bc8930819bca3";
		//String checkoutid="f37ee78da843424d936223f7393a49a9";
		String cartId = "6a7804b7-6e6f-4ec3-ac1c-f7951704082c";
		Map<String, String> map = new HashMap<String, String>();
		
		String paymentUrl = "https://sandbox.api.mastercard.com/masterpass/paymentdata/" + oauth_verifier + "?cartId=" + cartId + "&checkoutId=" + checkoutid;
		System.out.println("Payment URL :" + paymentUrl);
		
		String authorizationHeader = MasterCardSignatureBuilder.generateSignature(getPrivateKey(), consumerKey, paymentUrl,
                "GET",map, null);
		System.out.println("Header : " + authorizationHeader);		
		
		String response = connector.doRequest(paymentUrl, "GET", authorizationHeader, null).get("Message");
		System.out.println("response :" + response);
		
		//Postback API
		String postbackUrl = "https://sandbox.api.mastercard.com/masterpass/postback";
		PostbackPOJO postback = new PostbackPOJO(oauth_verifier, "USD", "1", "true", "123456", "2017-04-04");
		
		Gson gson = new Gson();
		String json = gson.toJson(postback);
		System.out.println("Postback request : " + json);
		
		String postbackHeader = MasterCardSignatureBuilder.generateSignature(getPrivateKey(), consumerKey, postbackUrl,
                "POST",map, json);
		
		System.out.println("postback header :" + postbackHeader);
		
		String postResponse = connector.doRequest(postbackUrl, "POST", postbackHeader, json).get("MESSAGE");
		
		System.out.println("postback response:" + postResponse);
		
		return null;
	}
	
	public static PrivateKey getPrivateKey() {
        PrivateKey privateKey = null;
        String keyAlias = "MasterPassSandbox";
        String pwd = "MasterPassSandbox";
        //String keyAlias = "airteltest";
        //String pwd = "changeit";
        String certPath = "certs/MasterPassSandbox-sandbox.p12";
        //String certPath = "certs/AIRTELTEST-sandbox.p12";
        try {
            KeyStore ks = KeyStore.getInstance("PKCS12");
            ks.load(MasterPassController.class.getClassLoader().getResourceAsStream(certPath), pwd.toCharArray());
            privateKey = (PrivateKey) ks.getKey(keyAlias, pwd.toCharArray());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return privateKey;
    }
	
	/*RestTemplate restTemplate = new RestTemplate();
	HttpHeaders headers = new HttpHeaders();
	headers.add("Authorization", authorizationHeader);
	HttpEntity<String> entity = new HttpEntity<String>("parameters", headers);
	System.out.println("Data Built");
	ResponseEntity<String> response = restTemplate.exchange(paymentUrl, HttpMethod.GET, entity, String.class);
	System.out.println("Response " + response.getBody());*/
}
