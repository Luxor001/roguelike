package com.panacea.RufusPyramid.game.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.GridPoint2;
import com.panacea.RufusPyramid.game.view.animations.AbstractAnimation;

/*Snippet preso da https://github.com/libgdx/libgdx/wiki/2D-Animation*/
public class Animator extends ViewObject {

    private com.panacea.RufusPyramid.game.view.animations.AnimWalk walkAnim;
    public GridPoint2 screenLimits;
    private AbstractAnimation anim;

    int counter=0;
    public static int x,y;

    public void create() {
        //Esempio di animazione, scollegata dai model
//        walkAnim = new com.panacea.RufusPyramid.game.view.animations.AnimWalk(new GridPoint2(0,0), new GridPoint2(0,32), 40.0f);
//        walkAnim.create(/*path*/);
//        this.anim = new AnimDamage(new GridPoint2(32,32), 10);

        screenLimits = new GridPoint2(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
//        InputManager.get().addProcessor(new TestInput());
    }

    @Override
    public void render(float delta) {
        super.render(delta);

        //x++;//movimento di prova..
        //walkAnim.updateLocation(new GridPoint2(x, y)); //aggiorno la posizione da disegnare della "tile"
//        walkAnim.render(delta);
//        this.anim.render(delta);
    }
}