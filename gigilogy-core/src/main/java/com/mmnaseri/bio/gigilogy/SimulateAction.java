package com.mmnaseri.bio.gigilogy;

import com.agileapes.nemo.action.Action;
import com.agileapes.nemo.api.Help;
import com.agileapes.nemo.api.Option;
import com.mmnaseri.bio.gigilogy.being.Gigil;
import com.mmnaseri.bio.gigilogy.life.Colonization;
import com.mmnaseri.bio.gigilogy.life.State;

/**
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (2013/6/23, 18:55)
 */
@SuppressWarnings("FieldCanBeLocal")
@Help(
        value = "Will simulate the lives of specified gigils and their descendants",
        description = "This action starts with the given initial gigils, and helps them move " +
                "around with their lives, while keeping a state of the world they live in. See the " +
                "options for more customization."
)
public class SimulateAction extends Action {

    @Option(alias = 'i', required = true)
    @Help(
            value = "The initial number of gigils for which the simulation will run.",
            description = "These will be born in the smallest square space possible so that " +
                    "they have a reasonable chance of meeting each other before wandering off to " +
                    "die alone. However, this is not guaranteed."
    )
    private int initial;

    @Option(alias = 'l')
    @Help(
            value = "The maximum number of gigils alive at any time",
            description = "Should this limit be exceeded, the extra gigils will die in order of age. " +
                    "Consider this a model for overusing natural resources in a habitat that can " +
                    "host a limited number of people."
    )
    private int limit = 1000;

    @Option(alias = 'c')
    @Help(
            value = "The initial state of colonization",
            description = "You can either set it to 'single', which means that all the initial gigils " +
                    "-- and subsequently their descendants -- will be of the same colony, 'split', which " +
                    "will divide the gigils into two colonies, and 'individual' which will let marriage pacts " +
                    "be the basis of the formation of colonies."
    )
    private Colonization colonization = Colonization.SINGLE;

    @Option(alias = 'd')
    @Help(
            value = "The number of days this action will simulate",
            description = "A negative number means that the number of days will be equal to the number of " +
                    "initial gigils multiplied by 100."
    )
    private int days = -1;

    @Override
    public void execute() throws Exception {
        if (limit > Gigil.getLimit()) {
            throw new IllegalArgumentException("The limit cannot exceed " + Gigil.getLimit());
        }
        if (days <= 0) {
            days = initial * 100;
        }
        final State state = new State(initial, limit, colonization);
        int max = state.getGigils().size();
        for (int i = 0; i < days; i++) {
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
