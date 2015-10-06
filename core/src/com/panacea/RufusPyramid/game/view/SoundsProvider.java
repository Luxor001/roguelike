package com.panacea.RufusPyramid.game.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.files.FileHandle;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Lux on 25/09/2015.
 */
public class SoundsProvider {

    private static SoundsProvider SINGLETON;
    private HashMap<Sounds, Sound[]> soundsMap;


    public enum Sounds {
        COMBAT_SLICE(3), //the number into the parenthesis indicates the size of the array returned
        COIN(1),
        FOOTSTEPS_INTERNAL(2),
        INVENTORY_OPEN(1),
        CLICK(1);

        private final int id;
        Sounds(int id) { this.id = id; }
        public int getValue() { return id; }
    }
    private SoundsProvider(){
        soundsMap = new HashMap<Sounds, Sound[]>();
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

    public boolean isLoaded(Sounds reqSound){
        return soundsMap.containsKey(reqSound);
    }

    private Sound[] getAudioFiles(Sounds reqSound){
        ArrayList<Sound> files = new ArrayList<Sound>();

        switch(reqSound){
            case COMBAT_SLICE:{
                files.add(Gdx.audio.newSound(Gdx.files.internal("data/sfx/combat/swing1.ogg")));
                files.add(Gdx.audio.newSound(Gdx.files.internal("data/sfx/combat/swing2.ogg")));
                files.add(Gdx.audio.newSound(Gdx.files.internal("data/sfx/combat/swing3.ogg")));
                break;
            }
            case COIN:{
                files.add(Gdx.audio.newSound((Gdx.files.internal("data/sfx/misc/coins.ogg"))));
                break;
            }
            case FOOTSTEPS_INTERNAL:{
                files.add(Gdx.audio.newSound(Gdx.files.internal("data/sfx/misc/footsteps1.ogg")));
                files.add(Gdx.audio.newSound(Gdx.files.internal("data/sfx/misc/footsteps2.ogg")));
                break;
            }
            case INVENTORY_OPEN:{
                files.add(Gdx.audio.newSound(Gdx.files.internal("data/sfx/misc/inventory.ogg")));
                break;
            }
            case CLICK:{
                files.add(Gdx.audio.newSound(Gdx.files.internal("data/sfx/misc/pick.ogg")));
                break;
            }
        }
        Sound [] dummyArray = new Sound[files.size()];
        return files.toArray(dummyArray);
    }
}
