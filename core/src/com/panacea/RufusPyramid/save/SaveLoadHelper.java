package com.panacea.RufusPyramid.save;

import com.badlogic.gdx.Gdx;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.Serializer;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

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

    public SaveLoadHelper() {}

    /********** Save methods ***********/
    public void startSave() {
        this.kryo = new Kryo();
//        try {
            this.output = new Output(Gdx.files.local(saveFile).write(false));
//        } catch (FileNotFoundException e) {
//            Gdx.app.error(SaveLoadHelper.class.toString(), "Errore nella lettura del file " + saveFile);
//            e.printStackTrace();
//        }
    }

    public void stopSave() {
        this.output.close();
        this.output = null;
        this.kryo = null;
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
    }

    public boolean isSaving() {
        return this.kryo != null && this.output != null;
    }

    /*********** Load methods ***********/
    public void startLoad() {
        this.kryo = new Kryo();
//        try {
            this.input = new Input(Gdx.files.local(saveFile).read());
//        } catch (FileNotFoundException e) {
//            Gdx.app.error(SaveLoadHelper.class.toString(), "Errore nella lettura del file " + saveFile);
//            e.printStackTrace();
//        }
    }

    public void stopLoad() {
        this.input.close();
        this.input = null;
        this.kryo = null;
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
}
