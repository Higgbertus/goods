package com.goods.game.Space.Ships;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Quaternion;
import com.badlogic.gdx.math.Vector3;
import com.goods.game.Space.GameObjectModelInstance;
import com.goods.game.Space.ObjectType;
import com.goods.game.Space.Planets.PlanetObjectModelInstance;

/**
 * Created by Higgy on 31.08.2017.
 */

public class ShipObjectModelInstance extends GameObjectModelInstance{
    private static ObjectType oType = ObjectType.Ship;

    // Ship Settings
    private Vector3 direction;
    private Vector3 destination;
    private boolean isMoving;
    private float shipNormalTravelSpeed, shipRotationSpeed, shipWarpTravelSpeed;
    private final float speedFactor = 0.01f, rotateFactor = 10f;
    private Vector3 position;
    private final float minWarpDistance = 50f, stopDistance = 10f;


     //   shipObjectModelInstance.setDirection((new Vector3(300,100,0).sub(200,150,0)).nor());

    public Vector3 getDirection() {
        return direction;
    }

    private void setDirection() {
        Vector3 shipPos = new Vector3();
        this.transform.getTranslation(shipPos);
        Vector3 norTarget;
        norTarget = new Vector3(destination.sub(shipPos));
        norTarget.nor();
        this.direction = norTarget;
    }

    public boolean isMoving() {
        return isMoving;
    }

    public void setMoving(boolean moving) {
        isMoving = moving;
    }

    public Vector3 getDestination() {
        return destination;
    }

    public void setDestination(Vector3 destination) {
        this.destination = destination;
        setDirection();
    }

    private float calcSpeedperDistance(){
        float newSpeed;
        if (canWarp()){
            // distance > minWarpDistance
            newSpeed = shipWarpTravelSpeed;
        }else{
            if (hasReachedDestination()){
                // distance < stopDistance
                newSpeed = 0;
            }else{
                // distance between 10 and 50
                newSpeed = (shipNormalTravelSpeed* (getDistanceToDestination()-stopDistance))/minWarpDistance;
            }
        }
        return newSpeed * speedFactor;
    }

    public void rotateToTarget() {
        //check if ship reached rotation point

Quaternion q = new Quaternion();
        Matrix4 mtx = new Matrix4();
        mtx.rotate(direction, Vector3.Y);
        mtx.inv();
        //mtx.setTranslation(100, 100, 0);
//        q.setFromMatrix(mtx);
//        this.updateTransform();


       this.transform.set(mtx);
    }

    private boolean isInDirection(){
        return false;
    }

    public void moveShip(Vector3 target){
        setDestination(target);
        rotateToTarget();
       // moveToTarget();
    }

    private void moveToTarget() {
       // direction.scl(1.1f);
        float speed = calcSpeedperDistance();
        Vector3 tmp = new Vector3(direction.x*speed,direction.y*speed,direction.z*speed);
        this.transform.setTranslation(tmp.x,tmp.y,tmp.z);

    }

    private float getDistanceToDestination(){
        return this.transform.getTranslation(new Vector3()).dst(destination);
    }

    private boolean canWarp(){
        if (getDistanceToDestination()> minWarpDistance){
            Material mat = this.materials.get(0);
            mat.clear();
            mat.set(new Material(ColorAttribute.createDiffuse(Color.LIME)));
            return true;
        }else{
            Material mat = this.materials.get(0);
            mat.clear();
            mat.set(new Material(ColorAttribute.createDiffuse(Color.GRAY)));
            return false;
        }
    }

    public boolean hasReachedDestination(){
        if (getDistanceToDestination()< stopDistance){
            return true;
        }else{
            return false;
        }
    }

    // Helper
    public ShipObjectModelInstance(Model model, float size, float shipNormalTravelSpeed, float shipWarpTravelSpeed, float shipRotationSpeed) {
        super(model, size, oType);
        this.shipNormalTravelSpeed = shipNormalTravelSpeed;
        this.shipRotationSpeed = shipRotationSpeed;
        this.shipWarpTravelSpeed = shipWarpTravelSpeed;
    }

    @Override
    public String toString() {
        return oType.name()+super.getId()+"; Pos:"+this.transform.getTranslation(new Vector3());
    }

}
