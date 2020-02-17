package filesplitter.merger;

import filesplitter.utility.Utility;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Vector;

/**
 * Classe astratta che fornisce le basi per delle classi merger.
 */
public abstract class AbstractMerger implements Runnable {

    /**
     * Stringa contenente il nome del file di output.
     */
    private String outputFile;

    /**
     * Vettore di file contenente i file da unire.
     */
    private Vector<File> files;

    /**
     * Metodo costruttore
     *
     * @param fileName primo file da prendere in ingresso
     */
    public AbstractMerger(File fileName) {
//        this.fileName = fileName;
        files = new Vector<>();
        files.add(fileName);
        setFiles();

        outputFile = fileName.getPath().substring(0, fileName.getPath().lastIndexOf(1 + "."));
    }

    /**
     * Metodo run.
     * Esegue la procedura di unione dei file
     */
    @Override
    public void run(){
        merge();
    };

    /**
     * Procedura di unione dei file.
     */
    public void merge() {
        try {

            FileOutputStream outputStream = new FileOutputStream(this.outputFile);

            while (this.files.size() != 0) {
                InputStream inputStream = getInputStream();
                byte[] buffer;
                while ((buffer = inputStream.readNBytes(Utility.bufferSize)).length != 0) {
                    outputStream.write(buffer);
                }

                closeInputStream(inputStream);
            }

            outputStream.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Restituisce un file di input.
     *
     * @return stream di input
     */
    public abstract InputStream getInputStream();

    /**
     * Chiude lo stream di input.
     *
     * @param in stream da chiudere
     */
    public abstract void closeInputStream(InputStream in);

    /**
     * Inserisce nel vettore files i file da unire.
     */
    public void setFiles() {
        for (int i = 2; ; i++) {
            File file = new File(this.files.firstElement().getPath().replace(1 + ".", i + "."));
            if (file.exists()) {
                files.add(file);
            } else {
                break;
            }
        }
    }

    /**
     * Restituisce il primo file del vettore
     *
     * @return file
     */
    public File getFile() {
        return files.remove(0);
    }

}
