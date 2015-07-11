package com.panacea.RufusPyramid.creatures;

import com.panacea.RufusPyramid.map.Tile;

/**
 * Deve davvero essere una classe astratta? O basta chiamarla "GenericCreature" ?
 */
public abstract class AbstractCreature implements ICreature {

    private static int nextFreeIdCreature = -1;

    private int idCreature;
    private String name, description;
    private int maximumHP, currentHP;
    private double attack, defence, speed;
    private Tile position;
    private Backpack backpack;

    public AbstractCreature(String name, String description, int maximumHP, double attack, double defence, double speed) {
        this.idCreature = getUniqueCreatureId();
        this.setName(name);
        this.setDescription(description);
        this.setHPMaximum(maximumHP);
        this.setHPCurrent(this.getHPMaximum());
        this.setAttackValue(attack);
        this.setDefenceValue(defence);
        this.setSpeed(speed);
        this.setPosition(null);
        this.backpack = new Backpack();
    }

    private static int getUniqueCreatureId() {
        return AbstractCreature.nextFreeIdCreature++;
    }

    @Override
    public int getID() {
        return this.idCreature;
    }

    @Override
    public int getHPCurrent() {
        return this.currentHP;
    }

    @Override
    public void setHPCurrent(int currentHP) {
        this.currentHP = currentHP;
    }

    @Override
    public int getHPMaximum() {
        return this.maximumHP;
    }

    @Override
    public void setHPMaximum(int maxHP) {
        this.maximumHP = maxHP;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public void setName(String name) {
        this.name = (name == null ? "Unknown" : name);
    }

    @Override
    public String getDescription() {
        return this.description;
    }

    @Override
    public void setDescription(String description) {
        this.description = (description == null ? "" : description);
    }

    @Override
    public double getAttackValue() {
        return this.attack;
    }

    @Override
    public void setAttackValue(double attackValue) {
        this.attack = attackValue;
    }

    @Override
    public double getDefenceValue() {
        return this.defence;
    }

    @Override
    public void setDefenceValue(double defenceValue) {
        this.defence = defenceValue;
    }

    /**
     * Può essere null se la creatura non è nella mappa.
     * @return la posizione della creatura nella mappa, null se non è presente in mappa.
     */
    @Override
    public Tile getPosition() {
        return this.position;
    }

    @Override
    public void setPosition(Tile currentPosition) {
        this.position = currentPosition;
    }

    @Override
    public Backpack getEquipment() {
        return this.backpack;
    }

    @Override
    public double getSpeed() {
        return this.speed;
    }

    @Override
    public void setSpeed(double currentSpeed) {
        this.speed = currentSpeed;
    }
}
