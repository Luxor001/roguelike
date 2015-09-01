package com.panacea.RufusPyramid.game.view.animations;

/**
 * Created by gio on 20/07/15.
 */
public interface IAnimationEndListenable {
    void addListener(AnimationEndedListener listener);

    void fireAnimationEndedEvent(com.panacea.RufusPyramid.game.view.animations.AnimationEndedEvent event);
}
