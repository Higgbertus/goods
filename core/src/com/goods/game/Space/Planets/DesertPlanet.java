package com.goods.game.Space.Planets;

import com.badlogic.gdx.graphics.g3d.Model;
import com.goods.game.Space.Ressources.RessourceObject;
import com.goods.game.Space.Ressources.RessourceType;

/**
 * Created by Higgy on 29.08.2017.
 */

public class DesertPlanet extends PlanetObjectModelInstance {
    private static PlanetType pType = PlanetType.Desert;

    public DesertPlanet(Model model, float size, float ressourceDepositeFactor) {
        super(model, size, pType, "0Life", ressourceDepositeFactor, false, false, true);
        createRessources();
    }

    private void createRessources(){
        addRessources(new RessourceObject(RessourceType.Metal,0.8));
        addRessources(new RessourceObject(RessourceType.Crystal,0.3));
    }
}
