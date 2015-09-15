package com.panacea.RufusPyramid.game.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.SpriteCache;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.GridPoint2;
import com.panacea.RufusPyramid.common.Utilities;
import com.panacea.RufusPyramid.game.items.Item;
import com.panacea.RufusPyramid.map.Map;
import com.panacea.RufusPyramid.map.MapContainer;
import com.panacea.RufusPyramid.map.Tile;

import java.io.File;
import java.util.List;

/**
 * Created by Lux on 15/09/2015.
 */
public class ItemsDrawer extends ViewObject{

    private List<Item> items;

    public ItemsDrawer(List<Item> items) {
        this.items = items;
    }

    @Override
    public void create() {
        super.create();

        /*
        MapContainer mapCont= map.getMapContainer();
        camera = GameCamera.get();

        int numRows = mapCont.rLenght();
        int numColumns = mapCont.cLenght();

        initializeTextures(map.getType());
        //Imposto le immagini e i parametri per la visualizzazione
        spriteMap = new Sprite[numRows][numColumns];
        spriteCache = new SpriteCache(numRows*numColumns,true);
        spriteCache.beginCache();
        for (int row = 0; row < numRows; row++) {
            for (int col = 0; col < numColumns; col++) {

                Tile tileToDraw = mapCont.getTile(row, col);
                if(tileToDraw.getType() != Tile.TileType.Door) {
                    TextureRegion tileTexture = MapDrawer.getTexture(map.getType(), tileToDraw);

                    //TODO set texture in a map
                    Sprite tileSprite = new Sprite(tileTexture);
                    GridPoint2 absolutePos = Utilities.convertToAbsolutePos(new GridPoint2(col, row));
                    tileSprite.setPosition(absolutePos.x, absolutePos.y);
                    spriteMap[row][col] = tileSprite;
                    spriteCache.add(tileSprite, tileSprite.getX(), tileSprite.getY());
                }
                else
                {
                    dynamicMapTiles.add(tileToDraw);
                }

            }
        }
        spriteCacheIndex = spriteCache.endCache();*/
    }

    @Override
    public void render(float delta) {
        super.render(delta);

        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

        SpriteBatch batch = GameBatch.get();
        batch.begin();
        for (Item item:items) {
            GridPoint2 absolutePos=Utilities.convertToAbsolutePos(item.getPosition());
            batch.draw(getTexture(item.getClass()),absolutePos.x,absolutePos.y);
        }
        batch.end();

    }
    /*

    private void initializeTextures(Map.MapType mapType){ //load the texture regions based on current map type
        String path="";
        int wallsFrameCols = 0;
        int groundsFrameCols = 0;

        switch (mapType){
            case DUNGEON_COBBLE:
                path = "dungeon1";
                wallsFrameCols=7;
                break;
            case DUNGEON_SAND:
                path = "dungeon2";
                groundsFrameCols=4;
                wallsFrameCols=10;
                break;
            case DUNGEON_SEWERS:
                path = "dungeon3";
                wallsFrameCols=7;
                break;
            case DUNGEON_CAVE:
                path = "dungeon4";
                break;
            default:
                path = "dungeon1";
                break;
        }

        walls = loadTextureRegion(new Texture("data/walls/"+path+".png"));
        grounds = loadTextureRegion(new Texture("data/grounds/"+path+".png"));
        if(new File("data/grounds/"+path+"_deco.png").exists())
            grounds_deco = loadTextureRegion(new Texture("data/grounds/"+path+"_deco.png"));
        doors = loadTextureRegion(new Texture("data/mapObjects/doors_"+path+".png"));

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
                if(Utilities.randInt(0,3,(int)System.nanoTime()) == 3 && grounds_deco != null)
                    textRegion = grounds_deco[Utilities.randInt(0,grounds_deco.length-1,(int) System.nanoTime())];
                else
                    textRegion = grounds[Utilities.randInt(0,grounds.length-1,(int) System.nanoTime())];
                break;
            case Door:{
                if(tile.getDoorState())
                    textRegion =doors[1];
                else
                    textRegion = doors[0];
            }
            break;
        }
        return textRegion;
    }*/
}

}
