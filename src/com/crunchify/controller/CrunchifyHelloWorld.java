package com.crunchify.controller;

import java.security.KeyStore;
import java.security.PrivateKey;
import java.util.Date;
import java.util.Enumeration;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.google.gson.Gson;
import com.mastercard.merchant.checkout.PaymentDataApi;
import com.mastercard.merchant.checkout.PostbackApi;
import com.mastercard.merchant.checkout.model.Postback;
import com.mastercard.sdk.core.MasterCardApiConfig;
import com.mastercard.sdk.core.util.QueryParams;

@Controller
public class CrunchifyHelloWorld {

	@RequestMapping("/pciapi")
	public ModelAndView helloWorld(@RequestParam(value="oauth_token") String oauth_token,@RequestParam(value="oauth_verifier") String oauth_verifier,
			@RequestParam(value="checkout_resource_url") String checkout_resource_url, @RequestParam(value="checkoutId") String checkoutId) {
		
		String consumerKey = "Z0Kx59E6M6u3f_-70fkzN3Qi22VLYJs8ydtvfnI4e52ba9b5!3e9c9b44604141dd917a28e1758ede9f0000000000000000"; 
		String keyPassword = "MasterPassSandbox"; 
		System.setProperty("org.slf4j.simpleLogger.defaultLogLevel", "debug"); 
		System.out.println("In Controller");
		PrivateKey privateKey = null;
		try {
						
			KeyStore ks = KeyStore.getInstance("PKCS12");
		       ks.load (CrunchifyHelloWorld.class.getClassLoader().getResourceAsStream ("certs/MasterPassSandbox-sandbox.p12"),
		         keyPassword.toCharArray ());
		       Enumeration aliases = ks.aliases ();
		       String keyAlias = "";
		 
		       while (aliases.hasMoreElements ()){
		         keyAlias = (String) aliases.nextElement ();
		        }
		 
		       privateKey = (PrivateKey)ks.getKey(keyAlias, keyPassword.toCharArray());
		       
		       //MasterCardApiConfig.setSandBox(false); // to use production environment else
		       MasterCardApiConfig.setSandBox(true); // to use sandbox environment    
		       MasterCardApiConfig.setConsumerKey(consumerKey);
		       MasterCardApiConfig.setPrivateKey(privateKey);
			
		       System.out.println(privateKey);
				System.out.println("Token:"+oauth_token + "Verifier:"+oauth_verifier+"Checkout Resource URL"+checkout_resource_url);
						
			QueryParams queryParams = new QueryParams().add("checkoutId",
					"e60e6400d7ce41e1979bc8930819bca3").add("cartId", "6a7804b7-6e6f-4ec3-ac1c-f7951704082c");
			com.mastercard.merchant.checkout.model.PaymentData response = PaymentDataApi.show(oauth_verifier,
					queryParams);

			System.out.println("Card: \n" + response.getCard());
			System.out.println("Auth Options: \n" + response.getAuthenticationOptions());
			System.out.println("personal Info: \n" + response.getPersonalInfo());
			System.out.println("Shipping Address: \n" + response.getShippingAddress());
			System.out.println("Tokenized Card: \n" + response.getTokenizedCard());
			
			/*ZonedDateTime zdt = LocalDateTime.now().atZone(ZoneId.systemDefault());
		    Date date = Date.from(zdt.toInstant());*/
			
			Postback  postback = new Postback()    
			           .transactionId(oauth_verifier)
			           .currency("USD")    
			           .paymentCode("123456")
			           .paymentSuccessful(true)
			           .amount(756.45)
			           .paymentDate(new Date());
			         
			 PostbackApi.create(postback);
			 
			 Gson gson = new Gson();
			 String json = gson.toJson(postback);
			 
			 System.out.println("Postback :  \n" + json);

		} catch (Exception e) {			
			System.err.println("Message: "+e.getMessage());			
		}
		return new ModelAndView("pciapi", "message", "test");
	}
}
