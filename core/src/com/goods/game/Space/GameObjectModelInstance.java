package com.goods.game.Space;

import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;

import java.util.ArrayList;

/**
 * Created by Higgy on 01.09.2017.
 */

public class GameObjectModelInstance extends ModelInstance {

    public final Vector3 center = new Vector3();
    public final Vector3 dimensions = new Vector3();
    public final float radius;
    private Vector3 origin = new Vector3();
    private final static BoundingBox bounds = new BoundingBox();
    private Vector3 center1 = new Vector3();
    // GameObjectModelInstance Settings
    private final int sizeFactor = 10;
    private float size;
    private int mass;
    private double surfaceArea;
    protected double volume;
    private String name;
    private static int id;
    private boolean isAlive, hasChildObjects =false;
    private float selfRotationSpeed = 0.4f;
    private Vector3 rotation;

    public float getSelfRotationSpeed() {
        return selfRotationSpeed;
    }

    public void setSelfRotationSpeed(float selfRotationSpeed) {
        this.selfRotationSpeed = selfRotationSpeed;
    }

    public float getOrbitRotationSpeed() {
        return orbitRotationSpeed;
    }



    private float orbitRotationSpeed = 4f;
    public boolean hasChildObjects() {
        return hasChildObjects;
    }

// Planet Environment

    // Helper
    private Vector3 position;

    public Vector3 getParentPosition() {
        return parentPosition;
    }

    public void setParentPosition(Vector3 parentPosition) {
        this.parentPosition = parentPosition;
    }

    private Vector3 parentPosition;
    private ArrayList<GameObjectModelInstance> orbit;

    public Vector3 getRotation() {
        return rotation;
    }

    public GameObjectModelInstance(Model model, float size, ObjectType type) {
        super(model);
        orbit = new ArrayList<GameObjectModelInstance>();
        isAlive = true;
        calculateBoundingBox(bounds);
        bounds.getCenter(center);
        bounds.getDimensions(dimensions);
        radius = dimensions.len() / 2f;
        this.transform.getScale(origin);
        id++;
        this.name = type.name()+id;
        this.size = size;
        volume = ((4 * MathUtils.PI * Math.pow(size*sizeFactor,3))/3);
        surfaceArea = 4 * MathUtils.PI * Math.pow(size*sizeFactor,2);
        position = new Vector3();
        int a,b;
        do{
            a = MathUtils.random(1);
            b = MathUtils.random(1);
        }while(a+b==0);
        rotation = new Vector3(0,a,b);
    }

    private float distanceToStar(){
        return this.getParentPosition().dst(this.getPos());
    }

    public void setOrbitRotationSpeed() {
        orbitRotationSpeed = 10f / distanceToStar();
    }

    public Vector3 getCenter() {
        return center;
    }

    public Vector3 getPos() {
        return position.cpy();
    }

    public void setPos(Vector3 position) {
        this.position = position;
    }

    public void addObjectToOrbit(GameObjectModelInstance newObject){
        orbit.add(newObject);
        hasChildObjects = true;
    }
    public void removeObjectFromOrbit(GameObjectModelInstance newObject){
        orbit.remove(newObject);
    }

    public ArrayList<GameObjectModelInstance> getOrbitObjects(){
       return orbit;
    }

    protected float getSize() {
        return size;
    }

    protected void setSize(float size) {
        this.size = size;
    }

    public static int getId() {
        return id;
    }

    @Override
    public String toString() {
        return name+"; "+"Pos:"+position.toString()+"; Size:"+size;
    }


}
