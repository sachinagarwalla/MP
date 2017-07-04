package com.mastercard.masterpass.oauth;

import org.apache.commons.codec.binary.Base64;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.Signature;
import java.security.SignatureException;

public class OAuthSigner {

	private PrivateKey privateKey;

	public OAuthSigner setPrivateKey(PrivateKey privateKey) {
		
		if(privateKey == null){
			throw new RuntimeException("Private key cannot be null");
		}
		
		this.privateKey = privateKey;
		return this;
	}
	
	public String generateSignature(String baseString) {
		try {
			Signature signature = Signature.getInstance("SHA1withRSA");
			signature.initSign(privateKey);
			signature.update(baseString.getBytes("UTF-8"));
			return Base64.encodeBase64String(signature.sign());

		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (SignatureException e) {
			e.printStackTrace();
		} catch (InvalidKeyException e) {
			e.printStackTrace();
		}
		return null;
	}
	
}
