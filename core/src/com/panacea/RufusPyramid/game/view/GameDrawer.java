package com.panacea.RufusPyramid.game.view;

import com.badlogic.gdx.math.Vector2;
import com.panacea.RufusPyramid.game.GameModel;
import com.panacea.RufusPyramid.game.creatures.DefaultHero;
import com.panacea.RufusPyramid.game.view.ui.UIDrawer;

import java.util.LinkedList;
import java.util.List;

/**
 * Contiene tutti gli oggetti da renderizzare a video durante una sessione di gioco.
 * E' eventualmente possibile aggiungerne e rimuoverne di altri.
 * Created by gio on 11/07/15.
 */
public class GameDrawer extends ViewObject {
    private static GameDrawer SINGLETON = new GameDrawer();
    private transient final UIDrawer uiDrawer;

    private List<com.panacea.RufusPyramid.game.view.ViewObject> viewList;
    private MapDrawer mapDrawer;
    private CreaturesDrawer creaturesDrawer;
    private ItemsDrawer itemsDrawer;

    public static GameDrawer get() {
        return GameDrawer.SINGLETON;
    }
    public static void reset() { GameDrawer.SINGLETON = new GameDrawer(); }

    private GameDrawer() {
        this.mapDrawer = new MapDrawer(GameModel.get().getCurrentMap());  //map.create richiamato automaticamente da ViewObject
        this.viewList = new LinkedList<com.panacea.RufusPyramid.game.view.ViewObject>();
        this.viewList.add(this.mapDrawer);

        this.itemsDrawer = new ItemsDrawer(GameModel.get().getItems());
        this.viewList.add(this.itemsDrawer);

//        this.viewList.add(new Animator());
        this.creaturesDrawer = new CreaturesDrawer(GameModel.get().getCreatures());
        this.viewList.add(this.creaturesDrawer);

        this.uiDrawer = new UIDrawer();
        this.viewList.add(this.uiDrawer);
    }

    public void add(com.panacea.RufusPyramid.game.view.ViewObject toAdd) {
        this.viewList.add(toAdd);
    }

    public void remove(com.panacea.RufusPyramid.game.view.ViewObject toRemove) {
        this.viewList.remove(toRemove);
    }

    @Override
    public void create() {
        for (com.panacea.RufusPyramid.game.view.ViewObject view : viewList) {
            view.create();
        }
    }

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);

        for (com.panacea.RufusPyramid.game.view.ViewObject view : viewList) {
            view.resize(width, height);
        }
    }

//    int i=0;
    @Override
    public void render(float delta) {
        super.render(delta);

        if (GameModel.get() == null) {
            this.dispose();
            return;
        }

        DefaultHero hero = GameModel.get().getHero();
        GameCamera.get().position.set(new Vector2(hero.getAbsoluteTickPosition().x, hero.getAbsoluteTickPosition().y), 0); /*Update the camera based on the hero position*/

        GameCamera.get().update();
        GameBatch.get().setProjectionMatrix(GameCamera.get().combined);

        for (com.panacea.RufusPyramid.game.view.ViewObject view : viewList) {
            view.render(delta);
        }

       /* if(i == 100) {
            System.out.println(Gdx.app.getJavaHeap() / 1024);
            System.out.println(Gdx.app.getNativeHeap() / 1024);
            i=0;
        }i++;
        */

        if(hero.getFirstTarget() != null)
           this.uiDrawer.getAttackButton().setDisabled(false);
        else
            this.uiDrawer.getAttackButton().setDisabled(true);

        float valuePercentage = (hero.getHPCurrent() * 100)  / hero.getBaseStats().getMaximumHP();
        uiDrawer.setHealthBarValue(valuePercentage);
    }

    @Override
    public void pause() {
        super.pause();

        for (com.panacea.RufusPyramid.game.view.ViewObject view : viewList) {
            view.pause();
        }
    }

    @Override
    public void resume() {
        super.resume();

        for (com.panacea.RufusPyramid.game.view.ViewObject view : viewList) {
            view.resume();
        }
    }

    @Override
    public void dispose() {
        super.dispose();

        for (com.panacea.RufusPyramid.game.view.ViewObject view : viewList) {
            view.dispose();
        }
    }

    public MapDrawer getMapDrawer() {
        return this.mapDrawer;
    }

    public CreaturesDrawer getCreaturesDrawer() {
        return this.creaturesDrawer;
    }

    public UIDrawer getUIDrawer(){ return this.uiDrawer;}

    public ItemsDrawer getItemsDrawer() {
        return this.itemsDrawer;
    }
}
