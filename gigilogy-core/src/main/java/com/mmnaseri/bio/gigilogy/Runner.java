package com.mmnaseri.bio.gigilogy;

import com.agileapes.nemo.action.impl.HelpAction;
import com.agileapes.nemo.action.impl.UsageAction;
import com.agileapes.nemo.exec.ExecutorContext;
import com.agileapes.nemo.util.ExceptionMessage;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

/**
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (2013/6/22, 21:22)
 */
public class Runner {

    public static void main(String[] args) throws Exception {
        Logger.getRootLogger().setLevel(Level.OFF);
        Logger.getLogger("com.mmnaseri.bio.gigilogy.life").setLevel(Level.ALL);
        final ExecutorContext context = new ExecutorContext();
        context.addAction("simulate", new Simulate());
        context.addAction("help", new HelpAction());
        final UsageAction usageAction = new UsageAction();
        usageAction.setDefaultAction(true);
        context.addAction("usage", usageAction);
        try {
            context.execute("simulate", "-i", "2");
        } catch (Exception e) {
            System.err.println(new ExceptionMessage(e));
        }
    }

}
