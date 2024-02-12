package org.example;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

public class DataReader {

    private static final String FOLDER_PATH = "src/main/resources/";

    public static List<Float[]> readData(String fileName) {
        String filePath = FOLDER_PATH + fileName;
        List<Float[]> data;

        try (Stream<String> rows = Files.lines(Paths.get(filePath))) {
            data = rows
                    .distinct()
                    //.filter(it -> it.matches("(\"[0-9.]*\")(;\"[0-9.]*\")*"))
                    .map(it -> Arrays.stream(it.split(";"))
                            .map(number -> number.equals("")
                                    ? 0L
                                    : Float.valueOf(number.replaceAll("\"", "")))
                            .toArray(Float[]::new))
                    .toList();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return data;
    }

}
