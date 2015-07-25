package com.panacea.RufusPyramid.game.actions;

/**
 * Created by gio on 23/07/15.
 */
public class ActionPerformedEvent {
    private IAction actionPerformed;

    public ActionPerformedEvent(IAction actionPerformed) {
        this.actionPerformed = actionPerformed;
    }

    public IAction getChosenAction() {
        return this.actionPerformed;
    }
}
