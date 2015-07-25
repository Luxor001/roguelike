package com.panacea.RufusPyramid.game.creatures;

import com.panacea.RufusPyramid.game.actions.ActionResult;
import com.panacea.RufusPyramid.game.actions.IAgent;
import com.panacea.RufusPyramid.map.Tile;

/**
 * General creature interface.
 */
public interface ICreature extends IAgent {

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

    public int getEnergy();
    public void setEnergy(int currentEnergy);

    /**
     * Questo metodo è l'unico a poter lanciare un ActionChosenEvent.
     * Nel caso di creatura gestita dall'AI deve scegliere autonomamente quale azione effettuar
     * e richimarci sopra il .perform()
     * In caso di creatura gestita da utente deve attendere un input, scegliere di conseguenza
     * quale azione eseguire e richiamare il .perform.
     * In caso di successo dell'azione il metodo deve SEMPRE lanciare un ActionChosenEvent per permettere
     * al GameMaster di gestire la turnazione.
     * @param resultPreviousAction
     */
    public void chooseNextAction(ActionResult resultPreviousAction);

    public Backpack getEquipment();
}
