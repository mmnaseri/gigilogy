package com.mmnaseri.bio.gigilogy;

import com.agileapes.nemo.action.impl.HelpAction;
import com.agileapes.nemo.action.impl.UsageAction;
import com.agileapes.nemo.exec.ExecutorContext;
import com.agileapes.nemo.util.ExceptionMessage;

/**
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (2013/6/22, 21:22)
 */
public class Runner {

    public static void main(String[] args) throws Exception {
        final ExecutorContext context = new ExecutorContext();
        final Run action = new Run();
        action.setDefaultAction(true);
        context.addAction("simulate", action);
        context.addAction("help", new HelpAction());
        context.addAction("usage", new UsageAction());
        try {
            context.execute(args);
        } catch (Exception e) {
            System.err.println(new ExceptionMessage(e));
        }
    }

}
