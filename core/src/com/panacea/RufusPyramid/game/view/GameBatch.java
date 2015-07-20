package com.panacea.RufusPyramid.game.view;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 * Created by gioele.masini on 19/07/2015.
 */
public class GameBatch {
    private static SpriteBatch batch = null;

    private GameBatch() {};

    public static SpriteBatch get() {
        //TODO istanziare di default all'avvio del gioco
        if (batch == null) {
            batch = createBatch();
        }
        return batch;
    }

    public static SpriteBatch createBatch() {
        batch = new SpriteBatch();                // #12
        batch.setProjectionMatrix(GameCamera.get().combined);
        return batch;
    }
}
