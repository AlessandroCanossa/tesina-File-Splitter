package filesplitter.splitter;

import filesplitter.utility.Utility;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * Classe splitter con compressione.
 * Divide in base a una dimensione massima.
 */
public class ZipSplitter extends AbstractSplitter {

	/**
	 * Metodo costruttore.
	 *
	 * @param splitSize dimensione delle singole parti
	 * @param fileName  nome del file di partenza
	 * @param type      indica il tipo di divisione da eseguire
	 */
	public ZipSplitter(long splitSize, File fileName, Utility.type type) {
		super(splitSize, fileName, type);
	}

	@Override
	public OutputStream getOutputStream(int times) {
		ZipOutputStream zout = null;
		try {
			zout = new ZipOutputStream(new FileOutputStream(super.getFileName() + times + Utility.ZIP_EXTENSION + Utility.SPLIT_EXTENSION));
			zout.putNextEntry(new ZipEntry(super.getFile().getName() + Utility.SPLIT_EXTENSION));

			setBytesToRead(getSplitSize());
		} catch (IOException e) {
			e.printStackTrace();
		}
		return zout;
	}

	@Override
	public void closeOutputStream(OutputStream out) {

		try {
			((ZipOutputStream) out).closeEntry();
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
