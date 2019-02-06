package com.vormetric.rest.helperclasses;

import java.net.MalformedURLException;
import java.net.URL;
import java.security.cert.Certificate;
import java.io.*;

import java.net.URLConnection;
import java.net.UnknownHostException;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLPeerUnverifiedException;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.log4j.Logger;

import com.jayway.jsonpath.JsonPath;

import javax.net.ssl.TrustManager;
import java.security.cert.Certificate;
import java.security.cert.X509Certificate;
import java.security.SecureRandom;
import javax.net.ssl.HostnameVerifier;

import java.util.Base64;
import java.net.URL;
import java.net.URLConnection;

public class VormetricCryptoServerHelper {

	static Logger log = Logger.getLogger(VormetricCryptoServerHelper.class.getName());
	
	/*
	 * public static void main(String[] args) throws Exception {
	 * disableCertValidation();
	 * 
	 * String con = getSecurityToken("192.168.159.141", "vtsroot",
	 * "Vormetric123!"); System.out.println("security token " + con);
	 * 
	 * }
	 */
	// Return open HTTP connection for provided URL
	public static HttpsURLConnection getUrlConnection(String tokenserver, String action) throws Exception {

		String java_version = System.getProperty("java.version");
		if (java_version.contains("1.8")) {

		} else {

		}

		String https_url = null;
		if (action == "tokenize") {
			https_url = "https://" + tokenserver + "/vts/rest/v2.0/tokenize/";
		} else if (action == "detokenize") {
			https_url = "https://" + tokenserver + "/vts/rest/v2.0/detokenize/";
		} else if (action == "encrypt") {
			https_url = "https://" + tokenserver + "/vts/crypto/v1/encrypt";
		} else if (action == "decrypt") {
			https_url = "https://" + tokenserver + "/vts/crypto/v1/decrypt";
		} else if (action == "batch") {
			https_url = "https://" + tokenserver + "/vts/crypto/v1/batch";
		} else {
			System.out.println("Bad action code ");
			System.exit(1);
		}

		URL url = new URL(https_url);
		HttpsURLConnection con = (HttpsURLConnection) url.openConnection();

		return con;

	}

	public static void disableCertValidation() throws Exception {
		TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
			public java.security.cert.X509Certificate[] getAcceptedIssuers() {
				return null;
			}

			public void checkClientTrusted(java.security.cert.X509Certificate[] certs, String authType) {
			}

			public void checkServerTrusted(java.security.cert.X509Certificate[] certs, String authType) {
			}
		} };

		// Install the all-trusting trust manager
		try {
			SSLContext sc = SSLContext.getInstance("SSL");
			sc.init(null, trustAllCerts, new java.security.SecureRandom());
			HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
		} catch (Exception e) {
			e.printStackTrace();
		}

		// Create all-trusting host name verifier
		HostnameVerifier allHostsValid = new HostnameVerifier() {
			public boolean verify(String hostname, SSLSession session) {
				return true;
			}
		};

		// Install the all-trusting host verifier
		HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);
	}

	public static String doDeCryptDataArray(String tokenserver, String userid, String password, String[] inputdata,
			String alg, String ivnumber, String action, String encryptdecryptkey) throws Exception {

		disableCertValidation();
		HttpsURLConnection con = getUrlConnection(tokenserver, action);

		String encidpwd = getEncodedIdPassword(userid, password);
		String payloadarray = "[";
		for (int i = 0; i < inputdata.length; i++) {
			payloadarray = payloadarray + "{\"ciphertext\":\"" + inputdata[i] + "\",\"alg\":\"" + alg
					+ "\",\"params\" : {\"iv\":\"" + Base64.getEncoder().encodeToString(ivnumber.getBytes("UTF-8"))
					+ "\"},\"kid\": \"" + encryptdecryptkey + "\"}";
			payloadarray = payloadarray + ",";
	}
	payloadarray = payloadarray.substring(0,payloadarray.length()-1);

		con.setRequestMethod("POST");
		con.setRequestProperty("Authorization", "Basic " + encidpwd);
		con.setDoOutput(true);

		OutputStreamWriter output = new OutputStreamWriter(con.getOutputStream());
		output.write(payloadarray);
		output.close();

		BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()));

		String brdata;
		StringBuffer returnvalue = new StringBuffer();
		while ((brdata = br.readLine()) != null) {
			returnvalue.append(brdata);
		}
		System.out.println("Base64 Decrypted payload: " + returnvalue.toString());
		String base64string = JsonPath.read(returnvalue.toString(), "$..plaintext").toString();
		// byte[] base64bytes = Base64.decodeBase64(base64string);
		byte[] base64bytes = Base64.getDecoder().decode(base64string);
		String base64originaldata = new String(base64bytes);

		System.out.println("Decrypted  data " + base64originaldata);
		br.close();

		con.disconnect();
		return base64originaldata;
	}

	public static String doEncryptData(String tokenserver, String userid, String password, String inputdata, String alg,
			String ivnumber, String action, String encryptdecryptkey) throws Exception {

		disableCertValidation();
		HttpsURLConnection con = getUrlConnection(tokenserver, action);

		String encidpwd = getEncodedIdPassword(userid, password);

		String payload = "{\"plaintext\":\"" + Base64.getEncoder().encodeToString(inputdata.getBytes("UTF-8"))
				+ "\",\"alg\":\"" + alg + "\",\"params\" : {\"iv\":\""
				+ Base64.getEncoder().encodeToString(ivnumber.getBytes("UTF-8")) + "\"},\"kid\": \"" + encryptdecryptkey
				+ "\"}";

		System.out.println("payload = " + payload);
		
		con.setRequestMethod("POST");
		con.setRequestProperty("Authorization", "Basic " + encidpwd);
		con.setDoOutput(true);

		OutputStreamWriter output = new OutputStreamWriter(con.getOutputStream());
		output.write(payload);
		output.close();

		BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()));

		String brdata;
		StringBuffer returnvalue = new StringBuffer();
		while ((brdata = br.readLine()) != null) {
			returnvalue.append(brdata);
		}
		System.out.println("Base64 Encrypted payload: " + returnvalue.toString());
		String base64string = JsonPath.read(returnvalue.toString(), "$.ciphertext").toString();

		System.out.println("CipherText  data " + base64string);

		br.close();
		con.disconnect();
		return base64string;

	}

	// Encode ID and Password
	public static String getSecurityToken(String tokenserver, String userid, String password) throws IOException {

		String https_url = "https://" + tokenserver + "/api/api-token-auth/";
		URL url = new URL(https_url);
		HttpsURLConnection con = (HttpsURLConnection) url.openConnection();
		con.setRequestProperty("Content-Type", "application/json");
		// con.setRequestProperty("username", userid);
		// con.setRequestProperty("password", password);
		con.setRequestMethod("POST");
		String encidpwd = getEncodedIdPassword(userid, password);
		con.setRequestProperty("Authorization", "Basic " + encidpwd);
		con.setDoOutput(false);

		BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()));

		String brdata;
		StringBuffer returnvalue = new StringBuffer();
		while ((brdata = br.readLine()) != null) {
			returnvalue.append(brdata);
		}
		System.out.println("return payload: " + returnvalue.toString());
		String base64string = JsonPath.read(returnvalue.toString(), "$.token").toString();

		System.out.println("token  data " + base64string);

		br.close();
		con.disconnect();

		return base64string;
	}

	// Encode ID and Password
	public static String getEncodedIdPassword(String userid, String password) {

		String idpassword = userid + ":" + password;
		String encodedidpwd = null;

		try {
			encodedidpwd = Base64.getEncoder().encodeToString(idpassword.getBytes("UTF-8"));
		} catch (Exception e) {
			System.out.println("ID and Password encoding failure");
			e.printStackTrace();
		}

		return encodedidpwd;
	}

	// detokenize array of tokendata values
	public static String doDeTokenizeArray(String tokenserver, String userid, String password, String tokengroup,
			String tokentemplate, String[] dataarray, String action) throws Exception {

		VormetricCryptoServerHelper.disableCertValidation();
		HttpsURLConnection con = VormetricCryptoServerHelper.getUrlConnection(tokenserver, action);

		String encidpwd = VormetricCryptoServerHelper.getEncodedIdPassword(userid, password);

		con.setRequestMethod("POST");
		con.setRequestProperty("Authorization", "Basic " + encidpwd);
		con.setDoOutput(true);

		OutputStreamWriter output = new OutputStreamWriter(con.getOutputStream());

		String payloadarray = "[";
		for (int i = 0; i < dataarray.length; i++) {
			payloadarray = payloadarray + "{\"token\":\"" + dataarray[i] + "\",\"tokengroup\":\"" + tokengroup
					+ "\",\"tokentemplate\":\"" + tokentemplate + "\"}";
			payloadarray = payloadarray + ",";
		}
		payloadarray = payloadarray.substring(0,payloadarray.length()-1);
		
		payloadarray = payloadarray + "]";

		String originaldata = JsonPath.read(payloadarray.toString(), "$..data").toString();
		
		output.write(payloadarray);
		output.close();

		BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()));

		String brdata;
		while ((brdata = br.readLine()) != null) {
			System.out.println(brdata);
		}

		br.close();
		con.disconnect();
		
		return originaldata;

	}

	// Detokenize single data token
	public static String doDeTokenizeData(String tokenserver, String userid, String password, String tokengroup,
			String tokentemplate, String data, String action) throws Exception {

		VormetricCryptoServerHelper.disableCertValidation();
		HttpsURLConnection con = VormetricCryptoServerHelper.getUrlConnection(tokenserver, action);

		String encidpwd = VormetricCryptoServerHelper.getEncodedIdPassword(userid, password);

		String payload = "{\"token\":\"" + data + "\",\"tokengroup\":\"" + tokengroup + "\",\"tokentemplate\":\""
				+ tokentemplate + "\"}";

		con.setRequestMethod("POST");
		con.setRequestProperty("Authorization", "Basic " + encidpwd);
		con.setDoOutput(true);

		OutputStreamWriter output = new OutputStreamWriter(con.getOutputStream());
		output.write(payload);
		output.close();

		BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()));

		String brdata;
		StringBuffer returnvalue = new StringBuffer();
		while ((brdata = br.readLine()) != null) {
			returnvalue.append(brdata);
		}
		System.out.println("Original data payload: " + returnvalue.toString());
		String originaldata = JsonPath.read(returnvalue.toString(), "$.data").toString();

		System.out.println("Original data " + originaldata);

		br.close();
		con.disconnect();

		return originaldata;
	}

	// Tokenize array of data values (1000 max)
	public static String doTokenizeArray(String tokenserver, String userid, String password, String tokengroup,
			String tokentemplate, String[] dataarray, String action) throws Exception {

		disableCertValidation();
		HttpsURLConnection con = getUrlConnection(tokenserver, action);

		String encidpwd = getEncodedIdPassword(userid, password);

		con.setRequestMethod("POST");
		con.setRequestProperty("Authorization", "Basic " + encidpwd);
		con.setDoOutput(true);

		OutputStreamWriter output = new OutputStreamWriter(con.getOutputStream());

		String payloadarray = "[";
		for (int i = 0; i < dataarray.length; i++) {
			payloadarray = payloadarray + "{\"data\":\"" + dataarray[i] + "\",\"tokengroup\":\"" + tokengroup
					+ "\",\"tokentemplate\":\"" + tokentemplate + "\"}";
			payloadarray = payloadarray + ",";
		}
		payloadarray = payloadarray.substring(0,payloadarray.length()-1);
		
		payloadarray = payloadarray + "]";

		String tokendata = JsonPath.read(payloadarray.toString(), "$..token").toString();
		output.write(payloadarray);
		output.close();

		BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()));

		String brdata;
		while ((brdata = br.readLine()) != null) {
			System.out.println(brdata);
		}

		br.close();
		con.disconnect();
		
		return tokendata;

	}

	// Tokenize single data value
	public static String doTokenizeData(String tokenserver, String userid, String password, String tokengroup,
			String tokentemplate, String data, String action) throws Exception {

		VormetricCryptoServerHelper.disableCertValidation();
		HttpsURLConnection con = VormetricCryptoServerHelper.getUrlConnection(tokenserver, action);
		System.out.println("url = " + con.toString());
		String encidpwd = VormetricCryptoServerHelper.getEncodedIdPassword(userid, password);

		String payload = "{\"data\":\"" + data + "\",\"tokengroup\":\"" + tokengroup + "\",\"tokentemplate\":\""
				+ tokentemplate + "\"}";

		con.setRequestMethod("POST");
		con.setRequestProperty("Authorization", "Basic " + encidpwd);
		con.setDoOutput(true);

		OutputStreamWriter output = new OutputStreamWriter(con.getOutputStream());
		output.write(payload);
		output.close();

		BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()));
		String brdata;
		StringBuffer returnvalue = new StringBuffer();
		while ((brdata = br.readLine()) != null) {
			returnvalue.append(brdata);
		}
		System.out.println("Tokenized payload: " + returnvalue.toString());
		String tokenvalue = JsonPath.read(returnvalue.toString(), "$.token").toString();

		System.out.println("Tokendata " + tokenvalue);
		br.close();
		con.disconnect();

		return tokenvalue;

	}

	public static String doDeCryptData(String tokenserver, String userid, String password, String inputdata, String alg,
			String ivnumber, String action, String encryptdecryptkey) throws Exception {

		disableCertValidation();
		HttpsURLConnection con = getUrlConnection(tokenserver, action);

		String encidpwd = getEncodedIdPassword(userid, password);

		String payload = "{\"ciphertext\":\"" + inputdata + "\",\"alg\":\"" + alg + "\",\"params\" : {\"iv\":\""
				+ Base64.getEncoder().encodeToString(ivnumber.getBytes("UTF-8")) + "\"},\"kid\": \"" + encryptdecryptkey
				+ "\"}";
System.out.println("payload = " + payload);
		con.setRequestMethod("POST");
		con.setRequestProperty("Authorization", "Basic " + encidpwd);
		con.setDoOutput(true);

		OutputStreamWriter output = new OutputStreamWriter(con.getOutputStream());
		output.write(payload);
		output.close();

		BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()));

		String brdata;
		StringBuffer returnvalue = new StringBuffer();
		while ((brdata = br.readLine()) != null) {
			returnvalue.append(brdata);
		}
		System.out.println("Base64 Decrypted payload: " + returnvalue.toString());
		String base64string = JsonPath.read(returnvalue.toString(), "$.plaintext").toString();
		// byte[] base64bytes = Base64.decodeBase64(base64string);
		byte[] base64bytes = Base64.getDecoder().decode(base64string);
		String base64originaldata = new String(base64bytes);

		System.out.println("Decrypted  data " + base64originaldata);
		br.close();

		con.disconnect();
		return base64originaldata;
	}

	public static String doEncryptDataArray(String tokenserver, String userid, String password, String[] inputdata,
			String alg, String ivnumber, String action, String encryptdecryptkey) throws Exception {

		disableCertValidation();
		HttpsURLConnection con = getUrlConnection(tokenserver, action);

		String encidpwd = getEncodedIdPassword(userid, password);
		String payloadarray = "[";
		for (int i = 0; i < inputdata.length; i++) {

			payloadarray = payloadarray + "{\"encrypt\": {\"plaintext\":\""
					+ Base64.getEncoder().encodeToString(inputdata[i].getBytes("UTF-8")) + "\",\"alg\":\"" + alg
					+ "\",\"params\" : {\"iv\":\"" + Base64.getEncoder().encodeToString(ivnumber.getBytes("UTF-8"))
					+ "\"},\"kid\": \"" + encryptdecryptkey + "\"}}";
		
				payloadarray = payloadarray + ",";
		}
		payloadarray = payloadarray.substring(0,payloadarray.length()-1);
		payloadarray = payloadarray + "]";
System.out.println("payloadarray = " + payloadarray);
		con.setRequestMethod("POST");
		con.setRequestProperty("Authorization", "Basic " + encidpwd);
		con.setDoOutput(true);

		OutputStreamWriter output = new OutputStreamWriter(con.getOutputStream());
		output.write(payloadarray);
		output.close();

		BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()));

		String brdata;
		StringBuffer returnvalue = new StringBuffer();
		while ((brdata = br.readLine()) != null) {
			returnvalue.append(brdata);
		}
		System.out.println("Base64 Encrypted payload: " + returnvalue.toString());

		String base64string = JsonPath.read(returnvalue.toString(), "$..ciphertext").toString();

		System.out.println("CipherText  data " + base64string);

		br.close();
		con.disconnect();
		return base64string;

	}

}
