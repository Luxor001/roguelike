package com.panacea.RufusPyramid.game.creatures;

import com.panacea.RufusPyramid.game.GameModel;
import com.panacea.RufusPyramid.game.actions.ActionResult;
import com.panacea.RufusPyramid.game.actions.IAction;
import com.panacea.RufusPyramid.game.actions.PassAction;

/**
 * Created by gio on 22/07/15.
 */
public class Enemy extends AbstractCreature {
    private CreatureAI creatureAI;

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
        if(creatureAI == null)
            creatureAI = new CreatureAI(this, GameModel.get().getHero());
        IAction action = creatureAI.chooseNextAction();
        this.fireActionChosenEvent(action);
    }
}
