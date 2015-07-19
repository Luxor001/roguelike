package com.panacea.RufusPyramid.view;

import com.badlogic.gdx.math.GridPoint2;
import com.panacea.RufusPyramid.common.AttributeChangeEvent;
import com.panacea.RufusPyramid.creatures.AbstractCreature;
import com.panacea.RufusPyramid.creatures.DefaultHero;

import java.util.ArrayList;

/**
 * Created by gio on 16/07/15.
 */
public class HeroDrawer extends ViewObject {

    private DefaultHero heroModel;
    private AbstractCreature.PositionChangeListener posChangeListener;

    public HeroDrawer(DefaultHero heroModel) {
        this.heroModel = heroModel;
        this.posChangeListener = new AbstractCreature.PositionChangeListener() {
            @Override
            public void changed(AbstractCreature.PositionChangeEvent event, Object source) {
                HeroDrawer.this.walkAnimation((DefaultHero) source, event.getPath());
            }
        };
        this.heroModel.addChangeListener(this.posChangeListener);
    }

    private void walkAnimation(DefaultHero source, ArrayList<GridPoint2> path) {
        //TODO animate

    }
}
