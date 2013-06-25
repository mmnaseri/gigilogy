package com.mmnaseri.bio.gigilogy.being;

import com.mmnaseri.bio.gigilogy.life.Action;
import com.mmnaseri.bio.gigilogy.life.Position;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

/**
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (2013/6/22, 20:40)
 */
public abstract class Gigil {

    private static final Log log = LogFactory.getLog(Gigil.class);
    private static final List<String> names = new ArrayList<String>();
    private static final List<String> remaining = new ArrayList<String>();
    private static final List<String> used = new ArrayList<String>();
    private static final int limit;

    static {
        final BufferedReader reader = new BufferedReader(new InputStreamReader(Gigil.class.getResourceAsStream("/names.txt")));
        String line;
        try {
            while ((line = reader.readLine()) != null) {
                names.add(line);
            }
            reader.close();
        } catch (IOException e) {
            throw new RuntimeException("Failed to read names");
        }
        limit = names.size();
        remaining.addAll(names);
    }

    private int age = 0;
    private Position position = new Position(1, 1);
    private String name;
    private Colony colony;
    protected final double friendliness;

    public abstract Set<? extends Gigil> reproduce(Gigil gigil);
    public abstract boolean canReproduce(Gigil gigil);

    protected Gigil() {
        log.info("Deciding on a name for the newly born gigil");
        String name;
        if (used.containsAll(remaining)) {
            log.info("We will have to improvise");
            int times = Math.max((int) Math.round((double) used.size() / (double) names.size()), 2);
            do {
                final List<String> theName = new ArrayList<String>();
                for (int i = 0; i < times; i++) {
                    theName.add(names.get(new Random().nextInt(names.size())));
                }
                name = "";
                for (int i = 0; i < times; i++) {
                    if (i > 0) {
                        name += " ";
                    }
                    name += theName.get(i);
                }
            } while (used.contains(name));
        } else {
            do {
                if (remaining.size() == 1) {
                    name = remaining.get(0);
                } else {
                    name = remaining.get(new Random().nextInt(remaining.size()));
                }
            } while (used.contains(name));
        }
        used.add(name);
        remaining.remove(name);
        setName(name);
        log.info("The name is: " + name);
        friendliness = new Random().nextDouble();
        log.info("Friendliness level is: " + friendliness);
    }

    public static int getLimit() {
        return limit;
    }

    public void die() {
        if (colony != null) {
            colony.remove(this);
        }
        log.info(this + " is dead, and " + name + " can be used again");
        if (used.remove(name)) {
            remaining.add(name);
        }
    }

    public abstract double getHealth();

    public abstract boolean fight(Gigil gigil);

    public abstract Action meet(Gigil gigil);

    public int getAge() {
        return age;
    }

    public void ageOn() {
        age++;
    }

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Colony getColony() {
        return colony;
    }

    public void setColony(Colony colony) {
        this.colony = colony;
    }

    public double getDistance(Gigil second) {
        return Math.sqrt(Math.pow(getPosition().getX() - second.getPosition().getX(), 2) +
                Math.pow(getPosition().getY() - second.getPosition().getY(), 2));
    }

    @Override
    public String toString() {
        return name + (colony != null ? " of " + colony.getName() : " of no colony");
    }

    @Override
    public boolean equals(Object object) {
        return !(object == null || !(object instanceof Gigil)) && position.equals(((Gigil) object).position);
    }

}
