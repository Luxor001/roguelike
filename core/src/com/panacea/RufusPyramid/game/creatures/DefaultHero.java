package com.panacea.RufusPyramid.game.creatures;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.panacea.RufusPyramid.game.GameModel;
import com.panacea.RufusPyramid.game.actions.ActionResult;
import com.panacea.RufusPyramid.game.view.ui.HealthBar;
import com.panacea.RufusPyramid.map.Tile;


/**
 * Created by gio on 09/07/15.
 */
public class DefaultHero extends AbstractCreature {

    private static final String DESCRIPTION = "The hero of the game.";
    private static final int MAX_HP = 25;
    private static final double ATTACK = 5;
    private static final double DEFENCE = 5;
    private static final double SPEED = 5;
    public HealthBar health;

//    TODO aggiungere il livello con l'oggetto Level
    public DefaultHero(String name) {
        super(name, DESCRIPTION, MAX_HP, ATTACK, DEFENCE, SPEED);
        //TODO la posizione di spawn va presa dalla mappa;
    }

    @Override
    public void chooseNextAction(ActionResult resultPreviousAction) {
        //TODO il giocatore ora è libero di impartire ordini al proprio giocatore
        //TODO dal controller abilita l'input utente
        return;
    }
}
