package com.panacea.RufusPyramid.common;

/**
 * Created by gio on 18/07/15.
 */
public class AttributeChangeEvent<T> {

    private T newAttributeValue;

    public AttributeChangeEvent(T newAttributeValue) {
        this.newAttributeValue = newAttributeValue;
    }

    public T getValue() {
        return this.newAttributeValue;
    }

}
