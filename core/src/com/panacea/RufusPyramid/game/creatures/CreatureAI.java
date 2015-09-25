package com.panacea.RufusPyramid.game.creatures;

/**
 * Created by Lux on 25/09/2015.
 */
public class CreatureAI {
    public enum State{
        STANDING,
        FOLLOWING,
        ATTACKING,
        LOSTSIGHT,
        ROAMING
    }
    private State currentState;
    private AbstractCreature creature;

    public CreatureAI(AbstractCreature creature){
        currentState = State.STANDING;
        this.creature = creature;
    }

    public void chooseNextAction(){


    }
}
