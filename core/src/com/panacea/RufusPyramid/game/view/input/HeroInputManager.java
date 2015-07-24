package com.panacea.RufusPyramid.game.view.input;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.panacea.RufusPyramid.common.Utilities;
import com.panacea.RufusPyramid.game.creatures.HeroController;
import com.panacea.RufusPyramid.game.view.GameCamera;

public class HeroInputManager extends InputAdapter {
    //TODO estendere GestureDetector?

    private int screenWidth = Gdx.graphics.getWidth();
    private int screenHeight = Gdx.graphics.getHeight();

    private HeroController hero;

    private boolean isPaused = false;

    private Vector2 touchDownPosition;

    public HeroInputManager(HeroController hero) {
        this.hero = hero;
    }

    @Override
    public boolean touchUp (int screenX, int screenY, int pointer, int button) {
        if (isPaused) return false;
        if (hasBeenDragged(this.touchDownPosition, new Vector2(screenX, screenY))) return false;

        if (screenY > screenHeight-screenHeight/4) {
            //move up
            this.hero.moveOneStep(Utilities.Directions.SOUTH);
        } else if (screenY < screenHeight/4) {
            //move down
            this.hero.moveOneStep(Utilities.Directions.NORTH);
        } else if (screenX < screenWidth/2) {
            //move left
            this.hero.moveOneStep(Utilities.Directions.WEST);
        } else if (screenX > screenWidth/2) {
            //move right
            this.hero.moveOneStep(Utilities.Directions.EAST);
        } else {
            return false;   //not performed
        }


        /* TODO Vai alla posizione (x,y) sulla mappa, tramite un algoritmo di path*/
        Vector3 gamePos = GameCamera.get().unproject(new Vector3(screenX, screenY, 0));
        GridPoint2 mapPos = new GridPoint2(Math.round(gamePos.x/32), Math.round(gamePos.y/32));

        return true;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        this.touchDownPosition = new Vector2(screenX, screenY);
        return false;
    }

    public void setPaused(boolean isPaused) {
        this.isPaused = isPaused;
    }

    private boolean hasBeenDragged(Vector2 touchDownPosition, Vector2 touchUpPosition) {
        int sensibility = 5;
        Vector2 diff = touchDownPosition.sub(touchUpPosition);
        if (Math.abs(diff.x) > sensibility || Math.abs(diff.y) > sensibility) {
            return true;
        }
        return false;
    }
}