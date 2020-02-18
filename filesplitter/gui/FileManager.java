package filesplitter.gui;

import filesplitter.splitter.AbstractSplitter;
import filesplitter.utility.Utility;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.util.Objects;
import java.util.Vector;

/**
 * Classe che definisce la struttura del dialog per la gestione delle scelte relative a un file
 */
public abstract class FileManager {

	/**
	 * Pannello di root che contiene gli elementi grafici.
	 */
	private JPanel rootPanel;

	/**
	 * Campo di testo che mostra il nome del file scelto.
	 */
	private JTextField fileName;

	/**
	 * Radiobutton per la scelta della divisione in base alla dimensione.
	 */
	private JRadioButton sizeDivision;

	/**
	 * Radiobutton per la scelta della divisione con compressione.
	 */
	private JRadioButton zipDivision;

	/**
	 * Radiobutton per la scelta della divisione in base al numero di parti.
	 */
	private JRadioButton partDivision;

	/**
	 * Radiobutton per la scelta della divisione criptata.
	 */
	private JRadioButton cryptoDivision;

	/**
	 * Bottone per applicare le scelte fatte.
	 */
	private JButton applyButton;

	/**
	 * Bottone per chiudere il pannello senza salvare le scelte.
	 */
	private JButton cancelButton;

	/**
	 * Campo di testo per la dimensione delle parti.
	 */
	private JTextField partSize;

	/**
	 * Campo di testo per il numero delle parti.
	 */
	private JTextField numPart;

	/**
	 * Box di scelta per l'unità di misura della dimensione delle parti.
	 */
	private JComboBox<String> comboBox;

	/**
	 * File selezionato
	 */
	private File file;

	/**
	 * Vettore contenente gli splitter dei vari file.
	 */
	private Vector<AbstractSplitter> splitters;

	/**
	 * Dialog che contiene il pannello di root.
	 */
	private JDialog parent;

	/**
	 * Pannello da cui è stato chiamato il file manager.
	 */
	private SplitterPanel splitterPanel;

	/**
	 * Metodo costruttore del file manager
	 * Gestisce la creazione dei componenti gui e la logica dei pulsanti.
	 *
	 * @param parent        dialog creatore
	 * @param splitters     vettore degli splitter
	 * @param panel         pannello che chiama il manager
	 * @param splitterFrame frame del pannello chiamante
	 * @param file          file da gestire
	 */
	FileManager(JDialog parent, Vector<AbstractSplitter> splitters, SplitterPanel panel, JFrame splitterFrame, File file) {
		this.splitterPanel = panel;
		this.parent = parent;
		this.splitters = splitters;
		this.file = file;

		splitterFrame.setEnabled(false);

		ButtonGroup group = new ButtonGroup();
		group.add(sizeDivision);
		group.add(zipDivision);
		group.add(partDivision);
		group.add(cryptoDivision);

		fileName.setAutoscrolls(true);
		fileName.setText(this.file.getName());

		partSize.setEditable(false);
		numPart.setEditable(false);
		applyButton.setEnabled(false);

		sizeDivision.addChangeListener(e -> {
			if (sizeDivision.isSelected()) {
				partSize.setEditable(true);
				numPart.setEditable(false);
				applyButton.setEnabled(true);
			}
		});

		zipDivision.addChangeListener(e -> {
			if (zipDivision.isSelected()) {
				partSize.setEditable(true);
				numPart.setEditable(false);
				applyButton.setEnabled(true);
			}
		});

		partDivision.addChangeListener(e -> {
			if (partDivision.isSelected()) {
				partSize.setEditable(false);
				numPart.setEditable(true);
				applyButton.setEnabled(true);
			}
		});

		cryptoDivision.addActionListener(e -> {
			if (cryptoDivision.isSelected()){
				partSize.setEditable(true);
				numPart.setEditable(false);
				applyButton.setEnabled(true);
			}
		});

		cancelButton.addActionListener(actionEvent -> {
			splitterFrame.setEnabled(true);
			parent.dispose();
		});

		applyButton.addActionListener(actionEvent -> {
			if (partSize.getText().isEmpty() && numPart.getText().isEmpty()) {
				errorPopup();
			} else {
				fileToSplitter();
				splitterFrame.setEnabled(true);
				parent.dispose();
			}
		});

		this.parent.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				splitterFrame.setEnabled(true);
			}
		});
	}

	/**
	 * Metodo usato per convertire in intero la stringa relativa alla dimensione delle parti
	 *
	 * @return dimensione delle parti
	 */
	Integer partSize() {
		if (!isInteger(partSize.getText()))
			errorPopup();

		int factor = 0;
		switch (Objects.requireNonNull(comboBox.getSelectedItem()).toString()) {
			case "B":
				factor = Utility.B;
				break;
			case "KB":
				factor = Utility.KB;
				break;
			case "MB":
				factor = Utility.MB;
				break;
			case "GB":
				factor = Utility.GB;
				break;
		}

		try {
			return Integer.parseInt(partSize.getText()) * factor;
		} catch (NumberFormatException e) {
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * Metodo per la conversione in intero della stringa relativa al numero di parti.
	 *
	 * @return numero di parti.
	 */
	Integer partNum() {
		if (!isInteger(numPart.getText()))
			errorPopup();

		try {
			return Integer.parseInt(numPart.getText());
		} catch (NumberFormatException e) {
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * Metodo che definisce il popup in caso di errato/mancato inserimento dei valori di parti e dimensioni.
	 */
	private void errorPopup() {
		JOptionPane.showMessageDialog(this.parent, "Insert a valid number", "Error", JOptionPane.ERROR_MESSAGE);
	}

	/**
	 * Metodo per la verifica che una stringa contenga solo numeri interi non negativi.
	 *
	 * @param s stringa da controllare
	 * @return vero se la stringa contiene solo numeri, falso altrimenti
	 */
	private boolean isInteger(String s) {
		for (int i = 0; i < s.length(); i++) {
			if (s.charAt(i) < 48 || s.charAt(i) > 56)
				return false;
		}
		return true;
	}

	/**
	 * Metodo astratto per la gestione dello splitter di un file.
	 */
	public abstract void fileToSplitter();

	/**
	 * Metodo che restituisce il pannello di root.
	 *
	 * @return pannello di root
	 */
	JPanel getRootPanel() {
		return rootPanel;
	}

	/**
	 * Metodo che restituisce il campo di testo contenente il nome del file.
	 *
	 * @return campo di testo del nome del file
	 */
	JTextField getFileName() {
		return fileName;
	}

	/**
	 * Metodo che restituisce il radiobutton che seleziona la divisione per dimensioni.
	 *
	 * @return radiobutton della divisione per dimensione.
	 */
	JRadioButton getSizeDivision() {
		return sizeDivision;
	}

	/**
	 * Metodo che restituisce il radiobutton che seleziona la divisione con compressione.
	 *
	 * @return radiobutton della divisione con compressione
	 */
	JRadioButton getZipDivision() {
		return zipDivision;
	}

	/**
	 * Metodo che restituisce il radiobutton che seleziona la divisione per parti.
	 *
	 * @return radiobutton della divisione per parti.
	 */
	JRadioButton getPartDivision() {
		return partDivision;
	}

	/**
	 * Metodo che restituisce il radiobutton che seleziona la divisione criptata.
	 *
	 * @return radiobutton della divisione criptata.
	 */
	JRadioButton getCryptoDivision() {
		return cryptoDivision;
	}

	/**
	 * Metodo che restituisce il campo di testo contenente la dimensione delle parti.
	 *
	 * @return campo di testo della dimensione delle parti
	 */
	JTextField getPartSize() {
		return partSize;
	}


	/**
	 * Metodo che restituisce il campo di testo contenente il numero delle parti.
	 *
	 * @return campo di testo del numero delle parti
	 */
	JTextField getNumPart() {
		return numPart;
	}

	/**
	 * Metodo che restituisce il file da gestire
	 *
	 * @return file da gestire
	 */
	public File getFile() {
		return file;
	}

	/**
	 * Metodo che restituisce il vettore degli splitter
	 *
	 * @return vettore degli splitter
	 */
	Vector<AbstractSplitter> getSplitters() {
		return splitters;
	}

	/**
	 * Metodo che restituisce il pannello che ha chiamato il file manager
	 *
	 * @return pannello chiamante
	 */
	SplitterPanel getSplitterPanel() {
		return splitterPanel;
	}
}
