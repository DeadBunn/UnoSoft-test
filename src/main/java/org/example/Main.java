package org.example;

import java.util.List;

public class Main {
    public static void main(String[] args) {

        long start = System.currentTimeMillis();

        List<Float[]> data = DataReader.readData(args[0]);
        List<List<Float[]>> groups = GroupDivider.divide(data);
        DataWriter.writeData(groups);

        long finish = System.currentTimeMillis();

        System.out.println((float) (finish - start) / 1000);
    }

}