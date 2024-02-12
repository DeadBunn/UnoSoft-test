package org.example;

import java.util.List;

public class Main {
    public static void main(String[] args) {

        Long start = System.currentTimeMillis();

        List<Float[]> data = DataReader.readData(args[0]);
        List<List<Float[]>> groups = GroupDivider.divide(data);
        DataWriter.writeData(groups);

        Long finish = System.currentTimeMillis();

        System.out.println((finish - start) / 1000);
    }

}