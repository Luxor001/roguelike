package com.panacea.RufusPyramid.game.items;

import com.badlogic.gdx.math.GridPoint2;

/**
 * Created by Lux on 13/09/2015.
 */
public abstract class Item {

    private GridPoint2 position; //refers to a Relative position, not absolute!

    private String itemType;

    public Item(String itemType){
        this.itemType = itemType;
    }

    public GridPoint2 getPosition(){
        return this.position;
    }

    public String getItemType(){ //HACK: every subclass of item register to Item it's own type (typically an ENUM, such as AXE, DAGGER ecc.ecc.). This way, a collection of items (as shown in ItemsDrawer) can get immediately the type of the Item WITHOUT the conversion to the correct subclass.
        return itemType;
    }
    public void setPosition(GridPoint2 position){ //perchè esiste setPosition e non viene messa la position nel costruttore? nel caso di chestItem, ad esempio, l'oggetto non ha position
        this.position = position;
    }


}
