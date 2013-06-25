package com.mmnaseri.bio.gigilogy;

import com.agileapes.nemo.action.impl.HelpAction;
import com.agileapes.nemo.action.impl.UsageAction;
import com.agileapes.nemo.util.ExceptionMessage;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import static com.agileapes.nemo.exec.ExecutorContext.withActions;

/**
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (2013/6/22, 21:22)
 */
public class Runner {

    public static void main(String[] args) {
        Logger.getRootLogger().setLevel(Level.OFF);
        Logger.getLogger("com.mmnaseri.bio.gigilogy.life").setLevel(Level.ALL);
        try {
            withActions(UsageAction.class, SimulateAction.class, HelpAction.class).execute(args);
        } catch (Exception e) {
            System.err.println(new ExceptionMessage(e));
        }
    }

}
