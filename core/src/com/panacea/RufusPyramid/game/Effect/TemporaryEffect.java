package com.panacea.RufusPyramid.game.Effect;

/**
 * Created by Lux on 18/09/2015.
 */
public class TemporaryEffect extends Effect {

    private int turns;

    protected TemporaryEffect() {}

    public TemporaryEffect(EffectType type, float coefficient, int turns){
        super(type,coefficient);
        this.turns = turns;
    }

    public int getTurns(){
        return turns;
    }

    public void setTurns(int turns){
        this.turns = turns;
    }
}
