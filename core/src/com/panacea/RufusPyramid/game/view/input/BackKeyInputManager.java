package com.panacea.RufusPyramid.game.view.input;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by gio on 31/10/15.
 */
public class BackKeyInputManager extends InputAdapter {
    private List<BackKeyListener> backKeyListeners;

    public BackKeyInputManager() {
        this.backKeyListeners = new ArrayList<BackKeyListener>();
    }

    @Override
    public boolean keyDown(int keycode) {
        if (keycode == Input.Keys.BACK) {
            //throw event
            return this.fireBackKeyEvent(new BackKeyEvent());
        }
        return false;
    }

    private boolean fireBackKeyEvent(BackKeyEvent event) {
        boolean handled = false;
        for (BackKeyListener listener : this.backKeyListeners) {
            handled = listener.backKeyDown(event, this);
            if (handled) {
                break;
            }
        }

        return handled;
    }

    public void addBackKeyListener(BackKeyListener listener) {
        this.backKeyListeners.add(listener);
    }

    public interface BackKeyListener {
        /**
         * Permette di specificare cosa fare quando viene premuto il tasto BACK di Android.
         * Deve ritornare true se il tasto è stato gestito e non deve essere propagato.
         * @param event evento
         * @param source sorgente dell'evento
         * @return true se l'evento è stato gestito, falso altrimenti
         */
        public boolean backKeyDown(BackKeyEvent event, Object source);
    }

    public class BackKeyEvent {

    }
}
