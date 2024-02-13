package org.example;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class DataWriter {

    private static final String FOLDER_PATH = "src/main/resources/";

    public static void writeData(List<List<Double[]>> groups) {

        String newFileName = FOLDER_PATH + "result.txt";
        Path outputPath = Path.of(newFileName);

        try {
            if (!Files.exists(outputPath)) {
                Files.createFile(outputPath);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        try (FileWriter writer = new FileWriter(newFileName, false);) {
            writer.write("Количество групп: " + groups.stream().filter(it -> it.size() >= 2).count() + "\n");

            int countGroups = 1;

            for (List<Double[]> group : groups) {
                writer.write(getStringFromList(group, countGroups++));
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static String getStringFromList(List<Double[]> group, int number) {
        StringBuilder sb = new StringBuilder();

        sb.append("Группа ").append(number).append("\n");

        for (Double[] line : group) {
            for (int i = 0; i < line.length - 1; i++) {
                sb.append("\"").append(line[i] == 0 ? "" : line[i]).append("\";");
            }
            sb.append("\"").append(line[line.length - 1] == 0 ? "" : line[line.length - 1]).append("\"\n");
        }

        return sb.toString();
    }
}
