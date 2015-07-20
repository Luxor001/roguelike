package com.panacea.RufusPyramid.game.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteCache;
import com.badlogic.gdx.math.GridPoint2;
import com.panacea.RufusPyramid.map.MapContainer;
import com.panacea.RufusPyramid.map.Tile;

/**
 * Created by gio on 11/07/15.
 */
public class MapView extends ViewObject {

    private static final int TILE_WIDTH = 32;
    private static final int TILE_HEIGHT = 32;


    private MapContainer map;
    private OrthographicCamera camera;

    private Sprite[][] spriteMap;
    private SpriteCache spriteCache;
    private int spriteCacheIndex;

    public MapView(MapContainer map) {
        this.map = map;
    }

    @Override
    public void create() {
        super.create();

        camera = GameCamera.get();

        int numRows = 30;       //TODO map.rLenght()
        int numColumns = 30;    //TODO map.cLenght()

        //Imposto le immagini e i parametri per la visualizzazione
        spriteMap = new Sprite[numRows][numColumns];
        spriteCache = new SpriteCache();
        spriteCache.beginCache();
        for (int row = 0; row < numRows; row++) {
            for (int col = 0; col < numColumns; col++) {

                //temporary full wall map
                    Tile tileToDraw = new Tile(new GridPoint2(row, col), Tile.TileType.Solid); //map.getTile(row, col);
                    Texture tileTexture = MapView.getTexture(tileToDraw);

                    //TODO set texture in a map
                    Sprite tileSprite = new Sprite(tileTexture);
                    tileSprite.setPosition(row * TILE_WIDTH, col * TILE_HEIGHT);
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

    private static Texture getTexture(Tile tile) {
        //TODO return wall texture and then return proper texture using tile values
        return new Texture(Gdx.files.internal("data/wall23.gif"));
    }
}
