package com.panacea.RufusPyramid.game.creatures;

/**
 * Created by lux on 19/09/15.
 */
public class Stats {

    private int maximumHP;
    private double attack, defence, speed;

    public Stats(int maximumHP, double attack, double defence, double speed){
        this.maximumHP = maximumHP;
        this.attack = attack;
        this.defence = defence;
        this.speed = speed;
    }

    public Stats(Stats statsToBeCloned){ //copy constructor, ref at: http://www.javapractices.com/topic/TopicAction.do?Id=12
        this(statsToBeCloned.getMaximumHP(), statsToBeCloned.getAttack(), statsToBeCloned.getDefence(), statsToBeCloned.getSpeed());
    }

    public Stats(){

    }

    public void setMaximumHP( int max ){ maximumHP = max; };
    public int getMaximumHP(){ return maximumHP; }

    public void  setAttack( double att ) { attack = att; }
    public double getAttack(){ return attack; }

    public void setDefence( double def ) { defence = def; }
    public double getDefence(){ return defence; }

    public void setSpeed( double speed ) { this.speed = speed; }
    public double getSpeed() { return this.speed; }

}
