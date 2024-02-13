package org.example;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

public class DataReader {

    private static final String FOLDER_PATH = "src/main/resources/";

    public static List<Double[]> readData(String fileName) {
        String filePath = FOLDER_PATH + fileName;
        List<Double[]> data;

        try (Stream<String> rows = Files.lines(Paths.get(filePath))) {
            data = rows
                    .distinct()
                    .filter(it -> it.matches("((\"[0-9.]*\")?)(;(\"[0-9.]*\")?)*"))
                    .map(it -> Arrays.stream(it.split(";"))
                            .map(number -> (number.equals("\"\"") || number.isEmpty())
                                    ? 0L
                                    : Double.valueOf(number.replaceAll("\"", "")))
                            .toArray(Double[]::new))
                    .toList();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return data;
    }

}
