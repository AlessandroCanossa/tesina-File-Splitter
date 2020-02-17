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

//    /**
//     * Metodo costruttore di base.
//     */
//    public FileData() {
//        this("", 0, "", 0, "");
//    }

    /**
     * Metodo costruttore.
     *
     * @param fileName nome del file.
     * @param size     dimensione del file in byte.
     * @param type     tipo di divisione.
     * @param parts    parti stimate
     * @param status   stato del file nella coda.
     */
    public FileData(String fileName, long size, String type, int parts, String status) {
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
    public String getFileName() {
        return fileName;
    }

//    /**
//     * Imposta il nome del file.
//     *
//     * @param fileName nome del file.
//     */
//    public void setFileName(String fileName) {
//        this.fileName = fileName;
//    }

    /**
     * Restituisce la dimensione del file.
     *
     * @return dimensione del file.
     */
    public long getSize() {
        return size;
    }

//    /**
//     * Imposta la dimensione del file.
//     *
//     * @param size dimensione del file.
//     */
//    public void setSize(long size) {
//        this.size = size;
//    }

    /**
     * Restituisce il tipo di divisione del file.
     *
     * @return tipo di divisione.
     */
    public String getType() {
        return type;
    }

//    /**
//     * Imposta il tipo di divisione del file.
//     *
//     * @param type tipo di divisione.
//     */
//    public void setType(String type) {
//        this.type = type;
//    }

    /**
     * Restituisce lo stato del file nella coda.
     *
     * @return stato del file.
     */
    public String getStatus() {
        return status;
    }

    /**
     * Imposta lo stato del file nella coda.
     *
     * @param status stato del file.
     */
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * Restituisce il numero di parti stimate.
     *
     * @return numero di parti
     */
    public int getParts() {
        return parts;
    }

//    /**
//     * Imposta il numero di parti stimate.
//     *
//     * @param parts numero di parti
//     */
//    public void setParts(int parts) {
//        this.parts = parts;
//    }

}
