package com.mmnaseri.bio.gigilogy.being;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (2013/6/22, 20:42)
 */
public class Colony {

    private static final List<String> names = new ArrayList<String>();

    static {
        final BufferedReader reader = new BufferedReader(new InputStreamReader(Colony.class.getResourceAsStream("/names.txt")));
        String line;
        try {
            while ((line = reader.readLine()) != null) {
                names.add(line);
            }
            reader.close();
        } catch (IOException e) {
            throw new RuntimeException("Failed to read names");
        }

    }

    private final Map<String, Gigil> beings = new ConcurrentHashMap<String, Gigil>();
    private final String name;

    public Colony(String name) {
        this.name = name;
    }

    public void add(Gigil gigil) {
        gigil.setColony(this);
        beings.put(gigil.getName(), gigil);
    }

    public void addAll(Collection<Gigil> gigils) {
        for (Gigil gigil : gigils) {
            add(gigil);
        }
    }

    public Set<Gigil> getNearBy(Gigil gigil, double distance) {
        if (!beings.containsValue(gigil)) {
            throw new IllegalArgumentException(gigil + " is not a part of this colony");
        }
        final HashSet<Gigil> set = new HashSet<Gigil>();
        for (Gigil being : beings.values()) {
            if (being.equals(gigil)) {
                continue;
            }
            if (gigil.getDistance(being) <= distance) {
                set.add(being);
            }
        }
        return set;
    }

    public String getName() {
        return name;
    }

    public Map<String, Gigil> getBeings() {
        return beings;
    }

    public void join(Colony colony) {
        for (Gigil being : beings.values()) {
            remove(being);
            being.setName(being.getName() + " of " + getName());
            colony.add(being);
        }
    }

    public void remove(Gigil gigil) {
        beings.remove(gigil.getName());
    }

    @Override
    public String toString() {
        return getName() + " colony";
    }

}
