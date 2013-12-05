package security;

import org.springframework.security.authentication.encoding.MessageDigestPasswordEncoder;

public class SecureAuthentication {
	public static String encryptPassword(String textUserName, String textPassword) {
		Object saltString = textUserName + security.SaltGenerator.saltKey;
		try
		{
			MessageDigestPasswordEncoder m = new MessageDigestPasswordEncoder("SHA-512");
			String encodedPassword = m.encodePassword(textPassword,saltString);
			return encodedPassword;
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
