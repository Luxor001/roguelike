package com.panacea.RufusPyramid.creatures;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.GridPoint2;
import com.panacea.RufusPyramid.common.InputManager;
import com.panacea.RufusPyramid.common.ViewObject;

/**
 * Created by gioele.masini on 06/07/2015.
 */
public class TestAnimation extends ViewObject {

    com.panacea.RufusPyramid.creatures.Animator animator;
    GridPoint2 screenLimits;

    int counter=0;
    public static int x,y;

    public void create () {
        TextureRegion region = new TextureRegion();
        animator = new com.panacea.RufusPyramid.creatures.Animator(0.33f, region);
        animator.create();

        screenLimits = new GridPoint2(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        InputManager.getInstance().addProcessor(new TestInput());
    }

    @Override
    public void render () {
        super.render();

        x++;//movimento di prova..
        animator.updateLocation(new GridPoint2(x,y)); //aggiorno la posizione da disegnare della "tile"
        animator.render();

        if(x >= screenLimits.x) //se esci fuori dallo schermo, resetta a 0
            x=0;

    }

    /*Intercettazione degli input dell'utente*/
    private class TestInput extends InputAdapter {
        @Override
        public boolean touchDown(int x, int y, int pointer, int button) {
            // your touch down code here
            TestAnimation.x = x;
            TestAnimation.y = Math.abs(y - screenLimits.y); /*l'origine, secondo lo spritebatch, è in basso a sinistra, pertanto bisogna "aggiustare" come viene interpretato l'input da android*/
            return true;
        }

        @Override
        public boolean touchUp(int x, int y, int pointer, int button) {
            return true;
        }
    }
}
