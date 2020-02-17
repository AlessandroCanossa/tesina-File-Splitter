package filesplitter.gui;

import filesplitter.merger.AbstractMerger;
import filesplitter.merger.BaseMerger;
import filesplitter.merger.CryptoMerger;
import filesplitter.merger.ZipMerger;
import filesplitter.splitter.AbstractSplitter;
import filesplitter.utility.Utility;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.util.Arrays;
import java.util.Vector;

/**
 * Classe che implementa il pannello principale dell'applicazione.
 * Contiene i bottoni delle funzionalità, la tabella dei file e la barra di caricamento.
 */
public class SplitterPanel implements PropertyChangeListener {
	/**
	 * Pannello radice che contiene tutti gli elementi della gui.
	 */
	private JPanel rootPanel;

	/**
	 * Bottone per l'aggiunta dei file da dividere in coda.
	 */
	private JButton add;

	/**
	 * Bottone per rimuovere i file dalla coda.
	 */
	private JButton delete;

	/**
	 * Bottone per modificare i file nella coda.
	 */
	private JButton modify;

	/**
	 * Bottone per far iniziare le divisioni dei file.
	 */
	private JButton start;

	/**
	 * Bottone per eseguire l'unione di un file.
	 */
	private JButton merge;

	/**
	 * Toolbar che contiene i vari bottoni.
	 */
	private JToolBar toolBar;

	/**
	 * Barra di caricamento che mostra il progresso della divisione dei file.
	 */
	private JProgressBar progressBar;

	/**
	 * Pannello che contiene la tabella dei file.
	 * In caso i file siano troppi, è possibile scorrerla.
	 */
	private JScrollPane scrollPane;

	/**
	 * Vettore di AbstractSplitter che contiene i vari splitter dei file.
	 */
	private Vector<AbstractSplitter> splitter;

	/**
	 * Modello di tabella per mostrare i file in coda.
	 */
	private DataTableModel tableModel;

	/**
	 * Tabella dei file.
	 */
	private JTable table;

	/**
	 * Frame che contiene il pannello Rootpanel
	 */
	private JFrame parent;

	/**
	 * Costruttore che inizializza tutti gli elementi della gui.
	 *
	 * @param parent frame che contiene il pannello.
	 */
	public SplitterPanel(JFrame parent) {
		this.parent = parent;

		add.addActionListener(new AddAction());
		delete.addActionListener(new DeleteAction());
		start.addActionListener(new StartAction());
		modify.addActionListener(new ModifyAction());
		merge.addActionListener(new MergeAction());

		toolBar.setFloatable(false);

		progressBar.setValue(0);

		splitter = new Vector<>();
	}

	/**
	 * Metodo per la creazione della tabella dei file
	 */
	private void createUIComponents() {

		tableModel = new DataTableModel();
		table = new JTable(tableModel);
		table.getTableHeader().setReorderingAllowed(false);
		table.getTableHeader().setResizingAllowed(false);

		// creo il render delle celle
		DefaultTableCellRenderer centre = new DefaultTableCellRenderer();
		centre.setHorizontalAlignment(JLabel.CENTER);

		// imposto che i contenuti delle celle siano centrati
		table.setDefaultRenderer(String.class, centre);
		table.setDefaultRenderer(Integer.class, centre);
		table.setDefaultRenderer(Long.class, centre);

		scrollPane = new JScrollPane(table);
		scrollPane.setPreferredSize(new Dimension(380, 280));
	}

	/**
	 * Metodo per l'aggiunta alla tabella di una riga che descrive un determinato file.
	 *
	 * @param fileData Dati del file da tenere nella tabella.
	 */
	public void addRowToTable(FileData fileData) {
		tableModel.addRow(fileData);
	}

	/**
	 * Metodo per la modifica di una riga della tabella
	 *
	 * @param fileData Dati del file da modificare.
	 */
	public void modifyRow(FileData fileData) {
		tableModel.modifySplit(table.getSelectedRow(), fileData);
	}

	/**
	 * Metodo che restituisce il modello della tabella.
	 *
	 * @return modello della tabella.
	 */
	public DataTableModel getTableModel() {
		return tableModel;
	}

	/**
	 * Metodo che aggiorna il completamento della barra di caricamento.
	 * Viene chiamato quando la bound property progress di ProgressWorker viene modificata.
	 *
	 * @param e valore della bound property progress
	 */
	@Override
	public void propertyChange(PropertyChangeEvent e) {
		if ("progress".equals(e.getPropertyName())) {
			progressBar.setValue(progressBar.getValue() + (Integer) e.getNewValue() - (Integer) e.getOldValue());
		}
	}

	/**
	 * Metodo che restituisce il pannello di root
	 *
	 * @return rootpanel
	 */
	public JPanel getRootPanel() {
		return rootPanel;
	}

	/**
	 * Metodo che restituisce il vettore degli splitter
	 *
	 * @return vettore degli splitter
	 */
	public Vector<AbstractSplitter> getSplitter() {
		return splitter;
	}

	/**
	 * Classe Listener che viene agganciata al bottone Start
	 */
	private class StartAction implements ActionListener {

		/**
		 * Metodo chiamato dal bottone start,
		 * imposta la barra di caricamento e
		 * fa partire gli splitter su thread diversi.
		 *
		 * @param actionEvent evento generato dal pulsante.
		 */
		@Override
		public void actionPerformed(ActionEvent actionEvent) {
			progressBar.setMaximum(100 * splitter.size());
			boolean[] completed = new boolean[splitter.size()];
			Arrays.fill(completed, false);

			for (int i = 0; i < splitter.size(); i++) {
				SplitWorker pw = new SplitWorker(i, SplitterPanel.this, completed);
				pw.addPropertyChangeListener(SplitterPanel.this);
				pw.execute();
			}
		}
	}

	/**
	 * Classe listener che viene agganciata al bottone add.
	 */
	private class AddAction implements ActionListener {


		/**
		 * Metodo chiamato dal bottone add,
		 * fa selezionare i file da dividere
		 * e li aggiunge alla coda
		 *
		 * @param actionEvent evento generato dal bottone add
		 */
		@Override
		public void actionPerformed(ActionEvent actionEvent) {
			JFileChooser fileChooser = new JFileChooser();
			fileChooser.setMultiSelectionEnabled(true);
			fileChooser.showOpenDialog(rootPanel);

			File[] file = fileChooser.getSelectedFiles();

			for (File f : file) {
				JDialog dialog = new JDialog();
				FileManager fileManager = new AddFile(dialog, splitter, SplitterPanel.this, parent, f);
				dialog.add(fileManager.getRootPanel());
				dialog.setResizable(false);
				dialog.pack();
				dialog.setVisible(true);
			}
		}
	}

	/**
	 * Classe listener agganciata al bottone delete
	 */
	private class DeleteAction implements ActionListener {

		/**
		 * Metodo chiamato dal bottone delete,
		 * rimuove la riga selezionata dalla coda.
		 *
		 * @param actionEvent evento generato dal bottone delete
		 */
		@Override
		public void actionPerformed(ActionEvent actionEvent) {
			if (table.getSelectedRow() != -1) {
				tableModel.deleteRow(table.getSelectedRow());

				if (splitter.size() > 1) {
					splitter.remove(table.getSelectedRow());
				} else {
					splitter = new Vector<>();
				}

			} else {
				JOptionPane.showMessageDialog(rootPanel, "Selezionare una riga da cancellare!", "Errore", JOptionPane.ERROR_MESSAGE);
			}
		}
	}

	/**
	 * Classe listener agganciata al bottone modify
	 */
	private class ModifyAction implements ActionListener {

		/**
		 * Metodo chiamato dal bottone modify,
		 * apre un pannello per la modifica del
		 * file corrispondente alla riga selezionata
		 *
		 * @param actionEvent evento generato dal bottone modify.
		 */
		@Override
		public void actionPerformed(ActionEvent actionEvent) {
			if (table.getSelectedRow() != -1) {
				JDialog modFile = new JDialog();
				FileManager fileManager = new ModFile(modFile, splitter, SplitterPanel.this, parent, splitter.get(table.getSelectedRow()).getFile());
				modFile.add(fileManager.getRootPanel());
				modFile.setResizable(false);
				modFile.pack();
				modFile.setVisible(true);
			} else
				JOptionPane.showMessageDialog(rootPanel, "Selezionare una riga da modificare!", "Errore", JOptionPane.ERROR_MESSAGE);

		}
	}

	/**
	 * Classe listener agganciata al bottone merge
	 */
	private class MergeAction implements ActionListener {

		/**
		 * Metodo chiamato dal bottone merge,
		 * fa selezionare il file della prima parte,
		 * poi lo unisce con le altre parti.
		 *
		 * @param actionEvent evento generato dal bottone merge.
		 */
		@Override
		public void actionPerformed(ActionEvent actionEvent) {
			JFileChooser chooser = new JFileChooser();
			chooser.setFileFilter(new FileNameExtensionFilter("file PAR", "par"));
			chooser.showOpenDialog(rootPanel);

			File file = chooser.getSelectedFile();

			if (file != null && file.getName().contains(Utility.SPLIT_EXTENSION)) {
				MergeWorker mw = new MergeWorker(file);
				mw.execute();
			}
		}
	}

	/**
	 * Classe che gestisce il merge dei file.
	 */
	private class MergeWorker extends SwingWorker<Void, Void> {
		private AbstractMerger merger;

		/**
		 * Metodo costruttore che inizializza il merger in base al tipo di file
		 *
		 * @param file prima parte dei file da unire.
		 */
		public MergeWorker(File file) {
			if (file.getName().contains(Utility.CRYPTO_EXTENSION)) {
				String psw = "";
				while (psw.isEmpty())
					psw = JOptionPane.showInputDialog(rootPanel, "Inserire la password per decriptare i file!");

				merger = new CryptoMerger(file, psw);
			} else if (file.getName().contains(Utility.ZIP_EXTENSION))
				merger = new ZipMerger(file);
			else
				merger = new BaseMerger(file);
		}

		/**
		 * Metodo che esegue in background l'unione dei file.
		 *
		 * @return null
		 */
		@Override
		protected Void doInBackground() {
			Thread t = new Thread(merger);
			t.start();

			while (t.isAlive()) {
				JOptionPane.showMessageDialog(rootPanel, "Unione in corso", "", JOptionPane.INFORMATION_MESSAGE);
			}

			return null;
		}

		/**
		 * Metodo chiamato alla fine del metodo doInBackground,
		 * Notifica all'utente che l'unione è terminata.
		 */
		@Override
		protected void done() {
			JOptionPane.showMessageDialog(rootPanel, "Unione completata", "", JOptionPane.INFORMATION_MESSAGE);
		}
	}
}
