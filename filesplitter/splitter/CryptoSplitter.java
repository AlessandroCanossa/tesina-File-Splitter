package filesplitter.splitter;

import filesplitter.utility.Utility;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import javax.crypto.Cipher;
import javax.crypto.CipherOutputStream;

import static filesplitter.utility.CryptoManager.*;

/**
 * Classe splitter con criptazione delle parti del file.
 */
public class CryptoSplitter extends AbstractSplitter {

	/**
	 * Password usata per criptare le parti del file.
	 */
	private String psw;

	/**
	 * Cipher da usare per criptare le parti.
	 */
	private Cipher cipher;

	/**
	 * Metodo costruttore per divisione per dimensione.
	 *
	 * @param value dimensione delle parti o numero delle parti
	 * @param file  nome del file di partenza
	 * @param type  indica il tipo di divisione da eseguire
	 * @param psw 	password di criptazione dei file
	 */

	public CryptoSplitter(long value, File file, Utility.type type, String psw) {
		super(value, file, type);

		this.psw = psw;

		setCipher();
	}

	@Override
	public OutputStream getOutputStream(int times) {
		FileOutputStream out = null;
		try {
			setBytesToRead(getSplitSize());

			out = new FileOutputStream(this.getFileName() + times + Utility.CRYPTO_EXTENSION + Utility.SPLIT_EXTENSION);

			if (times == 1) {
				out.write(cipher.getIV());
			}
		} catch (
				IOException e) {
			e.printStackTrace();
		}

		return new CipherOutputStream(out, cipher);
	}

	@Override
	public void closeOutputStream(OutputStream out) {
		try {
			out.flush();
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Metodo che crea il cipher per la criptazione dei file.
	 */
	private void setCipher() {
		try {
			cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
			cipher.init(Cipher.ENCRYPT_MODE, getKey(psw), getIvParameter());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
