package com.panacea.RufusPyramid.game.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.panacea.RufusPyramid.common.Utilities;
import com.panacea.RufusPyramid.game.creatures.ICreature;

import java.util.HashMap;

/**
 * Created by gioele.masini on 13/10/2015.
 */
public class MusicPlayer {

    private static Music activeAmbient = null;
    private static HashMap<Integer, CreatureProfile> creaturesProfiles = new HashMap<Integer, CreatureProfile>();
    private static boolean mute = false;
    private static float volumeLevel = 1f;

    private MusicPlayer(){}

    public static void setAmbient(AmbientType ambientType) {
        if (MusicPlayer.activeAmbient != null) {
            MusicPlayer.activeAmbient.stop();
        }

        SoundsProvider.Musics type = null;
        switch (ambientType) {
            case MENU:
                type = SoundsProvider.Musics.MENU;
                break;
            case GAME:
                type = SoundsProvider.Musics.GAME;
                break;
            case GAMEOVER:
                type = SoundsProvider.Musics.GAMEOVER;
                break;
            case NONE:
                break;
        }
        MusicPlayer.activeAmbient = SoundsProvider.get().getMusic(type);
        MusicPlayer.play(MusicPlayer.activeAmbient);
    }

    public static void playSound(SoundType soundType) {
        MusicPlayer.playSound(soundType, null, null);
    }

    public static void playSound(SoundType soundType, ICreature.CreatureType creatureType, Integer creatureId) {
        SoundsProvider.Sounds type = null;
        switch(soundType) {
            case STRIKE:
                switch(creatureType) {
                    case HERO:
                        type = SoundsProvider.Sounds.COMBAT_SLICE;
                        break;
                    case ORC:
                        type = SoundsProvider.Sounds.COMBAT_MNSTR;
                        break;
                    case SKELETON:
                        type = SoundsProvider.Sounds.COMBAT_MNSTR;
                        break;
                    case UGLYYETI:
                        type = SoundsProvider.Sounds.COMBAT_MNSTR;
                        break;
                    case WRAITH:
                        type = SoundsProvider.Sounds.COMBAT_WRATH;
                        break;
                }
                break;
            case WALK:  //0.5
                type = SoundsProvider.Sounds.FOOTSTEPS_INTERNAL;
                break;
            case CAST:
                Gdx.app.log(MusicPlayer.class.toString(), "Nessun suono disponibile per il cast.");
//                type = SoundsProvider.Sounds.;
                break;
            case DEATH:
                Gdx.app.log(MusicPlayer.class.toString(), "Nessun suono disponibile per la morte.");
//                type = SoundsProvider.Sounds.;
                break;
            case GOLD_PICK:
                type = SoundsProvider.Sounds.GOLD_PICKUP;
                break;
            case ITEM_PICK: //0.6
                type = SoundsProvider.Sounds.CLICK;
                break;
            case INVENTORY_OPEN:
                type = SoundsProvider.Sounds.INVENTORY_OPEN;
                break;
            case INVENTORY_CLOSE:
                type = SoundsProvider.Sounds.INVENTORY_CLOSE;
                break;
        }
        if (type != null) {
            Sound[] sound = SoundsProvider.get().getSound(type);
            MusicPlayer.play(sound, soundType, creatureId);
        }
    }

    private static void play(Sound[] sounds, SoundType soundType, Integer creatureId) {
        Sound soundToPlay = null;
        CreatureProfile profile = creaturesProfiles.get(creatureId);
        if (profile == null) {
            profile = new CreatureProfile();
            MusicPlayer.creaturesProfiles.put(creatureId, profile);
        }

        Integer soundIndex = profile.getBind(soundType);
        if (soundIndex == null) {
            soundIndex = Utilities.randInt(0, sounds.length - 1);
            profile.setBind(soundType, soundIndex);
        }
        soundToPlay = sounds[soundIndex];

        MusicPlayer.play(soundToPlay, false);
    }

    private static void play(Sound sound, boolean looping) {
        if (sound != null) {
            long id = sound.play();
            sound.setVolume(id, MusicPlayer.getVolume());
            sound.setLooping(id, looping);
        }
    }

    private static void play(Music sound) {
        if (sound != null) {
            sound.setVolume(MusicPlayer.getVolume());
            sound.play();
            sound.setLooping(true);
        }
    }
    
    public static void setMute(boolean mute) {
        MusicPlayer.mute = mute;

        if (MusicPlayer.activeAmbient != null) {
            activeAmbient.setVolume(MusicPlayer.getVolume());
        }
        //TODO azzerare o ripristinare il volume di tutti i suoni in corso
    }

    public static float getVolume() {
        if (MusicPlayer.isMute()) {
            return 0f;
        } else {
            return MusicPlayer.volumeLevel;
        }
    }

    public static boolean isMute() {
        return MusicPlayer.mute;
    }

    public enum SoundType {
        STRIKE,
        WALK,
        CAST,
        DEATH,
        GOLD_PICK,
        ITEM_PICK,
        INVENTORY_OPEN,
        INVENTORY_CLOSE
    }

    public enum AmbientType {
        MENU,
        GAME,
        GAMEOVER,
        NONE
    }

    protected static class CreatureProfile {
        private HashMap<SoundType, Integer> bindings = new HashMap<SoundType,Integer>();

        public void setBind(SoundType soundType, Integer index) {
            bindings.put(soundType, index);
        }

        public Integer getBind(SoundType soundType) {
            return bindings.get(soundType);
        }
    }
}
