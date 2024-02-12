package org.example;

import java.util.*;
import java.util.stream.Collectors;

public class GroupDivider {

    private static class Group {
        private final List<Float[]> group;

        private final List<Group> linkedGroups = new ArrayList<>();

        public Group(List<Float[]> group) {
            this.group = group;
        }

        public static List<List<Group>> getConnectedGroups(Set<Group> groupsList) {
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

    public static List<List<Float[]>> divide(List<Float[]> data) {

        List<Map<Float, Group>> groupsByColumns = findGroupsByColumns(data);

        for (int i = 0; i < groupsByColumns.size() - 1; i++) {
            Map<Float, Group> headGroups = groupsByColumns.get(i);
            for (int j = i + 1; j < groupsByColumns.size(); j++) {
                Map<Float, Group> checkedGroups = groupsByColumns.get(j);

                for (Map.Entry<Float, Group> currentGroup : headGroups.entrySet()) {
                    for (Float[] line : currentGroup.getValue().group) {

                        if (!(line.length > j)) {
                            continue;
                        }

                        Group groupToConnect = checkedGroups.get(line[j]);
                        if (groupToConnect != null) {
                            groupToConnect.linkedGroups.add(currentGroup.getValue());
                            currentGroup.getValue().linkedGroups.add(groupToConnect);
                            break;
                        }
                    }
                }
            }
        }

        Set<Group> groupsList = groupsByColumns
                .stream()
                .flatMap(it -> it.values().stream())
                .collect(Collectors.toSet());


        return Group.getConnectedGroups(groupsList)
                .stream()
                .map(it -> it.stream()
                        .flatMap(g -> g.group.stream())
                        .distinct()
                        .collect(Collectors.toList()))
                .sorted((list1, list2) -> Integer.compare(list2.size(), list1.size()))
                .collect(Collectors.toList());
    }

    private static int findMaxLength(List<Float[]> data) {
        return data.stream()
                .max(Comparator.comparingInt(it -> it.length))
                .orElse(new Float[0])
                .length;
    }

    private static List<Map<Float, Group>> findGroupsByColumns(List<Float[]> data) {
        int maxLength = findMaxLength(data);

        List<Map<Float, Group>> result = new ArrayList<>();

        for (int i = 0; i < maxLength; i++) {
            int finalI = i;
            Float[] currentElements = data.stream()
                    .map(it -> Arrays.stream(it)
                            .skip(finalI)
                            .findFirst()
                            .orElse(0F)
                    )
                    .toArray(Float[]::new);
            Set<Float> notHaveCopies = new HashSet<>();
            Set<Float> hasCopies = new HashSet<>();
            for (Float l : currentElements) {
                if (l != 0 && !notHaveCopies.add(l)) {
                    hasCopies.add(l);
                }
            }

            Map<Float, Group> mapWithMatches = data.stream()
                    .filter(s -> Arrays.stream(s)
                            .skip(finalI)
                            .limit(1)
                            .anyMatch(hasCopies::contains))
                    .collect(Collectors.groupingBy(
                            s -> Arrays.stream(s)
                                    .skip(finalI)
                                    .limit(1)
                                    .findFirst().orElse(0F),
                            Collectors.collectingAndThen(Collectors.toList(), Group::new)
                    ));

            if (!mapWithMatches.isEmpty()) {
                result.add(mapWithMatches);
            }
        }
        return result;
    }
}
