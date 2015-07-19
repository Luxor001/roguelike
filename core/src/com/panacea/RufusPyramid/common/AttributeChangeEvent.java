package com.panacea.RufusPyramid.common;

import com.badlogic.gdx.scenes.scene2d.Event;

/**
 * Created by gio on 18/07/15.
 */
public class AttributeChangeEvent<T> {

    private T newAttributeValue;
//    private Object source;

    public AttributeChangeEvent(T newAttributeValue) {
        this.newAttributeValue = newAttributeValue;
    }

//    public AttributeChangeEvent(T newAttributeValue, Object source) {
//        this.newAttributeValue = newAttributeValue;
//        this.source = source;
//    }

    public T getValue() {
        return this.newAttributeValue;
    }

//    public Object getSource() {
//        return this.source;
//    }
}
