package com.panacea.RufusPyramid.common;

import com.panacea.RufusPyramid.game.creatures.Enemy;
import com.panacea.RufusPyramid.game.creatures.ICreature;
import com.panacea.RufusPyramid.game.creatures.Stats;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by gioele.masini on 10/10/2015.
 */
public class StaticDataProvider {
    private static final StaticDataProvider SINGLETON = new StaticDataProvider();
    private static Database gameDb;

    public static void setDatabase(Database gameDb) {
        StaticDataProvider.gameDb = gameDb;
    }

    /**
     * Fornisce la creatura richiesta con le stats generate casualmente in base ai parametri.
     * Ricordarsi di impostarle immediatamente.
     *
     * @param type Il tipo di nemico richiesto
     * @return Il nemico richiesto con le stats a null, se non viene trovato restituisce null
     */
    public static Enemy getEnemy(ICreature.CreatureType type) {
        Enemy requested = null;
        Database.Result res = gameDb.query("SELECT * FROM Enemies WHERE Type = '" + type.name()+ "'");
        if (!res.isEmpty() && res.moveToNext()) {
            requested = new Enemy(
                    res.getString(res.getColumnIndex("Name")),
                    res.getString(res.getColumnIndex("Description")),
                    Stats.generateRandom(
                            res.getInt(res.getColumnIndex("HpMin")),
                            res.getInt(res.getColumnIndex("HpMax")),
                            res.getFloat(res.getColumnIndex("AttackMin")),
                            res.getFloat(res.getColumnIndex("AttackMax")),
                            res.getFloat(res.getColumnIndex("DefenceMin")),
                            res.getFloat(res.getColumnIndex("DefenceMax")),
                            res.getFloat(res.getColumnIndex("SpeedMin")),
                            res.getFloat(res.getColumnIndex("SpeedMax"))
                    ),
                    type
            );
            requested.sigthLength = Utilities.randInt(
                    res.getInt(res.getColumnIndex("SightMin")),
                    res.getInt(res.getColumnIndex("SightMax"))
            );
        }

        return requested;
    }

    public static List<Enemy> listEnemies() {
        List<Enemy> list = new ArrayList<Enemy>(4);
        Database.Result res = gameDb.query("SELECT * FROM Enemies WHERE Type != \"HERO\"");
        if (!res.isEmpty()) {
            Enemy temp;
            while (res.moveToNext()) {
                temp = new Enemy(
                        res.getString(res.getColumnIndex("Name")),
                        res.getString(res.getColumnIndex("Description")),
                        null,
                        ICreature.CreatureType.valueOf(res.getString(res.getColumnIndex("Type")))
                );
                list.add(temp);
            }
        }
        return list;
    }
}
