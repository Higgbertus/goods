package com.goods.game.Space.Planets;

import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.math.Vector3;
import com.goods.game.Space.GameObjectModelInstance;
import com.goods.game.Space.ObjectType;
import com.goods.game.Space.Ressources.RessourceObject;

import java.util.ArrayList;

/**
 * Created by Higgy on 31.08.2017.
 */

public class PlanetObjectModelInstance extends GameObjectModelInstance{

    private static ObjectType oType = ObjectType.Planet;

    // Planet Settings
    private int buildingSpaces;
    private PlanetType type;
    // Factor to calculate the RessourceObject per Size
    private float ressourceDepositeFactor;

    // Planet Environment
    private boolean isColonizable, hasLifes, isHostileEnvironment;
    private int maxLifeSize;
    private ArrayList<RessourceObject> ressources;

    // Helper
    private Vector3 position;


    public PlanetObjectModelInstance(Model model, float size, PlanetType type, String name, float ressourceDepositeFactor, boolean isColonizable, boolean hasLifes, boolean isHostileEnvironment) {
        super(model, size, oType, name);
        ressources = new ArrayList<RessourceObject>();
        this.ressourceDepositeFactor = ressourceDepositeFactor;
        this.isColonizable= isColonizable;
        this.hasLifes = hasLifes;
        this.isHostileEnvironment = isHostileEnvironment;
        this.type = type;
    }
    public ArrayList<RessourceObject> getRessources() {
        return ressources;
    }

    public void addRessources(RessourceObject ressources) {
        this.ressources.add(ressources);
        calculateRessourceDistribution();
    }

    private void calculateRessourceDistribution() {
        for (int i = 0; i < ressources.size(); i++) {
            ressources.get(i).setDeposite((volume / ressources.size()) * ressourceDepositeFactor);
        }

    }

    public void setType(PlanetType type) {
        this.type = type;
    }
    protected float getRessourceDepositeFactor() {
        return ressourceDepositeFactor;
    }


    @Override
    public String toString() {
        return type.name()+super.getId();
    }
}
