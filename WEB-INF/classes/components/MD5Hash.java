/* Component: MD5Hash
 * Computes the hash value using md5
 * Call using:
 * 		MD5Hash.generateMD("message");
 * Returns md5 hash value if it was generated. On exception, it returns the original.
 */

package components;

import java.math.BigInteger;
import java.security.DigestException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5Hash {
	public static String generateMD(String message) {
		MessageDigest md = null;
		try {
			md = MessageDigest.getInstance("MD5");
			md.update(message.getBytes());
			byte[] digest = new byte[16];
			md.digest(digest, 0, 16);
			BigInteger number = new BigInteger(1, digest);
			return number.toString(16);
		} catch (NoSuchAlgorithmException e) {
			// TODO custom error page
			System.out.println(e.toString());
			return message;
		} catch (DigestException e) {
			// TODO Auto-generated catch block
			System.out.println(e.toString());
			return message;
		}
	}
}
