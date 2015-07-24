package com.panacea.RufusPyramid.game.actions;

/**
 * Created by gio on 23/07/15.
 */
public interface ActionPerformedListener {
    void performed(ActionPerformedEvent event, IAgent source);
}
