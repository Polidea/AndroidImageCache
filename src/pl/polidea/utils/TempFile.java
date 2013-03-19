package pl.polidea.utils;

import java.io.File;
import java.io.IOException;

/**
 * @author Mateusz Grzechociński <mateusz.grzechocinski@gmail.com>
 */
public class TempFile {

    public static final String NO_SUFFIX = null;

    private File value;

    private TempFile() {
    }

    private TempFile(File containingDirectory) throws IOException {
        this.value = File.createTempFile("web", NO_SUFFIX, containingDirectory);
    }

    public static TempFile createInDir(File dir) throws IOException {
        if (!dir.exists()) {
            throw new IllegalArgumentException(String.format("Cache dir %s does not exist", dir.getAbsolutePath()));
        }
        return new TempFile(dir);
    }

    public static TempFile nullObject() {
        return new TempFile();
    }

    public boolean delete() {
        return value != null && value.delete();
    }

    public File asJavaFile() {
        return value;
    }
}
