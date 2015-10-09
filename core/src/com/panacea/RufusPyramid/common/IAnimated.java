package com.panacea.RufusPyramid.common;

import com.panacea.RufusPyramid.game.view.animations.AnimationEndedListener;

/**
 * Interfaccia da implementare quando una classe renderizza direttamente o indirettamente
 * (mediante un'altra classe, di view) un'animazione. Richiede l'implementazione di un
 * metodo che permette l'aggiunta di un listener alla fine dell'animazione.
 *
 * Created by gio on 05/10/15.
 */
public interface IAnimated {
    public void addAnimationEndedListener(AnimationEndedListener listener);
}
