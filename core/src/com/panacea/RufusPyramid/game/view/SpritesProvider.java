package com.panacea.RufusPyramid.game.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.panacea.RufusPyramid.common.StaticDataProvider;
import com.panacea.RufusPyramid.game.creatures.ICreature;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * Classe utilizzata per caricare staticamente tutti gli sprites e per richiedere i necessari
 * quando serve, a seconda dell'azione da effettuare.
 *
 * Created by gio on 20/09/15.
 */
public class SpritesProvider {
    /*  TODO: alla chiusura del programma fare il dispose di tutte queste textures
        Oppure per ogni TextureRegion[][] lanciare "region[0][0].getTexture().dispose()" */
    private static List<Texture> allTextures = new LinkedList<Texture>();

    private static final String BASE_PATH = "data/";
    private static TextureRegion[][] hero1 = loadTexture("creatures/hero_spritesheetx32.png", 13, 21);
    private static TextureRegion[][] orc_base = loadTexture(StaticDataProvider.getSpritesheetPath(ICreature.CreatureType.ORC), 13, 21);
    private static TextureRegion[][] skeleton_base = loadTexture(StaticDataProvider.getSpritesheetPath(ICreature.CreatureType.SKELETON), 13, 21);
    private static TextureRegion[][] wtfcreature_base = loadTexture(StaticDataProvider.getSpritesheetPath(ICreature.CreatureType.UGLYYETI), 7, 5);
    private static TextureRegion[][] wraith_base = loadTexture(StaticDataProvider.getSpritesheetPath(ICreature.CreatureType.WRAITH), 8, 6);
    private static TextureRegion[] staticFire = loadTexture("animations/fireloop.png", 50, 1)[0];
    private static TextureRegion[] speakers = loadTexture("ui/speakers.png", 2, 1)[0];

    /**
     *  Ritorna la corretta TextureRegion dato un oggetto da animare e l'azione da effettuare.
     *
     * @param oggettoDaAnimare
     * @param azione
     * @return la texture region dell'oggetto necessaria ad effettuare l'animazione dell'azione richiesta.
     */
    public static TextureRegion[] getSprites(ICreature.CreatureType oggettoDaAnimare, Azione azione) {
        TextureRegion[] animationFrames = null;
        TextureRegion[][] allFrames = null;

        switch(oggettoDaAnimare) {
            case HERO:
                allFrames = hero1;
                break;
            case ORC:
                allFrames = orc_base;
                break;
            case SKELETON:
                allFrames = skeleton_base;
                break;
            case UGLYYETI:
                allFrames = wtfcreature_base;
                break;
            case WRAITH:
                allFrames = wraith_base;
                break;
            default:
                throw new IllegalArgumentException("Nessuno sprite disponibile per l'oggetto " + oggettoDaAnimare);
        }


//        if (oggettoDaAnimare.equals(Oggetto.HERO1) || oggettoDaAnimare.equals(Oggetto.ORC_BASE)) {
//            int frameCols = 13;
//            int frameRows = 21;
            int animationCols = 1, animationRow = 1;

            switch (azione) {
                case STAND:
                        animationCols = 1;                    // Colonne 1
                        animationRow = 12;                    // Righe 9-12
                    if(oggettoDaAnimare == ICreature.CreatureType.UGLYYETI){
                        animationCols = 1;
                        animationRow = 1;
                    }
                    if(oggettoDaAnimare == ICreature.CreatureType.WRAITH){
                        animationCols = 8;
                        animationRow = 5;
                    }
                    break;
                case WALK:
                        animationCols = 9;                    // Colonne 9
                        animationRow = 12;                    // Righe 9-12
                    if(oggettoDaAnimare == ICreature.CreatureType.UGLYYETI){
                        animationCols = 4;
                        animationRow = 1;
                    }
                    if(oggettoDaAnimare == ICreature.CreatureType.WRAITH){
                        animationCols = 8;
                        animationRow = 5;
                    }
                    break;
                case STRIKE:
                        animationCols = 6;                    // Colonne 6
                        animationRow = 16;                    // Righe 13-16
                    if(oggettoDaAnimare == ICreature.CreatureType.UGLYYETI){
                        animationCols = 7;
                        animationRow = 5;
                    }
                    if(oggettoDaAnimare == ICreature.CreatureType.WRAITH){
                        animationCols = 8;
                        animationRow = 6;
                    }
                    break;
                case CAST:
                        animationCols = 4;                    // Colonne 7
                        animationRow = 7;                     // Righe 1-4
                    break;
                case DEATH:
                        animationCols = 6;                    // Colonne 6
                        animationRow = 1;                    // Righe 21
                    break;
                default:
                    throw new NullPointerException("Impossibile trovare lo sprite corretto per l'animazione " + azione + " dell'oggetto " + oggettoDaAnimare + ".");
            }
            animationFrames = Arrays.copyOf(allFrames[animationRow - 1], animationCols);
//        }

        if (animationFrames == null)
            throw new NullPointerException("Impossibile trovare lo sprite corretto per l'animazione " + azione + " dell'oggetto " + oggettoDaAnimare + ".");
        return animationFrames;
    }

    public static TextureRegion[] getStaticSprites(OggettoStatico obj) {
        TextureRegion[] animationFrames = null;

        switch(obj) {
            case FIRE:
                animationFrames = staticFire;
                break;
            case SPEAKERS:
                animationFrames = speakers;
                break;
            default:
                throw new IllegalArgumentException("Nessuno sprite disponibile per l'oggetto " + obj);
        }

        return animationFrames;
    }

    public enum OggettoStatico {
        FIRE, SPEAKERS
    }

    public enum Azione {
        STAND, WALK, STRIKE, CAST, DEATH
    }

    private static TextureRegion[][] loadTexture(String path, int cols, int rows) {
        Texture animationTexture = new Texture(Gdx.files.internal(BASE_PATH + path));
        allTextures.add(animationTexture);
        return TextureRegion.split(animationTexture, animationTexture.getWidth()/cols, animationTexture.getHeight()/rows);
    }
}
