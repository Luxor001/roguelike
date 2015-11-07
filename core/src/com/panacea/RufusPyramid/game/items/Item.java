package com.panacea.RufusPyramid.game.items;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.utils.XmlReader;
import com.panacea.RufusPyramid.common.Utilities;
import com.panacea.RufusPyramid.game.Effect.Effect;
import com.panacea.RufusPyramid.game.Effect.TemporaryEffect;
import com.panacea.RufusPyramid.game.creatures.Backpack;
import com.panacea.RufusPyramid.game.items.usableItems.IItemType;
import com.panacea.RufusPyramid.game.items.usableItems.MiscItem;
import com.panacea.RufusPyramid.game.items.usableItems.Weapon;
import com.panacea.RufusPyramid.game.items.usableItems.Wearable;

import java.util.ArrayList;
import java.util.List;

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

    public static Item generateRandomItem(int seed) {
        int rand = Utilities.randInt(1, 4, seed);
        return generateRandomItem(rand, seed);
    }

    /**
     * Genera un oggetto random di tipo "item". A seconda del valore di quest'ultimo genera:
     * 1- Dell'oro
     * 2- Un MiscItem
     * 3- Una Weapon
     * 4- Un Wearable
     * @param item tipo di oggetto
     * @param seed
     * @return L'oggetto generato
     */
    public static Item generateRandomItem(int item, int seed) {
        int rand;
        Item newItem = null;
        List<Effect> listaEffetti = null;
        switch (item){
            case 1:
                newItem = new GoldItem(Utilities.randInt(25,100,seed));
                break;
            case 2: //MiscItem
                rand = Utilities.randInt(0, MiscItem.MiscItemType.values().length -1, seed);
                MiscItem.MiscItemType mType = MiscItem.MiscItemType.values()[rand];
                if(mType == MiscItem.MiscItemType.HEALTH_POTION)
                    newItem = new MiscItem(MiscItem.MiscItemType.HEALTH_POTION, new TemporaryEffect(Effect.EffectType.HEALTH, 20, 1), "Health Potion");
                if(mType == MiscItem.MiscItemType.INVINCIBILITY_POTION)
                    newItem = new MiscItem(MiscItem.MiscItemType.INVINCIBILITY_POTION, new TemporaryEffect(Effect.EffectType.INVINCIBILITY, 1, 5), "Health Potion");
                if(mType == MiscItem.MiscItemType.RAW_MEAT)
                    newItem = new MiscItem(MiscItem.MiscItemType.RAW_MEAT, new TemporaryEffect(Effect.EffectType.HEALTH, 10, 1), "Health Potion");
                break;
            case 3: //Weapon
                rand = Utilities.randInt(0, Weapon.WeaponType.values().length - 1, seed);
                Weapon.WeaponType wType = Weapon.WeaponType.values()[rand];
                listaEffetti = getEffectsFromXml(wType.name());
                newItem = new Weapon(wType, listaEffetti, wType.name().toLowerCase());
                break;
            case 4: //Wearable - Armour
                rand = Utilities.randInt(0, Wearable.WearableType.values().length -1, seed);
                Wearable.WearableType aType = Wearable.WearableType.values()[rand];
                listaEffetti = getEffectsFromXml(aType.name());
                newItem = new Wearable(aType, listaEffetti, aType.name().toLowerCase());
        }
        return newItem;
    }

    private static List<Effect> getEffectsFromXml(String type){
        List<Effect> listaEffetti = new ArrayList<Effect>();
        try{
            XmlReader xml = new XmlReader();
            XmlReader.Element xml_element = xml.parse(Gdx.files.internal(Utilities.ITEMS_XML_PATH));

            XmlReader.Element baseRoot = xml_element.getChildByNameRecursive(type);
            //TODO:Da completare!
           /* int textRegionIndex = Integer.parseInt(baseRoot.getChildByName(type).get("TexturePosition"));
            Texture newTexture = new Texture(Gdx.files.internal("data/"+basePath));*/
            for (int i = 0; i < baseRoot.getChildCount(); i++) {
                XmlReader.Element child = baseRoot.getChild(i);
                Effect effect = null;
                if (child.getName().equals("Defense") || child.getName().equals("Attack") || child.getName().equals("Speed")) {
                    Effect.EffectType itemType = Effect.EffectType.valueOf(child.getName().toUpperCase());
                    effect = new Effect(itemType, baseRoot.getFloat(child.getName()));
                }
                if (effect != null) {
                    listaEffetti.add(effect);
                }
            }
        }
        catch(Exception e){
            e.printStackTrace();
        }

        return listaEffetti;
    }


    public enum ItemType implements IItemType {
        CHEST,
        GOLD
    }
}
