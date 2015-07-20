package com.panacea.RufusPyramid.game.view.animations;

import java.util.ArrayList;

public class AbstrAnimation extends com.panacea.RufusPyramid.game.view.ViewObject implements IListenableToTheEnd {
    private ArrayList<AnimationEndedListener> endListeners;

    public AbstrAnimation() {
        super();
        this.endListeners = new ArrayList<AnimationEndedListener>();
    }

    @Override
    public void addListener(AnimationEndedListener listener) {
        this.endListeners.add(listener);
    }

    public void fireAnimationEndedEvent() {
        AnimationEndedEvent event = new AnimationEndedEvent();
        this.fireAnimationEndedEvent(event);
    }

    @Override
    public void fireAnimationEndedEvent(AnimationEndedEvent event) {
        if (this.endListeners == null) throw new NullPointerException("Lista di listeners == null. Devi prima richiamare il costruttore super! Altrimenti non inizializza le variabili.");

        for (AnimationEndedListener listener : this.endListeners) {
            listener.ended(event, this);
        }
    }
}
