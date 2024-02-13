package org.example;

import java.util.List;

public class Main {
    public static void main(String[] args) {

        Long start = System.currentTimeMillis();

        List<Double[]> data = DataReader.readData(args[0]);
        List<List<Double[]>> groups = GroupDivider.divide(data);
        DataWriter.writeData(groups);

        Long finish = System.currentTimeMillis();

        System.out.println((finish - start) / 1000);
    }

}