package filesplitter.splitter;

import filesplitter.utility.Utility;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * Classe astratta che fornisce il necessario per classi splitter.
 */
public abstract class AbstractSplitter implements Runnable {

	/**
	 * Numero di byte massimi di ogni parte di file.
	 */
	private long splitSize;

	/**
	 * File da dividere.
	 */
	private File startFile;

	/**
	 * Numero di parti in cui dividere il file.
	 */
	private int parts;

	/**
	 * Tipo di divisione da inizializzare
	 */
	private Utility.type type;

	/**
	 * Byte da leggere e scrivere in ogni parte
	 */
	private long bytesPerPart;

	/**
	 * Byte scritti. Viene utilizzata per l'aggiornamento della barra dei progressi.
	 */
	private long byteWritten;

	private String outputDirectory;

	/**
	 * Metodo costruttore per divisione per dimensione.
	 *
	 * @param value     dimensione delle parti o numero delle parti
	 * @param startFile nome del file di partenza
	 * @param type      indica il tipo di divisione da eseguire
	 */
	AbstractSplitter(long value, File startFile, Utility.type type) {
		if (value > 0) {
			this.startFile = startFile;
			this.type = type;

			this.byteWritten = 0;

			// in base al type definisco la varibile da inizializzare a value
			if (this.type == Utility.type.PART) {
				this.parts = (int) value;
				this.splitSize = this.startFile.length() / this.parts;
			} else {
				this.splitSize = value;
			}

			makeDirectory();
		}
	}

	/**
	 * Metodo run.
	 * Esegue la procedura di divisione del file in parti.
	 */
	@Override
	public void run() {
		split();
	}

	/**
	 * Procedura di divisione dei file.
	 */
	private void split() {
		try {
			FileInputStream inputStream = new FileInputStream(this.getStartFile());

			int times = 1;
			while (inputStream.available() != 0) {

				OutputStream outputStream = getOutputStream(times);

				int maxCicle = (int) (bytesPerPart / Utility.bufferSize);

				for (int i = 0; i <= maxCicle && inputStream.available() != 0; i++) {

					byte[] buffer;
					if (bytesPerPart - Utility.bufferSize < 0) {
						buffer = inputStream.readNBytes((int) bytesPerPart);
						byteWritten += bytesPerPart;
					} else {
						buffer = inputStream.readNBytes(Utility.bufferSize);
						bytesPerPart -= Utility.bufferSize;
						byteWritten += Utility.bufferSize;
					}
					outputStream.write(buffer);


					/* se la divisione è per parti
					 * e si è arrivati alla scrittura del numero di parti designato,
					 * tutti i byte rimanenti vengono messi nell'ultima parte.
					 * Ciò può causare che l'ultima parte venga un minimo più
					 * grossa delle altre
					 */
					if (getType() == Utility.type.PART && times == getParts() && inputStream.available() != 0) {
						buffer = inputStream.readNBytes(Utility.bufferSize);
						outputStream.write(buffer);
					}
				}

				closeOutputStream(outputStream);

				times++;
			}
			inputStream.close();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Restituisce un file di OutputStream di tipo opportuno.
	 *
	 * @param times numero di file aperto
	 * @return un file di OutputStream
	 */
	public abstract OutputStream getOutputStream(int times);

	/**
	 * Chiude lo stream di output.
	 *
	 * @param out stream di output
	 */
	public abstract void closeOutputStream(OutputStream out);

	/**
	 * Restituisce la dimensione massima di ogni singola parte
	 *
	 * @return dimensione massima di ogni singola parte
	 */
	public long getSplitSize() {
		return splitSize;
	}

	/**
	 * Restituisce il file di input
	 *
	 * @return file di input
	 */
	public File getStartFile() {
		return startFile;
	}

	/**
	 * Restituisce il percorso del file di output.
	 * Nel caso esista una directory di output, restituisce il percorso con la directory
	 * altrimenti resitutisce il percoso del file di partenza.
	 *
	 * @return percorso del file di output.
	 */
	String getOutputPath() {
		if (new File(outputDirectory).isDirectory()) {
			return outputDirectory + File.separator + startFile.getName();
		} else
			return startFile.getPath();
	}

	/**
	 * Crea una directory di output per i file in scrittura.
	 */
	private void makeDirectory() {
		outputDirectory = startFile.getPath().substring(0, startFile.getPath().lastIndexOf('.'));
		boolean bool = new File(outputDirectory).mkdir();
		if (!bool)
			System.err.println("Impossibile creare la cartella di destinazione.s");
	}

	/**
	 * Restituisce il numero massimo di parti
	 *
	 * @return numero massimo di parti
	 */
	public int getParts() {
		return parts;
	}

	/**
	 * Restituisce il tipo di divisione da eseguire
	 *
	 * @return tipo di divisione da eseguire
	 */
	public Utility.type getType() {
		return type;
	}

	/**
	 * Imposta il numero di byte da leggere per ogni singola parte
	 *
	 * @param bytesToRead dimensione di ogni file
	 */
	void setBytesToRead(long bytesToRead) {
		this.bytesPerPart = bytesToRead;
	}

	/**
	 * Restituisce il numero dei byte scritti.
	 *
	 * @return numero dei byte scritti.
	 */
	public long getByteWritten() {
		return byteWritten;
	}
}
