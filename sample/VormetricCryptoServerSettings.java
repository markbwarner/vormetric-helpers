package com.vormetric.rest.sample;

import java.util.Map;
import org.apache.log4j.Logger;

public class VormetricCryptoServerSettings {
	String vcsdebug = "1";
	
	static Logger log = Logger.getLogger(VormetricCryptoServerSettings.class.getName());
	
	
	public VormetricCryptoServerSettings() {
		super();
		 Map<String, String> env = System.getenv();
		    System.out.println("name of logger" + log.getName());
		 log.debug("this is a test");
		 log.info("this is a test");
	     for (String envName : env.keySet()) {
	    	 if (envName.equalsIgnoreCase("vcsuserid"))
	    	 {
	    		 vcsuserid= env.get(envName);
	    		  if (vcsdebug.equalsIgnoreCase("1"))	 System.out.println("vcsuserid=" + vcsuserid);
	    	 }
	    	 else if (envName.equalsIgnoreCase("vcspassword"))
	    	 {
	    		 vcspassword= env.get(envName);
	    		  if (vcsdebug.equalsIgnoreCase("1"))	 System.out.println("vcspassword=" + vcspassword);
	    	 }
	    	 else if (envName.equalsIgnoreCase("vcstokenserver"))
	    	 {
	    		 vcstokenserver= env.get(envName);
	    		  if (vcsdebug.equalsIgnoreCase("1"))	 System.out.println("vcstokenserver=" + vcstokenserver);
	    	 }
	    	 else if (envName.equalsIgnoreCase("vcsivnumber"))
	    	 {
	    		 vcsivnumber = env.get(envName);
	    		  if (vcsdebug.equalsIgnoreCase("1")) System.out.println("vcsivnumber=" + vcsivnumber);
	    	 }
	    	 else if (envName.equalsIgnoreCase("vcsencryptdecryptkey"))
	    	 {
	    		 vcsencryptdecryptkey = env.get(envName);
	    		  if (vcsdebug.equalsIgnoreCase("1"))	 System.out.println("vcsencryptdecryptkey=" + vcsencryptdecryptkey);
	    	 }
	    	 else if (envName.equalsIgnoreCase("vcsalg"))
	    	 {
	    		 vcsalg = env.get(envName);
	    		  if (vcsdebug.equalsIgnoreCase("1"))	 System.out.println("vcsalg=" + vcsalg);
	    	 }
	    	 else if (envName.equalsIgnoreCase("vcstokengroup"))
	    	 {
	    		 vcstokengroup  = env.get(envName);
	    		  if (vcsdebug.equalsIgnoreCase("1"))	 System.out.println("vcstokengroup =" + vcstokengroup );
	    	 }
	    	 else if (envName.equalsIgnoreCase("vcstokentemplate"))
	    	 {
	    		 vcstokentemplate = env.get(envName);
	    		  if (vcsdebug.equalsIgnoreCase("1"))	 System.out.println("vcstokentemplate=" + vcstokentemplate);
	    	 }
	    	 else if (envName.equalsIgnoreCase("vcsdebug"))
	    	 {
	    		 vcsdebug = env.get(envName);
	    		  if (vcsdebug.equalsIgnoreCase("1")) System.out.println("vcsdebug=" + vcsdebug);
	    	 }
	     if (vcsdebug.equalsIgnoreCase("1")) System.out.format("%s=%s%n", envName, env.get(envName));
	     }
	}

	
	public VormetricCryptoServerSettings(String vcstokenserver, String vcsuserid, String vcspassword) {
		super();
		this.vcstokenserver = vcstokenserver;
		this.vcsuserid = vcsuserid;
		this.vcspassword = vcspassword;
	}

	String vcstokenserver = "192.168.159.141";
	String vcsuserid = "vtsroot";
	String vcspassword = "yourpwd";
	String data = null;
	String[] dataarray = null;
	String vcstokengroup = "TextTokenGroup";
	String vcstokentemplate = "Text";
	String vcsaction = "encrypt";
	
	 String vcsalg = "A128CTR";
	 String vcsivnumber = "0123456789012345";
	 String vcsivtext = "Thisisatestonlya";
	 String vcsencryptdecryptkey = "firstkeyviarest128";
	 
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

	public String getvcstokenserver() {
		return vcstokenserver;
	}

	public void setvcstokenserver(String vcstokenserver) {
		this.vcstokenserver = vcstokenserver;
	}

	public String getvcsuserid() {
		return vcsuserid;
	}

	public void setvcsuserid(String vcsuserid) {
		this.vcsuserid = vcsuserid;
	}

	public String getvcspassword() {
		return vcspassword;
	}

	public void setvcspassword(String vcspassword) {
		this.vcspassword = vcspassword;
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

	public String[] getDataarray() {
		return dataarray;
	}

	public void setDataarray(String[] dataarray) {
		this.dataarray = dataarray;
	}

	public String getvcsTokengroup() {
		return vcstokengroup;
	}

	public void setvcsTokengroup(String tokengroup) {
		this.vcstokengroup = tokengroup;
	}

	public String getvcsTokentemplate() {
		return vcstokentemplate;
	}

	public void setvcsTokentemplate(String tokentemplate) {
		this.vcstokentemplate = tokentemplate;
	}

	public String getvcsAction() {
		return vcsaction;
	}

	public void setvcsAction(String action) {
		this.vcsaction = action;
	}

	public String getvcsalg() {
		return vcsalg;
	}

	public void setvcsalg(String vcsalg) {
		this.vcsalg = vcsalg;
	}

	public String getvcsivnumber() {
		return vcsivnumber;
	}

	public void setvcsivnumber(String vcsivnumber) {
		this.vcsivnumber = vcsivnumber;
	}

	public String getvcsivtext() {
		return vcsivtext;
	}

	public void setvcsivtext(String vcsivtext) {
		this.vcsivtext = vcsivtext;
	}

	public String getvcsencryptdecryptkey() {
		return vcsencryptdecryptkey;
	}

	public void setvcsencryptdecryptkey(String vcsencryptdecryptkey) {
		this.vcsencryptdecryptkey = vcsencryptdecryptkey;
	}


	@Override
	public String toString() {
		return "VormetricCryptoServerSettings [vcstokenserver=" + vcstokenserver + ", vcsuserid=" + vcsuserid
				+ ", vcspassword=" + vcspassword + ", vcstokengroup=" + vcstokengroup + ", vcstokentemplate="
				+ vcstokentemplate + ", vcsaction=" + vcsaction + ", vcsalg=" + vcsalg + ", vcsivnumber=" + vcsivnumber
				+ ", vcsivtext=" + vcsivtext + ", vcsencryptdecryptkey=" + vcsencryptdecryptkey + "]";
	}

}
