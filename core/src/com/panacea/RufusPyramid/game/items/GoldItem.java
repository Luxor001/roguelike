package com.panacea.RufusPyramid.game.items;

/**
 * Created by Lux on 13/09/2015.
 */
public class GoldItem extends Item {
    private int goldAmount = 0;

    public GoldItem(int goldAmount){
        super("GoldItem");
        this.goldAmount=goldAmount;
    }

    public int getGoldAmount(){
        return goldAmount;
    }
}
