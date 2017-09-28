package student.cs4341.project2.fileinterface;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

public final class FileUtilities {
    /**
     * Private Constructor for Utilities class
     */
    private FileUtilities() {
        throw new IllegalStateException("Must disallow instantiation of: FileUtilities");
    }

    /**
     * Blocks till the given path exists in the FS
     * @param filePath
     */
    public static void waitForFileInFS(final Path filePath) {
        while (Files.notExists(filePath)) ;
    }

    /**
     * Determines if the given path exists in the FS
     * @param filePath
     * @return
     */
    public static boolean fileExists(final Path filePath) {
        return Files.exists(filePath);
    }

    /**
     * Reads in the content of the given path and returns the contents as a string
     * @param filePath
     * @return
     */
    public static String readFile(final Path filePath) {
        try {
            return new String(Files.readAllBytes(filePath));
        } catch (IOException e) {
            throw new RuntimeException("Failed to read the file! :(");
        }
    }

    /**
     * Writes contents to a given file path with the given contents<br />
     * If the file exists, we purge all its contents and write from seek zero.
     * @param filePath
     * @param contents
     */
    public static void writeToFile(final Path filePath, final String contents) {
        try {
            Files.write(filePath, contents.getBytes(), StandardOpenOption.WRITE, StandardOpenOption.TRUNCATE_EXISTING);
        } catch (IOException e) {
            throw new RuntimeException("Failed to write to the file! :(");
        }
    }
}
