package com.panacea.RufusPyramid.common;

import com.badlogic.gdx.InputMultiplexer;

/**
 * Created by gioele.masini on 06/07/2015.
 */
public class InputManager extends InputMultiplexer {
    private static InputManager SINGLETON = new InputManager();

    private InputManager() { };

    public static InputManager getInstance() {
        return InputManager.SINGLETON;
    }
}
