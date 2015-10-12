package com.panacea.RufusPyramid.game.view;

import com.badlogic.gdx.audio.Sound;
import com.panacea.RufusPyramid.common.AssetsProvider;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Lux on 25/09/2015.
 */
public class SoundsProvider {

    private static SoundsProvider SINGLETON;
    private final AssetsProvider as;
    private HashMap<Sounds, Sound[]> soundsMap;


    public enum Sounds {
        COMBAT_SLICE(3), //the number into the parenthesis indicates the size of the array returned
        COMBAT_WRATH(3),
        COMBAT_MNSTR(2),
        GOLD_PICKUP(2),
        FOOTSTEPS_INTERNAL(2),
        INVENTORY(2),
        CLICK(1);

        private final int id;
        Sounds(int id) { this.id = id; }
        public int getValue() { return id; }
    }
    private SoundsProvider(){
        
        soundsMap = new HashMap<Sounds, Sound[]>();
        this.as = AssetsProvider.get();
    }

    public static SoundsProvider get(){
        if(SINGLETON == null)
            SINGLETON = new SoundsProvider();
        return SINGLETON;
    }

    public Sound[] getSound(Sounds reqSound){
        if(!soundsMap.containsKey(reqSound))
            loadSound(reqSound);

        return soundsMap.get(reqSound);
    }

    public void loadSound(Sounds reqSound){
        Sound[] sounds = getAudioFiles(reqSound);
        soundsMap.put(reqSound, sounds);
    }

    public void loadAllSounds() {
        for(Sounds type : Sounds.values()) {
            loadSound(type);
        }
    }

    public boolean isLoaded(Sounds reqSound){
        return soundsMap.containsKey(reqSound);
    }

    private Sound[] getAudioFiles(Sounds reqSound){
        ArrayList<Sound> files = new ArrayList<Sound>();

        switch(reqSound){
            case COMBAT_SLICE:{
                files.add(this.as.get("data/sfx/combat/swing1.ogg", Sound.class));
                files.add(this.as.get("data/sfx/combat/swing2.ogg", Sound.class));
                files.add(this.as.get("data/sfx/combat/swing3.ogg", Sound.class));
                break;
            }
            case COMBAT_MNSTR:{
                files.add(this.as.get("data/sfx/combat/mnstr1.ogg", Sound.class));
                files.add(this.as.get("data/sfx/combat/mnstr2.ogg", Sound.class));
                break;
            }
            case COMBAT_WRATH:{
                files.add(this.as.get("data/sfx/combat/shade1.ogg", Sound.class));
                files.add(this.as.get("data/sfx/combat/shade2.ogg", Sound.class));
                files.add(this.as.get("data/sfx/combat/shade3.ogg", Sound.class));
                break;
            }
            case GOLD_PICKUP:{
                files.add(this.as.get("data/sfx/misc/gold_pickup1.ogg", Sound.class));
                files.add(this.as.get("data/sfx/misc/gold_pickup2.ogg", Sound.class));
                break;
            }
            case FOOTSTEPS_INTERNAL:{
                files.add(this.as.get("data/sfx/misc/footsteps1.ogg", Sound.class));
                files.add(this.as.get("data/sfx/misc/footsteps2.ogg", Sound.class));
                break;
            }
            case INVENTORY:{
                files.add(this.as.get("data/sfx/misc/inventory.ogg", Sound.class));
                files.add(this.as.get("data/sfx/misc/inventory_close.ogg", Sound.class));
                break;
            }
            case CLICK:{
                files.add(this.as.get("data/sfx/misc/pick.ogg", Sound.class));
                break;
            }
        }
        Sound [] dummyArray = new Sound[files.size()];
        return files.toArray(dummyArray);
    }

    public static void requestAssets() {
        AssetsProvider as = AssetsProvider.get();
        as.load("data/sfx/misc/inventory.ogg", Sound.class);
        as.load("data/sfx/misc/inventory_close.ogg", Sound.class);
        as.load("data/sfx/combat/swing1.ogg", Sound.class);
        as.load("data/sfx/combat/swing2.ogg", Sound.class);
        as.load("data/sfx/combat/swing3.ogg", Sound.class);
        as.load("data/sfx/combat/mnstr1.ogg", Sound.class);
        as.load("data/sfx/combat/mnstr2.ogg", Sound.class);
        as.load("data/sfx/combat/shade1.ogg", Sound.class);
        as.load("data/sfx/combat/shade2.ogg", Sound.class);
        as.load("data/sfx/combat/shade3.ogg", Sound.class);
        as.load("data/sfx/misc/gold_pickup1.ogg", Sound.class);
        as.load("data/sfx/misc/gold_pickup2.ogg", Sound.class);
        as.load("data/sfx/misc/footsteps1.ogg", Sound.class);
        as.load("data/sfx/misc/footsteps2.ogg", Sound.class);
        as.load("data/sfx/misc/inventory.ogg", Sound.class);
        as.load("data/sfx/misc/inventory_close.ogg", Sound.class);
        as.load("data/sfx/misc/pick.ogg", Sound.class);
    }
}
