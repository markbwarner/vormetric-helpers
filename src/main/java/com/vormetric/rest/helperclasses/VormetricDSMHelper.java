package com.vormetric.rest.helperclasses;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import org.apache.log4j.Logger;

import com.jayway.jsonpath.JsonPath;

public class VormetricDSMHelper {

	String dsmdebug = "1";

	static Logger log = Logger.getLogger(VormetricCryptoServerSettings.class.getName());

	String dsmuserid = "superadmin";
	String dsmpwd = "yourpwd";
	String dsmip = "192.168.159.130";
	String dsmkeyfilter = "first";
	String dsmusekeyfilter = "0";

	public String getDsmkeyfilter() {
		return dsmkeyfilter;
	}

	public void setDsmkeyfilter(String dsmkeyfilter) {
		this.dsmkeyfilter = dsmkeyfilter;
	}

	public String getDsmusekeyfilter() {
		return dsmusekeyfilter;
	}

	public void setDsmusekeyfilter(String dsmusekeyfilter) {
		this.dsmusekeyfilter = dsmusekeyfilter;
	}

	TreeMap<String, String> tmap = new TreeMap<>();

	public VormetricDSMHelper() {
		super();
		Map<String, String> env = System.getenv();
		System.out.println("name of logger" + log.getName());
		log.debug("this is a test");
		log.info("this is a test");
		for (String envName : env.keySet()) {
			if (envName.equalsIgnoreCase("dsmuserid")) {
				dsmuserid = env.get(envName);
				if (dsmdebug.equalsIgnoreCase("1"))
					System.out.println("dsmuserid=" + dsmuserid);
			} else if (envName.equalsIgnoreCase("dsmpwd")) {
				dsmpwd = env.get(envName);
				if (dsmdebug.equalsIgnoreCase("1"))
					System.out.println("dsmpwd=" + dsmpwd);
			} else if (envName.equalsIgnoreCase("dsmip")) {
				dsmip = env.get(envName);
				if (dsmdebug.equalsIgnoreCase("1"))
					System.out.println("dsmip=" + dsmip);
			} else if (envName.equalsIgnoreCase("dsmkeyfilter")) {
				dsmkeyfilter = env.get(envName);
				if (dsmdebug.equalsIgnoreCase("1"))
					System.out.println("dsmkeyfilter=" + dsmkeyfilter);
			} else if (envName.equalsIgnoreCase("dsmusekeyfilter")) {
				dsmusekeyfilter = env.get(envName);
				if (dsmdebug.equalsIgnoreCase("1"))
					System.out.println("dsmusekeyfilter=" + dsmusekeyfilter);
			} else if (envName.equalsIgnoreCase("vcsdebug")) {
				dsmdebug = env.get(envName);
				if (dsmdebug.equalsIgnoreCase("1"))
					System.out.println("vcsdebug=" + dsmdebug);
			}
			if (dsmdebug.equalsIgnoreCase("1"))
				System.out.format("%s=%s%n", envName, env.get(envName));
		}
		// TODO Auto-generated constructor stub
	}

	public VormetricDSMHelper(String dsmuserid, String dsmpwd, String dsmip) {
		super();
		this.dsmuserid = dsmuserid;
		this.dsmpwd = dsmpwd;
		this.dsmip = dsmip;
	}

	public static String[] keysAsArray(Map<String, String> map) {
		return map.keySet().toArray(new String[map.keySet().size()]);
	}

	public static String[] valuesAsArray(Map<String, String> map) {
		return map.values().toArray(new String[map.keySet().size()]);
	}

	// assumes the key is of type String
	public static List<String> keysAsList(Map<String, String> map) {
		List<String> list = new ArrayList<String>(map.keySet());
		return list;
	}

	public static List<String> valuesAsList(Map<String, String> map) {
		List<String> list = new ArrayList<String>(map.values());
		return list;
	}

	public static void main(String[] argv) throws Exception {

		/*
		 * ArrayList<String> testdname =
		 * getDomainObjectAttributeList2("192.168.159.130", "superadmin",
		 * "Admin123!", "", "domains", "description");
		 * System.out.println("length " + testdname);
		 * 
		 * String domainparts [] = "Test domain-1034".split("-");
		 * 
		 * for (int i = 0; i < domainparts.length; i++) {
		 * System.out.println(domainparts[i]);
		 * 
		 * }
		 */

		/*
		 * TreeMap<String,String> tm =
		 * getDomainObjectAttributeListObj2("192.168.159.130", "superadmin",
		 * "Admin123!", "", "domains", "description");
		 * 
		 * keysAsList(tm).forEach(System.out::println);
		 * valuesAsList(tm).forEach(System.out::println);
		 */

		TreeMap<String, String> tm1 = getDomainObjectAttributeObj("192.168.159.130", "superadmin", "Admin123!",
				"1000/keys/asymmetric/286", "domains", "publicKey");

		keysAsList(tm1).forEach(System.out::println);
		valuesAsList(tm1).forEach(System.out::println);

		/*
		 * String testid[] = getDomainObjectAttributeList("192.168.159.130",
		 * "superadmin", "Admin123!", "", "domains", "id");
		 * System.out.println("length " + testid.length); for (int i = 0; i <
		 * testid.length; i++) { System.out.println(testid[i]); }
		 * 
		 * String test[] = getDomainObjectAttributeList("192.168.159.130",
		 * "superadmin", "Admin123!", "1000/hosts", "domains", "description");
		 * System.out.println("length " + test.length); for (int i = 0; i <
		 * test.length; i++) { System.out.println(test[i]); } //keys/asymmetric/
		 * 
		 * String test2[] = getDomainObjectAttributeList("192.168.159.130",
		 * "superadmin", "Admin123!", "1000/keys/asymmetric/", "domains",
		 * "description"); System.out.println("length " + test2.length); for
		 * (int i = 0; i < test2.length; i++) { System.out.println(test2[i]); }
		 */

		// System.out.println("test results " + test);
		// test = getKeyAttribute("192.168.159.130", "superadmin", "Admin123!",
		// "1000/keys/asymmetric/286", "domains",
		// "publicKey");
		// System.out.println("test results " + test);
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
			//can also do wild card searches.
			//https_url = "https://" + dsm + "/dsm/v1/domains/" + endpt + "?limit=100&sort=name&name=v";	
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

	public static String getDomainObjectAttribute(String dsmserver, String userid, String password, String endpt,
			String action, String attrib) throws Exception {

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

	public static TreeMap<String, String> getDomainObjectAttributeObjR(String dsmserver, String userid, String password,
			String endpt, String action, String attrib) throws Exception {

		disableCertValidation();

		HttpsURLConnection con = getUrlConnection(dsmserver, action, endpt);
		TreeMap<String, String> tmap = new TreeMap<>();
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
		// System.out.println(" results " + returnvalue);
		String attribresults = JsonPath.read(returnvalue.toString(), attrib).toString();
		String idresults = JsonPath.read(returnvalue.toString(), "id").toString();
		// System.out.println("Return value is " + attribresults + " id " +
		// idresults);
		tmap.put(attribresults, idresults);
		br.close();

		con.disconnect();
		return tmap;
	}

	public static TreeMap<String, String> getDomainObjectAttributeObj(String dsmserver, String userid, String password,
			String endpt, String action, String attrib) throws Exception {

		disableCertValidation();

		HttpsURLConnection con = getUrlConnection(dsmserver, action, endpt);
		TreeMap<String, String> tmap = new TreeMap<>();
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
		// System.out.println(" results " + returnvalue);
		String attribresults = JsonPath.read(returnvalue.toString(), attrib).toString();
		String idresults = JsonPath.read(returnvalue.toString(), "id").toString();
		// System.out.println("Return value is " + attribresults + " id " +
		// idresults);
		tmap.put(idresults, attribresults);
		br.close();

		con.disconnect();
		return tmap;
	}

	public TreeMap<String, String> getDomainObjectAttributeListObj2(String dsmserver, String userid, String password,
			String endpt, String action, String attrib) throws Exception {

		disableCertValidation();

		HttpsURLConnection con = getUrlConnection(dsmserver, action, endpt);

		TreeMap<String, String> tmap = new TreeMap<>();

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

		String resultsstr = JsonPath.read(returnvalue.toString(), "$.." + attrib).toString();
		resultsstr = resultsstr.replace("[", "");
		resultsstr = resultsstr.replace("]", "");
		String[] resultslist = resultsstr.split(",");

		System.out.println(returnvalue.toString());
		String idstr = JsonPath.read(returnvalue.toString(), "$.." + "id").toString();
		idstr = idstr.replace("[", "");
		idstr = idstr.replace("]", "");
		String[] idstrlist = idstr.split(",");

		/*
		 * ArrayList<String> al = new ArrayList<String>(); for (int i = 0; i <
		 * resultslist.length; i++) { al.add(resultslist[i] + "-" +
		 * idstrlist[i]); }
		 */

		String temp = null;
		for (int i = 0; i < idstrlist.length; i++) {
			if (endpt.contains("keys")) {

				if (dsmusekeyfilter.equals("1") && !endpt.contains("vault")) {
					temp = resultslist[i].substring(1, resultslist[i].length()-1);
					if (temp.startsWith(dsmkeyfilter))
						tmap.put(resultslist[i], idstrlist[i]);
				} else
					tmap.put(resultslist[i], idstrlist[i]);
			} else
				tmap.put(resultslist[i], idstrlist[i]);
		}

		// Collections.sort(al);

		System.out.println(" resultsstr " + resultsstr);

		br.close();

		con.disconnect();
		return tmap;
	}

	public static ArrayList<String> getDomainObjectAttributeList2(String dsmserver, String userid, String password,
			String endpt, String action, String attrib) throws Exception {

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

		String resultsstr = JsonPath.read(returnvalue.toString(), "$.." + attrib).toString();
		resultsstr = resultsstr.replace("[", "");
		resultsstr = resultsstr.replace("]", "");
		String[] resultslist = resultsstr.split(",");

		System.out.println(returnvalue.toString());
		String idstr = JsonPath.read(returnvalue.toString(), "$.." + "id").toString();
		idstr = idstr.replace("[", "");
		idstr = idstr.replace("]", "");
		String[] idstrlist = idstr.split(",");

		ArrayList<String> al = new ArrayList<String>();
		for (int i = 0; i < resultslist.length; i++) {
			al.add(resultslist[i] + "-" + idstrlist[i]);
		}

		Collections.sort(al);

		System.out.println(" resultsstr " + resultsstr);

		br.close();

		con.disconnect();
		return al;
	}

	public static ArrayList<String> getDomainObjectAttributeList(String dsmserver, String userid, String password,
			String endpt, String action, String attrib) throws Exception {

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

		String resultsstr = JsonPath.read(returnvalue.toString(), "$.." + attrib).toString();
		resultsstr = resultsstr.replace("[", "");
		resultsstr = resultsstr.replace("]", "");
		String[] resultslist = resultsstr.split(",");

		ArrayList<String> al = new ArrayList<String>();
		for (int i = 0; i < resultslist.length; i++) {
			al.add(resultslist[i]);
		}

		Collections.sort(al);

		System.out.println(" resultsstr " + resultsstr);

		br.close();

		con.disconnect();
		return al;
	}

	public static String getDomainObjectAttributeListStr(String dsmserver, String userid, String password, String endpt,
			String action, String attrib) throws Exception {

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

		String resultsstr = JsonPath.read(returnvalue.toString(), "$.." + attrib).toString();
		resultsstr = resultsstr.replace("[", "");
		resultsstr = resultsstr.replace("]", "");
		System.out.println(" resultsstr " + resultsstr);

		br.close();

		con.disconnect();
		return resultsstr;
	}

	public String getDsmuserid() {
		return dsmuserid;
	}

	public void setDsmuserid(String dsmuserid) {
		this.dsmuserid = dsmuserid;
	}

	public String getDsmpwd() {
		return dsmpwd;
	}

	public void setDsmpwd(String dsmpwd) {
		this.dsmpwd = dsmpwd;
	}

	public String getDsmip() {
		return dsmip;
	}

	public void setDsmip(String dsmip) {
		this.dsmip = dsmip;
	}

}
