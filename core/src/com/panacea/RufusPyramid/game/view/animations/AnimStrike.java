package com.panacea.RufusPyramid.game.view.animations;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.math.GridPoint2;
import com.panacea.RufusPyramid.common.Utilities;
import com.panacea.RufusPyramid.game.creatures.DefaultHero;
import com.panacea.RufusPyramid.game.creatures.ICreature;
import com.panacea.RufusPyramid.game.view.SoundsProvider;
import com.panacea.RufusPyramid.game.view.SpritesProvider;

/**
 * Classe che permette di effettuare l'animazione di una camminata.
 * <p/>
 * Created by gio on 11/07/15.
 */
public class AnimStrike extends AbstractCreatureAnimation {

    private final ICreature.CreatureType modelType;

    public AnimStrike(ICreature creature, GridPoint2 position, boolean flipX) {
        super(creature, position, flipX, SpritesProvider.Azione.STRIKE);
        this.modelType = creature.getCreatureType();
    }

    public void create() {
        super.create();
        if (this.modelType.equals(ICreature.CreatureType.HERO)) {
            int randSound = Utilities.randInt(0, SoundsProvider.Sounds.COMBAT_SLICE.getValue() - 1);
            SoundsProvider.get().loadSound(SoundsProvider.Sounds.COMBAT_SLICE);
            this.setAnimationSound(SoundsProvider.get().getSound(SoundsProvider.Sounds.COMBAT_SLICE)[randSound]);
        }
        if (this.modelType.equals(ICreature.CreatureType.WRAITH)) {
            int randSound = Utilities.randInt(0, SoundsProvider.Sounds.COMBAT_WRATH.getValue() - 1);
            SoundsProvider.get().getSound(SoundsProvider.Sounds.COMBAT_WRATH);
            this.setAnimationSound(SoundsProvider.get().getSound(SoundsProvider.Sounds.COMBAT_WRATH)[randSound]);
            this.setSoundVolume(0.5f);
        }
        if (this.modelType.equals(ICreature.CreatureType.UGLYYETI)) {
            int randSound = Utilities.randInt(0, SoundsProvider.Sounds.COMBAT_MNSTR.getValue() - 1);
            SoundsProvider.get().getSound(SoundsProvider.Sounds.COMBAT_MNSTR);
            this.setAnimationSound(SoundsProvider.get().getSound(SoundsProvider.Sounds.COMBAT_MNSTR)[randSound]);
            this.setSoundVolume(0.5f);
        }
    }
}
