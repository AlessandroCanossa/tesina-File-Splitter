package filesplitter.gui;

import filesplitter.splitter.AbstractSplitter;
import filesplitter.splitter.BaseSplitter;
import filesplitter.splitter.CryptoSplitter;
import filesplitter.splitter.ZipSplitter;
import filesplitter.utility.Utility;

import javax.swing.*;
import java.io.File;
import java.util.Vector;

/**
 * Classe che implementa la gestione dell'aggiunta di un file da dividere.
 */
public class AddFile extends FileManager {

	/**
	 * Metodo costruttore del file manager di aggiunta dei file.
	 * Gestisce la creazione dei componenti gui e la logica dei pulsanti.
	 *
	 * @param parent        dialog creatore
	 * @param splitters     vettore degli splitter
	 * @param panel         pannello che chiama il manager
	 * @param splitterFrame frame del pannello chiamante
	 * @param file          file da gestire
	 */
	AddFile(JDialog parent, Vector<AbstractSplitter> splitters, SplitterPanel panel, JFrame splitterFrame, File file) {
		super(parent, splitters, panel, splitterFrame, file);
	}

	/**
	 * Metodo che gestisce l'aggiunta del giusto tipo di splitter alla coda degli splitter.
	 * Aggiunge il file alla tabella dei file.
	 */
	@Override
	public void fileToSplitter() {
		String type = "";
		int estimatedParts = 0;

		if (getSizeDivision().isSelected()) {
			type = "Default";

			getSplitters().add(new BaseSplitter(partSize(), getFile(), Utility.type.DEFAULT));

			estimatedParts = (int) Math.ceil((double) getFile().length() / partSize());
		} else if (getZipDivision().isSelected()) {
			type = "Compression";

			getSplitters().add(new ZipSplitter(partSize(), getFile(), Utility.type.ZIP));

			estimatedParts = (int) Math.ceil((double) getFile().length() / partSize());
		} else if (getPartDivision().isSelected()) {
			type = "Parts";

			getSplitters().add(new BaseSplitter(partNum(), getFile(), Utility.type.PART));

			estimatedParts = partNum();
		} else if (getCryptoDivision().isSelected()) {
			type = "Crypto";

			String psw = "";
			while (psw.isEmpty())
				psw = JOptionPane.showInputDialog(getRootPanel(), "Inserisci una password!");

			getSplitters().add(new CryptoSplitter(partSize(), getFile(), Utility.type.CRYPTO, psw));

			estimatedParts = (int) Math.ceil((double) getFile().length() / partSize());
		}

		FileData fileData = new FileData(getFileName().getText(), getFile().length(), type, estimatedParts, "on hold");

		getSplitterPanel().addRowToTable(fileData);
	}
}
