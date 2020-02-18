package filesplitter.merger;

import filesplitter.utility.Utility;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.spec.IvParameterSpec;
import java.io.*;

import static filesplitter.utility.CryptoManager.getKey;

/**
 * Classe merger per file criptati.
 */
public class CryptoMerger extends AbstractMerger {

	/**
	 * Password per la decriptazione delle parti.
	 */
	private String psw;

	/**
	 * Cipher da usare per le varie parti.
	 */
	private Cipher cipher;

	/**
	 * Metodo costruttore
	 *
	 * @param fileName primo file da prendere in ingresso
	 * @param psw      password per decriptare le parti.
	 */
	public CryptoMerger(File fileName, String psw) {
		super(fileName);

		this.psw = psw;
	}

	@Override
	public InputStream getInputStream() {
		FileInputStream in = null;

		try {
			File file = getFile();
			in = new FileInputStream(file);

			if (file.getName().contains("1" + Utility.CRYPTO_EXTENSION)) {
				setCipher(in.readNBytes(16));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return new CipherInputStream(in, cipher);
	}

	@Override
	public void closeInputStream(InputStream in) {
		try {
			in.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	/**
	 * Metodo che crea il cipher per la decriptazione dei file.
	 * Usa l'algorimto AES, la modalità CBC e il padding PKCS5Padding.
	 *
	 * @param iv vettore di inizializzazione
	 */
	private void setCipher(byte[] iv) {
		try {
			cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
			cipher.init(Cipher.DECRYPT_MODE, getKey(psw), new IvParameterSpec(iv));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
