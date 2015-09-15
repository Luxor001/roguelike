package com.panacea.RufusPyramid.game.items;

import com.badlogic.gdx.math.GridPoint2;

/**
 * Created by Lux on 13/09/2015.
 */
public abstract class Item {

    private GridPoint2 position; //refers to a Relative position, not absolute!

    public Item(){
    }

    public GridPoint2 getPosition(){
        return this.position;
    }

    public boolean setPosition(GridPoint2 position){
        this.position = position; //TODO: Fare controllo se posizione è valida!
        return true;
    }


}
