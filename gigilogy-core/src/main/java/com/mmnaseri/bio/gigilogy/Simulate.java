package com.mmnaseri.bio.gigilogy;

import com.agileapes.nemo.action.Action;
import com.agileapes.nemo.api.Option;
import com.mmnaseri.bio.gigilogy.being.Gigil;
import com.mmnaseri.bio.gigilogy.life.Colonization;
import com.mmnaseri.bio.gigilogy.life.State;

/**
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (2013/6/23, 18:55)
 */
@SuppressWarnings("FieldCanBeLocal")
public class Simulate extends Action {

    @Option(alias = 'i', required = true)
    private int initial;

    @Option(alias = 'l')
    private int limit = 1000;

    @Option(alias = 'c')
    private Colonization colonization = Colonization.SINGLE;

    @Override
    public void execute() throws Exception {
        if (limit > Gigil.getLimit()) {
            throw new IllegalArgumentException("The limit cannot exceed " + Gigil.getLimit());
        }
        final State state = new State(initial, limit, colonization);
        int max = state.getGigils().size();
        for (int i = 0; i < initial * 1000; i++) {
            State.log.info("");
            State.log.info("Day " + state.getDay());
            State.log.info(state.getGigils().size() + " are alive");
            state.progress();
            max = Math.max(max, state.getGigils().size());
            if (state.getGigils().isEmpty()) {
                State.log.info("Alas, they are all dead, after " + state.getDay() + " days");
                break;
            }
        }
        State.log.info("Their population reached a maximum of " + max);
        State.log.info("Total births: " + state.getBirths());
        State.log.info("Total deaths: " + state.getDeaths());
        State.log.info("Average lifespan: " + Math.round((double) state.getDaysAlive() / state.getDeaths() * 10) / 10.0);
    }
}
