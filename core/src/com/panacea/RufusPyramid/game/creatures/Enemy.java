package com.panacea.RufusPyramid.game.creatures;

import com.panacea.RufusPyramid.game.GameModel;
import com.panacea.RufusPyramid.game.actions.ActionResult;
import com.panacea.RufusPyramid.game.actions.IAction;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by gio on 22/07/15.
 */
public class Enemy extends AbstractCreature {
    private transient CreatureAI creatureAI;
    private static final Set<CreatureAI.State> displayHealthBarStates = new HashSet<CreatureAI.State>(Arrays.asList(CreatureAI.State.ATTACKING, CreatureAI.State.FOLLOWING));
    public Enemy(String name, String description, int maximumHP, double attack, double defence, double speed, CreatureType type) {
        super(name, description, maximumHP, attack, defence, speed);
        this.setCreatureType(type);
    }
    public Enemy(String name, String description, Stats stats, CreatureType type, int id) {
        super(name, description, stats, id);
        this.setCreatureType(type);
    }
    public Enemy(String name, String description, Stats stats, CreatureType type) {
        super(name, description, stats.getMaximumHP(), stats.getAttack(), stats.getDefence(),stats.getSpeed());
        this.setCreatureType(type);
    }
    private Enemy(){

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
        if(displayHealthBarStates.contains(creatureAI.getCurrentState()))
            this.getHealthBar().setVisible(true);
        else
            this.getHealthBar().setVisible(false);
        this.fireActionChosenEvent(action);
    }

}
