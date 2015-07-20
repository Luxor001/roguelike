package com.panacea.RufusPyramid.game;

import com.panacea.RufusPyramid.game.creatures.DefaultHero;
import com.panacea.RufusPyramid.map.MapContainer;

import java.util.ArrayList;

/**
 * Created by gio on 19/07/15.
 */
public class GameModel {
    /* Static methods */
    
    private static GameModel SINGLETON = null;

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
    
    
    private GameModel() {
        this.currentMapIndex = 0;
        this.maps = new ArrayList<MapContainer>();
        this.maps.add(new MapContainer(40, 40));
        this.hero = new DefaultHero("Rufus");
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
}
