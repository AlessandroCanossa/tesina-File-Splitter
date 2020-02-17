package filesplitter.gui;

import filesplitter.splitter.AbstractSplitter;

import javax.swing.*;

/**
 * Classe per effettuare la divisione dei file.
 * Fa uso di SwingWorker per gestire i thread.
 */
public class SplitWorker extends SwingWorker<Void, Void> {

	/**
	 * Indice relativo al file nella coda degli splitter.
	 */
	private int index;

	/**
	 * Pannello che chiama SplitWorker
	 */
	private SplitterPanel panel;

	/**
	 * Array delle divisioni già completate.
	 * Gli indici sono gli stessi della coda dei file.
	 */
	private boolean[] completed;

	/**
	 * Metodo costruttore dello SplitWorker
	 *
	 * @param i         indice del file
	 * @param panel     pannello chiamante
	 * @param completed array delle divisioni completate
	 */
	public SplitWorker(int i, SplitterPanel panel, boolean[] completed) {
		this.index = i;
		this.panel = panel;
		this.completed = completed;
	}

	/**
	 * Metodo che esegue in background la divisione dei file.
	 *
	 * @return null
	 */
	@Override
	protected Void doInBackground() {
		setProgress(0);
		AbstractSplitter sp = panel.getSplitter().get(index);
		Thread t = new Thread(sp);
		t.start();

		panel.getTableModel().updateStatus(index, "current");
		while (t.getState() != Thread.State.TERMINATED) {
			double progress = ((double) sp.getByteWritten() / sp.getFile().length() * 100f);
			setProgress((int) progress);
		}

		return null;
	}

	/**
	 * Metodo chiamato alla fine del metodo doInBackground
	 * Aggiorna l'array dei file completati.
	 * A totale completamento lo notifica all'utente e svuota la coda dei file.
	 */
	@Override
	protected void done() {
		setProgress(100);
		panel.getTableModel().updateStatus(index, "completed");
		completed[index] = true;

		for (boolean b : completed) {
			if (!b)
				return;
		}

		JOptionPane.showMessageDialog(panel.getRootPanel(), "Esecuzione completata!", "", JOptionPane.INFORMATION_MESSAGE);
		panel.getSplitter().removeAllElements();
	}
}
