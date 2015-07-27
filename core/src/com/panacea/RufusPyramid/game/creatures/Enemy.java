package com.panacea.RufusPyramid.game.creatures;

import com.badlogic.gdx.Gdx;
import com.panacea.RufusPyramid.common.Utilities;
import com.panacea.RufusPyramid.game.actions.ActionChosenEvent;
import com.panacea.RufusPyramid.game.actions.ActionResult;
import com.panacea.RufusPyramid.game.actions.IAction;
import com.panacea.RufusPyramid.game.actions.MoveAction;
import com.panacea.RufusPyramid.game.actions.PassAction;

/**
 * Created by gio on 22/07/15.
 */
public class Enemy extends AbstractCreature {
    public Enemy(String name, String description, int maximumHP, double attack, double defence, double speed) {
        super(name, description, maximumHP, attack, defence, speed);
    }

    /**
     * L'azione di default della classe Enemy, per ora, è passare il turno,
     * In questo modo la creatura rimarrà sempre nello stesso punto.
     * @param resultPreviousAction il risultato dell'azione precedente
     */
    @Override
    public void chooseNextAction(ActionResult resultPreviousAction) {
        IAction action = new PassAction();

        this.fireActionChosenEvent(action);
    }
}
