package com.panacea.RufusPyramid.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector3;
import com.panacea.RufusPyramid.creatures.DefaultHero;
import com.panacea.RufusPyramid.creatures.HeroController;

public class HeroInputManager extends InputAdapter {
//    final OrthographicCamera camera;
//    final Vector3 curr = new Vector3();
//    final Vector3 last = new Vector3(-1, -1, -1);
//    final Vector3 delta = new Vector3();

    private int screenWidth = Gdx.graphics.getWidth();
    private int screenHeight = Gdx.graphics.getHeight();

    private HeroController hero;

    public HeroInputManager(HeroController hero) {
//        this.camera = camera;
        this.hero = hero;
    }

//    @Override
//    public boolean touchDragged (int x, int y, int pointer) {
////        camera.unproject(curr.set(x, y, 0));
////        if (!(last.x == -1 && last.y == -1 && last.z == -1)) {
////            camera.unproject(delta.set(last.x, last.y, 0));
////            delta.sub(curr);
////            camera.position.add(delta.x, delta.y, 0);
////        }
////        last.set(x, y, 0);
////        return false;
//    }
//
//    @Override
//    public boolean touchUp (int x, int y, int pointer, int button) {
////        last.set(-1, -1, -1);
////        return false;
//    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        if (screenY > screenHeight-screenHeight/4) {
            //move up
            this.hero.moveOneStep(HeroController.MoveDirection.SOUTH);
        } else if (screenY < screenHeight/4) {
            //move down
            this.hero.moveOneStep(HeroController.MoveDirection.NORTH);
        } else if (screenX < screenWidth/2) {
            //move left
            this.hero.moveOneStep(HeroController.MoveDirection.WEST);
        } else if (screenX > screenWidth/2) {
            //move right
            this.hero.moveOneStep(HeroController.MoveDirection.EAST);
        } else {
            return false;   //not processed
        }
        return true;
    }
}