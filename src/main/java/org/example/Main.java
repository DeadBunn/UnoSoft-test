package org.example;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        LinkedList<ArrayList<Long>> data = DataReader.readData("lng.txt");

        LinkedList<LinkedList<ArrayList<Long>>> result = GroupDivider.divide(data);

        int n = 1;

        for (LinkedList<ArrayList<Long>> group : result){
            System.out.println("Группа " + n++);
            for (ArrayList<Long> lines : group){
                for (Long number : lines){
                    System.out.print(number + " ");
                }
                System.out.println();
            }
        }
    }

}