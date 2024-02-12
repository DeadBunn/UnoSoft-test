package org.example;

import java.util.*;
import java.util.stream.Collectors;

public class GroupDivider {

    public static List<List<Float[]>> divide(List<Float[]> data) {

        List<Map<Float, List<Float[]>>> groupsByColumns = findGroupsByColumns(data);

        GroupManager<Float> groupManager = new GroupManager<>(groupsByColumns);

        for (int i = 0; i < groupsByColumns.size() - 1; i++) {
            Map<Float, List<Float[]>> headGroups = groupsByColumns.get(i);
            for (int j = i + 1; j < groupsByColumns.size(); j++) {
                Map<Float, List<Float[]>> checkedGroups = groupsByColumns.get(j);

                for (Map.Entry<Float, List<Float[]>> currentGroup : headGroups.entrySet()) {
                    for (Float[] line : currentGroup.getValue()) {

                        if (!(line.length > j)) {
                            continue;
                        }

                        List<Float[]> groupToConnect = checkedGroups.get(line[j]);
                        if (groupToConnect != null) {
                            groupManager.connectGroups(currentGroup.getValue(), i, currentGroup.getKey(), groupToConnect, j, line[j]);
                            break;
                        }
                    }
                }
            }
        }
        return groupManager.result();
    }

    private static int findMaxLength(List<Float[]> data) {
        return data.stream()
                .max(Comparator.comparingInt(it -> it.length))
                .orElse(new Float[0])
                .length;
    }

    public static List<Map<Float, List<Float[]>>> findGroupsByColumns(List<Float[]> data) {
        int maxLength = findMaxLength(data);

        List<Map<Float, List<Float[]>>> result = new ArrayList<>();

        for (int i = 0; i < maxLength; i++) {
            int finalI = i;
            System.out.println("Начало поиска для " + i + " " + System.currentTimeMillis());
            Float[] currentElements = data.stream()
                    .map(it -> Arrays.stream(it)
                            .skip(finalI)
                            .findFirst()
                            .orElse(0F)
                    )
                    .toArray(Float[]::new);
            System.out.println("Конец поиска для " + i + " " + System.currentTimeMillis());
            Set<Float> notHaveCopies = new HashSet<>();
            Set<Float> hasCopies = new HashSet<>();
            for (Float l : currentElements) {
                if (l != 0 && !notHaveCopies.add(l)) {
                    hasCopies.add(l);
                }
            }

            System.out.println("Конец поиска копий для " + i + " " + System.currentTimeMillis());

            Map<Float, List<Float[]>> mapWithMatches = data.stream()
                    .filter(s -> Arrays.stream(s)
                            .skip(finalI)
                            .limit(1)
                            .anyMatch(hasCopies::contains))
                    .collect(Collectors.groupingBy(
                            s -> Arrays.stream(s)
                                    .skip(finalI)
                                    .limit(1)
                                    .findFirst().orElse(0F),
                            Collectors.toList()
                    ));

            if (!mapWithMatches.isEmpty()) {
                result.add(mapWithMatches);
            }
        }
        return result;
    }
}
