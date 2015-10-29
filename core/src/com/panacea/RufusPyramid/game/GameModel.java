package com.panacea.RufusPyramid.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Vector2;
import com.panacea.RufusPyramid.common.StaticDataProvider;
import com.panacea.RufusPyramid.common.Utilities;
import com.panacea.RufusPyramid.game.Effect.Effect;
import com.panacea.RufusPyramid.game.creatures.CreatureDeadEvent;
import com.panacea.RufusPyramid.game.creatures.CreatureDeadListener;
import com.panacea.RufusPyramid.game.creatures.DefaultHero;
import com.panacea.RufusPyramid.game.creatures.Enemy;
import com.panacea.RufusPyramid.game.creatures.ICreature;
import com.panacea.RufusPyramid.game.creatures.Stats;
import com.panacea.RufusPyramid.game.items.ChestItem;
import com.panacea.RufusPyramid.game.items.GoldItem;
import com.panacea.RufusPyramid.game.items.Item;
import com.panacea.RufusPyramid.game.items.usableItems.MiscItem;
import com.panacea.RufusPyramid.game.items.usableItems.Weapon;
import com.panacea.RufusPyramid.game.items.usableItems.Wearable;
import com.panacea.RufusPyramid.map.Map;
import com.panacea.RufusPyramid.map.MapFactory;
import com.panacea.RufusPyramid.map.Tile;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

/**
 * Created by gio on 19/07/15.
 */
public class GameModel {
    /* Static methods */
    
    private static GameModel SINGLETON = null;
    private ArrayList<ICreature> creatures; //FIXMEABSOLUTELY: togliere transient
    private transient Diary diary;
    private transient ArrayList<Item> items; //FIXMEABSOLUTELY: togliere transient

    public static final double[] extractedItem = new double[]      { 00, 01, 02, 03}; //0 = golditem, 1 = miscitem, 2= weapon, 3=wearable //TODO: DA METTERE SU DB!
    public static final double[] itemProb = new double[]          { 0.6, 0.2, 0.1, 0.1}; //probabilità

    public static final double[] extractedEnemy = new double[]      { 00, 01, 02, 03}; //0 = skeleton, 1 = ORC, 2= UGLYYETI, 3=WRAITH //TODO: DA METTERE SU DB!
    public static final double[] enemyProb = new double[]          { 0.25, 0.25, 0.25, 0.25}; //probabilità nemici


    /**
     * Ritorna l'unica istanza (singleton) di GameModel.
     * @return l'unica istanza di GameModel.
     */
    public static GameModel get() {
        return SINGLETON;
    }

    public static void set(GameModel instance) {
        SINGLETON = instance;
        instance.creatureDeadListener.setGameModel(instance);
        for (ICreature creature: instance.creatures) {
            creature.addCreatureDeadListener(instance.creatureDeadListener);
        }
    }
    
    public static void createInstance() {
        SINGLETON = new GameModel();
    }
    

    /* Instance methods */
    private DefaultHero hero;
    private ArrayList<Map> maps;
    private int currentMapIndex;
    
    private RemoveOnDeathListener creatureDeadListener;

    private GameModel() {
        this.creatureDeadListener = new RemoveOnDeathListener();
        this.creatureDeadListener.setGameModel(this);
        this.currentMapIndex = 0;
        this.creatures = new ArrayList<ICreature>();
        this.maps = new ArrayList<Map>();
        this.items = new ArrayList<Item>();

        for(int i=0; i < 3; i++){
            Map newMap = new MapFactory().generateMap(new Random(System.nanoTime()).nextInt(), 1);
            this.maps.add(newMap);
            GridPoint2 spawnpoint=newMap.getSpawnPoint().getPosition();
        }

        this.hero = new DefaultHero("Rufus", 50);
        this.addCreature(this.hero);
        this.diary = new Diary();
        initializeMap();
      }

    public ArrayList<Map> getMaps() {
        return this.maps;
    }

    public Map getCurrentMap() {
        return this.maps.get(this.currentMapIndex);
    }

    private void initializeMap(){
        this.hero.setPosition(new Tile(new GridPoint2(getCurrentMap().getSpawnPoint().getPosition().x, getCurrentMap().getSpawnPoint().getPosition().y), Tile.TileType.Solid));
        GridPoint2 absolute = Utilities.convertToAbsolutePos(getCurrentMap().getSpawnPoint().getPosition());
        this.hero.setAbsoluteTickPosition(new GridPoint2(absolute));


        Enemy newEnemy = null;
        for(int i=0; i < Utilities.randInt(7,13); i++) { //numero casuale di nemici da 7 a 13..
            int index = (int)Utilities.randWithProb(extractedEnemy,enemyProb);//0 = skeleton, 1 = ORC, 2= UGLYYETI, 3=WRAITH
            newEnemy = StaticDataProvider.getEnemy(ICreature.CreatureType.values()[index]);
            if (newEnemy != null) {
                newEnemy.setPosition(getCurrentMap().getRandomEnemyPosition());
                this.addCreature(newEnemy);
            } else {
                Gdx.app.error(GameModel.class.toString(), "Variabile newEnemy is null, why?! randType is " + index);
            }
        }

        for(int i=0; i < 5; i++) {
            Item newItem = null;
            Tile randomPos;
            ArrayList<Effect> itemEffects = new ArrayList<Effect>();
            int index = (int)Utilities.randWithProb(extractedItem,itemProb);//0 = golditem, 1 = miscitem, 2= weapon, 3=wearable
            switch (index){
                case 0:{
                    itemEffects.add(new Effect(Effect.EffectType.ATTACK));
                    int goldAmount = Utilities.randInt(25,100);
                    newItem = new ChestItem(new GoldItem(goldAmount));
                    break;
                }
                case 1:{
                    int goldAmount = Utilities.randInt(25,100);
                    itemEffects.add(new Effect(Effect.EffectType.HEALTH, 0.5f));
                    newItem = new ChestItem(new MiscItem(MiscItem.MiscItemType.HEALTH_POTION,itemEffects,"Health Potion"));
                    break;
                }
                case 2:{
                    itemEffects.add(new Effect(Effect.EffectType.ATTACK,2f));
                    newItem = new ChestItem(new Weapon(Weapon.WeaponType.DAGGER, itemEffects, "Sharp ToothPick Dagger"));
                    break;
                }
                case 3:{
                    itemEffects.add(new Effect(Effect.EffectType.DEFENSE,3f));
                    newItem = new ChestItem(new Wearable(Wearable.WearableType.ARMOR, itemEffects, "Rusty Chainmail"));
                    break;
                }
            }
            randomPos = getCurrentMap().getRandomItemLocation();
            if (randomPos != null && randomPos.getPosition() != getCurrentMap().getSpawnPoint().getPosition()) {
                newItem.setPosition(randomPos.getPosition());
                this.addItem(newItem);
            }
        }
    }
    public void changeMap(){
        if(currentMapIndex+1 < maps.size()){
            currentMapIndex = 1;
            Iterator<ICreature> e = creatures.iterator();
            while (e.hasNext()) {
                if(e.next().getCreatureType() != ICreature.CreatureType.HERO)
                    e.remove();
            }
            items.clear();
            initializeMap();
        }
    }

    public DefaultHero getHero() {
        return this.hero;
    }
    
    /**
     *  Aggiunge una mappa al gioco.
     *
     *  @return L'indice con il quale la mappa è stata aggiunta, quindi il numero del livello corrispondente.
     */
    public int addMap(Map newMap) {
        this.getMaps().add(newMap);
        return this.getMaps().indexOf(newMap);
    }

    public List<ICreature> getCreatures() {
        return this.creatures;
    }

    public ArrayList<Item> getItems(){
        return this.items;
    }

    public void addItem(Item item){
        this.items.add(item);
    }

    public void addCreature(ICreature newCreature) {
//        if (newCreature instanceof DefaultHero) {
//            Gdx.app.error(this.getClass().toString(), "Per aggiungere l'eroe devi usare il metodo addHero()! Eroe NON aggiunto.");
//            return;
//        }
        this.creatures.add(newCreature);
        newCreature.addCreatureDeadListener(this.creatureDeadListener);
    }

    public Diary getDiary() {
        return this.diary;
    }

    public List<Item> generateChests(int quantity){

        for(int i = 0; i < quantity; i++){
            int seed = (int) System.nanoTime();
            Item randomItem = Item.generateRandomItem(seed);
            ChestItem chest = new ChestItem(randomItem);
            items.add(chest);
        }
        return null;
    }

    public void disposeAll() {
        //TODO per ora non fa niente, non credo sia necessario fare qualcosa
        GameModel.SINGLETON = null;
    }

    private static class RemoveOnDeathListener implements CreatureDeadListener {
        private GameModel gModel;

        public RemoveOnDeathListener() {}
        
        public void setGameModel(GameModel gameModel) {
            this.gModel = gameModel;
        }

        @Override
        public void changed(CreatureDeadEvent event, Object source) {
            this.gModel.creatures.remove(source);
            Gdx.app.log(this.getClass().toString(), "Morta la creatura " + ((ICreature) source).getName());
        }
    }
}
