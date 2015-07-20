package com.panacea.RufusPyramid.view.animations;

/**
 * Created by gio on 20/07/15.
 */
public interface IListenableToTheEnd {
    void addListener(AnimationEndedListener listener);

    void fireAnimationEndedEvent(AnimationEndedEvent event);
}
