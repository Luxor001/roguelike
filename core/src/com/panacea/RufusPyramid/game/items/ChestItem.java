package com.panacea.RufusPyramid.game.items;

import com.panacea.RufusPyramid.common.Utilities;

/**
 * Created by Lux on 13/09/2015.
 */
public class ChestItem extends Item{

    private Item itemStored;
//    private List<OpenedChestListener> openChestListeners;

    public ChestItem(Item itemStored){
        super(ItemType.CHEST);
        this.itemStored=itemStored;
    }

    public ChestItem(){
        super(ItemType.CHEST);

        int seed=(int)System.nanoTime();
        int randInt = Utilities.randInt(0, 3, seed); //FIXME: andrebbe fatto con un db.. ma vedremo se avremo tempo
        switch (randInt){
            case 1:{
                int newGold= Utilities.randInt(25,100,seed);
                itemStored = new GoldItem(newGold);
                break;
            }
            case 2:{

                break;
            }
        }
    }

    public Item getItemStored(){
        return itemStored;
    }


}
