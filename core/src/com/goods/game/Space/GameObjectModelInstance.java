package com.goods.game.Space;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.utils.MeshPartBuilder;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Quaternion;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.Ray;
import com.goods.game.Space.Shapes.ObjectShape;
import com.goods.game.SpaceTrader;

import java.util.ArrayList;

/**
 * Created by Higgy on 01.09.2017.
 */

public class GameObjectModelInstance extends ModelInstance {
//   public final Vector3 position = new Vector3();
//    public final Quaternion rotation = new Quaternion();
//    public final Vector3 scale = new Vector3();

    /*
    * 3D fields
    * */
    private Vector3 position = new Vector3();
    private Quaternion rotation = new Quaternion();
    // muss am Anfang 1 sein...
    private Vector3 scale = new Vector3(1,1,1);

    /*
     * Object fields
     * */
    private ObjectShape ObjectShape;
    // GameObjectModelInstance Settings
    private final int sizeFactor = 20;
    private float size;
    private double surfaceArea;
    protected double volume;
    private String name;
    private int id;
    private boolean isAlive, hasChildObjects =false;
    private float selfRotationSpeed = 0.4f;
    private ObjectType type;

private boolean debugMode;
    private Vector3 parentPosition;
    private ArrayList<GameObjectModelInstance> orbit;
    private ModelInstance[] axes;
    private ModelBuilder modelBuilder;
    private Model model;

    public GameObjectModelInstance(Model model, float size, ObjectType type) {
        super(model);
        orbit = new ArrayList<GameObjectModelInstance>();
        modelBuilder = new ModelBuilder();
        isAlive = true;
        id++;
        this.type = type;
        this.name = type.name()+id;
        this.size = size;
        volume = ((4 * MathUtils.PI * Math.pow(size*sizeFactor,3))/3);
        surfaceArea = 4 * MathUtils.PI * Math.pow(size*sizeFactor,2);
        if (SpaceTrader.debugMode){
            axes = new ModelInstance[3];
            createAxes();
        }
    }

    public void setPosition(Vector3 position) {
        this.position = position;
    }
    public void setRotation(Quaternion rotation) {
        this.rotation = rotation;
    }
    public void setScale(Vector3 scale) {
        this.scale = scale;
    }
    public Vector3 getPosition() {
        return position;
    }
    public Quaternion getRotation() {
        return rotation;
    }
    public Vector3 getScale() {
        return scale;
    }
    public float getSelfRotationSpeed() {
        return selfRotationSpeed;
    }


    public ObjectShape getObjectShape() {
        return ObjectShape;
    }

    public void setObjectShape(ObjectShape objectShape) {
        this.ObjectShape = objectShape;
    }

    public void setSelfRotationSpeed(float selfRotationSpeed) {
        this.selfRotationSpeed = selfRotationSpeed;
    }

    public boolean hasChildObjects() {
        return hasChildObjects;
    }

    public Vector3 getParentPosition() {
        return parentPosition;
    }

    public void setParentPosition(Vector3 parentPosition) {
        this.parentPosition = parentPosition;
    }

    public void updateTransform () {
        this.transform.set(position, rotation, scale);
        if (SpaceTrader.debugMode){
            transformAxes();
        }

    }

    private void createAxes(){
        Vector3 pos = new Vector3();
        this.transform.getTranslation(pos);
        modelBuilder.begin();
        MeshPartBuilder builder = modelBuilder.part("line", 1, 3, new Material());
        builder.setColor(Color.RED);
        builder.line(pos,new Vector3(20,0,0));
        model = modelBuilder.end();
        axes[0] =new ModelInstance(model);
        modelBuilder.begin();
        builder = modelBuilder.part("line", 1, 3, new Material());
        builder.setColor(Color.BLUE);
        builder.line(pos,new Vector3(0,100,0));
        model = modelBuilder.end();
        axes[1] =new ModelInstance(model);
        modelBuilder.begin();
        builder = modelBuilder.part("line", 1, 3, new Material());
        builder.setColor(Color.GREEN);
        builder.line(pos,new Vector3(0,0,20));
        model = modelBuilder.end();
        axes[2] =new ModelInstance(model);
    }

    public void transformAxes(){
// TODO: 06.09.2017 greift noch auf transform zu
        position = this.getPosition();
        rotation  =this.getRotation();
        scale = this.getScale();
        for (int i = 0; i < 3 ; i++) {
            axes[i].transform.set(position, rotation, scale);
        }
    }

    public ModelInstance getLine(int i){
        if (SpaceTrader.debugMode){
            return axes[i];
        }
        return null;
    }

    public ObjectType getType() {
        return type;
    }

    public boolean isVisible(Camera cam) {
        return ObjectShape == null ? false : ObjectShape.isVisible(transform, cam);
    }

    /** @return -1 on no intersection, or when there is an intersection: the squared distance between the center of this
     * object and the point on the ray closest to this object when there is intersection. */
    /** @return -1 on no intersection, or when there is an intersection: the squared distance between the center of this
     * object and the point on the ray closest to this object when there is intersection. */
    public float intersects(Ray ray) {
        return ObjectShape == null ? -1f : ObjectShape.intersects(transform, ray);
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
