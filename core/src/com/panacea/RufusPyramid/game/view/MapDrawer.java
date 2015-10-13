package com.panacea.RufusPyramid.game.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.SpriteCache;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.GridPoint2;
import com.panacea.RufusPyramid.common.AssetsProvider;
import com.panacea.RufusPyramid.common.Utilities;
import com.panacea.RufusPyramid.map.Map;
import com.panacea.RufusPyramid.map.MapContainer;
import com.panacea.RufusPyramid.map.Tile;

import java.util.ArrayList;

/**
 * Created by gio on 11/07/15.
 */
public class MapDrawer extends ViewObject {

    private static final String decalsPath = "data/deco/map";

    private Map map;
    private OrthographicCamera camera;

    private Sprite[][] spriteMap;
    private SpriteCache spriteCache;
    private int spriteCacheIndex;

    private static TextureRegion[] walls;
    private static TextureRegion[] grounds;
    private static TextureRegion[] grounds_deco;
    private static TextureRegion[] doors; //doors consists in a 64x32 block, "closed" state in the first 32px

    private ArrayList<Tile> dynamicMapTiles=new ArrayList<Tile>();
    private Map.MapType typeOfLoadedAssets = null;

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

        initializeTextures(map.getType());

        //Imposto le immagini e i parametri per la visualizzazione
        spriteMap = new Sprite[numRows][numColumns];
        spriteCache = new SpriteCache((numRows*numColumns)*2,true);
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

                        //se la tile attuale è "camminabile" allora aggiungi un possibile decal di pavimento, se supera la casualità
                        if(tileToDraw.getType() == Tile.TileType.Walkable && Utilities.randInt(0,15) == 15){
                            TextureRegion decalTexture = grounds_deco[Utilities.randInt(0,grounds_deco.length-1)];
                            spriteCache.add(decalTexture, tileSprite.getX(), tileSprite.getY());
                        }
                    }
                else
                    {
                        dynamicMapTiles.add(tileToDraw);
                    }
            }
        }

        spriteCacheIndex = spriteCache.endCache();
    }

    private void initializeTextures(Map.MapType mapType){ //load the texture regions based on current map type
        AssetsProvider ap = AssetsProvider.get();
        String path = MapDrawer.getFileNameFromMapType(mapType);

        this.requestAssetsConditional(mapType);
        walls = loadTextureRegion(ap.get("data/walls/"+path+".png", Texture.class));
        grounds = loadTextureRegion(ap.get("data/grounds/" + path + ".png", Texture.class));
        doors = loadTextureRegion(ap.get("data/mapObjects/doors_" + path + ".png", Texture.class));

/*        if(new File("data/grounds/"+path+"_deco.png").exists())
          grounds_deco = loadTextureRegion(new Texture("data/grounds/"+path+"_deco.png"));*/
        grounds_deco = loadDecals(MapDrawer.decalsPath);
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

        SpriteBatch batch = GameBatch.get();
        batch.begin();
        for (Tile tile:dynamicMapTiles) {
            GridPoint2 absolutePos=Utilities.convertToAbsolutePos(tile.getPosition());
            batch.draw(getTexture(map.getType(),tile),absolutePos.x,absolutePos.y);
        }
        batch.end();

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
                if(tile.getDoorState())
                    textRegion =doors[1];
                else
                    textRegion = doors[0];
            }
                break;
        }
        return textRegion;
    }

    private TextureRegion[] loadDecals(String rootDirectory){
        ArrayList<TextureRegion> allTextures = new ArrayList<TextureRegion>();
        FileHandle files = Gdx.files.internal(rootDirectory);
        Texture texture;
        for (FileHandle file: files.list()) {
            texture = new Texture(file);
            TextureRegion[][] reg =  TextureRegion.split(texture, texture.getWidth(), texture.getHeight());
            allTextures.add(reg[0][0]);
        }
        TextureRegion[] values = new TextureRegion[allTextures.size()];
        return allTextures.toArray(values);
    }

    public static void requestAssets() {
        AssetsProvider ap = AssetsProvider.get();

        //Tiles will be requested later, at game start, when someone will choose the map type

        //Requesting decals
        FileHandle files = Gdx.files.internal(decalsPath);
        for (FileHandle file: files.list()) {
            ap.load(file.path(), Texture.class);
        }
    }

    public void requestAssetsConditional(Map.MapType mapType) {
        if (this.typeOfLoadedAssets != null) {
            if (this.typeOfLoadedAssets.equals(mapType)) {
                return;
            } else {
                this.unloadAssets(this.typeOfLoadedAssets);
            }
        }

        String path = MapDrawer.getFileNameFromMapType(mapType);

        String wallsPath = "data/walls/" + path + ".png";
        String groundsPath = "data/grounds/" + path + ".png";
        String doorsPath = "data/mapObjects/doors_" + path + ".png";

        //Requesting tiles to AssetsProvider
        AssetsProvider ap = AssetsProvider.get();
        ap.load(wallsPath, Texture.class);
        ap.load(groundsPath, Texture.class);
        ap.load(doorsPath, Texture.class);
        ap.finishLoading();

        walls = loadTextureRegion(new Texture(wallsPath));
        grounds = loadTextureRegion(new Texture(groundsPath));

/*        if(new File("data/grounds/"+path+"_deco.png").exists())
          grounds_deco = loadTextureRegion(new Texture("data/grounds/"+path+"_deco.png"));*/
        doors = loadTextureRegion(new Texture(doorsPath));

        this.typeOfLoadedAssets = mapType;
    }

    private void unloadAssets(Map.MapType mapType) {
        String path = MapDrawer.getFileNameFromMapType(mapType);

        String wallsPath = "data/walls/" + path + ".png";
        String groundsPath = "data/grounds/" + path + ".png";
        String doorsPath = "data/mapObjects/doors_" + path + ".png";

        AssetsProvider ap = AssetsProvider.get();
        ap.unload(wallsPath);
        ap.unload(groundsPath);
        ap.unload(doorsPath);
    }

    private static String getFileNameFromMapType(Map.MapType mapType) {
        String path = "";
        switch (mapType){
            case DUNGEON_COBBLE:
                path = "dungeon1";
//                wallsFrameCols=7;
                break;
            case DUNGEON_SAND:
                path = "dungeon2";
//                groundsFrameCols=4;
//                wallsFrameCols=10;
                break;
            case DUNGEON_SEWERS:
                path = "dungeon3";
//                wallsFrameCols=7;
                break;
            case DUNGEON_CAVE:
                path = "dungeon4";
                break;
            default:
                path = "dungeon1";
                break;
        }
        return path;
    }
}
