package filesplitter;

import filesplitter.gui.*;

import javax.swing.*;

/**
 * Classe principale del programma.
 * Contiene il metodo main.
 */
public class Main {

	/**
	 * Metodo main.
	 * Crea il frame principale dell'applicazione.
	 * @param args Argomenti passati da riga di comando.
	 */
	public static void main(String[] args) {
		JFrame frame = new JFrame("File Splitter");
		SplitterPanel prova = new SplitterPanel(frame);
		frame.add(prova.getRootPanel());
		frame.pack();
		frame.setLocationRelativeTo(null);			// imposto il frame centrato nello schermo.
		frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		frame.setVisible(true);
	}
}
