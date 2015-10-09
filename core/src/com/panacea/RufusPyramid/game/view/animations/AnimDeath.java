package com.panacea.RufusPyramid.game.view.animations;

import com.panacea.RufusPyramid.game.creatures.ICreature;
import com.panacea.RufusPyramid.game.view.SpritesProvider;

/**
 * Classe che permette di effettuare l'animazione di una camminata.
 * <p/>
 * Created by gio on 11/07/15.
 */
public class AnimDeath extends AbstractCreatureAnimation {

    public AnimDeath(ICreature creature, AnimationData data) {
        super(creature, data, SpritesProvider.Azione.DEATH);
    }
}
