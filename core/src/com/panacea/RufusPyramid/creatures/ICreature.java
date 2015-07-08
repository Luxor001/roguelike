package com.panacea.RufusPyramid.creatures;

import com.badlogic.gdx.math.GridPoint2;
import com.panacea.RufusPyramid.items.IItem;

import java.util.List;

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

    public int getAttackValue();
    public void setAttackValue(int attackValue);

    public int getDefenceValue();
    public void setDefenceValue(int defenceValue);

    public GridPoint2 getPosition();
    public void setPosition(GridPoint2 currentPosition);

    public List<IItem> getEquipment();
    public void setEquipment(List<IItem> currentEquipment);

    public int getSpeed();
    public void setSpeed(int currentSpeed);
}
