package com.panacea.RufusPyramid.game.view.input;

import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;

/**
 * Created by gioele.masini on 06/07/2015.
 */
public class InputManager extends InputMultiplexer {
    private static InputManager SINGLETON = new InputManager();
    private HeroInputManager heroProcessor;

    private InputManager() { };

    public static InputManager get() {
        return InputManager.SINGLETON;
    }
    public static void reset() { InputManager.SINGLETON = new InputManager(); }

    @Override
    public void addProcessor(InputProcessor processor) {
        super.addProcessor(processor);
        if (processor instanceof HeroInputManager) {
            this.heroProcessor = (HeroInputManager)processor;
        }
    }

    public HeroInputManager getHeroProcessor() {
        return this.heroProcessor;
    }
}
