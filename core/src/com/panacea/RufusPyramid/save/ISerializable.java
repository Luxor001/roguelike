package com.panacea.RufusPyramid.save;

import com.esotericsoftware.kryo.Serializer;

/**
 * Created by gio on 17/10/15.
 */
public interface ISerializable<T> {
    public Serializer<T> getSerializer();
}
