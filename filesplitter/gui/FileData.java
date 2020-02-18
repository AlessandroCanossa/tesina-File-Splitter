package filesplitter.gui;

/**
 * Classe FileData
 * Definisce i tipi di dati che devono essere inseriti in una tabella.
 */
public class FileData {

	/**
	 * Nome del file.
	 */
	private String fileName;

	/**
	 * Dimensione del file.
	 */
	private long size;

	/**
	 * Tipo di divisione del file.
	 */
	private String type;

	/**
	 * Numero di parti in uscita stimate
	 */
	private int parts;

	/**
	 * Stato del file nella coda.
	 */
	private String status;

	/**
	 * Metodo costruttore.
	 *
	 * @param fileName nome del file.
	 * @param size     dimensione del file in byte.
	 * @param type     tipo di divisione.
	 * @param parts    parti stimate
	 * @param status   stato del file nella coda.
	 */
    FileData(String fileName, long size, String type, int parts, String status) {
		this.fileName = fileName;
		this.size = size;
		this.status = status;
		this.type = type;
		this.parts = parts;
	}

	/**
	 * Restituisce il nome del file
	 *
	 * @return nome del file.
	 */
    String getFileName() {
		return fileName;
	}

	/**
	 * Restituisce la dimensione del file.
	 *
	 * @return dimensione del file.
	 */
    long getSize() {
		return size;
	}

	/**
	 * Restituisce il tipo di divisione del file.
	 *
	 * @return tipo di divisione.
	 */
	public String getType() {
		return type;
	}

	/**
	 * Restituisce lo stato del file nella coda.
	 *
	 * @return stato del file.
	 */
    String getStatus() {
		return status;
	}

	/**
	 * Imposta lo stato del file nella coda.
	 *
	 * @param status stato del file.
	 */
    void setStatus(String status) {
		this.status = status;
	}

	/**
	 * Restituisce il numero di parti stimate.
	 *
	 * @return numero di parti
	 */
    int getParts() {
		return parts;
	}

}
