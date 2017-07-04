package com.crunchify.common;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSocketFactory;

import org.springframework.stereotype.Service;

@Service
public class Connector {

	public static final String AUTHORIZATION = "Authorization";
	public static final String CONTENT_TYPE = "content-type";
	public static final String CONTENT_LENGTH = "content-length";
	public static final String APPLICATION_XML = "application/xml; charset=utf-8";
	public static final int SC_MULTIPLE_CHOICES = 300;
	public static final String HTML_TAG = "<html>";
	public static final String BODY_OPENING_TAG = "<body>";
	public static final String BODY_CLOSING_TAG = "</body>";
	public static final String MESSAGE = "Message";
	public static final String HTTP_CODE = "HttpCode";

	public Map<String, String> doRequest(String httpsURL,
			String requestMethod, String headers, String body) {

		HttpsURLConnection con = null;
		try {
			System.out.println("Entry into Connectot doRequest");
			con = setupConnection(httpsURL, requestMethod, headers, body);
			con.connect();
			if (body != null) {
				writeBodyToConnection(body, con);
			}

			return checkForErrorsAndReturnResponse(con);
		} catch (IOException e) {
			throw new RuntimeException();

		} finally {
			if (con != null) {
				con.disconnect();
			}
		}
	}

	protected HttpsURLConnection setupConnection(String httpsURL,
			String requestMethod, String header, String body) {

		HttpsURLConnection con;
		URL url;
		System.out.println("Entry into setupConnection");
		try {
			url = new URL(httpsURL);
		} catch (MalformedURLException e) {
			throw new RuntimeException();
		}
		try {
			con = (HttpsURLConnection) url.openConnection();
		} catch (IOException e) {
			throw new RuntimeException();
		}
		try {
			con.setRequestMethod(requestMethod);
		} catch (ProtocolException e) {
			throw new RuntimeException();
		}
		
		con.setSSLSocketFactory((SSLSocketFactory) SSLSocketFactory
				.getDefault());
		con.setDoOutput(true);
		con.setDoInput(true);
		con.addRequestProperty(AUTHORIZATION, header);
		con.setRequestProperty("User-Agent",
				"MC Open API OAuth Framework v1.0-Java");
		System.out.println("Connection set");
		if (body != null) {
			con.addRequestProperty(CONTENT_TYPE, APPLICATION_XML);
			con.addRequestProperty(CONTENT_LENGTH,
					Integer.toString(body.length()));
		}

		return con;
	}

	protected Map<String, String> checkForErrorsAndReturnResponse(
			HttpsURLConnection connection) {
		try {
			System.out.println("Entry into checkForErrorsAndReturnResponse");
			if (connection.getResponseCode() >= SC_MULTIPLE_CHOICES) {
				String message = readResponse(connection.getErrorStream());
				// Cut the html off of the error message and leave the body
				if (message.contains(HTML_TAG)) {
					message = message.substring(
							message.indexOf(BODY_OPENING_TAG) + 6,
							message.indexOf(BODY_CLOSING_TAG));
				}
				throw new RuntimeException();
			} else {
				Map<String, String> responseMap = new HashMap<String, String>();
				responseMap.put(MESSAGE,
						readResponse(connection.getInputStream()));
				responseMap.put(HTTP_CODE,
						String.valueOf(connection.getResponseCode()));
				System.out.println("Response Map " + responseMap);
				return responseMap;
			}
		} catch (IOException e) {
			throw new RuntimeException();
		}
	}

	protected String readResponse(InputStream in) {
		BufferedReader reader = new BufferedReader(new InputStreamReader(in));
		String inputLine;
		StringBuilder builder = new StringBuilder();

		try {
			while ((inputLine = reader.readLine()) != null) {
				builder.append(inputLine);
			}
		} catch (IOException e) {
			throw new RuntimeException();
		}
		return builder.toString();
	}

	protected void writeBodyToConnection(String body, HttpsURLConnection con)
			throws IOException {
		OutputStreamWriter request = null;
		try {
			CharsetEncoder encoder = Charset.forName("UTF-8").newEncoder();
			request = new OutputStreamWriter(con.getOutputStream(), encoder);
			request.append(body);
			request.flush();
		} catch (IOException e) {
			throw new IOException(e);
		} finally {
			if (request != null) {
				request.close();
			}
		}
	}
}
