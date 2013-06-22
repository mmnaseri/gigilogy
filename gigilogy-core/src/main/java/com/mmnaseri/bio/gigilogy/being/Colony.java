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
public class Colony<G extends Gigil<G>> {

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

    private final Map<String, G> beings = new ConcurrentHashMap<String, G>();

    public void add(G gigil) {
        if (gigil.getName() != null && beings.containsKey(gigil.getName())) {
            throw new IllegalArgumentException("Another gigil with this name already exists");
        }
        if (gigil.getName() == null) {
            String name = names.get(new Random().nextInt(names.size()));
            while (beings.containsKey(name)) {
                if (beings.size() >= names.size()) {
                    name = names.get(new Random().nextInt(names.size())) + " " + name;
                } else {
                    name = names.get(new Random().nextInt(names.size()));
                }
            }
            gigil.setName(name);
        }
        beings.put(gigil.getName(), gigil);
    }

    public Set<G> getNearBy(G gigil, double distance) {
        if (!beings.containsValue(gigil)) {
            throw new IllegalArgumentException(gigil + " is not a part of this colony");
        }
        final HashSet<G> set = new HashSet<G>();
        for (G being : beings.values()) {
            if (being.equals(gigil)) {
                continue;
            }
            if (getDistance(gigil, being) <= distance) {
                set.add(being);
            }
        }
        return set;
    }

    private double getDistance(G first, G second) {
        return Math.sqrt(Math.pow(first.getPosition().getX() - second.getPosition().getX(), 2) +
                Math.pow(first.getPosition().getY() - second.getPosition().getY(), 2));
    }

}
