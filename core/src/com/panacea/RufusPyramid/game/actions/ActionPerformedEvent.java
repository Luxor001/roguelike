package com.panacea.RufusPyramid.game.actions;

/**
 * Created by gio on 23/07/15.
 */
public class ActionPerformedEvent {
    private IAction actionPerformed;
    private boolean result;

    public ActionPerformedEvent(IAction actionPerformed, boolean actionResult) {
        this.actionPerformed = actionPerformed;
        this.result = actionResult;
    }

    public IAction getPerformedAction() {
        return this.actionPerformed;
    }

    public boolean getActionResult() {
        return this.result;
    }
}
