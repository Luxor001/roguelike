package com.panacea.RufusPyramid.game.creatures;

import com.badlogic.gdx.Gdx;
import com.panacea.RufusPyramid.common.AttributeChangeEvent;
import com.panacea.RufusPyramid.common.IAnimated;
import com.panacea.RufusPyramid.game.view.animations.AnimationEndedEvent;
import com.panacea.RufusPyramid.game.view.animations.AnimationEndedListener;
import com.panacea.RufusPyramid.game.view.animations.IAnimationEndListenable;

import java.util.ArrayList;

/**
 * Created by gio on 27/07/15.
 */
public class CreatureDeadEvent extends AttributeChangeEvent<Integer> implements IAnimated {
    private ArrayList<AnimationEndedListener> listeners;




    /**
     *
     * (Eventuali diversi approcci:
     * - l'evento contiene l'animazione, richiedibile con una get. Ma mi sembra brutto.
     * - Il listener all'evento viene passato dall'esterno. In questo modo l'evento non contiene
     *      mai direttamente l'animazione.
     *
     * @param hp
     * @param animDeath
     */
    public CreatureDeadEvent(Integer hp, IAnimationEndListenable animDeath) {
        super(hp);
        this.listeners = new ArrayList<AnimationEndedListener>();

        if (hp > 0) {
            // Almeno per ora! (In alcuni giochi le creature muoiono anche se avrebbero ancora
            // diversi hp ma per caso o fatalità subiscono danni irreversibili, come testa mozzata,
            // trasformazione in pietra, fame e stenti, etc.)
            Gdx.app.error(this.getClass().toString(), "Una creatura ha lanciato l'evento con ancora " + hp + " hp!! So bad!");
        }

        if (animDeath != null) {
            animDeath.addListener(new AnimationEndedListener() {
                @Override
                public void ended(AnimationEndedEvent event, Object source) {
                    CreatureDeadEvent.this.fireAnimationEndedEvent();
                }
            });
        }
    }

    /**
     * Ritorna il numero di hp rimanenti alla creatura.
     * (In futuro una creatura potrebbe morire perchè le è stata mozzata la testa
     * nonostante non abbia ancora perso tutti gli hp)
     * @return Gli hp rimanenti alla creatura morta.
     */
    @Override
    public Integer getValue() {
        return super.getValue();
    }

    /* Gestisco l'animazione di morte */
    @Override
    public void addAnimationEndedListener(AnimationEndedListener listener) {
        this.listeners.add(listener);
    }

    private void fireAnimationEndedEvent() {
        for (AnimationEndedListener listener : this.listeners) {
            listener.ended(new AnimationEndedEvent(), this);
        }
    }
}
