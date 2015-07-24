package com.panacea.RufusPyramid.game.creatures;

import com.badlogic.gdx.math.GridPoint2;
import com.panacea.RufusPyramid.common.AttributeChangeEvent;
import com.panacea.RufusPyramid.common.AttributeChangeListener;
import com.panacea.RufusPyramid.common.Utilities;
import com.panacea.RufusPyramid.game.actions.ActionPerformedEvent;
import com.panacea.RufusPyramid.game.actions.ActionPerformedListener;
import com.panacea.RufusPyramid.game.actions.IAgent;
import com.panacea.RufusPyramid.game.actions.MoveAction;
import com.panacea.RufusPyramid.map.Tile;

import java.util.ArrayList;
import java.util.List;

/**
 * Deve davvero essere una classe astratta? O basta chiamarla "GenericCreature" ?
 */
public abstract class AbstractCreature implements ICreature {

    private static int nextFreeIdCreature = -1;

    private ArrayList<PositionChangeListener> changeListeners;

    private int idCreature;
    private String name, description;
    private int maximumHP, currentHP;
    private double attack, defence, speed;
    private Tile position;
    private com.panacea.RufusPyramid.game.creatures.Backpack backpack;
    private int energy;
    private List<ActionPerformedListener> actionPerformedListeners;

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
        this.backpack = new com.panacea.RufusPyramid.game.creatures.Backpack();

        this.changeListeners = new ArrayList<PositionChangeListener>();
        this.actionPerformedListeners = new ArrayList<ActionPerformedListener>(1);
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

    /**
     * Modifica la posizione dell'eroe e ne provoca un'animazione di camminata.
     * @param currentPosition posizione da impostare.
     * @param path lista ordinata delle tiles percorse per arrivare alla currentPosition.
     */
    public void setPosition(Tile currentPosition, ArrayList<Tile> path) {
//        Gdx.app.log(AbstractCreature.class.toString(), "Chiamata a setPosition(), tile a " + currentPosition.getPosition().x + "," + currentPosition.getPosition().y);
        this.position = currentPosition;

        if (this.position != null) {    //TODO && this.map.isPointInsideMap(this.position)
            if (path.lastIndexOf(currentPosition) != path.size() - 1) {
                throw new IllegalArgumentException(
                        "L'ultimo elemento della lista path deve essere la Tile settata (corrispondente al paramentro currentPosition).");
            }

            ArrayList<GridPoint2> pointPath = new ArrayList<GridPoint2>();
            for (Tile tile : path) {
                pointPath.add(tile.getPosition());
            }
            this.firePositionChangeEvent(pointPath);

            this.fireActionPerformedEvent(
                    new ActionPerformedEvent(new MoveAction(this, Utilities.Directions.EAST), true),
                    this);
        }
    }

    @Override
    public com.panacea.RufusPyramid.game.creatures.Backpack getEquipment() {
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

    public void addChangeListener(PositionChangeListener listener) {
        this.changeListeners.add(listener);
    }

    private void firePositionChangeEvent(ArrayList<GridPoint2> path) {
//        Gdx.app.log(AbstractCreature.class.toString(), "Chiamata a firePositionChangeEvent()");
        PositionChangeEvent event = new PositionChangeEvent(this.getPosition().getPosition(), path);
        for (PositionChangeListener listener : this.changeListeners) {
            listener.changed(event, this);
//            Gdx.app.log(AbstractCreature.class.toString(), "firePositionChangeEvent(), foreach iteration.");
        }
    }

    public static class PositionChangeEvent extends AttributeChangeEvent<GridPoint2> {
        private final ArrayList<GridPoint2> path;

        public PositionChangeEvent(GridPoint2 newAttributeValue, ArrayList<GridPoint2> path) {
            super(newAttributeValue);
            this.path = path;
        }

        public ArrayList<GridPoint2> getPath() {
            return this.path;
        }
    }

    public static abstract class PositionChangeListener implements AttributeChangeListener<PositionChangeEvent> {
        public PositionChangeListener() {
        }

        public abstract void changed(PositionChangeEvent event, Object source);
    }

    @Override
    public int getEnergy() {
        return this.energy;
    }

    @Override
    public void setEnergy(int currentEnergy) {
        this.energy = currentEnergy;
    }

    @Override
    public void addActionProcessedListener(ActionPerformedListener listener) {
        this.actionPerformedListeners.add(listener);
    }

    @Override
    public void fireActionPerformedEvent(ActionPerformedEvent event, IAgent source) {
        for (ActionPerformedListener listener : this.actionPerformedListeners) {
            listener.performed(event, source);
        }
    }
}
