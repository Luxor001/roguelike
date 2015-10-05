package com.panacea.RufusPyramid.game.creatures;

import com.badlogic.gdx.Gdx;
import com.panacea.RufusPyramid.common.AttributeChangeEvent;

/**
 * Created by gio on 27/07/15.
 */
public class CreatureDeadEvent extends AttributeChangeEvent<Integer> {
    public CreatureDeadEvent(Integer hp) {
        super(hp);

        if (hp > 0) {
            // Almeno per ora! (In alcuni giochi le creature muoiono anche se avrebbero ancora
            // diversi hp ma per caso o fatalità subiscono danni irreversibili, come testa mozzata,
            // trasformazione in pietra, fame e stenti, etc.)
            Gdx.app.error(this.getClass().toString(), "Una creatura ha lanciato l'evento con ancora " + hp + " hp!! So bad!");
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
}
