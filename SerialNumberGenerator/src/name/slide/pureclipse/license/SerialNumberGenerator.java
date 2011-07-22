package name.slide.pureclipse.license;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;

public class SerialNumberGenerator {
	
	private static String generateSN(String email) throws UnsupportedEncodingException, NoSuchAlgorithmException {
		return LicenseValidator.getDigest(email + "AK");
	}
	
	private static String generateSNTemp(String email) throws UnsupportedEncodingException, NoSuchAlgorithmException {
		return LicenseValidator.getDigest(email + "AKT");
	}
	
	public static void main(String[] args) throws UnsupportedEncodingException, NoSuchAlgorithmException {
		if (args.length == 0) {
			System.out.println("Email address not specified");
			System.out.println("Usage: java -jar generator.jar [-t] <Email address>");
			System.out.println("	-t - for temporary key");
		}
		else {
			if (args.length > 1 && args[0].equals("-t")) {
				System.out.println("Email address: " + args[1]);
				System.out.println("Serial number: " + generateSNTemp(args[1]));
			}
			else {
				System.out.println("Email address: " + args[0]);
				System.out.println("Serial number: " + generateSN(args[0]));
			}
		}
	}
	
}
