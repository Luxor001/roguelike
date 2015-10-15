package com.panacea.RufusPyramid.game.view.animations;

import com.panacea.RufusPyramid.game.creatures.ICreature;
import com.panacea.RufusPyramid.game.view.MusicPlayer;
import com.panacea.RufusPyramid.game.view.SpritesProvider;

/**
 * Classe che permette di effettuare l'animazione di una camminata.
 * <p/>
 * Created by gio on 11/07/15.
 */
public class AnimStrike extends AbstractCreatureAnimation {

    public AnimStrike(ICreature creature, AnimationData data) {
        super(creature, data, SpritesProvider.Azione.STRIKE);
    }

    public void create() {
        super.create();
        this.setAnimationSound(MusicPlayer.SoundType.STRIKE);
    }
}
