package com.vormetric.rest.sample;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Collections;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;


import com.jayway.jsonpath.JsonPath;

//import net.minidev.json.JSONArray;

public class VormetricDSMHelper {

	public static void main(String[] argv) throws Exception {

		ArrayList<String> testdname = getDomainObjectAttributeList2("192.168.159.130", "superadmin", "yourpwd", "",
				"domains", "description");
		System.out.println("length " + testdname);
		
		 String domainparts [] = "Test domain-1034".split("-");
		 
		 for (int i = 0; i < domainparts.length; i++) {
			 System.out.println(domainparts[i]);
			
		}
		
/*		String testid[] = getDomainObjectAttributeList("192.168.159.130", "superadmin", "yourpwd", "",
				"domains", "id");
		System.out.println("length " + testid.length);
		for (int i = 0; i < testid.length; i++) {
			System.out.println(testid[i]);
		}
		
		String test[] = getDomainObjectAttributeList("192.168.159.130", "superadmin", "yourpwd", "1000/hosts",
				"domains", "description");
		System.out.println("length " + test.length);
		for (int i = 0; i < test.length; i++) {
			System.out.println(test[i]);
		}
		//keys/asymmetric/
		
		String test2[] = getDomainObjectAttributeList("192.168.159.130", "superadmin", "yourpwd", "1000/keys/asymmetric/",
				"domains", "description");
		System.out.println("length " + test2.length);
		for (int i = 0; i < test2.length; i++) {
			System.out.println(test2[i]);
		}*/
		
	//	System.out.println("test results " + test);
		//test = getKeyAttribute("192.168.159.130", "superadmin", "yourpwd", "1000/keys/asymmetric/286", "domains",
			//	"publicKey");
	//	System.out.println("test results " + test);
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

	// Return open HTTP connection for provided URL
	public static HttpsURLConnection getUrlConnection(String dsm, String action, String endpt) throws Exception {

		String java_version = System.getProperty("java.version");
		if (java_version.contains("1.8")) {

		} else {

		}

		String https_url = null;
		if (action == "log") {
			https_url = "https://" + dsm + "/dsm/v1/log/" + endpt;
		} else if (action == "system") {
			https_url = "https://" + dsm + "/dsm/v1/system/" + endpt;
		} else if (action == "domains") {
			https_url = "https://" + dsm + "/dsm/v1/domains/" + endpt + "?limit=100&sort=name";
		} else if (action == "admin") {
			https_url = "https://" + dsm + "/dsm/v1/admin/" + endpt;
		} else {
			System.out.println("Bad action code ");
			System.exit(1);
		}

		URL url = new URL(https_url);
		HttpsURLConnection con = (HttpsURLConnection) url.openConnection();

		return con;

	}
	// system
	// https://{{dsm}}/dsm/v1/system/configs

	// vault
	// https://{{dsm}}/dsm/v1/domains/1000/keys/vault/asymmetric/

	// keys list
	// https://{{dsm}}/dsm/v1/domains/1000/keys/symmetric/
	// public key works
	// https://{{dsm}}/dsm/v1/domains/1000/keys/asymmetric/

	// keys single attribute
	// https://{{dsm}}/dsm/v1/domains/1000/keys/symmetric/
	// public key works
	// https://{{dsm}}/dsm/v1/domains/1000/keys/asymmetric/286

	// log
	// https://{{dsm}}/dsm/v1/log/boot.log

	// host
	// https://{{dsm}}/dsm/v1/domains/1000/hosts/all

	public static String getDomainObjectAttribute(String dsmserver, String userid, String password, String endpt, String action,
			String attrib) throws Exception {

		disableCertValidation();

		HttpsURLConnection con = getUrlConnection(dsmserver, action, endpt);

		String encidpwd = getEncodedIdPassword(userid, password);

		con.setRequestMethod("GET");
		con.setRequestProperty("Authorization", "Basic " + encidpwd);
		con.setDoOutput(false);

		BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()));

		String brdata;
		StringBuffer returnvalue = new StringBuffer();
		while ((brdata = br.readLine()) != null) {
			returnvalue.append(brdata);
		}
		System.out.println(" results " + returnvalue);
		String results = JsonPath.read(returnvalue.toString(), attrib).toString();

		System.out.println("Return value is " + results);
		br.close();

		con.disconnect();
		return results;
	}
	

	public static ArrayList<String> getDomainObjectAttributeList2(String dsmserver, String userid, String password, String endpt, String action,
			String attrib) throws Exception {

		disableCertValidation();

		HttpsURLConnection con = getUrlConnection(dsmserver, action, endpt);

		String encidpwd = getEncodedIdPassword(userid, password);

		con.setRequestMethod("GET");
		con.setRequestProperty("Authorization", "Basic " + encidpwd);
		con.setDoOutput(false);

		BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()));

		String brdata;
		StringBuffer returnvalue = new StringBuffer();
		while ((brdata = br.readLine()) != null) {
			returnvalue.append(brdata);
		}
		
		String resultsstr = JsonPath.read(returnvalue.toString(), "$.."+ attrib).toString();
		resultsstr = resultsstr.replace("[", "");
		resultsstr = resultsstr.replace("]", "");
		String [] resultslist = resultsstr.split(",");
		
		System.out.println(returnvalue.toString());
		String idstr = JsonPath.read(returnvalue.toString(), "$.."+ "id").toString();
		idstr = idstr.replace("[", "");
		idstr = idstr.replace("]", "");
		String [] idstrlist = idstr.split(",");
		
        ArrayList<String> al = new ArrayList<String>(); 	
		for (int i = 0; i < resultslist.length; i++) {
			al.add(resultslist[i] + "-" + idstrlist[i]);
		}


        Collections.sort(al); 
        
		System.out.println(" resultsstr " + resultsstr);

		br.close();

		con.disconnect();
		return  al;
	}
	
	
	public static ArrayList<String> getDomainObjectAttributeList(String dsmserver, String userid, String password, String endpt, String action,
			String attrib) throws Exception {

		disableCertValidation();

		HttpsURLConnection con = getUrlConnection(dsmserver, action, endpt);

		String encidpwd = getEncodedIdPassword(userid, password);

		con.setRequestMethod("GET");
		con.setRequestProperty("Authorization", "Basic " + encidpwd);
		con.setDoOutput(false);

		BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()));

		String brdata;
		StringBuffer returnvalue = new StringBuffer();
		while ((brdata = br.readLine()) != null) {
			returnvalue.append(brdata);
		}
		
		String resultsstr = JsonPath.read(returnvalue.toString(), "$.."+ attrib).toString();
		resultsstr = resultsstr.replace("[", "");
		resultsstr = resultsstr.replace("]", "");
		String [] resultslist = resultsstr.split(",");
		
        ArrayList<String> al = new ArrayList<String>(); 	
		for (int i = 0; i < resultslist.length; i++) {
			al.add(resultslist[i]);
		}


        Collections.sort(al); 
        
		System.out.println(" resultsstr " + resultsstr);

		br.close();

		con.disconnect();
		return  al;
	}

	public static String getDomainObjectAttributeListStr(String dsmserver, String userid, String password, String endpt, String action,
			String attrib) throws Exception {
	
		disableCertValidation();
	
		HttpsURLConnection con = getUrlConnection(dsmserver, action, endpt);
	
		String encidpwd = getEncodedIdPassword(userid, password);
	
		con.setRequestMethod("GET");
		con.setRequestProperty("Authorization", "Basic " + encidpwd);
		con.setDoOutput(false);
	
		BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()));
	
		String brdata;
		StringBuffer returnvalue = new StringBuffer();
		while ((brdata = br.readLine()) != null) {
			returnvalue.append(brdata);
		}
		
		String resultsstr = JsonPath.read(returnvalue.toString(), "$.."+ attrib).toString();
		resultsstr = resultsstr.replace("[", "");
		resultsstr = resultsstr.replace("]", "");
		System.out.println(" resultsstr " + resultsstr);
	
		br.close();
	
		con.disconnect();
		return resultsstr;
	}
	
}
