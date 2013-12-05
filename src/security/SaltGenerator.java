package security;

import org.springframework.security.authentication.dao.SaltSource;
import org.springframework.security.core.userdetails.UserDetails;

public class SaltGenerator implements SaltSource {
	 public static String saltKey = "BSISV2V"; 
	 @Override
   public Object getSalt(UserDetails user) {
       return user.getUsername() + saltKey;                
   }
}
