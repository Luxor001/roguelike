package com.panacea.RufusPyramid.game.creatures;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.panacea.RufusPyramid.common.AttributeChangeEvent;
import com.panacea.RufusPyramid.common.AttributeChangeListener;
import com.panacea.RufusPyramid.common.Utilities;
import com.panacea.RufusPyramid.game.Effect.Effect;
import com.panacea.RufusPyramid.game.actions.ActionChosenEvent;
import com.panacea.RufusPyramid.game.actions.ActionChosenListener;
import com.panacea.RufusPyramid.game.actions.IAction;
import com.panacea.RufusPyramid.game.actions.IAgent;
import com.panacea.RufusPyramid.game.actions.MoveAction;
import com.panacea.RufusPyramid.game.view.GameDrawer;
import com.panacea.RufusPyramid.game.view.ui.HealthBar;
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
    private int currentHP;
    private Tile position;
    private com.panacea.RufusPyramid.game.creatures.Backpack backpack;
    private int energy;
    private List<ActionChosenListener> actionChosenListeners;
    private List<CreatureDeadListener> creatureDeadListeners;

    public static int DEFAULT_HEALTHBAR_HEIGHT = 7;
    public static int DEFAULT_HEALTHBAR_WIDTH = 20;
    private ArrayList<Effect> effects;
    private Stats baseStats;
    private transient HealthBar healthBar;
    private GridPoint2 absoluteTickPosition; //absolute position for the current "tick" cycle, useful for camera centering.

    private boolean flipX;
    public int sigthLength = 6; //TODO : Da mettere da costruttore!

    private CreatureType creatureType;

    public AbstractCreature(String name, String description, Stats stats, int id) {
        this.effects = new ArrayList<Effect>();

        this.idCreature = id;
        this.setName(name);
        this.setDescription(description);
        this.baseStats = stats;
        this.setHPCurrent(this.baseStats.getMaximumHP());
        this.setPosition(null);
        this.backpack = new com.panacea.RufusPyramid.game.creatures.Backpack();

        this.changeListeners = new ArrayList<PositionChangeListener>();
        this.actionChosenListeners = new ArrayList<ActionChosenListener>(1);
        this.creatureDeadListeners = new ArrayList<CreatureDeadListener>();

        SpriteDrawable foreGround = new SpriteDrawable(new Sprite(new Texture(Gdx.files.internal("data/health_bar.png"))));
        SpriteDrawable background = new SpriteDrawable(new Sprite(new Texture(Gdx.files.internal("data/loading-frame.png"))));

        this.healthBar = new HealthBar((float)0,(float)getCurrentStats().getMaximumHP(),1,false,new HealthBar.ProgressBarStyle(background,foreGround));
        this.healthBar.setValue((getHPCurrent() * 100)  / getBaseStats().getMaximumHP());
        this.healthBar.setHeight(DEFAULT_HEALTHBAR_HEIGHT);
        this.healthBar.setWidth(DEFAULT_HEALTHBAR_WIDTH );
        this.healthBar.setVisible(false);
        this.flipX = false;
    }

    public AbstractCreature(String name, String description, Stats stats) {
        this(name, description, stats, getUniqueCreatureId());
    }

    public AbstractCreature(String name, String description, int maximumHP, double attack, double defence, double speed) {
        this(name, description, new Stats(maximumHP, attack, defence, speed));
    }
    public AbstractCreature(){

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
        if (this.currentHP <= 0) {
            this.fireCreatureDeadEvent();
        } else if (this.currentHP < this.getCurrentStats().getMaximumHP()) {
            this.getHealthBar().setVisible(true);
        }
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

            this.fireActionChosenEvent(
                    new ActionChosenEvent(new MoveAction(this, Utilities.Directions.EAST)),
                    this);

        }
    }

    @Override
    public com.panacea.RufusPyramid.game.creatures.Backpack getEquipment() {
        return this.backpack;
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
    public ArrayList<Effect> getEffects(){
        return effects;
    } //The effects (+ATTACK, -DEFENCE ecc.ecc.) this creatures currently has.
    @Override
    public void addEffect(Effect effect){
        effects.add(effect);
    }
    public void addEffects(List<Effect> effects){
        for (Effect currEffect: effects) {
            this.effects.add(currEffect);
        }
    }

    @Override
    public void addActionChosenListener(ActionChosenListener listener) {
        this.actionChosenListeners.add(listener);
    }

    @Override
    public void fireActionChosenEvent(ActionChosenEvent event, IAgent source) {
        for (ActionChosenListener listener : this.actionChosenListeners) {
            listener.performed(event, source);
        }
    }

    public void fireActionChosenEvent(IAction chosenAction) {
        this.fireActionChosenEvent(new ActionChosenEvent(chosenAction), this);
    }

    public void fireCreatureDeadEvent() {
        // TODO: andrebbe messo nel controller... Ma come?
        GameDrawer.get().getCreaturesDrawer().startDeath(this);
        // TODO: DA SISTEMARE LE ANIMAZIONI

        CreatureDeadEvent event = new CreatureDeadEvent(this.getHPCurrent(), null);
        for (CreatureDeadListener listener : this.creatureDeadListeners) {
            listener.changed(event, this);
        }
    }

    public void addCreatureDeadListener(CreatureDeadListener listener) {
        this.creatureDeadListeners.add(listener);
    }

    public boolean hasEffects(){
        return !effects.isEmpty();
    }

    public Stats getBaseStats(){
        return baseStats;
    }

    public void setBaseStats(Stats stats) {
        this.baseStats = stats;
    }

    public Stats getCurrentStats(){ //current stats calculated by the effects of the creature

        Stats currStats = new Stats(baseStats);
        for(Effect effect: effects){
            float value = effect.getCoefficient();
            switch(effect.getTye()){
                case ATTACK:{
                    currStats.setAttack(baseStats.getAttack() + value);
                    break;
                }
                case DEFENSE:{
                    currStats.setAttack(baseStats.getDefence() + value);
                    break;
                }
                case SPEED:{
                    currStats.setAttack(baseStats.getSpeed() + value);
                    break;
                }
                case MAX_HEALTH:{
                    currStats.setMaximumHP(baseStats.getMaximumHP() + (int)value);
                }
            }
        }
        return currStats;
    }

    public HealthBar getHealthBar(){
        return this.healthBar;
    }
    public void setHealthBar(HealthBar bar){
        this.healthBar = bar;
    }

    public GridPoint2 getAbsoluteTickPosition(){
        return absoluteTickPosition;
    }
    public void setAbsoluteTickPosition(GridPoint2 position){
        if(absoluteTickPosition == null)
            absoluteTickPosition = new GridPoint2();
        absoluteTickPosition=position;
    }

    public boolean getFlipX(){
        return flipX;
    }
    public void setFlipX(boolean flipX){
        this.flipX = flipX;
    }

    public CreatureType getCreatureType(){
        return creatureType;
    }
    public void setCreatureType(CreatureType param){
        this.creatureType =  param;
    }

    @Override
    public boolean equals(Object obj) {
        return obj != null
                && obj instanceof ICreature
                && ((ICreature) obj).getID() == this.getID();
    }
}
