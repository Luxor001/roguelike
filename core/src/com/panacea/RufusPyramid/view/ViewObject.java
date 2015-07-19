package com.panacea.RufusPyramid.view;

import com.badlogic.gdx.ApplicationListener;

/**
 * Classe da estendere per creare oggetti renderizzabili.
 * Contiene tutti i metodi utili allo scopo, fare override di quelli necessari.
 * Created by gioele.masini on 06/07/2015.
 */
public class ViewObject implements ApplicationListener {
    //TODO rimuovere l'implements di ApplicationListener?

    public ViewObject() {
        this.create();
    }

    /**
     * Non è da chiamare! Viene richiamato automaticamente con il costruttore
     */
    @Override
    public void create() {

    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void render() {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void dispose() {

    }
}
