package com.vormetric.rest.helperclasses;

import java.util.TreeMap;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args ) throws Exception
    {
        System.out.println( "Hello World!" );
        
		TreeMap<String, String> tm1 = VormetricDSMHelper.getDomainObjectAttributeObj("192.168.159.130", "superadmin", "Admin123!",
				"1000/keys/asymmetric/286", "domains", "publicKey");

		VormetricDSMHelper.keysAsList(tm1).forEach(System.out::println);
		VormetricDSMHelper.valuesAsList(tm1).forEach(System.out::println);
		
/*    	VormetricDSMHelper vdsm = new VormetricDSMHelper();
		String testdname[] = VormetricDSMHelper.getDomainObjectAttributeList("192.168.159.130", "superadmin", "Admin123!", "",
				"domains", "description");
		System.out.println("length " + testdname.length);
		for (int i = 0; i < testdname.length; i++) {
			System.out.println(testdname[i]);
		}
		
		String testid[] = vdsm.getDomainObjectAttributeList("192.168.159.130", "superadmin", "Admin123!", "",
				"domains", "id");
		System.out.println("length " + testid.length);
		for (int i = 0; i < testid.length; i++) {
			System.out.println(testid[i]);
		}
		
		String test[] = vdsm.getDomainObjectAttributeList("192.168.159.130", "superadmin", "Admin123!", "1000/hosts",
				"domains", "description");
		System.out.println("length " + test.length);
		for (int i = 0; i < test.length; i++) {
			System.out.println(test[i]);
		}
		//keys/asymmetric/
		
		String test2[] = vdsm.getDomainObjectAttributeList("192.168.159.130", "superadmin", "Admin123!", "1000/keys/asymmetric/",
				"domains", "description");
		System.out.println("length " + test2.length);
		for (int i = 0; i < test2.length; i++) {
			System.out.println(test2[i]);
		}
*/		
		VormetricCryptoServerSettings vcs = new VormetricCryptoServerSettings();
		String calltype = "encrypt";
		String results = null;
		String text = "thisisatest";
			results = VormetricCryptoServerHelper.doEncryptData(vcs.getvcstokenserver(),
					vcs.getvcsuserid(), vcs.getvcspassword(), text, vcs.getvcsalg(),
					vcs.getvcsivnumber(), calltype, vcs.getvcsencryptdecryptkey());
 
			calltype = "decrypt";
			results = VormetricCryptoServerHelper.doDeCryptData(vcs.getvcstokenserver(),
					vcs.getvcsuserid(), vcs.getvcspassword(), results, vcs.getvcsalg(),
					vcs.getvcsivnumber(), calltype, vcs.getvcsencryptdecryptkey());
 
		
    }
}
