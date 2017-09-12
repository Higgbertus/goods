package com.goods.game.Space.Planets;

import com.badlogic.gdx.graphics.g3d.Model;
import com.goods.game.Space.Ressources.RessourceObject;
import com.goods.game.Space.Ressources.RessourceType;

/**
 * Created by Higgy on 29.08.2017.
 */

public class GasPlanet extends PlanetObjectModelInstance {
    private static PlanetType pType = PlanetType.Gas;
    public GasPlanet(Model model, float size, float ressourceDepositeFactor) {
        super(model, size, pType, ressourceDepositeFactor, false, false, true);
        createRessources();
    }

    private void createRessources(){
        addRessources(new RessourceObject(RessourceType.Gas, 1));
    }
}
