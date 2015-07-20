package com.panacea.RufusPyramid.game.creatures;

import com.panacea.RufusPyramid.map.Tile;

/**
 * General creature interface.
 */
public interface ICreature {

    public int getID();

    public int getHPCurrent();
    public void setHPCurrent(int currentHP);

    public int getHPMaximum();
    public void setHPMaximum(int maxHP);

    public String getName();
    public void setName(String name);

    public String getDescription();
    public void setDescription(String description);

    public double getAttackValue();
    public void setAttackValue(double attackValue);

    public double getDefenceValue();
    public void setDefenceValue(double defenceValue);

    public double getSpeed();
    public void setSpeed(double currentSpeed);

    public Tile getPosition();
    public void setPosition(Tile currentPosition);

    public Backpack getEquipment();
}
