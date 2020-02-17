package filesplitter.gui;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;

/**
 * Classe che definisce il modello per la tabella dei dati.
 */
public class DataTableModel extends AbstractTableModel {
	/**
	 * ArrayList di oggetti di tipo FileData.
	 * Contiene i dati da mostrare in tabella.
	 */
	private ArrayList<FileData> data;

	/**
	 * Stringa contenente i nomi delle colonne della tabella.
	 */
	private String[] columnNames = {"Name", "Size", "Division type", "Parts", "Status"};

	/**
	 * Metodo costruttore.
	 */
	public DataTableModel() {
		this.data = new ArrayList<>();
	}

	/**
	 * Metodo che restituisce il numero delle colonne.
	 *
	 * @return numero delle colonne.
	 */
	@Override
	public int getColumnCount() {
		return columnNames.length;
	}

	/**
	 * Metodo che restituisce il numero delle righe della tabella.
	 *
	 * @return numero delle righe.
	 */
	@Override
	public int getRowCount() {
		return data.size();
	}

	/**
	 * Metodo che restituisce il valore a una determinata casella
	 * definita da row e col.
	 *
	 * @param row numero di riga.
	 * @param col numero di colonna.
	 * @return dato contenuto nella casella.
	 */
	@Override
	public Object getValueAt(int row, int col) {
		FileData d = data.get(row);

		switch (col) {
			case 0:
				return d.getFileName();
			case 1:
				return d.getSize();
			case 2:
				return d.getType();
			case 3:
				return d.getParts();
			case 4:
				return d.getStatus();
			default:
				return null;
		}
	}

	/**
	 * Metodo getter per il nome di una colonna.
	 *
	 * @param col numero di colonna.
	 * @return nome della colonna.
	 */
	@Override
	public String getColumnName(int col) {
		return columnNames[col];
	}

	/**
	 * Metodo getter per la classe degli elementi nella colonna.
	 *
	 * @param col numero di colonna.
	 * @return classe degli oggetti nella colonna.
	 */
	@Override
	public Class<?> getColumnClass(int col) {
		return getValueAt(0, col).getClass();
	}

	/**
	 * Metodo per l'aggiunta di una riga
	 *
	 * @param fileData dati della riga da aggiungere.
	 */
	public void addRow(FileData fileData) {
		this.data.add(fileData);
		this.fireTableDataChanged();
	}

	/**
	 * Metodo per la rimozione di una riga.
	 *
	 * @param row numero di riga da rimuovere
	 */
	public void deleteRow(int row) throws IndexOutOfBoundsException {
		this.data.remove(row);
		this.fireTableDataChanged();
	}

	/**
	 * Metodo per l'aggiornamento dello stato di un file nella coda.
	 *
	 * @param row    numero di riga.
	 * @param status nuovo stato.
	 */
	public void updateStatus(int row, String status) {
		this.data.get(row).setStatus(status);
		this.fireTableDataChanged();
	}

	public void modifySplit(int row, FileData data) {
		this.data.set(row, data);
		this.fireTableDataChanged();
	}
}
