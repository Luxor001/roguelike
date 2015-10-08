package com.panacea.RufusPyramid.game.view.animations;

import com.badlogic.gdx.math.GridPoint2;

/**
 * Created by gioele.masini on 08/10/2015.
 */
public class AnimationData {
    public final GridPoint2 position;
    public final boolean flipX;

    public AnimationData(GridPoint2 creaturePosition, boolean flipX) {
        this.flipX = flipX;
        this.position = creaturePosition;
    }
}
