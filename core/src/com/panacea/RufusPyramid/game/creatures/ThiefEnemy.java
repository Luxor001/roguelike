package com.panacea.RufusPyramid.game.creatures;

import com.panacea.RufusPyramid.common.Utilities;
import com.panacea.RufusPyramid.game.actions.ActionResult;
import com.panacea.RufusPyramid.game.actions.IAction;
import com.panacea.RufusPyramid.game.actions.MoveAction;

/**
 * Created by gio on 27/07/15.
 */
public class ThiefEnemy extends Enemy {
    public ThiefEnemy(String name, String description, int maximumHP, double attack, double defence, double speed) {
        super(name, description, maximumHP, attack, defence, speed);
    }

    @Override
    public void chooseNextAction(ActionResult resultPreviousAction) {
//        super.chooseNextAction(resultPreviousAction);
        IAction action = new MoveAction(this, Utilities.Directions.EAST);

        this.fireActionChosenEvent(action);
    }
}
