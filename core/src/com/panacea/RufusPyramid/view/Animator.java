package com.panacea.RufusPyramid.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.GridPoint2;
import com.panacea.RufusPyramid.common.InputManager;
import com.panacea.RufusPyramid.view.animations.AnimWalk;

import java.util.ArrayList;

/*Snippet preso da https://github.com/libgdx/libgdx/wiki/2D-Animation*/
public class Animator extends ViewObject {

    private AnimWalk walkAnim;
    public GridPoint2 screenLimits;

    int counter=0;
    public static int x,y;

    public void create() {
        TextureRegion region = new TextureRegion();
        walkAnim = new AnimWalk(0.33f, region);
        walkAnim.create(path);

        screenLimits = new GridPoint2(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
//        InputManager.getInstance().addProcessor(new TestInput());
    }

    @Override
    public void render() {
        super.render();

        //x++;//movimento di prova..
        //walkAnim.updateLocation(new GridPoint2(x, y)); //aggiorno la posizione da disegnare della "tile"
        walkAnim.render();

        if(x >= screenLimits.x) //se esci fuori dallo schermo, resetta a 0
            x=0;

    }

    /*Intercettazione degli input dell'utente*/
    private class TestInput extends InputAdapter {
        @Override
        public boolean touchDown(int x, int y, int pointer, int button) {
            // your touch down code here
            Animator.x = x;
            Animator.y = Math.abs(y - screenLimits.y); /*l'origine, secondo lo spritebatch, è in basso a sinistra, pertanto bisogna "aggiustare" come viene interpretato l'input da android*/
            return true;
        }

        @Override
        public boolean touchUp(int x, int y, int pointer, int button) {
            return true;
        }
    }
}