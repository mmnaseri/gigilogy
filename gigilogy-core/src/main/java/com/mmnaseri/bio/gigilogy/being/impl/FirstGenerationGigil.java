package com.mmnaseri.bio.gigilogy.being.impl;

import com.mmnaseri.bio.gigilogy.being.Gigil;
import com.mmnaseri.bio.gigilogy.life.Gender;

import java.util.Collections;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

/**
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (2013/6/22, 20:45)
 */
public class FirstGenerationGigil extends Gigil<FirstGenerationGigil> {

    private final Gender gender;

    public FirstGenerationGigil(Gender gender) {
        this.gender = gender;
    }

    public Gender getGender() {
        return gender;
    }

    @Override
    public Set<FirstGenerationGigil> reproduce(FirstGenerationGigil gigil) {
        if (gender.equals(gigil.getGender()) || getAge() < 7 || gigil.getAge() < 7) {
            return Collections.emptySet();
        }
        final HashSet<FirstGenerationGigil> gigils = new HashSet<FirstGenerationGigil>();
        final int max = (int) (getHealth() * 10);
        if (max > 0) {
            int count = new Random().nextInt(max);
            while (count-- > 0) {
                gigils.add(new FirstGenerationGigil(Math.random() > 0.5 ? Gender.MALE : Gender.FEMALE));
            }
        }
        return gigils;
    }

    @Override
    public double getHealth() {
        if (new Random().nextInt(2000) > 1980) {
            //disease!
            return 0;
        }
        final double random = new Random().nextDouble();
        if (getAge() < 7) {
            return random * 0.5;
        } else if (getAge() < 15) {
            return 0.2 + random * 0.8;
        } else if (getAge() < 25) {
            return 0.2 + random * 0.5;
        } else if (getAge() < 35) {
            return random * 0.3;
        }
        return 0;
    }

}
