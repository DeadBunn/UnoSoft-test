package org.example;

import java.util.*;
import java.util.stream.Collectors;

public class GroupDivider {

    public static List<List<Long[]>> divide(List<Long[]> data) {

        List<List<Long[]>> result = new LinkedList<>();

        List<Long[]> haveMatches = findElementsThatHaveMatches(data);

        List<Long[]> notHaveMatches = data
                .stream()
                .filter(it -> !haveMatches.contains(it))
                .toList();

        while (!haveMatches.isEmpty()) {
            LinkedList<Long[]> group = new LinkedList<>();

            Long[] firstElement = haveMatches.get(0);
            group.add(firstElement);
            haveMatches.remove(0);

            LinkedList<Long[]> newElements = new LinkedList<>();
            newElements.add(firstElement);

            while (!newElements.isEmpty()) {

                LinkedList<Long[]> addedElements = new LinkedList<>();

                Iterator<Long[]> iterator = haveMatches.iterator();

                while (iterator.hasNext()) {
                    Long[] element = iterator.next();
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

        result = result
                .stream()
                .sorted((list1, list2) -> Integer.compare(list2.size(), list1.size()))
                .collect(Collectors.toList());

        result.addAll(notHaveMatches
                .stream()
                .map(Collections::singletonList)
                .toList());

        return result;
    }

    private static boolean isElementBelongsToGroup(
            LinkedList<Long[]> group,
            Long[] element
    ) {

        for (Long[] groupElement : group) {
            if (isLinesHaveSameNumbers(groupElement, element)) {
                return true;
            }
        }

        return false;
    }

    private static boolean isLinesHaveSameNumbers(Long[] line1, Long[] line2) {
        int n = Math.min(line1.length, line2.length);

        for (int i = 0; i < n; i++) {
            if (line1[i] != 0 && line2[i] != 0 && line1[i].equals(line2[i])) {
                return true;
            }
        }

        return false;
    }

    private static List<Long[]> findElementsThatHaveMatches(List<Long[]> data) {

        int maxLength = findMaxLength(data);

        List<Long[]> haveMatches = new ArrayList<>();

        for (int i = 0; i < maxLength; i++) {
            int finalI = i;
            Long[] currentElements = data.stream()
                    .map(it -> Arrays.stream(it)
                            .skip(finalI)
                            .findFirst()
                            .orElse(0L)
                    )
                    .toArray(Long[]::new);

            Set<Long> notHaveCopies = new HashSet<>();
            Set<Long> hasCopies = new HashSet<>();
            for (long l : currentElements) {
                if (l != 0 && !notHaveCopies.add(l)) {
                    hasCopies.add(l);
                }
            }

            haveMatches.addAll(
                    data.stream()
                            .filter(s -> Arrays.stream(s)
                                    .skip(finalI)
                                    .limit(1)
                                    .anyMatch(hasCopies::contains))
                            .toList()
            );
        }

        return haveMatches
                .stream()
                .distinct()
                .collect(Collectors.toList());
    }

    private static int findMaxLength(List<Long[]> data) {
        return data.stream()
                .max(Comparator.comparingInt(it -> it.length))
                .orElse(new Long[0])
                .length;
    }
}
