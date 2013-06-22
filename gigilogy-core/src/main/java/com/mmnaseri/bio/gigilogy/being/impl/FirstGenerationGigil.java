package com.mmnaseri.bio.gigilogy.being.impl;

import com.mmnaseri.bio.gigilogy.being.Colony;
import com.mmnaseri.bio.gigilogy.being.Gigil;
import com.mmnaseri.bio.gigilogy.life.Action;
import com.mmnaseri.bio.gigilogy.life.Gender;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.Collections;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

/**
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (2013/6/22, 20:45)
 */
public class FirstGenerationGigil extends Gigil {

    private final static Log log = LogFactory.getLog(FirstGenerationGigil.class);
    public static final int REPRODUCTION_AGE_LIMIT = 7;

    private final Gender gender;

    public FirstGenerationGigil(Gender gender) {
        this.gender = gender;
        log.debug("Gender is " + gender);
    }

    public Gender getGender() {
        return gender;
    }

    @Override
    public Set<? extends Gigil> reproduce(Gigil gigil) {
        log.debug(this + " and " + gigil + " try to reproduce");
        if (equals(gigil)) {
            log.error("One cannot have kids with oneself!");
            return Collections.emptySet();
        } else if (getDistance(gigil) > 1) {
            log.error("They are too far apart to reproduce");
            return Collections.emptySet();
        } else if (!(gigil instanceof FirstGenerationGigil)) {
            log.error("They are not of the same race");
            return Collections.emptySet();
        } else if (gender.equals(((FirstGenerationGigil) gigil).getGender())) {
            log.error("They are of the same sex");
            return Collections.emptySet();
        } else if (getAge() < REPRODUCTION_AGE_LIMIT) {
            log.error(this + " is too young!");
            return Collections.emptySet();
        } else if (gigil.getAge() < REPRODUCTION_AGE_LIMIT) {
            log.error(gigil + " is too young!");
            return Collections.emptySet();
        }
        final HashSet<FirstGenerationGigil> gigils = new HashSet<FirstGenerationGigil>();
        final double myHealth = getHealth();
        final double gigilsHealth = gigil.getHealth();
        final int max = (int) Math.round(myHealth * gigilsHealth * 8);
        if (max > 0) {
            log.info("They can have a maximum of " + max + " kids");
            if (getColony() == null && gigil.getColony() != null) {
                log.info(this + " is joining colony " + gigil.getColony());
                setColony(gigil.getColony());
                gigil.getColony().add(this);
            } else if (getColony() != null && gigil.getColony() == null) {
                log.info(gigil + " is joining colony " + getColony());
                gigil.setColony(getColony());
                getColony().add(gigil);
            } else if (getColony() == null && gigil.getColony() == null) {
                log.info(this + " and " + gigil + " will found a new colony");
                final Colony colony = new Colony(gigil.getName() + "-" + getName());
                setColony(colony);
                gigil.setColony(colony);
                colony.add(gigil);
                colony.add(this);
            } else if (getColony() != null && gigil.getColony() != null) {
                if (myHealth > gigilsHealth) {
                    log.info(gigil.getColony() + " will join stronger colony " + getColony());
                    gigil.getColony().join(getColony());
                } else {
                    log.info(getColony() + " will join stronger colony " + gigil.getColony());
                    getColony().join(gigil.getColony());
                }
            }
            int count = new Random().nextInt(max);
            while (count-- > 0) {
                final FirstGenerationGigil firstGenerationGigil = new FirstGenerationGigil(Math.random() * (myHealth + gigilsHealth) < myHealth ? getGender() : ((FirstGenerationGigil) gigil).getGender());
                gigils.add(firstGenerationGigil);
                firstGenerationGigil.setColony(getColony());
                assert getColony() != null;
                getColony().add(firstGenerationGigil);
            }
            log.info("They had " + gigils.size() + " kids");
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
            return random * 0.4;
        } else if (getAge() < 15) {
            return 0.2 + random * 0.4;
        } else if (getAge() < 25) {
            final double health = 0.2 + (1.0 - Math.abs(getAge() - 20) / 5.0d) * random * 1.6;
            if (health > 1) {
                return 1;
            }
            return health;
        } else if (getAge() < 35) {
            return random * 0.3;
        }
        return 0;
    }

    @Override
    public boolean fight(Gigil gigil) {
        double myHealth = getHealth() * (getGender().equals(Gender.FEMALE) ? 0.8 : 1);
        double gigilsHealth = gigil.getHealth() * (gigil instanceof FirstGenerationGigil ? (((FirstGenerationGigil) gigil).getGender().equals(Gender.FEMALE) ? 0.8 : 1) : 0.3);
        myHealth *= 0.9 + Math.random() * 0.1;
        gigilsHealth *= 0.9 + Math.random() * 0.1;
        return myHealth > gigilsHealth;
    }

    @Override
    public Action meet(Gigil gigil) {
        if (gigil instanceof FirstGenerationGigil) {
            if (getGender().equals(Gender.FEMALE)) {
                if (((FirstGenerationGigil) gigil).getGender().equals(Gender.FEMALE)) {
                    return friendliness > 0.6 ? Action.BEFRIEND : (friendliness > 0.2 ? Action.LEAVE : Action.FIGHT);
                } else {
                    return friendliness > 0.4 ? Action.BEFRIEND : (friendliness > 0.1 ? Action.LEAVE : Action.FIGHT);
                }
            } else {
                if (((FirstGenerationGigil) gigil).getGender().equals(Gender.FEMALE)) {
                    return friendliness > 0.3 ? Action.BEFRIEND : (friendliness > 0.1 ? Action.LEAVE : Action.FIGHT);
                } else {
                    return friendliness > 0.7 ? Action.BEFRIEND : (friendliness > 0.5 ? Action.LEAVE : Action.FIGHT);
                }
            }
        } else {
            if (Math.random() > 0.5) {
                return Action.FIGHT;
            } else {
                return Action.LEAVE;
            }
        }
    }

    @Override
    public String toString() {
        return super.toString() + " [" + getGender().toString().charAt(0) + "]";
    }
}
