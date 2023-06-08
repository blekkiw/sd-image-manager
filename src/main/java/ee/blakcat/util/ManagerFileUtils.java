package ee.blakcat.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Stream;

import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;

@UtilityClass
public class ManagerFileUtils {

    public static final String DUPLICATE = "duplicates";

    @SneakyThrows
    public static List<Path> getFilesFromFolder(String folder) {
        return getFilesFromFolder(folder, f -> true);
    }

    @SneakyThrows
    public static List<Path> getFilesFromFolder(String folder, Predicate<Path> additionalFilter) {
        try (Stream<Path> stream = Files.walk(Paths.get(folder))) {
            return stream.filter(Files::isRegularFile)
                    .filter(additionalFilter)
                    .toList();
        }
    }

    public static void safeCreateFolder(Path parent) {
        if (!Files.exists(parent.resolve(DUPLICATE))) {
            try {
                Files.createDirectory(parent.resolve(DUPLICATE));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @SneakyThrows
    public List<String> getTemplate() {
        var input = ManagerFileUtils.class.getClassLoader().getResourceAsStream("template.html");

        assert input != null;
        try (InputStreamReader streamReader =
                     new InputStreamReader(input, StandardCharsets.UTF_8);
             BufferedReader reader = new BufferedReader(streamReader)) {
            return reader.lines().toList();
        }
       
    }

    public static void safeDeleteFile(String folder) {
        try {
            Files.delete(Paths.get(folder));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
