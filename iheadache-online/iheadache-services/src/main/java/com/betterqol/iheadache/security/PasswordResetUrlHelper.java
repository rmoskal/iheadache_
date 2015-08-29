package com.betterqol.iheadache.security;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import org.jboss.resteasy.util.Hex;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.betterqol.iheadache.model.HealthcarePrincipal;
import com.betterqol.iheadache.model.UserPrincipal;
import com.betterqol.iheadache.repository.HealthcarePrincipalRepository;
import com.betterqol.iheadache.repository.UserPrincipalRepository;

/***
 * Class creates a url for recovering a user password.  The url is a hash composed of the users
 * e-mail address and the time 24 hours later
 * @author rob
 *
 */

@Component
public class PasswordResetUrlHelper {
	
	@Autowired
	private UserPrincipalRepository ur;
	
	@Autowired
	HealthcarePrincipalRepository pr;

	private static String key = "secret_salt";
	
	public String encodeEmail(String email) {
		
		DateTime now = new DateTime();
		now = now.plusDays(1);
		return encryptBlowfish(email + "\\--#-\\"+now.toDate().getTime(), key);	
	}
	
	public UserPrincipal checkUrl(String encoded) throws PasswordResetException {
		return checkUrl(encoded, new DateTime());
	}
	
	public HealthcarePrincipal checkUrl2(String encoded) throws PasswordResetException {
		return checkUrl2(encoded, new DateTime());
	}
	
	UserPrincipal checkUrl(String encoded, DateTime expiry) throws PasswordResetException {	
		String payload = decryptBlowfish(encoded,key);
		if (payload == null)
			throw new IllegalArgumentException("The token submitted was incorrectly formatted 501.");
		String[] p = payload.split("\\\\--#-\\\\");
		DateTime t = new DateTime(Long.parseLong(p[1]));
		if (t.isBefore(expiry))
			throw new PasswordResetException(p[0]);
		
		 UserPrincipal uname = ur.findByName(p[0]);		
		 if (uname==null)
			 throw new IllegalArgumentException("The token submitted was incorrectly formatted 502.");
		 
		 return uname;
	}
	
	HealthcarePrincipal checkUrl2(String encoded, DateTime expiry) throws PasswordResetException {	
		String payload = decryptBlowfish(encoded,key);
		if (payload == null)
			throw new IllegalArgumentException("The token submitted was incorrectly formatted 601.");
		String[] p = payload.split("\\\\--#-\\\\");
		DateTime t = new DateTime(Long.parseLong(p[1]));
		if (t.isBefore(expiry))
			throw new PasswordResetException(p[0]);
		
		HealthcarePrincipal uname = pr.findByName(p[0]);		
		 if (uname==null)
			 throw new IllegalArgumentException("The token submitted was incorrectly formatted 602.");
		 
		 return uname;
	}
	
	public static String encryptBlowfish(String to_encrypt, String strkey) {
		  try {
		    SecretKeySpec key = new SecretKeySpec(strkey.getBytes(), "Blowfish");
		     Cipher cipher = Cipher.getInstance("Blowfish");
		     cipher.init(Cipher.ENCRYPT_MODE, key);
		     return  Hex.encodeHex(cipher.doFinal(to_encrypt.getBytes()));
		  } catch (Exception e) { return null; }
		}
		 
	public static String decryptBlowfish(String to_decrypt, String strkey) {
		  try {
		     SecretKeySpec key = new SecretKeySpec(strkey.getBytes(), "Blowfish");
		     Cipher cipher = Cipher.getInstance("Blowfish");
		     cipher.init(Cipher.DECRYPT_MODE, key);
		     byte[] decrypted = cipher.doFinal(Hex.decodeHex(to_decrypt));
		     return new String(decrypted);
		  } catch (Exception e) { return null; }

}
}
