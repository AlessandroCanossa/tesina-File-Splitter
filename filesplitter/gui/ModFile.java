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
 * Classe che implementa il file manager di modifica dei file.
 */
public class ModFile extends FileManager {
	private int indexToModify;

	/**
	 * Metodo costruttore del file manager di modifica dei file.
	 * Gestisce la creazione dei componenti gui e la logica dei pulsanti.
	 *
	 * @param parent        dialog creatore
	 * @param splitters     vettore degli splitter
	 * @param panel         pannello che chiama il manager
	 * @param splitterFrame frame del pannello chiamante
	 * @param file          file da gestire
	 */
	public ModFile(JDialog parent, Vector<AbstractSplitter> splitters, SplitterPanel panel, JFrame splitterFrame, File file) {
		super(parent, splitters, panel, splitterFrame, file);

		indexOfFile();
	}

	/**
	 * Metodo che gestisce la modifica delle scelte relative al file selezionato.
	 */
	@Override
	public void fileToSplitter() {
		String type = "";
		int estimatedParts = 0;

		if (getSizeDivision().isSelected()) {
			type = "default";

			getSplitters().set(indexToModify, new BaseSplitter(partSize(), getFile(), Utility.type.DEFAULT));

			estimatedParts = (int) getFile().length() / partSize();
		} else if (getZipDivision().isSelected()) {
			type = "Compression";

			getSplitters().set(indexToModify, new ZipSplitter(partSize(), getFile(), Utility.type.ZIP));

			estimatedParts = (int) getFile().length() / partSize();
		} else if (getPartDivision().isSelected()) {
			type = "in parts";

			getSplitters().set(indexToModify, new BaseSplitter(partNum(), getFile(), Utility.type.PART));

			estimatedParts = partNum();
		} else if (getCryptoDivision().isSelected()) {
			type = "Crypto";

			String psw = "";
			while (psw.isEmpty())
				psw = JOptionPane.showInputDialog(getRootPanel(), "Inserisci una password!");

			getSplitters().set(indexToModify, new CryptoSplitter(partSize(), getFile(), Utility.type.CRYPTO, psw));

			estimatedParts = (int) Math.ceil((double) getFile().length() / partSize());
		}
		FileData fileData = new FileData(getFileName().getText(), getFile().length(), type, estimatedParts, "on hold");

		getSplitterPanel().modifyRow(fileData);
	}

	/**
	 * Metodo che imposta lo stato iniziale del file manager con le scelte precedenti relative al file selezionato.
	 */
	public void indexOfFile() {
		for (AbstractSplitter s : getSplitters()) {
			if (s.getFile().equals(getFile())) {
				switch (s.getType()) {
					case DEFAULT: {
						getSizeDivision().setSelected(true);
						getPartSize().setText(Long.toString(s.getSplitSize()));
						break;
					}
					case ZIP: {
						getZipDivision().setSelected(true);
						getPartSize().setText(Long.toString(s.getSplitSize()));
						break;
					}
					case PART: {
						getPartDivision().setSelected(true);
						getNumPart().setText(Integer.toString(s.getParts()));
						break;
					}
					case CRYPTO: {
						getCryptoDivision().setSelected(true);
						getPartSize().setText(Long.toString(s.getSplitSize()));
						break;
					}
				}
				indexToModify = getSplitters().indexOf(s);
			}
		}
	}
}
