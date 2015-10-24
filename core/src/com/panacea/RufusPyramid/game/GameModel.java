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

    /**
     * Ritorna l'unica istanza (singleton) di GameModel.
     * @return l'unica istanza di GameModel.
     */
    public static GameModel get() {
        return SINGLETON;
    }

    public static void set(GameModel instance) {
        SINGLETON = instance;
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


        Map newMap = new MapFactory().generateMap(new Random(System.nanoTime()).nextInt());
        this.maps.add(newMap);
        this.hero = new DefaultHero("Rufus", 50);
        GridPoint2 spawnpoint=newMap.getSpawnPoint().getPosition();
        this.hero.setPosition(new Tile(new GridPoint2(spawnpoint.x, spawnpoint.y), Tile.TileType.Solid));
        GridPoint2 absolute = Utilities.convertToAbsolutePos(spawnpoint);
        this.hero.setAbsoluteTickPosition(new GridPoint2(absolute));
        this.addCreature(this.hero);


       Enemy newEnemy = null;
        for(int i=0; i < Utilities.randInt(7,13); i++) { //numero casuale di nemici da 7 a 13..
            int randType = Utilities.randInt(0,30);
//            Stats randStat;
//            float minSpeed, maxSpeed, minHp, maxHp, minAttack, maxAttack, minDefence, maxDefence, minSight, maxSight;
            if(randType <= 3){
                newEnemy = StaticDataProvider.getEnemy(ICreature.CreatureType.SKELETON);
//                randStat = Stats.generateRandom(6, 10, 3, 5, 5, 7, 5, 7);
//                newEnemy = new Enemy("Crypt Skeleton", "A pile of bones.", randStat, ICreature.CreatureType.SKELETON);
//                newEnemy.sigthLength = Utilities.randInt(4,7);
            }
            if (randType > 3 && randType <= 7) {
                newEnemy = StaticDataProvider.getEnemy(ICreature.CreatureType.ORC);
//                    randStat = new Stats(Utilities.randInt(8, 12), Utilities.randInt(4, 6), Utilities.randInt(4, 6), Utilities.randInt(3, 5));
//                    newEnemy = new Enemy("Cave Orc", "It's ugly as hell!!", randStat, ICreature.CreatureType.ORC);
//                    newEnemy.sigthLength = Utilities.randInt(4,7);
            }
            if (randType > 7 && randType <= 15) {
                newEnemy = StaticDataProvider.getEnemy(ICreature.CreatureType.UGLYYETI);
//                randStat = new Stats(Utilities.randInt(8, 12), Utilities.randInt(4, 6), Utilities.randInt(4, 6), Utilities.randInt(3, 5));
//                newEnemy = new Enemy("Ugly Beast", "His parents aren't proud", randStat, ICreature.CreatureType.UGLYYETI);
//                newEnemy.sigthLength = Utilities.randInt(4,7);
            }
            if (randType > 15 && randType <= 30) {
                newEnemy = StaticDataProvider.getEnemy(ICreature.CreatureType.WRAITH);
//                randStat = new Stats(Utilities.randInt(8, 12), Utilities.randInt(4, 6), Utilities.randInt(4, 6), Utilities.randInt(3, 5));
//                newEnemy = new Enemy("Dark Shade", "Dark as barman.", randStat, ICreature.CreatureType.WRAITH);
//                newEnemy.sigthLength = Utilities.randInt(4,7);
            }
            if (newEnemy != null) {
                newEnemy.setPosition(newMap.getRandomEnemyPosition());
                this.addCreature(newEnemy);
            } else {
                Gdx.app.error(GameModel.class.toString(), "Variabile newEnemy is null, why?! randType is " + randType);
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
            if (randomPos != null && randomPos.getPosition() != spawnpoint) {
                newItem.setPosition(randomPos.getPosition());
                this.addItem(newItem);
            }
        }
        this.diary = new Diary();
      }

    public ArrayList<Map> getMaps() {
        return this.maps;
    }

    public Map getCurrentMap() {
        return this.maps.get(this.currentMapIndex);
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
