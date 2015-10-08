package com.panacea.RufusPyramid.game.creatures;

import com.badlogic.gdx.math.Vector2;
import com.panacea.RufusPyramid.game.Effect.Effect;
import com.panacea.RufusPyramid.game.actions.ActionResult;
import com.panacea.RufusPyramid.game.actions.IAgent;
import com.panacea.RufusPyramid.game.view.ui.HealthBar;
import com.panacea.RufusPyramid.map.Tile;

import java.util.List;

/**
 * General creature interface.
 */
public interface ICreature extends IAgent {

    enum CreatureType{
        HERO,
        ORC,
        SKELETON,
        UGLYYETI,
        WRAITH
    }

    public int getID();

    public int getHPCurrent();
    public void setHPCurrent(int currentHP);

    public String getName();
    public void setName(String name);

    public String getDescription();
    public void setDescription(String description);

    public Tile getPosition();
    public void setPosition(Tile currentPosition);

    public int getEnergy();
    public void setEnergy(int currentEnergy);

    List<Effect> getEffects();
    void addEffect(Effect effect);
    void addEffects(List<Effect> effect);

    public Vector2 getAbsoluteTickPosition();
    public void setAbsoluteTickPosition(Vector2 position);
    public HealthBar getHealthBar();
    public void setHealthBar(HealthBar bar);
    /**
     * Questo metodo è l'unico a poter lanciare un ActionChosenEvent.
     * Nel caso di creatura gestita dall'AI deve scegliere autonomamente quale azione effettuar
     * e richimarci sopra il .perform()
     * In caso di creatura gestita da utente deve attendere un input, scegliere di conseguenza
     * quale azione eseguire e richiamare il .perform.
     * In caso di successo dell'azione il metodo deve SEMPRE lanciare un ActionChosenEvent per permettere
     * al GameMaster di gestire la turnazione.
     * @param resultPreviousAction
     */
    public void chooseNextAction(ActionResult resultPreviousAction);

    public void addCreatureDeadListener(CreatureDeadListener listener);

    public Backpack getEquipment();

    public Stats getBaseStats();

    public Stats getCurrentStats();

    public boolean getFlipX();
    public void setFlipX(boolean flipX);

    public CreatureType getCreatureType();
    public void setCreatureType(CreatureType type);
}
