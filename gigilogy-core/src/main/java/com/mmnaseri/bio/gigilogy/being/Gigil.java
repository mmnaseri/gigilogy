package com.mmnaseri.bio.gigilogy.being;

import com.mmnaseri.bio.gigilogy.life.Action;
import com.mmnaseri.bio.gigilogy.life.Position;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Set;

/**
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (2013/6/22, 20:40)
 */
public abstract class Gigil {

    private static final Log log = LogFactory.getLog(Gigil.class);
    private static final List<String> names = new ArrayList<String>();
    private static final List<String> used = new ArrayList<String>();

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

    }

    private int age = 0;
    private Position position = new Position(1, 1);
    private String name;
    private Colony colony;
    protected final double friendliness;

    public abstract Set<? extends Gigil> reproduce(Gigil gigil);

    protected Gigil() {
        log.info("Deciding on a name for the newly born gigil");
        String name;
        if (names.isEmpty()) {
            log.info("We will have to improvise");
            do {
                int index = new Random().nextInt(used.size() - 1);
                name = used.get(index) + " " + used.get(index + 1);
            } while (used.contains(name));
        } else {
            name = names.get(new Random().nextInt(names.size()));
        }
        names.remove(name);
        used.add(name);
        setName(name);
        log.info("The name is: " + name);
        friendliness = new Random().nextDouble();
        log.info("Friendliness level is: " + friendliness);
    }

    public static int getLimit() {
        return Integer.MAX_VALUE;
    }

    public void die() {
        if (colony != null) {
            colony.remove(this);
        }
        names.add(name);
        used.remove(name);
    }

    public abstract double getHealth();

    public abstract boolean fight(Gigil gigil);

    public abstract Action meet(Gigil gigil);

    public int getAge() {
        return age;
    }

    public void ageOn() {
        age ++;
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
