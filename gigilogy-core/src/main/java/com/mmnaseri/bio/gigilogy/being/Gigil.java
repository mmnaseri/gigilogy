package com.mmnaseri.bio.gigilogy.being;

import com.mmnaseri.bio.gigilogy.life.Position;

import java.util.Set;

/**
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (2013/6/22, 20:40)
 */
public abstract class Gigil<G extends Gigil> {

    private int age = 0;
    private Position position;
    private String name;

    public abstract Set<G> reproduce(G gigil);

    public void die() {
    }

    public abstract double getHealth();

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

    @Override
    public final String toString() {
        return name + "@" + position;
    }

    @Override
    public boolean equals(Object object) {
        return !(object == null || !(object instanceof Gigil<?>)) && position.equals(((Gigil) object).position);
    }

}
