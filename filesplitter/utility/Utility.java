package filesplitter.utility;

/**
 * Classe contenente costanti utili
 */
public final class Utility {
	/**
	 * Dimensione per un buffer
	 */
	public static final int bufferSize = (int) Math.pow(2, 13);

	/**
	 * Costante Byte
	 */
	public static final int B = 1;
	/**
	 * Costante KiloByte
	 */
	public static final int KB = (int) Math.pow(10, 3);
	/**
	 * Costante MegaByte
	 */
	public static final int MB = (int) Math.pow(10, 6);
	/**
	 * Costante GigaByte
	 */
	public static final int GB = (int) Math.pow(10, 9);

	/**
	 * Costante relativa all'estensione di divisione
	 */
	public static final String SPLIT_EXTENSION = ".par";

	
	/**
	 * Costante relativa all'estensione di divisione con criptazione
	 */
	public static final String CRYPTO_EXTENSION = ".crypt";

	/**
	 * Costante relativa all'estensione di divisione con compressione
	 */
	public static final String ZIP_EXTENSION = ".zip";


	/**
	 * Enumerati per i tipi di divisione
	 */
	public enum type {
		DEFAULT,
		ZIP,
		PART,
		CRYPTO
	}
}
