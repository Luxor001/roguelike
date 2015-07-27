package com.panacea.RufusPyramid.game.actions;

/**
 * Created by gio on 27/07/15.
 */
public class PassAction implements IAction {
    @Override
    public ActionResult perform() {
        return new ActionResult(true);
    }

    @Override
    public int getCost() {
        return 200;
    }
}
