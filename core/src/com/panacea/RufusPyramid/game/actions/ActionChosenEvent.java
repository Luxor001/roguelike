package com.panacea.RufusPyramid.game.actions;

/**
 * Created by gio on 23/07/15.
 */
public class ActionChosenEvent {
    private IAction actionChosen;

    public ActionChosenEvent(IAction actionChosen) {
        this.actionChosen = actionChosen;
    }

    public IAction getChosenAction() {
        return this.actionChosen;
    }
}
