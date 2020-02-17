package filesplitter.merger;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.ZipInputStream;

/**
 * Classe che definisce un merger di file compressi
 */
public class ZipMerger extends AbstractMerger {
    /**
     * Metodo costruttore.
     *
     * @param fileName file compresso #1.
     */
    public ZipMerger(File fileName) {
        super(fileName);
    }

    @Override
    public InputStream getInputStream() {
        ZipInputStream zin = null;
        try {
            zin = new ZipInputStream(new FileInputStream(getFile()));
            zin.getNextEntry();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return zin;
    }

    @Override
    public void closeInputStream(InputStream in) {
        try {
            ((ZipInputStream) in).closeEntry();
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
