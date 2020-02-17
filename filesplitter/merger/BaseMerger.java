package filesplitter.merger;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;


/**
 * Classe merger.
 * Unisce le varie parti di un file
 * per formare il file di partenza.
 */
public class BaseMerger extends AbstractMerger {
    /**
     * Metodo costruttore.
     *
     * @param fileName file #1.
     */
    public BaseMerger(File fileName) {
        super(fileName);
    }

    @Override
    public InputStream getInputStream() {
        FileInputStream in = null;
        try {
            in = new FileInputStream(getFile());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return in;
    }

    @Override
    public void closeInputStream(InputStream in) {
        try {
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}