package com.panacea.RufusPyramid.common;

/**
 * Created by gio on 18/07/15.
 */
public interface AttributeChangeListener<T extends AttributeChangeEvent> {
    void changed(T event, Object source);
}
