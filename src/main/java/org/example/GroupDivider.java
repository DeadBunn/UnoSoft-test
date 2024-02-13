package org.example;

import java.util.*;
import java.util.stream.Collectors;

public class GroupDivider {

    private static class Group {
        private final List<Double[]> group;

        private final List<Group> linkedGroups = new ArrayList<>();

        public Group(List<Double[]> group) {
            this.group = group;
        }

        public static List<List<Group>> getConnectedGroups(List<Group> groupsList) {
            List<List<Group>> connectedGroups = new ArrayList<>();
            Set<Group> visited = new HashSet<>();

            for (Group group : groupsList) {
                if (!visited.contains(group)) {
                    List<Group> connected = new ArrayList<>();
                    dfs(group, visited, connected);
                    connectedGroups.add(connected);
                }
            }

            return connectedGroups;
        }

        private static void dfs(Group group, Set<Group> visited, List<Group> connected) {
            visited.add(group);
            connected.add(group);

            for (Group linkedGroup : group.linkedGroups) {
                if (!visited.contains(linkedGroup)) {
                    dfs(linkedGroup, visited, connected);
                }
            }
        }
    }

    public static List<List<Double[]>> divide(List<Double[]> data) {

        List<Map<Double, Group>> groupsByColumns = findGroupsByColumns(data);

        for (int i = 0; i < groupsByColumns.size() - 1; i++) {
            Map<Double, Group> headGroups = groupsByColumns.get(i);
            for (int j = i + 1; j < groupsByColumns.size(); j++) {
                Map<Double, Group> checkedGroups = groupsByColumns.get(j);

                for (Map.Entry<Double, Group> currentGroup : headGroups.entrySet()) {
                    for (Double[] line : currentGroup.getValue().group) {

                        if (!(line.length > j)) {
                            continue;
                        }

                        Group groupToConnect = checkedGroups.get(line[j]);
                        if (groupToConnect != null) {
                            groupToConnect.linkedGroups.add(currentGroup.getValue());
                            currentGroup.getValue().linkedGroups.add(groupToConnect);
                        }
                    }
                }
            }
        }

        List<Group> groupsList = groupsByColumns
                .stream()
                .flatMap(it -> it.values().stream())
                .collect(Collectors.toList());


        return Group.getConnectedGroups(groupsList)
                .stream()
                .map(it -> it.stream()
                        .flatMap(g -> g.group.stream())
                        .distinct()
                        .toList()
                )
                .sorted((list1, list2) -> Integer.compare(list2.size(), list1.size()))
                .toList();
    }

    private static int findMaxLength(List<Double[]> data) {
        return data.stream()
                .max(Comparator.comparingInt(it -> it.length))
                .orElse(new Double[0])
                .length;
    }

    private static List<Map<Double, Group>> findGroupsByColumns(List<Double[]> data) {
        int maxLength = findMaxLength(data);

        List<Map<Double, Group>> result = new ArrayList<>();

        for (int i = 0; i < maxLength; i++) {

            int finalInt = i;

            Map<Double, List<Double[]>> resultMap = new HashMap<>();

            data.forEach(DoubleArray -> {
                if (DoubleArray.length > finalInt && DoubleArray[finalInt] != 0) {
                    resultMap.putIfAbsent(DoubleArray[finalInt], new ArrayList<>());
                    resultMap.get(DoubleArray[finalInt]).add(DoubleArray);
                }
            });

            Map<Double, Group> transformedMap = resultMap.entrySet().stream()
                    .filter(it -> it.getValue().size() >= 2)
                    .collect(Collectors.toMap(Map.Entry::getKey, entry -> new Group(entry.getValue())));

            if (!transformedMap.isEmpty()) {
                result.add(transformedMap);
            }
        }
        return result;
    }
}
