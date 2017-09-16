package student.cs4341.project2.fileinterface;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

public final class FileUtilities {
    private FileUtilities() {

    }

    public static void waitForFileInFS(final Path filePath) {
        while (Files.notExists(filePath)) ;
    }

    public static boolean fileExists(final Path filePath) {
        return Files.exists(filePath);
    }

    public static String readFile(final Path filePath) {
        try {
            return new String(Files.readAllBytes(filePath));
        } catch (IOException e) {
            throw new RuntimeException("Failed to read the file! :(");
        }
    }

    public static void writeToFile(final Path filePath, final String contents) {
        try {
            Files.write(filePath, contents.getBytes(), StandardOpenOption.WRITE, StandardOpenOption.TRUNCATE_EXISTING);
        } catch (IOException e) {
            throw new RuntimeException("Failed to write to the file! :(");
        }
    }
}
