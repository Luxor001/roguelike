package com.panacea.RufusPyramid.game.view.animations;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Vector2;
import com.panacea.RufusPyramid.common.Utilities;
import com.panacea.RufusPyramid.game.view.GameBatch;

/**
 * Mostra un numero rappresentante un danno,
 * che sale lentamente e scompare.
 * Created by gio on 25/07/15.
 */
public class AnimDamage extends AbstractAnimation {
    private final static float DURATION = 1.0f; // in secondi;
    private float elapsedTime;

    String text;
    Vector2 creaturePosition;
    BitmapFont font;
    Color fontColor;


    public AnimDamage(GridPoint2 creaturePosition, int damage) {
        this.text = Integer.toString(damage);
        GridPoint2 absolutePosition = Utilities.convertToAbsolutePos(creaturePosition);
        this.creaturePosition = new Vector2(
                absolutePosition.x + Utilities.DEFAULT_BLOCK_WIDTH/2,
                absolutePosition.y + Utilities.DEFAULT_BLOCK_HEIGHT+2);

        this.font = new BitmapFont();
        this.fontColor = Color.RED;
        this.font.setColor(this.fontColor);
        this.font.getData().setScale(1.2f);
        this.fontColor.set(1f, 0, 0, 0.7f);   //l'ultimo parametro è la trasparenza, ma fa "trasparire" tutti gli sprites! xD
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

        Vector2 velocity = new Vector2(Utilities.randInt(0,5),Utilities.randInt(8,10)); // Get the font velocity
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
