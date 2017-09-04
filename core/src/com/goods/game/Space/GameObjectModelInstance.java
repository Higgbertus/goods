package com.goods.game.Space;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.Ray;

import java.util.ArrayList;

/**
 * Created by Higgy on 01.09.2017.
 */

public class GameObjectModelInstance extends ModelInstance {
public com.goods.game.Space.Shapes.Shape shape;

    private Vector3 origin = new Vector3();

    private Vector3 center1 = new Vector3();
    // GameObjectModelInstance Settings
    private final int sizeFactor = 10;
    private float size;
    private int mass;
    private double surfaceArea;
    protected double volume;
    private String name;
    private int id;
    private boolean isAlive, hasChildObjects =false;
    private float selfRotationSpeed = 0.4f;
    private ObjectType type;

    public float getSelfRotationSpeed() {
        return selfRotationSpeed;
    }

    public void setSelfRotationSpeed(float selfRotationSpeed) {
        this.selfRotationSpeed = selfRotationSpeed;
    }


    public boolean hasChildObjects() {
        return hasChildObjects;
    }

// Planet Environment

    // Helper

    public Vector3 getParentPosition() {
        return parentPosition;
    }

    public void setParentPosition(Vector3 parentPosition) {
        this.parentPosition = parentPosition;
    }

    private Vector3 parentPosition;
    private ArrayList<GameObjectModelInstance> orbit;


    public GameObjectModelInstance(Model model, float size, ObjectType type) {
        super(model);
        orbit = new ArrayList<GameObjectModelInstance>();
        isAlive = true;

        this.transform.getScale(origin);
        id++;
        this.type = type;
        this.name = type.name()+id;
        this.size = size;
        volume = ((4 * MathUtils.PI * Math.pow(size*sizeFactor,3))/3);
        surfaceArea = 4 * MathUtils.PI * Math.pow(size*sizeFactor,2);

    }

    public ObjectType getType() {
        return type;
    }

    public boolean isVisible(Camera cam) {
        return shape == null ? false : shape.isVisible(transform, cam);
    }

    /** @return -1 on no intersection, or when there is an intersection: the squared distance between the center of this
     * object and the point on the ray closest to this object when there is intersection. */
    /** @return -1 on no intersection, or when there is an intersection: the squared distance between the center of this
     * object and the point on the ray closest to this object when there is intersection. */
    public float intersects(Ray ray) {
        return shape == null ? -1f : shape.intersects(transform, ray);
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

    public int getId() {
        return id;
    }

    @Override
    public String toString() {
        return name+"; "+"Pos:"+this.transform.getTranslation(new Vector3())+"; Size:"+size;
    }


}
