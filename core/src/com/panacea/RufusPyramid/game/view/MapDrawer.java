package com.panacea.RufusPyramid.game.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteCache;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.GridPoint2;
import com.panacea.RufusPyramid.common.Utilities;
import com.panacea.RufusPyramid.game.GameModel;
import com.panacea.RufusPyramid.map.Map;
import com.panacea.RufusPyramid.map.MapContainer;
import com.panacea.RufusPyramid.map.Tile;

import java.util.Random;

import javax.xml.soap.Text;

/**
 * Created by gio on 11/07/15.
 */
public class MapDrawer extends ViewObject {

    private static final int TILE_WIDTH = 32;
    private static final int TILE_HEIGHT = 32;


    private Map map;
    private OrthographicCamera camera;

    private Sprite[][] spriteMap;
    private SpriteCache spriteCache;
    private int spriteCacheIndex;

    private static TextureRegion[] walls;
    private static TextureRegion[] grounds;

    public MapDrawer(Map map) {
        this.map = map;
    }

    @Override
    public void create() {
        super.create();
        MapContainer mapCont= map.getMapContainer();
        camera = GameCamera.get();

        int numRows = mapCont.rLenght();
        int numColumns = mapCont.cLenght();

        //initializeTextures(map.getType()); //FIXME: da fixare
        initializeTextures(Map.MapType.DUNGEON_SAND);
        //Imposto le immagini e i parametri per la visualizzazione
        spriteMap = new Sprite[numRows][numColumns];
        spriteCache = new SpriteCache(numRows*numColumns,true);
        spriteCache.beginCache();
        for (int row = 0; row < numRows; row++) {
            for (int col = 0; col < numColumns; col++) {

                    Tile tileToDraw = mapCont.getTile(row, col);
                    TextureRegion tileTexture = MapDrawer.getTexture(map.getType(), tileToDraw);

                    //TODO set texture in a map
                    Sprite tileSprite = new Sprite(tileTexture);
                    GridPoint2 absolutePos=Utilities.convertToAbsolutePos(new GridPoint2(col,row));
                    tileSprite.setPosition(absolutePos.x,absolutePos.y);
                    spriteMap[row][col] = tileSprite;
                    spriteCache.add(tileSprite, tileSprite.getX(), tileSprite.getY());
            }
        }
        spriteCacheIndex = spriteCache.endCache();
    }

    @Override
    public void render(float delta) {
        super.render(delta);


        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

        spriteCache.setProjectionMatrix(camera.combined);
        spriteCache.begin();
        spriteCache.draw(spriteCacheIndex);
        spriteCache.end();
//        for (Sprite[] row : spriteMap) {
//            for (Sprite tile : row) {
//                tile.draw(spriteCache);
//            }
//        }

    }

    private void initializeTextures(Map.MapType mapType){ //load the texture regions based on current map type
        String path="";
        int wallsFrameCols = 0;
        int groundsFrameCols = 0;

        switch (mapType){
            case DUNGEON_COBBLE:
                path = "dungeon1.png";
                wallsFrameCols=7;
                break;
            case DUNGEON_SAND:
                path = "dungeon2.png";
                groundsFrameCols=4;
                wallsFrameCols=10;
                break;
            case DUNGEON_SEWERS:
                path = "dungeon3.png";
                wallsFrameCols=7;
                break;
            default:
                path = "dungeon1.png";
                break;
        }

        walls = loadTextureRegion("data/walls/"+path,wallsFrameCols,1);
        grounds = loadTextureRegion("data/grounds/"+path,groundsFrameCols,1);

    }
    private TextureRegion[] loadTextureRegion(String path, int frameCols, int frameRows){ //questo metodo esegue lo splitting di una texture e restituisce un array di textureRegion monodimensionale (ricorda: le texture possono essere un INSIEME di immagini!, le textureRegion ne rappresentano solo una!)
        Texture base = new Texture(Gdx.files.internal(path));
        TextureRegion[][] tmp = TextureRegion.split(base, Utilities.DEFAULT_BLOCK_WIDTH, Utilities.DEFAULT_BLOCK_HEIGHT);
        TextureRegion[] newTextRegion = new TextureRegion[frameCols * frameRows];
        int index = 0;
        for (int i = 0; i < frameRows; i++) {
            for (int j = 0; j < frameCols; j++) {
                TextureRegion tr = tmp[i][j];
                newTextRegion[index++] = tr;
            }
        }
        return newTextRegion;
    }
    private static TextureRegion[] loadTextureRegion(Texture text){
        int frameRows = text.getHeight() / Utilities.DEFAULT_BLOCK_HEIGHT;
        int frameCols = text.getWidth() / Utilities.DEFAULT_BLOCK_WIDTH;
        TextureRegion[][] tmp = TextureRegion.split(text, Utilities.DEFAULT_BLOCK_WIDTH, Utilities.DEFAULT_BLOCK_HEIGHT);
        TextureRegion[] newTextRegion = new TextureRegion[frameCols * frameRows];
        int index = 0;
        for (int i = 0; i < frameRows; i++) {
            for (int j = 0; j < frameCols; j++) {
                TextureRegion tr = tmp[i][j];
                newTextRegion[index++] = tr;
            }
        }
        return newTextRegion;
    }

    private static TextureRegion getTexture(Map.MapType mapType, Tile tile) {
        TextureRegion textRegion = null;
        Texture text=null;
        switch(tile.getType()){
            case Solid:
                textRegion = walls[Utilities.randInt(0,walls.length-1,(int) System.nanoTime())];
                break;
            case Walkable:
                textRegion = grounds[Utilities.randInt(0,grounds.length-1,(int) System.nanoTime())];
                break;
            case Door:{
                text = new Texture(Gdx.files.internal("data/door1_close.png"));
                textRegion = loadTextureRegion(text)[0];
            }
                break;
        }
        //TODO return wall texture and then return proper texture using tile values
        return textRegion;
    }
}
