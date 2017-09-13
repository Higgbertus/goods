package com.goods.game.Space.Ships;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Quaternion;
import com.badlogic.gdx.math.Vector3;
import com.goods.game.Space.GameObjectModelInstance;
import com.goods.game.Space.ObjectType;
import com.goods.game.Space.Ressources.Form;
import com.goods.game.Space.Ressources.RessourceType;
import com.goods.game.Space.Ressources.Storage;

/**
 * Created by Higgy on 31.08.2017.
 */

public class ShipObjectModelInstance extends GameObjectModelInstance{
    private static ObjectType oType = ObjectType.Ship;

    // Ship Settings
    private Vector3 direction, direction4Rotation;

    private GameObjectModelInstance destination;
    private boolean isMoving;
    private float shipNormalTravelSpeed, shipRotationSpeed, shipWarpTravelSpeed;
    private float accelerationPower, decelerationPower, rotationPower;

    private float mass;
    private final float speedFactor = 0.005f, rotateFactor = 10f;
    private float minWarpDistance = 50f;
   private Storage[] storages;


    public Vector3 getDirection() {
        return direction;
    }

    private void setDirection() {
        Vector3 norTarget;
        norTarget = new Vector3(destination.getPosition().cpy().sub(getPosition()));
        norTarget.nor();
        this.direction = norTarget;
        this.direction4Rotation = norTarget;
    }

    public boolean isMoving() {
        return isMoving;
    }

    public void setMoving(boolean moving) {
        isMoving = moving;
    }

    public GameObjectModelInstance getDestination() {
        return destination;
    }

    public void setDestination(GameObjectModelInstance destination) {
        this.destination = destination;
        setDirection();
    }

    private float calculateTravelSpeed(){
        float newSpeed;
        float distanceFactor, accelerationDecelerationFactor;
        if (canWarp()){
            // distance > minWarpDistance
            newSpeed = shipWarpTravelSpeed;
        }else{
            if (hasReachedDestination()){
                // distance < orbitDistance
                newSpeed = 0;
            }else{
                // distance between 10 and 50
                
                // Thrust calculation

                // Distance calculation
                distanceFactor = (shipNormalTravelSpeed* (getDistanceToDestination()- destination.getOrbitDistance()))/minWarpDistance;
                newSpeed = distanceFactor +10;
            }
        }
        return newSpeed * speedFactor;
    }

    private float calculateRotationSpeed(){
        return 0.1f;
    }

    public void rotateToTarget() {
        //check if ship reached rotation point

        Quaternion q = new Quaternion();
        Matrix4 mtx = new Matrix4();
        mtx.rotate(direction4Rotation, Vector3.Y);
        mtx.inv();
        q.setFromMatrix(mtx);
        setRotation(q);
        this.updateTransform();
//
//        Vector3 newDirection = new Vector3();
//        newDirection
//        newDirection.scl(calculateRotationSpeed());
//
//        Quaternion q = new Quaternion();
//        Matrix4 mtx = new Matrix4();
//        mtx.rotate(newDirection, Vector3.Y);
//        mtx.inv();
//        q.setFromMatrix(mtx);
//        setRotation(q);
//        this.updateTransform();
    }



    // notwendig?
    public void dockToTarget(Vector3 positionTarget, Vector3 scaleTarget, Quaternion rotationTarget){

    }

    private boolean isInDirection(){
        return false;
    }

    public void moveShip(GameObjectModelInstance target){
        setDestination(target);
        rotateToTarget();
        moveToTarget();
    }

    private void moveToTarget() {
        float speed = calculateTravelSpeed();
        Vector3 tmp = getPosition();
        tmp.add(new Vector3(direction.x*speed,direction.y*speed,direction.z*speed));
        this.setPosition(tmp);
        this.updateTransform();
    }

    private float getDistanceToDestination(){
        Vector3 pos = this.getPosition();
        float dist  = pos.dst(destination.getPosition());
        return dist;
    }

    // TODO: 07.09.2017 warp nur wenn rotation auf target ausgerichtet ist und es ausserhalb eines "raumes" ist
    private boolean canWarp(){
        if (getDistanceToDestination()> 50){
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
        if (getDistanceToDestination()< destination.getOrbitDistance()){
            return true;
        }else{
            return false;
        }
    }

    // Helper
    public ShipObjectModelInstance(Model model, float size, float shipNormalTravelSpeed, float shipWarpTravelSpeed, float shipRotationSpeed, float accelerationPower, float decelerationPower, float rotationPower) {
        super(model, size, oType);
        this.shipNormalTravelSpeed = shipNormalTravelSpeed;
        this.shipRotationSpeed = shipRotationSpeed;
        this.shipWarpTravelSpeed = shipWarpTravelSpeed;
        this.decelerationPower = decelerationPower;
        this.accelerationPower = accelerationPower;
        this.rotationPower = rotationPower;
        this.storages = new Storage[4];
    }

    public void setStorageType(int storage, Form form, float storageSize){
        storages[storage] = new Storage(form,storageSize);
    }

    public float getAmountOfStorage(int storage){
        return storages[storage].getCurrentAmount();
    }

    // TODO: 07.09.2017 load und unload zuerst storages mit wenig resourcen leer machen
    public float loadRessource(RessourceType type, float amount){
        for (int i = 0; i < storages.length; i++) {
            amount = storages[i].load(type,amount);
        }
        return amount;

    }

    public float unloadRessource(RessourceType type, float amount){
        for (int i = 0; i < storages.length; i++) {
            amount = storages[i].unload(type,amount);
        }
        return amount;
    }

    public int getStoragesAmount(){
       return storages.length;
    }

    @Override
    public String toString() {
        return oType.name()+super.getId()+"; Pos:"+this.getPosition()+"; Target: "+destination.toString();
    }

}
