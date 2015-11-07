package com.panacea.RufusPyramid.save;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.FloatArray;
import com.badlogic.gdx.utils.IntArray;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.Serializer;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.panacea.RufusPyramid.game.Diary;
import com.panacea.RufusPyramid.game.Effect.Effect;
import com.panacea.RufusPyramid.game.Effect.TemporaryEffect;
import com.panacea.RufusPyramid.game.GameController;
import com.panacea.RufusPyramid.game.GameMaster;
import com.panacea.RufusPyramid.game.GameModel;
import com.panacea.RufusPyramid.game.actions.ActionChosenEvent;
import com.panacea.RufusPyramid.game.actions.ActionChosenListener;
import com.panacea.RufusPyramid.game.actions.ActionResult;
import com.panacea.RufusPyramid.game.actions.AttackAction;
import com.panacea.RufusPyramid.game.actions.IAction;
import com.panacea.RufusPyramid.game.actions.IAgent;
import com.panacea.RufusPyramid.game.actions.InteractAction;
import com.panacea.RufusPyramid.game.actions.MoveAction;
import com.panacea.RufusPyramid.game.actions.OpenedChestListener;
import com.panacea.RufusPyramid.game.actions.PassAction;
import com.panacea.RufusPyramid.game.creatures.AbstractCreature;
import com.panacea.RufusPyramid.game.creatures.Backpack;
import com.panacea.RufusPyramid.game.creatures.CreatureAI;
import com.panacea.RufusPyramid.game.creatures.CreatureDeadListener;
import com.panacea.RufusPyramid.game.creatures.DefaultHero;
import com.panacea.RufusPyramid.game.creatures.Enemy;
import com.panacea.RufusPyramid.game.creatures.HeroController;
import com.panacea.RufusPyramid.game.creatures.ICreature;
import com.panacea.RufusPyramid.game.creatures.Level;
import com.panacea.RufusPyramid.game.creatures.Stats;
import com.panacea.RufusPyramid.game.items.ChestItem;
import com.panacea.RufusPyramid.game.items.GoldItem;
import com.panacea.RufusPyramid.game.items.Item;
import com.panacea.RufusPyramid.game.items.usableItems.IItemType;
import com.panacea.RufusPyramid.game.items.usableItems.MiscItem;
import com.panacea.RufusPyramid.game.items.usableItems.UsableItem;
import com.panacea.RufusPyramid.game.items.usableItems.Weapon;
import com.panacea.RufusPyramid.game.items.usableItems.Wearable;
import com.panacea.RufusPyramid.game.view.MapDrawer;
import com.panacea.RufusPyramid.game.view.input.HeroInputManager;
import com.panacea.RufusPyramid.game.view.ui.HealthBar;
import com.panacea.RufusPyramid.game.view.ui.UIDrawer;
import com.panacea.RufusPyramid.map.Map;
import com.panacea.RufusPyramid.map.MapContainer;
import com.panacea.RufusPyramid.map.MapFactory;
import com.panacea.RufusPyramid.map.Tile;

import java.util.AbstractList;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

/**
 * Created by gio on 17/10/15.
 *
 * Libgdx tutorial: https://github.com/libgdx/libgdx/wiki/Saved-game-serialization
 * Kryo doc:        https://github.com/EsotericSoftware/kryo#quickstart
 *
 * Tutorial easy di un terzo: http://www.moddb.com/games/office-management-101/features/implementing-a-savegame-system
 *
 * TODO: per aumentare l'efficienza si possono registrare le classi degli oggetti che
 * verranno salvati assegnandoli un ID. Kryo identificherà le istanze di tali classi
 * con l'ID al posto che con il nome della classe in modo quindi più efficiente.
 * Esempio in Kryo doc -> registration
 */
public class SaveLoadHelper {
    private static final String saveFile = "game.save";
    private Output output;
    private Input input;
    private Kryo kryo;
    private static SaveLoadHelper SINGLETON;

    public static SaveLoadHelper getIstance(){
        if(SINGLETON == null)
            SINGLETON = new SaveLoadHelper();
        return SINGLETON;
    }
    private SaveLoadHelper() {
        this.kryo = new Kryo();

        Serializer<UIDrawer> uiDrawerSerializer = new Serializer<UIDrawer>() {
            @Override
            public void write(Kryo kryo, Output output, UIDrawer object) {
            }

            @Override
            public UIDrawer read(Kryo kryo, Input input, Class<UIDrawer> type) {
                return new UIDrawer();
            }
        };

        Serializer<HashMap> hashMapSerializer = new Serializer<HashMap>() {
            @Override
            public void write(Kryo kryo, Output output, HashMap object) {
                kryo.writeClass(output, object.getClass());
                kryo.writeObject(output, new Integer(object.size()));

                for (Object k : object.keySet()) {
                    kryo.writeClassAndObject(output, k);
                    kryo.writeClassAndObject(output, object.get(k));
                }

            }

            @Override
            public HashMap read(Kryo kryo, Input input, Class<HashMap> type) {
                HashMap map = null;

                Class classe = kryo.readClass(input).getType();
                int size = kryo.readObject(input, Integer.class).intValue();
                if (classe.equals(LinkedHashMap.class)) {
                    map = new LinkedHashMap();
                }

                for (int i = 0; i < size; i++) {
                    Object key = kryo.readClassAndObject(input);
                    Object value = kryo.readClassAndObject(input);
                    map.put(key, value);
                }

                return map;
            }
        };

//        final Serializer<Backpack> backpackSerializer = new FieldSerializer<Backpack>(kryo, Backpack.class);

        Serializer<ICreature> creatureSerializer = new Serializer<ICreature>() {
            @Override
            public void write(Kryo kryo, Output output, ICreature object) {
                kryo.writeObject(output, object.getCreatureType());
                kryo.writeObject(output, new Integer(object.getID()));
                kryo.writeObject(output, object.getPosition());
                kryo.writeObject(output, object.getName());
                kryo.writeObject(output, object.getDescription());
                kryo.writeObjectOrNull(output, object.getAbsoluteTickPosition(), GridPoint2.class);
                kryo.writeObject(output, object.getBaseStats());
//                kryo.writeObject(output, object.getCurrentStats());
                kryo.writeObject(output, new Integer(object.getHPCurrent()));
                kryo.writeObject(output, new Integer(object.getEnergy()));
                kryo.writeObjectOrNull(output, object.getEquipment(), Backpack.class);
                kryo.writeObject(output, object.getEffects());

                switch (object.getCreatureType()) {
                    case HERO:
                        DefaultHero hero = (DefaultHero)object;
                        kryo.writeObject(output, new Integer(hero.getGoldAmount()));
                        break;
                    case ORC:
                    case SKELETON:
                    case UGLYYETI:
                    case WRAITH:
                        break;
                }
            }

            @Override
            public ICreature read(Kryo kryo, Input input, Class<ICreature> type) {
                ICreature.CreatureType creatureType = kryo.readObject(input, ICreature.CreatureType.class);
                int id = kryo.readObject(input, Integer.class).intValue();
                Tile tile = kryo.readObject(input, Tile.class);
                String name = kryo.readObject(input, String.class);
                String description = kryo.readObject(input, String.class);
                GridPoint2 absTickPos = kryo.readObjectOrNull(input, GridPoint2.class);
                Stats baseStats = kryo.readObject(input, Stats.class);
//                Stats currStats = kryo.readObject(input, Stats.class);
                int hpCurrent = kryo.readObject(input, Integer.class).intValue();
                int energy = kryo.readObject(input, Integer.class).intValue();
                Backpack backpack = kryo.readObjectOrNull(input, Backpack.class);
                ArrayList<Effect> effects = kryo.readObject(input, ArrayList.class);

                ICreature creature = null;
                switch(creatureType) {
                    case HERO:
                        int gold = kryo.readObject(input, Integer.class).intValue();
                        creature = new DefaultHero(name, gold, id);
                        break;
                    case ORC:
                    case SKELETON:
                    case UGLYYETI:
                    case WRAITH:
                        creature = new Enemy(name, description, baseStats, creatureType, id);
                        break;
                }

                creature.setAbsoluteTickPosition(absTickPos);
                creature.setDescription(description);
                creature.setEnergy(energy);
                creature.setHPCurrent(hpCurrent);
                creature.setPosition(tile);
                creature.setEquipment(backpack);
                creature.addEffects(effects);

                return creature;
            }
        };

        Serializer<ArrayList> listSerializer = new Serializer<ArrayList>() {
            {
                setAcceptsNull(true);
            }

            private Class genericType;

            public void setGenerics(Kryo kryo, Class[] generics) {
                if(generics != null) {
                    if (kryo.isFinal(generics[0])) genericType = generics[0];
                }
            }

            @Override
            public void write(Kryo kryo, Output output, ArrayList list) {
                int length = list.size();
                output.writeInt(length, true);
                if (length == 0) return;
//                if (genericType != null) {
//                    Serializer serializer = kryo.getSerializer(genericType);
//                    genericType = null;
//                    for (Object element : list)
//                        kryo.writeObjectOrNull(output, element, serializer);
//                } else {
//                Gdx.app.log(SaveLoadHelper.class.toString()+"::ArrayListSerializer", list.get(0) + " ma genericType è " + this.genericType);
                for (Object element : list) {
                    kryo.writeClassAndObject(output, element);
                }
//                }
            }

            @Override
            public ArrayList read(Kryo kryo, Input input, Class<ArrayList> type) {

                ArrayList array = new ArrayList();
                kryo.reference(array);
                int length = input.readInt(true);
                array.ensureCapacity(length);
//                if (genericType != null) {
//                    Class elementClass = genericType;
//                    Serializer serializer = kryo.getSerializer(genericType);
//                    genericType = null;
//                    for (int i = 0; i < length; i++)
//                        array.add(kryo.readObjectOrNull(input, elementClass, serializer));
//                } else {
//                Gdx.app.log(SaveLoadHelper.class.toString()+"::ArrayListSerializer", "genericType è " + this.genericType);
                for (int i = 0; i < length; i++) {
                    try {
                        Object obj = kryo.readClassAndObject(input);
                        array.add(obj);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
//                }
                return array;
            }
        };

//        try {
//        this.output = new Output(Gdx.files.local(saveFile).write(false));
//        } catch (FileNotFoundException e) {
//            Gdx.app.error(SaveLoadHelper.class.toString(), "Errore nella lettura del file " + saveFile);
//            e.printStackTrace();
//        }

        kryo.register(Array.class, new Serializer<Array>() {
            {
                setAcceptsNull(true);
            }

            private Class genericType;

            public void setGenerics(Kryo kryo, Class[] generics) {
                if (kryo.isFinal(generics[0])) genericType = generics[0];
            }

            public void write(Kryo kryo, Output output, Array array) {
                int length = array.size;
                output.writeInt(length, true);
                if (length == 0) return;
                if (genericType != null) {
                    Serializer serializer = kryo.getSerializer(genericType);
                    genericType = null;
                    for (Object element : array)
                        kryo.writeObjectOrNull(output, element, serializer);
                } else {
                    for (Object element : array)
                        kryo.writeClassAndObject(output, element);
                }
            }

            public Array read (Kryo kryo, Input input, Class<Array> type) {
                Array array = new Array();
                kryo.reference(array);
                int length = input.readInt(true);
                array.ensureCapacity(length);
                if (genericType != null) {
                    Class elementClass = genericType;
                    Serializer serializer = kryo.getSerializer(genericType);
                    genericType = null;
                    for (int i = 0; i < length; i++)
                        array.add(kryo.readObjectOrNull(input, elementClass, serializer));
                } else {
                    for (int i = 0; i < length; i++)
                        array.add(kryo.readClassAndObject(input));
                }
                return array;
            }
        });

        kryo.register(IntArray.class, new Serializer<IntArray>() {
            {
                setAcceptsNull(true);
            }

            public void write (Kryo kryo, Output output, IntArray array) {
                int length = array.size;
                output.writeInt(length, true);
                if (length == 0) return;
                for (int i = 0, n = array.size; i < n; i++)
                    output.writeInt(array.get(i), true);
            }

            public IntArray read (Kryo kryo, Input input, Class<IntArray> type) {
                IntArray array = new IntArray();
                kryo.reference(array);
                int length = input.readInt(true);
                array.ensureCapacity(length);
                for (int i = 0; i < length; i++)
                    array.add(input.readInt(true));
                return array;
            }
        });

        kryo.register(FloatArray.class, new Serializer<FloatArray>() {
            {
                setAcceptsNull(true);
            }

            public void write (Kryo kryo, Output output, FloatArray array) {
                int length = array.size;
                output.writeInt(length, true);
                if (length == 0) return;
                for (int i = 0, n = array.size; i < n; i++)
                    output.writeFloat(array.get(i));
            }

            public FloatArray read (Kryo kryo, Input input, Class<FloatArray> type) {
                FloatArray array = new FloatArray();
                kryo.reference(array);
                int length = input.readInt(true);
                array.ensureCapacity(length);
                for (int i = 0; i < length; i++)
                    array.add(input.readFloat());
                return array;
            }
        });

        kryo.register(Color.class, new Serializer<Color>() {
            public Color read (Kryo kryo, Input input, Class<Color> type) {
                Color color = new Color();
                Color.rgba8888ToColor(color, input.readInt());
                return color;
            }

            public void write (Kryo kryo, Output output, Color color) {
                output.writeInt(Color.rgba8888(color));
            }
        });

//        Serializer<ChestItem> chestItemSerializer =  new Serializer<ChestItem>() {
//
//            @Override
//            public void write(Kryo kryo, Output output, ChestItem object) {
//                GridPoint2 pos = object.getPosition();
//                Item storedItem = object.getItemStored();
//
//                kryo.writeClassAndObject(output, pos);
//                kryo.writeClassAndObject(output, storedItem);
//            }
//
//            @Override
//            public ChestItem read(Kryo kryo, Input input, Class<ChestItem> type) {
//                GridPoint2 pos = kryo.readObject(input, GridPoint2.class);
//                Item storedItem = kryo.readObject(input, Item.class);
//
//                ChestItem chest = new ChestItem(storedItem);
//                chest.setPosition(pos);
//                return chest;
//            }
//        };

        Serializer<Item> itemSerializer =  new Serializer<Item>() {

            @Override
            public void write(Kryo kryo, Output output, Item object) {
                IItemType itemType = object.getItemType();
                GridPoint2 pos = object.getPosition();

                kryo.writeObject(output, itemType);
                kryo.writeObjectOrNull(output, pos, GridPoint2.class);

                if (object instanceof ChestItem) {
                    Item storedItem = ((ChestItem)object).getItemStored();
                    kryo.writeObject(output, storedItem, this);
                } //else...
            }

            @Override
            public Item read(Kryo kryo, Input input, Class<Item> type) {
                IItemType itemType = kryo.readObject(input, IItemType.class);
                GridPoint2 pos = kryo.readObjectOrNull(input, GridPoint2.class);

                Item readItem;

                if (itemType.equals(Item.ItemType.CHEST)) {
                    Item storedItem = kryo.readObject(input, Item.class);
                    readItem = new ChestItem(storedItem);
                } else {
                    readItem = null;
                    Gdx.app.debug(SaveLoadHelper.class.toString(), "Item boh: " + itemType);
                }
                if(pos != null && readItem != null) //FIXMEABSOLUTELY: Qui è so, a volte il readItem è null. che sia colpa del serializzatore di IItemType?
                    readItem.setPosition(pos);
                return readItem;
            }
        };

        kryo.register(com.badlogic.gdx.scenes.scene2d.ui.Value.Fixed.class, new Serializer() {
            @Override
            public void write(Kryo kryo, Output output, Object object) {

            }

            @Override
            public Object read(Kryo kryo, Input input, Class type) {
                com.badlogic.gdx.scenes.scene2d.ui.Value.Fixed asd = new com.badlogic.gdx.scenes.scene2d.ui.Value.Fixed(input.readFloat());
                return asd;
            }
        });

        kryo.register(IItemType.class, new Serializer<IItemType>() {
            @Override
            public void write(Kryo kryo, Output output, IItemType itemType) {
                kryo.writeObject(output,itemType);
            }

            @Override
            public IItemType read(Kryo kryo, Input input, Class type) {
                IItemType asd=kryo.readObject(input,IItemType.class);
                return asd;
            }
        },1001);



        kryo.register(GameModel.class, 0);
        kryo.register(AbstractCreature.class, creatureSerializer, 1);
        kryo.register(CreatureAI.class, 2);
        kryo.register(DefaultHero.class, creatureSerializer, 3);
        kryo.register(Enemy.class, creatureSerializer, 3);
        kryo.register(ICreature.class, creatureSerializer, 4);
        kryo.register(Level.class, 5);
        kryo.register(Stats.class, 6);
        kryo.register(Effect.class, 7);
        kryo.register(TemporaryEffect.class, 8);
        kryo.register(Item.class, itemSerializer, 10);
        kryo.register(ChestItem.class, itemSerializer, 11);
        kryo.register(GoldItem.class, 12);
        kryo.register(MiscItem.class, 13);
        kryo.register(UsableItem.class, 14);
        kryo.register(Weapon.class, 15);
        kryo.register(Wearable.class, 16);
        kryo.register(Map.class, 17);
        kryo.register(MapContainer.class, 18);
        kryo.register(MapDrawer.class, 19);
        kryo.register(MapFactory.class, 20);
        kryo.register(Tile.class, 21);
        kryo.register(IAgent.class, 22);
        kryo.register(ActionChosenListener.class, 23);
        kryo.register(CreatureDeadListener.class, 24);
        kryo.register(HeroController.class, 25);
        kryo.register(HeroInputManager.class, 26);
        kryo.register(ActionResult.class, 27);
        kryo.register(ArrayList.class, listSerializer,28);
        kryo.register(IAction.class, 29);
        kryo.register(ActionChosenEvent.class, 30);
        kryo.register(ActionResult.class, 31);
        kryo.register(AttackAction.class, 32);
        kryo.register(InteractAction.class, 33);
        kryo.register(MoveAction.class, 34);
        kryo.register(OpenedChestListener.class, 35);
        kryo.register(PassAction.class, 36);
        kryo.register(GameMaster.class, 37);
        kryo.register(GridPoint2.class, 38);
        kryo.register(HealthBar.class, 39);
        kryo.register(ICreature.CreatureType.class, 40);
        kryo.register(Diary.class, 41);
        kryo.register(AbstractList.class, 42);
        kryo.register(Diary.class, 44);
        kryo.register(LinkedHashMap.class, hashMapSerializer, 45);
        kryo.register(UIDrawer.class, uiDrawerSerializer, 46);
        kryo.register(Backpack.class, /*backpackSerializer,*/ 47);
    }

    /**
     * Controlla se c'è un gioco salvato caricabile e ritorna true o false rispettivamente.
     * @return true se c'è un gioco precedentemente salvato caricabile, false altrimenti.
     */
    public boolean existsSavedGame() {
        return Gdx.files.local(saveFile).exists();
    }

    /********** Save methods ***********/
    public void startSave() {
        this.output = new Output(Gdx.files.local(saveFile).write(false));
    }

    public void stopSave() {
        this.output.close();
        this.output = null;
    //    this.kryo = null;
    }

    /**
     * Per oggetti complessi è possibile creare un serializzatore
     * che non fa molto altro oltre a selezionare i valori importanti dall'oggetto
     * e salvarli.
     * @param toSave
     */
    public void saveWithSerializer(ISerializable toSave) {
        if (!isSaving()) {
            Gdx.app.error(SaveLoadHelper.class.toString(), "Tentativo di salvare un oggetto senza aver aperto il salvataggio! " + toSave);
            return;
        }

        Serializer serializer = toSave.getSerializer();
        if (serializer == null) {
            Gdx.app.error(SaveLoadHelper.class.toString(), "Serializer null per l'oggetto " + toSave);
            return;
        }

        serializer.write(this.kryo, this.output, toSave);
    }

    /**
     * Salva un qualsiasi oggetto java serializzandolo interamente.
     * @param toSave
     */
    public void saveObject(Object toSave) {
        if (!isSaving()) {
            Gdx.app.error(SaveLoadHelper.class.toString(), "Tentativo di salvare un oggetto senza aver aperto il salvataggio! " + toSave);
            return;
        }
        Gdx.app.log(SaveLoadHelper.class.toString(), "Si sta salvando un oggetto completo! Usare il meno possibile e solo con pojo.");
        this.kryo.writeObject(this.output, toSave);
        output.flush();
    }

    public boolean isSaving() {
        return this.kryo != null && this.output != null;
    }

    /*********** Load methods ***********/
    public void startLoad() {
        this.input = new Input(Gdx.files.local(saveFile).read());
//        com.esotericsoftware.minlog.Log.TRACE();

//        try {
//        } catch (FileNotFoundException e) {
//            Gdx.app.error(SaveLoadHelper.class.toString(), "Errore nella lettura del file " + saveFile);
//            e.printStackTrace();
//        }
    }

    public void stopLoad() {
        this.input.close();
        this.input = null;
      //  this.kryo = null;

        //Cancello il file in quanto il gioco è stato caricato
//        Gdx.files.local(saveFile).delete();
    }

    public <T> T loadWithSerializer(ISerializable toLoad, Class<T> classe) {
        if (!isLoading()) {
            Gdx.app.error(SaveLoadHelper.class.toString(), "Tentativo di caricare un oggetto senza aver aperto il caricamento! " + toLoad);
            return null;
        }

        Serializer<T> serializer = toLoad.getSerializer();
        if (serializer == null) {
            Gdx.app.error(SaveLoadHelper.class.toString(), "Serializer null per l'oggetto " + toLoad);
            return null;
        }

        return serializer.read(this.kryo, this.input, classe);
    }

    /**
     * Data una class in input viene restituito un oggetto istanza di tale classe
     * caricandolo dal file aperto in lettura.
     * Lancia un bufferUnderflow se si leggono troppi oggetti (andando oltre la fine del file).
     * Potrebbe lanciare IndexOutOfBoundsException se si leggono gli oggetti in ordine errato
     * o se si legge un oggetto sbagliato/in più in mezzo al mucchio.
     * @param classe classe dell'oggetto da leggere.
     * @param <T> classe dell'oggetto da leggere.
     * @return l'oggetto letto.
     */
    public <T> T loadObject(Class<T> classe) {
        return kryo.readObject(input, classe);
    }

    public boolean isLoading() {
        return this.kryo != null && this.input != null;
    }

    public void saveGame() {
        Gdx.app.log(GameController.class.toString(), "Inizio salvataggio");
        this.startSave();
        this.saveObject(GameModel.get());
        this.saveObject(GameController.getGm());
        this.stopSave();
        Gdx.app.log(GameController.class.toString(), "Salvataggio completato correttamente");
    }

//    public void loadGame() ?
}
