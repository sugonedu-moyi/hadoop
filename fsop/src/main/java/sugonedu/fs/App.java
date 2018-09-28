package sugonedu.fs;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

public class App {
    public static void main(String[] args) throws IOException {
        String home_string = System.getProperty("user.home");
        Path home_path = Paths.get(home_string);
        Path password_path = home_path.resolve("moyi/fstest/password");
        if (Files.exists(password_path)) {
            Files.delete(password_path);
        }
        if (Files.notExists(password_path.getParent())) {
            Files.createDirectories(password_path.getParent());
        }
        Files.copy(Paths.get("/etc/passwd"), password_path);
        try (BufferedWriter writer = Files.newBufferedWriter(password_path,
                StandardOpenOption.APPEND)) {
            writer.write("last line\n");
        }
        try (BufferedReader reader = Files.newBufferedReader(password_path)) {
            String line = null;
            int num_lines = 0;
            while ((line = reader.readLine()) != null) {
                num_lines++;
                if (line.contains("daemon")) {
                    System.out.println(line);
                }
            }
            System.out.println("Number of lines: " + num_lines);
        }
    }
}
