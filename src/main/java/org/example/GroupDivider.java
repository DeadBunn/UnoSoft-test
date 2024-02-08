package org.example;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;

public class GroupDivider {

    public static LinkedList<LinkedList<ArrayList<Long>>> divide(LinkedList<ArrayList<Long>> data) {

        LinkedList<LinkedList<ArrayList<Long>>> result = new LinkedList<>();

        while (!data.isEmpty()) {
            LinkedList<ArrayList<Long>> group = new LinkedList<>();

            ArrayList<Long> firstElement = data.pop();
            group.add(firstElement);

            LinkedList<ArrayList<Long>> newElements = new LinkedList<>();
            newElements.add(firstElement);

            while (!newElements.isEmpty()) {

                LinkedList<ArrayList<Long>> addedElements = new LinkedList<>();

                Iterator<ArrayList<Long>> iterator = data.iterator();
                while (iterator.hasNext()) {
                    ArrayList<Long> element = iterator.next();
                    if (isElementBelongsToGroup(newElements, element)) {
                        iterator.remove();
                        group.add(element);
                        addedElements.add(element);
                    }
                }

                newElements = addedElements;
            }

            result.add(group);
        }

        return result;
    }

    private static boolean isElementBelongsToGroup(
            LinkedList<ArrayList<Long>> group,
            ArrayList<Long> element
    ) {

        for (ArrayList<Long> groupElement : group) {
            if (isLinesHaveSameNumbers(groupElement, element)) {
                return true;
            }
        }

        return false;
    }

    private static boolean isLinesHaveSameNumbers(ArrayList<Long> line1, ArrayList<Long> line2) {
        int n = Math.min(line1.size(), line2.size());

        for (int i = 0; i < n; i++) {
            if (line1.get(i) != null && line2.get(i) != null && line1.get(i).equals(line2.get(i))) {
                line1.forEach(it -> System.out.print(it + " "));
                System.out.println();
                line2.forEach(it -> System.out.print(it + " "));
                return true;
            }
        }

        return false;
    }
}
