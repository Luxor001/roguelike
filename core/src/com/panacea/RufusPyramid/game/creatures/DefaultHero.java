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
    private int goldAmount;
    private ICreature firstTarget;
    public HealthBar health;

    private DefaultHero() {
        this("Awesome Awful Hero", 0);
    }

//    TODO aggiungere il livello con l'oggetto Level
    public DefaultHero(String name, int goldAmount, int id) {
        super(name, DESCRIPTION, new Stats(MAX_HP, ATTACK, DEFENCE, SPEED), id);
        this.getHealthBar().setVisible(true);
        this.setCreatureType(CreatureType.HERO);
        this.goldAmount = goldAmount;
    }

    public DefaultHero(String name, int goldAmount) {
        super(name, DESCRIPTION, new Stats(MAX_HP, ATTACK, DEFENCE, SPEED));
        this.getHealthBar().setVisible(true);
        this.setCreatureType(CreatureType.HERO);
        this.goldAmount = goldAmount;
    }

    @Override
    public void chooseNextAction(ActionResult resultPreviousAction) {
        //il giocatore ora è libero di impartire ordini al proprio eroe
        //dal controller si abilita l'input utente
        return;
    }

    public void setFirstTarget(ICreature target){
        this.firstTarget = target;
    }
    public ICreature getFirstTarget(){
        return this.firstTarget;
    }

    public int getGoldAmount(){
        return this.goldAmount;
    }
    public void addGold(int gold){
        this.goldAmount += gold;
    }
}
