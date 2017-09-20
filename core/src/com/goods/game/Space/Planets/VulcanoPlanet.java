package com.goods.game.Space.Planets;

import com.badlogic.gdx.graphics.g3d.Model;
import com.goods.game.Space.Ressources.RessourceObject;
import com.goods.game.Space.Ressources.RessourceType;

/**
 * Created by Higgy on 29.08.2017.
 */

public class VulcanoPlanet extends PlanetObjectModelInstance {
    private static PlanetType pType = PlanetType.Vulcano;
    public VulcanoPlanet(Model model, float size, float ressourceDepositeFactor) {
        super(model, size, pType, "Magma", ressourceDepositeFactor, false, false, true);
        createRessources();
    }

    private void createRessources(){
        addRessources(new RessourceObject(RessourceType.Metal,0.3));
        addRessources(new RessourceObject(RessourceType.Crystal,0.4));
        addRessources(new RessourceObject(RessourceType.Gas,0.6));
    }
}
