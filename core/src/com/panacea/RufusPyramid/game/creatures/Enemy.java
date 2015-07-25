package com.panacea.RufusPyramid.game.creatures;

import com.badlogic.gdx.Gdx;
import com.panacea.RufusPyramid.common.Utilities;
import com.panacea.RufusPyramid.game.actions.ActionPerformedEvent;
import com.panacea.RufusPyramid.game.actions.ActionResult;
import com.panacea.RufusPyramid.game.actions.IAction;
import com.panacea.RufusPyramid.game.actions.MoveAction;

/**
 * Created by gio on 22/07/15.
 */
public class Enemy extends AbstractCreature {
    public Enemy(String name, String description, int maximumHP, double attack, double defence, double speed) {
        super(name, description, maximumHP, attack, defence, speed);
    }

    @Override
    public void chooseNextAction(ActionResult resultPreviousAction) {
        IAction toPerform = new MoveAction(this, Utilities.Directions.WEST);
        boolean success = toPerform.perform();
        ActionPerformedEvent event = new ActionPerformedEvent(toPerform, success);
        this.fireActionChosenEvent(event, this);
        Gdx.app.log("Enemy" + this.getName(), "Moved");
    }
}
