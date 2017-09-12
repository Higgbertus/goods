package com.goods.game.Space.Planets;

import com.badlogic.gdx.graphics.g3d.Model;
import com.goods.game.Space.Ressources.RessourceObject;
import com.goods.game.Space.Ressources.RessourceType;

/**
 * Created by Higgy on 29.08.2017.
 */

public class TerraPlanet extends PlanetObjectModelInstance {
    private static PlanetType pType = PlanetType.Terrastic;
    public TerraPlanet(Model model, float size, float ressourceDepositeFactor) {
        super(model, size, pType, ressourceDepositeFactor, false,false,true);
        createRessources();
    }

    private void createRessources(){
        addRessources(new RessourceObject(RessourceType.Metal,0.3));
        addRessources(new RessourceObject(RessourceType.Crystal,0.1));
        addRessources(new RessourceObject(RessourceType.Water,0.5));
        addRessources(new RessourceObject(RessourceType.Gas,0.1));
    }
}
