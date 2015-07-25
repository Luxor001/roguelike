package com.panacea.RufusPyramid.game.actions;

/**
 * Created by gio on 25/07/15.
 */
public class ActionResult {
    private boolean success;

    public ActionResult(boolean success) {
        this.success = success;
    }

    public boolean hasSuccess() {
        return this.success;
    }
}
