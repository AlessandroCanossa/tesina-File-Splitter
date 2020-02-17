package filesplitter.utility;

import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

/**
 * Classe contenente metodi per la corretta creazione di un cipher.
 */
public final class CryptoManager {

	/**
	 * Metodo che restituisce una chiave di criptazione adeguata.
	 * Utilizza l'algoritmo AES.
	 *
	 * @param psw password da codificare
	 * @return chiave di criptazione
	 */
	public static Key getKey(String psw) {
		return new SecretKeySpec(getDigestedKey(psw), "AES");
	}

	/**
	 * Metodo che restituisce la password codificata.
	 * Utilizza l'algoritmo MD5
	 *
	 * @param psw password da codificare
	 * @return password codificata
	 */
	private static byte[] getDigestedKey(String psw) {
		MessageDigest md = null;

		try {
			md = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}

		assert md != null;
		md.update(psw.getBytes());

		return md.digest();
	}

	/**
	 * Metodo che restituisce un vettore di inizializzazione casuale.
	 *
	 * @return vettore di inizializzazione.
	 */
	public static IvParameterSpec getIvParameter() {
		SecureRandom sr = new SecureRandom();
		byte[] iv = new byte[16];
		sr.nextBytes(iv);
		return new IvParameterSpec(iv);
	}
}