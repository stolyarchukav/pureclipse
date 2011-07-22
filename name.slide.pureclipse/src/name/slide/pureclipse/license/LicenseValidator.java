package name.slide.pureclipse.license;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class LicenseValidator {

	public static LicenseType validate(String email, String serialNumber) {
		if (email != null) {
			try {
				String digest = getDigest(email.concat("AK"));
				if (digest.equals(serialNumber)) {
					return LicenseType.FULL;
				}
				digest = getDigest(email.concat("AKT"));
				if (digest.equals(serialNumber)) {
					return LicenseType.TEMP;
				}
			}
			catch (Exception e) {
				e.printStackTrace();
			}
		}
		return null;
	}
	
	static String getDigest(String input) throws UnsupportedEncodingException, NoSuchAlgorithmException {
		MessageDigest md = MessageDigest.getInstance("MD5");
        byte[] hash = new byte[32];
        md.update(input.toLowerCase().getBytes(), 0, input.length());
        hash = md.digest();
        StringBuilder builder = new StringBuilder();
        for (int q = 0; q < hash.length; q++) { 
            int halfbyte = (hash[q] >>> 4) & 0x0F;
            int twoHalfs = 0;
            do {
                if ((0 <= halfbyte) && (halfbyte <= 9)) {
                	builder.append((char) ('0' + halfbyte));
                }
                else { 
                	builder.append((char) ('a' + (halfbyte - 10)));
                }
                halfbyte = hash[q] & 0x0F;
            }
            while(twoHalfs++ < 1);
        } 
        return builder.toString().toUpperCase();
	}
	
}
