package com.panacea.RufusPyramid.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.GridPoint2;
import com.panacea.RufusPyramid.game.creatures.CreatureDeadEvent;
import com.panacea.RufusPyramid.game.creatures.CreatureDeadListener;
import com.panacea.RufusPyramid.game.creatures.DefaultHero;
import com.panacea.RufusPyramid.game.creatures.Enemy;
import com.panacea.RufusPyramid.game.creatures.ICreature;
import com.panacea.RufusPyramid.map.Map;
import com.panacea.RufusPyramid.map.MapContainer;
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
    private ArrayList<ICreature> creatures;
    private Diary diary;

    /**
     * Ritorna l'unica istanza (singleton) di GameModel.
     * @return l'unica istanza di GameModel.
     */
    public static GameModel get() {
        return SINGLETON;
    }
    
    public static void createInstance() {
        SINGLETON = new GameModel();
    }
    
    
    /* Instance methods */
    
    private DefaultHero hero;
    private ArrayList<MapContainer> maps;
    private int currentMapIndex;
    
    private CreatureDeadListener creatureDeadListener = new CreatureDeadListener() {
        @Override
        public void changed(CreatureDeadEvent event, Object source) {
            GameModel.this.creatures.remove(source);
            Gdx.app.log(this.getClass().toString(), "Morta la creatura " + ((ICreature)source).getName());
        }
    };


    private GameModel() {
        this.currentMapIndex = 0;
        this.creatures = new ArrayList<ICreature>();
        this.maps = new ArrayList<MapContainer>();


        Map newMap = new MapFactory().generateMap(new Random(System.nanoTime()).nextInt());
        this.maps.add(newMap.getMapContainer());
        this.hero = new DefaultHero("Rufus");
        GridPoint2 spawnpoint=newMap.getSpawnPoint();
        this.hero.setPosition(new Tile(new GridPoint2(spawnpoint.x, spawnpoint.y), Tile.TileType.Solid));
        this.addCreature(this.hero);

        Enemy newEnemy;
        for(int i=0; i < 10; i++) {
            newEnemy = new Enemy("PooPoo the smelly", "it Smells!", 100, 5, 5, 5);
            newEnemy.setPosition(newMap.getRandomEnemyPosition());
            this.addCreature(newEnemy);
        }
        this.diary = new Diary();
    }

    public ArrayList<MapContainer> getMaps() {
        return this.maps;
    }

    public MapContainer getCurrentMap() {
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
    public int addMap(MapContainer newMap) {
        this.getMaps().add(newMap);
        return this.getMaps().indexOf(newMap);
    }

    public List<ICreature> getCreatures() {
        return this.creatures;
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
}
