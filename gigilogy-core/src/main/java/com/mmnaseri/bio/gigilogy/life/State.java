package com.mmnaseri.bio.gigilogy.life;

import com.mmnaseri.bio.gigilogy.being.Colony;
import com.mmnaseri.bio.gigilogy.being.Gigil;
import com.mmnaseri.bio.gigilogy.being.impl.FirstGenerationGigil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.*;

/**
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (2013/6/23, 18:54)
 */
public class State {

    public final static Log log = LogFactory.getLog(State.class);
    private final static String[] fightReasons = new String[]{
            "I don't like your face",
            "You are way too close, buddy",
            "You are standing in my way",
            "You are so mean"
    };
    private final static String[] friendshipReasons = new String[]{
            "You look like a good person",
            "I think I've met you before",
            "I want to know you better",
            "we seem to share a connection"
    };
    private final Set<Gigil> gigils = new HashSet<Gigil>();
    private final Set<Gigil> dying = new HashSet<Gigil>();
    private int day = 0;
    private int limit;
    private int births = 0;
    private int deaths = 0;
    private int daysAlive = 0;

    public State(int initial, int limit, Colonization colonization) {
        this.limit = limit;
        final Position from = new Position(0, 0);
        final Position to = new Position((int) (Math.sqrt(initial) * 1.5), (int) (Math.sqrt(initial) * 1.5));
        double density = (double) initial / (to.getX() * to.getY());
        log.info("Initial density is %" + Math.round(density * 1000) / 10.0);
        Colony colony = colonization.equals(Colonization.SINGLE) ? new Colony("Firstborn") : null;
        for (int i = 0; i < initial; i++) {
            final FirstGenerationGigil gigil = new FirstGenerationGigil(i % 2 == 0 ? Gender.FEMALE : Gender.MALE);
            gigil.setPosition(newPosition(from, to));
            if (colony != null) {
                colony.add(gigil);
            }
            log.info("Bringing " + gigil + " to life at " + gigil.getPosition());
            birth(gigil);
        }
        if (colonization.equals(Colonization.SPLIT)) {
            colony = new Colony("First Colony");
            final Iterator<Gigil> iterator = gigils.iterator();
            for (int i = 0; i < initial / 2; i ++) {
                colony.add(iterator.next());
            }
            colony = new Colony("Second Colony");
            while (iterator.hasNext()) {
                colony.add(iterator.next());
            }
        }
    }

    private boolean isOccupied(Position position) {
        for (Gigil gigil : gigils) {
            if (gigil.getPosition().equals(position)) {
                return true;
            }
        }
        return false;
    }

    private Position newPosition(Position from, Position to) {
        Position position;
        int possible = (to.getX() - from.getX()) * (to.getY() - from.getY());
        do {
            possible--;
            position = new Position(from.getX() + new Random().nextInt(to.getX() - from.getX()),
                    from.getY() + new Random().nextInt(to.getY() - from.getY()));
        } while (isOccupied(position) && possible > -1);
        if (possible == -1) {
            throw new IllegalStateException("Not enough space for a new position");
        }
        return position;
    }

    private void watchLimit() {
        if (gigils.size() <= limit) {
            return;
        }
        log.info("Population limit reached. Some people have to go :-(");
        final ArrayList<Gigil> list = new ArrayList<Gigil>(gigils);
        Collections.sort(list, new Comparator<Gigil>() {
            @Override
            public int compare(Gigil o1, Gigil o2) {
                return new Integer(o2.getAge()).compareTo(o1.getAge());
            }
        });
        while (list.size() > limit) {
            markForDeath(list.get(0));
            list.remove(0);
        }
        killDying();
    }

    private void killDying() {
        for (Gigil gigil : dying) {
            log.info("The old man has set its eyes on " + gigil);
            gigil.die();
            death(gigil);
        }
        dying.clear();
    }

    private void markForDeath(Gigil gigil) {
        dying.add(gigil);
    }

    public Set<Gigil> getNeighbors(Gigil gigil) {
        final HashSet<Gigil> set = new HashSet<Gigil>();
        if (isDying(gigil)) {
            return set;
        }
        for (Gigil being : gigils) {
            if (being.equals(gigil)) {
                continue;
            }
            if (isDying(being)) {
                continue;
            }
            if (being.getDistance(gigil) <= 1) {
                set.add(being);
            }
        }
        return set;
    }

    public void progress() {
        final Map<Gigil, Set<? extends Gigil>> newlyBorn = new HashMap<Gigil, Set<? extends Gigil>>();
        day++;
        killDying();
        for (Gigil gigil : gigils) {
            gigil.ageOn();
        }
        for (Gigil gigil : gigils) {
            if (gigil.getHealth() <= 0) {
                log.info(gigil + " is simply too sick to carry on");
                markForDeath(gigil);
            }
            if (isDying(gigil)) {
                continue;
            }
            final Set<Gigil> neighbors = getNeighbors(gigil);
            if (neighbors.isEmpty()) {
                final List<Position> spots = getEmptySpots(gigil, 1);
                if (spots.isEmpty()) {
                    log.info(gigil + ": I am so lonely :-(");
                    continue;
                }
                final Position position = spots.get(new Random().nextInt(spots.size()));
                log.info(gigil + ": I want to meet people. I will move to  " + position);
                gigil.setPosition(position);
                continue;
            }
            if (neighbors.size() > 5) {
                log.info("Too many of them in one place. A fight is unavoidable ...");
                int health = 0;
                for (Gigil neighbor : neighbors) {
                    health += neighbor.getHealth();
                }
                if (health > gigil.getHealth()) {
                    log.info(gigil + " was too weak to survive it");
                    markForDeath(gigil);
                }
            }
            if (isDying(gigil)) {
                continue;
            }
            for (Gigil neighbor : neighbors) {
                if (isDying(gigil)) {
                    continue;
                }
                final Action action = gigil.meet(neighbor);
                log.info(gigil + ": Hey " + neighbor.getName() + "!");
                if (action.equals(Action.LEAVE)) {
                    log.info(gigil + ": since you don't answer me, I will move away");
                    final List<Position> spots = getEmptySpots(gigil, 1);
                    if (!spots.isEmpty()) {
                        final Position position = spots.get(new Random().nextInt(spots.size()));
                        gigil.setPosition(position);
                    } else {
                        log.info(gigil + ": but I don't have anywhere to go :-(");
                    }
                } else if (action.equals(Action.FIGHT)) {
                    log.info(gigil + ": " + fightReasons[new Random().nextInt(friendshipReasons.length)] + ", and I'm gonna kick your ass");
                    final boolean fight = gigil.fight(neighbor);
                    Gigil looser = fight ? neighbor : gigil;
                    log.info(looser + " has lost the fight");
                    if (Math.random() < 0.2) {
                        log.info("The fight was too much for " + looser);
                        markForDeath(looser);
                    }
                } else if (action.equals(Action.BEFRIEND)) {
                    log.info(gigil + ": " + friendshipReasons[new Random().nextInt(friendshipReasons.length)] + ", let's be friends!");
                    log.info(neighbor + ": Alrighty!");
                    if (gigil.canReproduce(neighbor)) {
                        log.info(gigil + ": I say, " + neighbor.getName() + ", let's get intimate.");
                        final Set<? extends Gigil> kids = gigil.reproduce(neighbor);
                        log.info("They got together and had " + kids.size() + " kids");
                        newlyBorn.put(gigil, kids);
                    }
                }
            }
        }
        killDying();
        handleNewborns(newlyBorn);
        watchLimit();
    }

    private List<Position> getEmptySpots(Gigil gigil, int margin) {
        final List<Position> positions = new ArrayList<Position>();
        final List<Position> unsuitable = new ArrayList<Position>();
        final Position position = gigil.getPosition();
        for (int x = position.getX() - margin; x < position.getX() + margin; x ++) {
            for (int y = position.getY() - margin; y < position.getY() + margin; y ++) {
                if (x == position.getX() && y == position.getY()) {
                    continue;
                }
                positions.add(new Position(x, y));
            }
        }
        for (Position spot : positions) {
            if (isOccupied(spot)) {
                unsuitable.add(spot);
            }
        }
        positions.removeAll(unsuitable);
        return positions;
    }

    private void handleNewborns(Map<Gigil, Set<? extends Gigil>> newlyBorn) {
        for (Gigil parent : newlyBorn.keySet()) {
            final Set<? extends Gigil> kids = newlyBorn.get(parent);
            if (kids.isEmpty()) {
                log.info(parent + " won't be having any kids");
                continue;
            }
            log.info(parent + " will have " + kids.size() + " kids");
            final List<Position> positions = getEmptySpots(parent, 2);
            log.info("There is enough room for " + positions.size() + " of them");
            for (Gigil kid : kids) {
                if (positions.isEmpty()) {
                    log.info("There wasn't enough room for " + kid);
                    kid.die();
                    break;
                }
                final Position position = positions.get(new Random().nextInt(positions.size()));
                kid.setPosition(position);
                birth(kid);
            }
        }
    }

    private boolean birth(Gigil kid) {
        births++;
        return gigils.add(kid);
    }

    private boolean death(Gigil gigil) {
        deaths++;
        daysAlive += gigil.getAge();
        return gigils.remove(gigil);
    }

    private boolean isDying(Gigil gigil) {
        return dying.contains(gigil);
    }

    public int getDay() {
        return day;
    }

    public Set<Gigil> getGigils() {
        return gigils;
    }

    public int getBirths() {
        return births;
    }

    public int getDeaths() {
        return deaths;
    }

    public int getDaysAlive() {
        return daysAlive;
    }
}
