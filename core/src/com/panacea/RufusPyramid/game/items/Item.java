package com.panacea.RufusPyramid.game.items;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.utils.XmlReader;
import com.panacea.RufusPyramid.common.Utilities;
import com.panacea.RufusPyramid.game.Effect.Effect;
import com.panacea.RufusPyramid.game.Effect.TemporaryEffect;
import com.panacea.RufusPyramid.game.items.usableItems.IItemType;
import com.panacea.RufusPyramid.game.items.usableItems.MiscItem;
import com.panacea.RufusPyramid.game.items.usableItems.Weapon;

/**
 * Created by Lux on 13/09/2015.
 */
public abstract class Item implements IItem {
    private static int ITEMS_TYPES = 4; //1 = gold, 2 = UsableItem, 3 = Weapon, 4 = Wearable

    private GridPoint2 position; //refers to a Relative position, not absolute!

    private IItemType itemType;

    public Item(IItemType itemType){
        this.itemType = itemType;
    }

    public Item() { };

    public GridPoint2 getPosition(){
        return this.position;
    }

    public IItemType getItemType(){ //HACK: every subclass of item register to Item it's own type (typically an ENUM, such as AXE, DAGGER ecc.ecc.). This way, a collection of items (as shown in ItemsDrawer) can get immediately the type of the Item WITHOUT the conversion to the correct subclass.
        return itemType;
    }
    public void setPosition(GridPoint2 position){ //perchè esiste setPosition e non viene messa la position nel costruttore? nel caso di chestItem, ad esempio, l'oggetto non ha position
        this.position = position;
    }

    public static Item generateRandomItem(int seed){
        int rand= Utilities.randInt(1,4,seed);
        Item newITem=null;
        switch (rand){
            case 1:
                newITem = new GoldItem(Utilities.randInt(25,100,seed));
                break;
            case 2:{ //MiscItem
                rand = Utilities.randInt(0, MiscItem.MiscItemType.values().length -1, seed);
                MiscItem.MiscItemType type = MiscItem.MiscItemType.values()[rand];
                if(type == MiscItem.MiscItemType.HEALTH_POTION)
                    newITem = new MiscItem(MiscItem.MiscItemType.HEALTH_POTION, new TemporaryEffect(Effect.EffectType.HEALTH, 20, 1), "Health Potion");
                if(type == MiscItem.MiscItemType.INVINCIBILITY_POTION)
                    newITem = new MiscItem(MiscItem.MiscItemType.INVINCIBILITY_POTION, new TemporaryEffect(Effect.EffectType.INVINCIBILITY, 1, 5), "Health Potion");
                if(type == MiscItem.MiscItemType.RAW_MEAT)
                    newITem = new MiscItem(MiscItem.MiscItemType.RAW_MEAT, new TemporaryEffect(Effect.EffectType.HEALTH, 10, 1), "Health Potion");
                break;
            }
            case 3:{//Weapon
                rand = Utilities.randInt(0, Weapon.WeaponType.values().length -1, seed);
                Weapon.WeaponType type = Weapon.WeaponType.values()[rand];
                getEffectsFromXml(type.name());
            }
        }
        return newITem;
    }

    private static Effect getEffectsFromXml(String type){
        try{
            XmlReader xml = new XmlReader();
            XmlReader.Element xml_element = xml.parse(Gdx.files.internal(Utilities.ITEMS_XML_PATH));

            XmlReader.Element baseRoot = xml_element.getChildByNameRecursive(type);
            //TODO:Da completare!
           /* int textRegionIndex = Integer.parseInt(baseRoot.getChildByName(type).get("TexturePosition"));
            Texture newTexture = new Texture(Gdx.files.internal("data/"+basePath));*/
        }
        catch(Exception e){

        }

        return null;
    }


    public enum ItemType implements IItemType {
        CHEST,
        GOLD
    }
}
