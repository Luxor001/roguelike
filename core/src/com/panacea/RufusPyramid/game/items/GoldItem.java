package com.panacea.RufusPyramid.game.items;

import com.badlogic.gdx.math.GridPoint2;

/**
 * Created by Lux on 13/09/2015.
 */
public class GoldItem extends Item {
    private int goldAmount = 0;

    public GoldItem(int goldAmount){
        super();
        this.goldAmount=goldAmount;
    }
}
