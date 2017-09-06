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
        Vector3 dest = new Vector3(destination);
        position = new Vector3();
        dest.sub(this.transform.getTranslation(position));
        dest.nor();
        this.direction = dest;
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
    private static Vector3 tmpV = new Vector3();
    private static Quaternion tmpQ = new Quaternion();

    public Vector3 velocity = new Vector3();
    public Vector3 acceleration = new Vector3();
    public Quaternion rotation = new Quaternion();
    private static final float friction = -1.0f;
    int a = 1;
    public void rotateToTarget(Vector3 target){
        //this.transform.rotate(0,1,0,2);
        //this.transform.setFromEulerAngles(1,0,0);

        // check if ship reached rotation point
Vector3 newPos = new Vector3(100,10,50);
            Quaternion actualRotation = new Quaternion();
            //this.transform.getRotation(actualRotation,false);

            Vector3 norPos, norTarget, pos;
        pos = new Vector3();
            norPos = new Vector3(pos);
            norTarget = new Vector3(target.sub(newPos));

            this.transform.getTranslation(pos);
            norPos.nor();
            norTarget.nor();

        //norPos.dot(norTarget);
        Matrix4 mtx = new Matrix4();
        //mtx.translate(100,0,0);
//mtx.translate(target);
        mtx.rotate(norTarget,Vector3.Y);
        mtx.inv();
        mtx.setTranslation(100,0,0);
        actualRotation.setFromMatrix(false,mtx);
       //mtx.translate(100,0,0);
       // mtx.setTranslation(50,0,0);





        // richtet heck nach direction
        // TODO: 05.09.2017 richtet es aus...
       // this.transform.setToRotation(norTarget,Vector3.X);
//        Matrix4 mtx = new Matrix4();
//        mtx.rotate(norTarget,Vector3.Y);
//        mtx.inv();
        //this.transform.set(mtx);
        //this.transform.set(actualRotation);
        this.transform.set(mtx);


//        if (pos.hasOppositeDirection(target)){
//
//        }else {
//
//            this.transform.rotate(pos,pos.dot(target));
//        }
        //this.transform.rotate(actualRotation);

        // isInDirection();


//        Vector3 direction = new Vector3(target);
//        direction.nor();
//        Vector3 planetOffset = new Vector3();
//        this.transform.getTranslation(planetOffset);
//        Matrix4 transform = new Matrix4();
//        Matrix4 tmp = new Matrix4();
//        transform.setTranslation(planetOffset);
//        direction.scl(shipRotationSpeed);
//        tmp.setFromEulerAngles(direction.x*shipRotationSpeed,direction.y*shipRotationSpeed,direction.z*shipRotationSpeed);
//        transform.mul(tmp);
//        this.transform.set(transform);
    }

    // gute position:
//    [0.7547174|0.6560507|0.0|700.0]
//    [-0.6560507|0.7547174|0.0|700.0]
//    [0.0|0.0|1.0|0.0]
//    [0.0|0.0|0.0|1.0]


    private boolean isInDirection(){


return false;


    }

    public void moveShip(){
        //this.transform.translate(direction.x,direction.y,direction.z).scl(shipTravelSpeed);
       // direction.scl(1.1f);
        rotateToTarget(destination);
        float speed = calcSpeedperDistance();
        Vector3 tmp = new Vector3(direction.x*speed,direction.y*speed,direction.z*speed);
        this.transform.translate(tmp.x,tmp.y,tmp.z);
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
