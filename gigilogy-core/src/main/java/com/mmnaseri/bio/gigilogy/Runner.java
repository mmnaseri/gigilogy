package com.mmnaseri.bio.gigilogy;

import com.mmnaseri.bio.gigilogy.being.Gigil;
import com.mmnaseri.bio.gigilogy.being.impl.FirstGenerationGigil;
import com.mmnaseri.bio.gigilogy.life.Action;
import com.mmnaseri.bio.gigilogy.life.Gender;
import com.mmnaseri.bio.gigilogy.life.Position;

import java.util.Collection;
import java.util.Collections;
import java.util.Set;

/**
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (2013/6/22, 21:22)
 */
public class Runner {

    public static void main(String[] args) {
        final FirstGenerationGigil first = new FirstGenerationGigil(Gender.FEMALE);
        while (first.getAge() < 20) {
            first.ageOn();
        }
        final FirstGenerationGigil second = new FirstGenerationGigil(Gender.MALE);
        while (second.getAge() < 20) {
            second.ageOn();
        }
        second.setPosition(new Position(1, 2));
        final Collection<Gigil> beings;
        if (!first.reproduce(second).isEmpty()) {
            beings = first.getColony().getBeings().values();
        } else {
            beings = Collections.emptySet();
        }
        for (Gigil being : beings) {
            for (Gigil gigil : beings) {
                if (being == gigil) {
                    continue;
                }
                System.out.println(being + " meets " + gigil);
                final Action meet = being.meet(gigil);
                System.out.println("\t" + meet);
                if (meet.equals(Action.BEFRIEND)) {
                    final Set<? extends Gigil> kids = being.reproduce(gigil);
                    if (!kids.isEmpty()) {
                        System.out.println("\tthey had " + kids.size() + " kids");
                    }
                } else if (meet.equals(Action.FIGHT)) {
                    final boolean fight = being.fight(gigil);
                    System.out.println("\t" + (fight ? being : gigil) + " wins the fight");
                }
            }
        }
    }

}
