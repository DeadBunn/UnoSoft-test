package org.example;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.LinkedList;

public class DataReader {

    private static final String FOLDER_PATH = "src/main/resources/";

    public static LinkedList<ArrayList<Long>> readData(String fileName){
        String filePath = FOLDER_PATH + fileName;
        LinkedList<ArrayList<Long>> data = new LinkedList<>();

        try (BufferedReader br = Files.newBufferedReader(Paths.get(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] tokens = line.split(";");
                ArrayList<Long> row = new ArrayList<>();
                boolean isValidLine = true;
                for (String token : tokens) {
                    if (!isValidToken(token)) {
                        isValidLine = false;
                        break;
                    }
                    row.add(token.equals("\"\"") ? null : Long.valueOf(token.replaceAll("\"", "")));
                }
                if (isValidLine) data.add(row);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return data;
    }

    private static boolean isValidToken(String line) {
        return line.matches("\"[0-9]*\"");
    }
}
