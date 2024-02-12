package org.example;

import java.util.*;
import java.util.stream.Collectors;

public class GroupManager<T> {

    private final Map<KeySet, Group> values = new HashMap<>();

    public GroupManager(List<Map<T, List<T[]>>> baseGroups) {
        for (int i = 0; i < baseGroups.size(); i++) {
            Map<T, List<T[]>> map = baseGroups.get(i);
            for (Map.Entry<T, List<T[]>> entry : map.entrySet()) {
                values.put(new KeySet(new Key(i, entry.getKey())), new Group(entry.getValue()));
            }
        }
    }

    public List<List<T[]>> result() {
        return values.values()
                .stream()
                .flatMap(it -> it.groupList.stream())
                .collect(Collectors.toList());
    }

    private class KeySet {
        private final Set<Key> keys = new HashSet<>();

        public KeySet(Key key) {
            keys.add(key);
        }

        public void connectKeySet(KeySet keySet) {
            keys.addAll(keySet.keys);
        }

        public void addKey(Key key) {
            keys.add(key);
        }

        public boolean containsKey(Key key) {
            return keys.contains(key);
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            KeySet keySet = (KeySet) o;
            return Objects.equals(keys, keySet.keys);
        }

        @Override
        public int hashCode() {
            return Objects.hash(keys);
        }
    }

    private class Key {
        private final int index;
        private final T value;

        public Key(int index, T value) {
            this.index = index;
            this.value = value;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Key key = (Key) o;
            return index == key.index && Objects.equals(value, key.value);
        }

        @Override
        public int hashCode() {
            return Objects.hash(index, value);
        }
    }

    private class Group {
        private final List<List<T[]>> groupList;

        public Group(List<T[]> group) {
            groupList = new ArrayList<>();
            groupList.add(group);
        }

        public void addBaseGroup(List<T[]> el) {
            groupList.add(el);
        }

        public void connectGroup(Group group) {
            groupList.addAll(group.groupList);
        }

        public boolean containsBaseGroup(List<T[]> baseGroup) {
            return groupList.contains(baseGroup);
        }
    }

    public void connectGroups(List<T[]> baseGroup1, int index1, T value1,
                              List<T[]> baseGroup2, int index2, T value2) {

        Key key1 = new Key(index1, value1);
        Key key2 = new Key(index2, value2);

        KeySet keySet1 = null;
        KeySet keySet2 = null;

        Iterator<KeySet> iterator = values.keySet().iterator();

        while (keySet1 == null || keySet2 == null) {
            KeySet iteratorKeySet = iterator.next();

            if (iteratorKeySet.containsKey(key1)) {
                keySet1 = iteratorKeySet;
            }
            if (iteratorKeySet.containsKey(key2)) {
                keySet2 = iteratorKeySet;
            }
        }

        if (keySet1 != keySet2) {
            Group group1 = values.remove(keySet1);
            Group group2 = values.remove(keySet2);

            group1.connectGroup(group2);
            keySet1.connectKeySet(keySet2);

            values.put(keySet1, group1);
        }
    }
}
