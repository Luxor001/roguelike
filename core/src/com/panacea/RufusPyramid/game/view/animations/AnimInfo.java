package com.panacea.RufusPyramid.game.view.animations;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Vector2;
import com.panacea.RufusPyramid.common.Utilities;
import com.panacea.RufusPyramid.game.view.GameBatch;

/**
 * Mostra un numero rappresentante un info generica
 * Created by gio on 25/07/15.
 */
public class AnimInfo extends AbstractAnimation {
    private final static float DURATION = 1.0f; // in secondi;
    private float elapsedTime;

    String text;
    Vector2 creaturePosition;
    BitmapFont font;
    Color fontColor;


    public AnimInfo(GridPoint2 creaturePosition, String info, Color fontColor) {
        this.text = info;
        GridPoint2 absolutePosition = Utilities.convertToAbsolutePos(creaturePosition);
        this.creaturePosition = new Vector2(
                absolutePosition.x + (Utilities.DEFAULT_BLOCK_WIDTH/2)/2, //FIXME:valori completamente a caso pur di aggiustare..
                absolutePosition.y + Utilities.DEFAULT_BLOCK_HEIGHT+5);

        this.font = new BitmapFont();
        this.fontColor = fontColor;
        this.font.setColor(this.fontColor);
        this.font.getData().setScale(0.7f);
        this.fontColor.set(fontColor.r,fontColor.g,fontColor.b, 0.7f);   //l'ultimo parametro è la trasparenza, ma fa "trasparire" tutti gli sprites! xD
        this.elapsedTime = 0;
    }

    @Override
    public void render(float delta) {
        super.render(delta);

        if (this.elapsedTime >= DURATION) {
            return;
        }

        GameBatch.get().begin();
        font.draw(GameBatch.get(), text, creaturePosition.x, creaturePosition.y);
        GameBatch.get().end();

        Vector2 velocity = new Vector2(0,10); // Get the font velocity
        creaturePosition.x += velocity.x*delta; // Delta from the render loop
        creaturePosition.y += velocity.y*delta;
        this.elapsedTime += delta;
        if (this.elapsedTime >= DURATION) {
            this.dispose();
        }
    }

    @Override
    public void dispose() {
        super.dispose();
        //TODO libera tutte le risorse
        this.font.dispose();
        this.fireAnimationEndedEvent();
    }
}
