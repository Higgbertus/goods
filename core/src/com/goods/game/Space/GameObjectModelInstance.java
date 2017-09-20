package com.goods.game.Space;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.utils.MeshPartBuilder;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Quaternion;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.Ray;
import com.badlogic.gdx.utils.StringBuilder;
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
    private Vector3 originalPosition;

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
    private boolean isAlive, hasChildObjects =false;
    private float selfRotationSpeed = 0.4f;
    private ObjectType type;
    Matrix4 tranfsormNEW;
    private boolean debugMode;
    private Vector3 parentPosition;
    private ArrayList<GameObjectModelInstance> orbit;
    private ModelInstance[] coordAxes;
    private ModelBuilder modelBuilder;
    private Model model;
    private float orbitRotationSpeed;
    private float orbitDistance = 10f;
    private static  int counter = 1;
    private int id = counter++;

    public GameObjectModelInstance(Model model, float size, ObjectType type, String name) {
        super(model);
        modelBuilder = new ModelBuilder();
        isAlive = true;
        this.type = type;
        this.size = size;
        this.name = name;
        volume = ((4 * MathUtils.PI * Math.pow(size*sizeFactor,3))/3);
        surfaceArea = 4 * MathUtils.PI * Math.pow(size*sizeFactor,2);
        tranfsormNEW = new Matrix4();
        sb = new StringBuilder();
        if (SpaceTrader.debugMode){
            coordAxes = new ModelInstance[3];
            createCoordAxes();
        }
    }


    public void setPosition(Vector3 position) {
        this.position = position;
        if (this.originalPosition == null){
            this.originalPosition = position;
        }

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

    public void setInitialRotation(Vector3 initRotationAngles, Vector3 target){
        // Dreht das Object auf der Umlaufbahn
        Vector3 starPos = new Vector3(target);
        Vector3 planetPos = new Vector3(originalPosition);
        Vector3 planetOffset = new Vector3(planetPos);
        Matrix4 rotation = new Matrix4();
        rotation.setFromEulerAngles(initRotationAngles.y,initRotationAngles.x,initRotationAngles.z);
        planetOffset.sub(starPos);
        tranfsormNEW.setTranslation(starPos);
        tranfsormNEW.mul(rotation);
        tranfsormNEW.translate(planetOffset);
        // originalPosition = tranfsormNEW.getTranslation(new Vector3());
        this.setRotation(tranfsormNEW.getRotation(new Quaternion()));
        this.setPosition(tranfsormNEW.getTranslation(new Vector3()));
        this.updateTransform();
    }

    private Vector3 faceVector = Vector3.Z;
    public void lookAt(Vector3 target, Vector3 axes){
        faceVector = axes;
        Matrix4 tranfsormNEW = new Matrix4();
        Quaternion q = new Quaternion();
        Matrix4 mtx = new Matrix4();
        Vector3 direction4Rotation = new Vector3(target);
        direction4Rotation.sub(getPosition());
        direction4Rotation.nor();

        mtx.rotate(direction4Rotation, axes);
        mtx.inv();
        q.setFromMatrix(mtx);

        tranfsormNEW.setTranslation(getPosition());
        tranfsormNEW.mul(mtx);
        this.setRotation(tranfsormNEW.getRotation(new Quaternion()));
        this.updateTransform();
    }

    private float distanceToStar(Vector3 planet){
        return this.getPosition().dst(planet);
    }

    private float getOrbitSpeed(Vector3 planet){
        return 100f / distanceToStar(planet);
    }

    // TODO: 11.09.2017 wenn star erzeugt wird wird auch gleich die umflaufbahn der planeten festgelegt => nach dem erzeuge eines planeten gleich entsprechend drehen
    public void rotateOrbitObjects(float deltaTime){
        for (GameObjectModelInstance orbitObjects:orbit) {
            orbitObjects.rotateAround(this.getPosition(),new Vector3(1,1,0),deltaTime,getOrbitSpeed(orbitObjects.getPosition()));
        }
    }

    public void rotateAround(Vector3 target, Vector3 axes, float deltaTime, float orbitSpeed){
        Vector3 starPos = new Vector3(target);
        Vector3 planetPos = new Vector3(originalPosition);
        Vector3 planetOffset = new Vector3(planetPos);
        Matrix4 rotation = new Matrix4();
        rotation.setFromEulerAngles(axes.y*orbitSpeed*deltaTime,axes.x*orbitSpeed*deltaTime,axes.z*orbitSpeed*deltaTime);
        planetOffset.sub(starPos);
        tranfsormNEW.setTranslation(starPos);
        tranfsormNEW.mul(rotation);
        tranfsormNEW.translate(planetOffset);
        this.setRotation(tranfsormNEW.getRotation(new Quaternion()));
        this.setPosition(tranfsormNEW.getTranslation(new Vector3()));
        this.updateTransform();
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

    private void createCoordAxes(){
        Vector3 pos = new Vector3();
        this.transform.getTranslation(pos);
        modelBuilder.begin();
        MeshPartBuilder builder = modelBuilder.part("line", 1, 3, new Material());
        builder.setColor(Color.RED);
        builder.line(pos,new Vector3(20,0,0));
        model = modelBuilder.end();
        coordAxes[0] =new ModelInstance(model);
        modelBuilder.begin();
        builder = modelBuilder.part("line", 1, 3, new Material());
        builder.setColor(Color.BLUE);
        builder.line(pos,new Vector3(0,100,0));
        model = modelBuilder.end();
        coordAxes[1] =new ModelInstance(model);
        modelBuilder.begin();
        builder = modelBuilder.part("line", 1, 3, new Material());
        builder.setColor(Color.GREEN);
        builder.line(pos,new Vector3(0,0,20));
        model = modelBuilder.end();
        coordAxes[2] =new ModelInstance(model);
    }

    public void transformAxes(){
// TODO: 06.09.2017 greift noch auf transform zu
        position = this.getPosition();
        rotation  =this.getRotation();
        scale = this.getScale();
        for (int i = 0; i < 3 ; i++) {
            coordAxes[i].transform.set(position, rotation, scale);
        }
    }

    public ModelInstance getLine(int i){
        if (SpaceTrader.debugMode){
            return coordAxes[i];
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
        if (orbit == null){
            orbit = new ArrayList<GameObjectModelInstance>();
            orbit.add(newObject);
        }else{
            orbit.add(newObject);
            hasChildObjects = true;
        }
    }
    public void removeObjectFromOrbit(GameObjectModelInstance newObject){
        if (orbit == null){
           return;
        }else{
            orbit.remove(newObject);
        }
    }

    public float getOrbitDistance() {
        return orbitDistance;
    }

    public void setOrbitDistance(float orbitDistance) {
        this.orbitDistance = orbitDistance;
    }

    public void setOrbitDistance() {
        this.orbitDistance = getSize()+1f;
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

    StringBuilder sb;

    public String getOrbitObjectsToString(){
        if (orbit == null){
            return "";
        }else{
            sb.delete(0, sb.length());
            // Material[60,100],Material[60,100],Material[60,100],Material[60,100]
            sb.append("\nOrbit:\n");
            sb.append(orbit.toString());
//            for (GameObjectModelInstance orbitObject:orbit) {
//                sb.append(orbitObject.toString()+",");
//            }
           // sb.replace(sb.length()-1,sb.length(),"");
            return sb.toString();
        }

    }

    @Override
    public String toString() {
        return name+"; "+"Pos:"+this.getPosition()+"; Size:"+size;
    }

    public String getName() {
        return name;
    }
}
