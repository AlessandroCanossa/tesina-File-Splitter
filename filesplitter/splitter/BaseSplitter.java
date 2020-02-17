package filesplitter.splitter;

import filesplitter.utility.Utility;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;


/**
 * Classe splitter con metodo di divisione normale.
 */
public class BaseSplitter extends AbstractSplitter {

    /**
     * Metodo costruttore.
     *
     * @param value    dimensione delle parti divise o numero di parti
     * @param fileName nome del file di partenza
     * @param type     indica il tipo di divisione da eseguire
     */
    public BaseSplitter(long value, File fileName, Utility.type type) {
        super(value, fileName, type);
    }

    @Override
    public OutputStream getOutputStream(int times) {
        FileOutputStream out = null;
        try {
            setBytesToRead(getSplitSize());

            out = new FileOutputStream(this.getFileName() + times + Utility.SPLIT_EXTENSION );
        } catch (
                IOException e) {
            e.printStackTrace();
        }
        return out;
    }

    @Override
    public void closeOutputStream(OutputStream out) {
        try {
            out.flush();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
