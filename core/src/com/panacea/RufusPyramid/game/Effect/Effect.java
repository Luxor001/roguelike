package com.panacea.RufusPyramid.game.Effect;

/**
 * Created by Lux on 18/09/2015.
 */
public class Effect {

    public enum EffectType{
        HEALTH,
        MANA,
        ATTACK,
        DEFENSE,
        POISON
    }

    private float coefficient;
    private EffectType type;
    public Effect(EffectType type, float coefficient){
        this.coefficient=coefficient;
        this.type = type;
    }

    public float getCoefficient(){
        return coefficient;
    }

    public EffectType getTye(){
        return type;
    }
}
