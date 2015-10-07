package com.panacea.RufusPyramid.game.creatures;

import com.panacea.RufusPyramid.game.actions.ActionResult;
import com.panacea.RufusPyramid.game.view.ui.HealthBar;


/**
 * Created by gio on 09/07/15.
 */
public class DefaultHero extends AbstractCreature {

    private static final String DESCRIPTION = "The hero of the game.";
    private static final int MAX_HP = 25;
    private static final double ATTACK = 7;
    private static final double DEFENCE = 7;
    private static final double SPEED = 7;
    private ICreature firstTarget;
    public HealthBar health;

//    TODO aggiungere il livello con l'oggetto Level
    public DefaultHero(String name) {
        super(name, DESCRIPTION, MAX_HP, ATTACK, DEFENCE, SPEED);
        //TODO la posizione di spawn va presa dalla mappa;
        this.getHealthBar().setVisible(true);
        this.setCreatureType(CreatureType.HERO);
    }

    @Override
    public void chooseNextAction(ActionResult resultPreviousAction) {
        //TODO il giocatore ora è libero di impartire ordini al proprio giocatore
        //TODO dal controller abilita l'input utente
        return;
    }

    public void setFirstTarget(ICreature target){
        this.firstTarget = target;
    }
    public ICreature getFirstTarget(){
        return this.firstTarget;
    }
}
