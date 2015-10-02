package com.panacea.RufusPyramid.game.actions;

import com.badlogic.gdx.graphics.Color;
import com.panacea.RufusPyramid.game.GameMaster;
import com.panacea.RufusPyramid.game.creatures.DefaultHero;
import com.panacea.RufusPyramid.game.creatures.ICreature;
import com.panacea.RufusPyramid.game.view.GameDrawer;

/**
 * Created by gio on 27/07/15.
 */
public class PassAction implements IAction {

    ICreature passer;
    public PassAction(ICreature passer){
        this.passer = passer;
    }
    @Override
    public ActionResult perform() {

        if(passer instanceof DefaultHero)
            GameDrawer.get().getCreaturesDrawer().displayInfo(passer.getPosition().getPosition(), "wait", Color.WHITE);

        return new ActionResult(true);
    }

    @Override
    public int getCost() {
        return GameMaster.DEFAULT_ACTION_COST;
    }
}
