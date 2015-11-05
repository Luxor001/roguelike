package com.panacea.RufusPyramid.game.creatures;

import com.panacea.RufusPyramid.game.Effect.Effect;
import com.panacea.RufusPyramid.game.Effect.TemporaryEffect;
import com.panacea.RufusPyramid.game.GameModel;
import com.panacea.RufusPyramid.game.items.usableItems.Equippable;
import com.panacea.RufusPyramid.game.items.usableItems.IItemType;
import com.panacea.RufusPyramid.game.items.usableItems.MiscItem;
import com.panacea.RufusPyramid.game.items.usableItems.UsableItem;
import com.panacea.RufusPyramid.game.items.usableItems.Weapon;
import com.panacea.RufusPyramid.game.items.usableItems.Wearable;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * Classe di gestione degli oggetti equipaggiati e trasportati.
 * Contiene un enum pubblico con i tipi di oggetti equipaggiabili.
 */
public class Backpack {
    private final static int MAX_STORAGE_CAPACITY = 18;

    private List<UsableItem> storage;
    private LinkedHashMap<Backpack.EquippableType, Equippable> equippedItems;
    private List<EquippedItemListener> equippedItemListeners;

    public Backpack() {
        this.equippedItems = new LinkedHashMap<Backpack.EquippableType, Equippable>(EquippableType.values().length);
        this.storage = new ArrayList<UsableItem>(MAX_STORAGE_CAPACITY);
        this.equippedItemListeners = new ArrayList<EquippedItemListener>();
        ArrayList<Effect> effectsList1 = new ArrayList<Effect>();
        effectsList1.add(new Effect(Effect.EffectType.ATTACK, 3));
        ArrayList<Effect> effectsList2 = new ArrayList<Effect>();
        effectsList2.add(new Effect(Effect.EffectType.ATTACK, 1));
        this.addItemToStorage(new Weapon(Weapon.WeaponType.PICK, effectsList2, "Neptuno's Trident"));
        this.addItemToStorage(new Weapon(Weapon.WeaponType.AXE, effectsList1, "GoldenAxe"));
        this.addItemToStorage(new MiscItem(MiscItem.MiscItemType.HEALTH_POTION, new TemporaryEffect(Effect.EffectType.HEALTH, 50, 1), "Superpozione"));
    }

    public Equippable getEquippedItem(Backpack.EquippableType itemToRetrieve) {
        return equippedItems.get(itemToRetrieve);
    }

    public List<Equippable> getAllEquippedItems() {
        List<Equippable> items = new ArrayList<Equippable>();
        for (EquippableType type : EquippableType.values()) {
            items.add(this.getEquippedItem(type));
        }
        return items;
    }

    /**
     *  Add item to backpack storage.
     *  Returns true if the item has been added successfully, false otherwise (storage full).
     */
    public boolean addItemToStorage(UsableItem newItem) {
        if (this.storage == null) {
            //Lazy initialization of storage arraylist (most creatures will not use this)
            this.storage = new ArrayList<UsableItem>(MAX_STORAGE_CAPACITY);
        }
        if (this.storage.size() == MAX_STORAGE_CAPACITY) {
            //Storage full
            return false;
        }

        this.storage.add(newItem);
        return true;
    }

    /*
     * Put an item in an equippable position.
     * Returns old equipped Item, if there is, null otherwise.
     */
    public Equippable setEquipItem(EquippableType position, Equippable newItem) {
        List<Equippable> oldItems = new ArrayList<Equippable>();
        if (newItem != null && newItem.equals(this.equippedItems.get(position))) {
            newItem = null;
        }
        Equippable oldItem = this.equippedItems.put(position, newItem);
        oldItems.add(oldItem);
        EquippedItemEvent event = new EquippedItemEvent(newItem, oldItems, position);
        fireEquippedItemEvent(event);
        return oldItem;
    }

    /**
     * Put an item in an equippable position.
     * Returns old equipped Item, if there is, null otherwise.
     */
    public Equippable setEquipItem(Equippable newItem) {
        EquippableType position = null;
        IItemType type = newItem.getItemType();

        if (type instanceof Weapon.WeaponType) {
            position = EquippableType.LEFT_HAND;
        } else if (type instanceof Wearable.WearableType) {
            switch ((Wearable.WearableType)type) {
                case ARMOR:
//                case ARMOR2:
                    position = EquippableType.CHEST;
                    break;
                case BOOTS:
                    position = EquippableType.FEET;
                    break;
                case HELM:
                    position = EquippableType.HEAD;
                    break;
                case SHIELD:
                    position = EquippableType.RIGHT_HAND;
                    break;
                case GAUNTLETS:
                    position = EquippableType.GAUNTLETS;
                    break;
            }
        }

        if (position != null) {
            return this.setEquipItem(position, newItem);
        } else {
            return null;
        }
    }

    public List<UsableItem> getStorage() {
        return this.storage;
    }

    public int getMaxStorageCapacity() {
        return MAX_STORAGE_CAPACITY;
    }

    public enum EquippableType {
        HEAD,
        CHEST,
        LEFT_HAND,  //Per adesso solo arma
        RIGHT_HAND, //Per adesso solo scudo
//        TWO_HANDED_WEAPON,
        GAUNTLETS,
//        LEFT_RING,
//        RIGHT_RING,
        FEET
    }

    public void addEquippedItemListener(EquippedItemListener listener) {
        this.equippedItemListeners.add(listener);
    }

    private void fireEquippedItemEvent(EquippedItemEvent event) {
        for (EquippedItemListener listener : this.equippedItemListeners) {
            listener.equippedItem(event, this);
        }

        UsableItem item;
        String coefficent;
        if (event.getEquippedItem() == null) {
            item = (UsableItem)event.getUnequippedItem().get(0);
            GameModel.get().getDiary().addLine(item.getItemName() + " unequipped (" + item.getEffects().get(0).getCoefficient() + ")");
        } else {
            item = (UsableItem)event.getEquippedItem();
            GameModel.get().getDiary().addLine(item.getItemName() + " equipped (" + item.getEffects().get(0).getCoefficient() + ")");
        }
//        GameModel.get().getDiary().addLine("" + GameModel.get().getHero().getCurrentStats().getAttack());
    }

    public List<Effect> getEquipEffects() {
        List<Effect> allEffects = new ArrayList<Effect>();
        for (Equippable item : this.getAllEquippedItems()) {
            if (item != null) {
                List<Effect> itemEffects = ((UsableItem) item).getEffects();
                if (itemEffects != null) {
                    allEffects.addAll(itemEffects);
                }
            }
        }
        return allEffects;
    }
}
