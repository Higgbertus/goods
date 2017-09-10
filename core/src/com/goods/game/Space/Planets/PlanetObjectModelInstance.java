package com.goods.game.Space.Planets;

import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Quaternion;
import com.badlogic.gdx.math.Vector3;
import com.goods.game.Space.GameObjectModelInstance;
import com.goods.game.Space.ObjectType;
import com.goods.game.Space.PlanetType;
import com.goods.game.Space.Ressources.RessourceObject;
import com.goods.game.Space.Stars.Star;

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
    private float xAngle, yAngle;
    // Helper
    Matrix4 tranfsormNEW;
    private float distanceToStar;
    public PlanetObjectModelInstance(Model model, float size, PlanetType type, float ressourceDepositeFactor, boolean isColonizable, boolean hasLifes, boolean isHostileEnvironment) {
        super(model, size, oType);
        ressources = new ArrayList<RessourceObject>();

        this.ressourceDepositeFactor = ressourceDepositeFactor;
        this.isColonizable= isColonizable;
        this.hasLifes = hasLifes;
        this.isHostileEnvironment = isHostileEnvironment;
        this.type = type;
        tranfsormNEW = new Matrix4();
    }

    @Override
    public void setPosition(Vector3 position) {
        super.setPosition(position);
    }

    // Winkel nur über eine Achse?????
    public void setAngles(float xAngle, float yAngle){
        this.xAngle = xAngle;
        this.yAngle = yAngle;
    }

    // Rotation:
    public void transform(Vector3 starPosition, float deltaTime){

        float speed = calculateOrbitSpeed(starPosition);


        Vector3 planetPos = new Vector3(this.getOriginalPosition());
        Vector3 planetOffset = new Vector3(planetPos);
        Matrix4 rotation = new Matrix4();
        // über den winkel wird die rotationsachse und geschwindikeit pro sekunde definiert efiniert
        // Positive und negative Werte möglich
        // Z muss immer 0 sein da Z auf den Stern zeigt und somit nur die Rotation des planeten ändert = Hat negative auswirkungen auf die Orbit rotation!!
        //rotation.setFromEulerAngles(361f*deltaTime,50f*deltaTime,0);
        //rotation.setFromEulerAngles(orbitRotationSpeed*,0);
        planetOffset.sub(starPosition);
        tranfsormNEW.setTranslation(starPosition);
        tranfsormNEW.mul(rotation);
        tranfsormNEW.translate(planetOffset);
        this.setPosition(tranfsormNEW.getTranslation(new Vector3()));
        this.setRotation(tranfsormNEW.getRotation(new Quaternion()));
        this.updateTransform();
    }

    private float calculateOrbitSpeed(Vector3 starPosition) {
        // Berechne Orbit Speed aus der Entfernung zum Stern, eventuell noch masse vom Planeten
       float distance = starPosition.dst(this.getPosition());
        return 10f / distance;
    }

    public float getOrbitRotationSpeed() {
        return orbitRotationSpeed;
    }

    private float distanceToStar(){
        return this.getParentPosition().dst(this.transform.getTranslation(new Vector3()));
    }

    private float orbitRotationSpeed;


    public void setOrbitRotationSpeed() {
        orbitRotationSpeed = 10f / distanceToStar;
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
        return type.name()+super.getId()+"; Pos:"+this.transform.getTranslation(new Vector3());
    }
}
