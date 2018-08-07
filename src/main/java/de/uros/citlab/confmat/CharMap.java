package de.uros.citlab.confmat;

import java.util.HashMap;

public class CharMap {
    private HashMap<Character, Integer> map = new HashMap<>();
    private HashMap<Integer, String> map2 = new HashMap<>();
    int size = 1;

    public int size() {
        return size;
    }

    public Integer get(Character character) {
        return map.get(character);
    }

    public String get(Integer index) {
        return map2.get(index);
    }

    public Integer add(Character key) {
        if (map.containsKey(key)) {
            return map.get(key);
        }
        map.put(key, size);
        map2.put(size, "" + key);
        size++;
        check(key);
        return size - 1;
    }

    private void check(Character c) {
        try {
            if (!map2.get(map.get(c)).contains("" + c)) {
                throw new RuntimeException("no valid mapping for maps");
            }
        } catch (NullPointerException ex) {
            throw new RuntimeException("no valid mapping for maps");
        }
    }

    public Integer add(String keys) {
        char[] chars = keys.toCharArray();
        int idx = add(chars[0]);
        for (int i = 1; i < chars.length; i++) {
            add(chars[i], idx);
        }
        for (int i = 1; i < chars.length; i++) {
            check(chars[i]);
        }
        return idx;
    }

    public Integer add(Character key, Integer value) {
        if (map.containsKey(key)) {
            throw new RuntimeException("key '" + key + "' is already in charmap at position " + map.get(key));
        }
        if (value > size()) {
            throw new RuntimeException("index must be <= size (=" + size() + ")");
        }
        if (value == size()) {
            add(key);
            return value;
        }
        map.put(key, value);
        map2.put(value, map2.get(value) + key);
        check(key);
        return value;
    }

    public HashMap<Character, Integer> getMap() {
        return map;
    }
}
