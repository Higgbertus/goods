package com.goods.game;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL30;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.graphics.g3d.utils.CameraInputController;
import com.badlogic.gdx.graphics.g3d.utils.MeshPartBuilder;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;
import com.badlogic.gdx.math.collision.Ray;
import com.goods.game.Space.GameObjectModelInstance;
import com.goods.game.Space.Shapes.SphereShape;
import com.goods.game.Space.Ships.ShipObjectModelInstance;
import com.goods.game.Space.Ships.TranspoterShip;
import com.goods.game.Space.SpaceMap;
import com.goods.game.Space.Stars.Star;

import java.util.ArrayList;

public class SpaceTraderTEST extends ApplicationAdapter implements InputProcessor {
    public Environment environment;
    public PerspectiveCamera perCam;
    public ModelBatch modelBatch;

    public CameraInputController camController;
    SpriteBatch spriteBatch;
    BitmapFont font;
    private ArrayList<GameObjectModelInstance> instances;
    private ArrayList<ModelInstance> instances2;
    private SpaceMap spaceMap;
    private int selected =-1;
    String planetInfos = "";

    // Camera Settings
    private final int zoomSpeed = 15;
    private final int rotateAngle = 70;
    private final float translateUnits = 300f;
    private final Vector3 camPosition= new Vector3(0,0,100),camDirection= new Vector3(0,0,0);
    ShipObjectModelInstance shipObjectModelInstance;

    public void create () {
        Gdx.app.setLogLevel(Application.LOG_DEBUG);
        spriteBatch = new SpriteBatch();
        FreeTypeFontGenerator generator2 = new FreeTypeFontGenerator(Gdx.files.internal("fonts/brlnsr.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 25;
        parameter.color = Color.RED;
        font = generator2.generateFont(parameter); // font size 12 pixels
        generator2.dispose(); // don't forget to dispose to avoid memory leaks!

        // Lichteinstellungen
        environment = new Environment();
        environment.set(new ColorAttribute(ColorAttribute.AmbientLight, 0.4f, 0.4f, 0.4f, 1f));
        // rgb color + position (x,y,z)
        //environment.add(new DirectionalLight().set(0.8f, 0.8f, 0.8f, -1f, -0.8f, 10f));
        environment.add(new DirectionalLight().set(0.8f, 0.8f, 0.8f, 10f, 5f, 10f));

        // TODO: 04.09.2017 instanzen aufteilen und besser mehrmal modelbatch.render(instances, xx) aufrufen?!?!?

//        instances = new ArrayList<GameObjectModelInstance>();
//        for (GameObjectModelInstance gameObjects : spaceMap.getAllObjects()) {
//            instances.add(gameObjects);
//        }

        // Kamera 67Grad, aspect ratio
        perCam = new PerspectiveCamera(67, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        // Kamera Position im raum
        perCam.position.set(camPosition);
        // Gibt an wohin die Kamera schaut
        perCam.lookAt(camDirection);
        // Gibt an wann etwas ausgeblendet wird
        perCam.near = 1f;
        perCam.far = 10000000f;
        // update camera => camera einstellungen übernehmen
        perCam.update();


        camController = new CameraInputController(perCam);

        Gdx.input.setInputProcessor(new InputMultiplexer(this,camController));


        instances = new ArrayList<GameObjectModelInstance>();
        modelBatch = new ModelBatch();
        Model model;
        ModelBuilder modelBuilder = new ModelBuilder();
        BoundingBox bounds = new BoundingBox();
        GameObjectModelInstance gameObjectModelInstance;

        com.goods.game.Space.Shapes.Shape sphereShape;
//        model = modelBuilder.createSphere(5,5,5,24,24,new Material(ColorAttribute.createDiffuse(Color.YELLOW)), VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal);
//        gameObjectModelInstance = new Star(model,5);
//        gameObjectModelInstance.transform.setTranslation(new Vector3(1,1,0));
//        gameObjectModelInstance.calculateBoundingBox(bounds);
//        sphereShape = new SphereShape(bounds);
//        gameObjectModelInstance.shape = sphereShape;
//        instances.add(gameObjectModelInstance);
        instances2 = new ArrayList<ModelInstance>();
        modelBuilder.begin();
        MeshPartBuilder builder = modelBuilder.part("line", 1, 3, new Material());
        builder.setColor(Color.RED);
        builder.line(0.0f, 0.0f, -5.0f, 0.0f, 0.0f, 5.0f);
        model = modelBuilder.end();
        instances2.add(new ModelInstance(model));

        model = modelBuilder.createSphere(10,10,10,24,24,new Material(ColorAttribute.createDiffuse(Color.YELLOW)), VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal);
        gameObjectModelInstance = new Star(model,5);
        gameObjectModelInstance.transform.setTranslation(new Vector3(10,20,60));
        gameObjectModelInstance.calculateBoundingBox(bounds);
        sphereShape = new SphereShape(bounds);
        gameObjectModelInstance.shape = sphereShape;
        instances.add(gameObjectModelInstance);

        model = modelBuilder.createCone(5,5*3,5,24,new Material(ColorAttribute.createDiffuse(Color.GRAY)), VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal);
        shipObjectModelInstance = new TranspoterShip(model,5);
        shipObjectModelInstance.transform.setTranslation(new Vector3(50,50,0));
        shipObjectModelInstance.calculateBoundingBox(bounds);
        sphereShape = new SphereShape(bounds);
        shipObjectModelInstance.shape = sphereShape;
//        shipObjectModelInstance.setDestination(new Vector3(300,100,0));
//        shipObjectModelInstance.setDirection((new Vector3(300,100,0).sub(200,150,0)).nor());



        //createSpaceMap();
    }

    private void createSpaceMap(){
        spaceMap = new SpaceMap();
        spaceMap.createMap(1000,1000,1000);
        spaceMap.fillMapWithObjects();
        spaceMap.createShip();
    }
float a=1;
    private int visibleCount;
    @Override
    public void render () {
        createDebugText();
        camController.update();
        Gdx.gl30.glClearColor(0,0,0,1);
        Gdx.gl30.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        Gdx.gl30.glClear(GL30.GL_COLOR_BUFFER_BIT|GL30.GL_DEPTH_BUFFER_BIT);
 Vector3 vec = new Vector3(a++,a++,a++);
        // rotate ship in direction to target
//        Vector3 destination = new Vector3();
//        Vector3 direction = new Vector3();
//        Vector3 direction2 = new Vector3();
//        Vector3 shipPos = new Vector3();
//
//        destination = shipObjectModelInstance.getDestination();
//        direction = shipObjectModelInstance.getDirection();
//        shipObjectModelInstance.transform.getTranslation(shipPos);

        Vector3 starPos = new Vector3(0,0,0);
        Vector3 planetOffset = new Vector3();
        instances.get(0).transform.getTranslation(planetOffset);
        planetOffset.sub(starPos);
        Vector3 rot = new Vector3(1,1,1);
        float a = 1.1f;
        rot.scl(a);
        Matrix4 transform = new Matrix4();
        Matrix4 tmp = new Matrix4();
        transform.setTranslation(starPos);
        tmp.setFromEulerAngles(rot.x,rot.y,rot.z);
        transform.mul(tmp);
        transform.translate(planetOffset);
        instances.get(0).transform.set(transform);

if (shipObjectModelInstance.isMoving()){
    if (shipObjectModelInstance.hasReachedDestination()){
        shipObjectModelInstance.setMoving(false);
        shipObjectModelInstance.rotateToTarget(instances.get(0).transform.getTranslation(new Vector3()));
    }else{
       // shipObjectModelInstance.moveShip();

        shipObjectModelInstance.rotateToTarget(instances.get(0).transform.getTranslation(new Vector3()));
    }
}else{
    shipObjectModelInstance.setDestination(new Vector3(0,0,0));
    shipObjectModelInstance.setMoving(true);
}
//        Matrix4 transform = new Matrix4();
//        Matrix4 tmp = new Matrix4();
//        tmp.setFromEulerAngles(100,0,0);
//        transform.mul(tmp);
//        shipObjectModelInstance.transform.set(transform);

        modelBatch.begin(perCam);
        visibleCount = 0;
        modelBatch.render(shipObjectModelInstance, environment);
        for (final GameObjectModelInstance instance : instances) {
            if (instance.isVisible(perCam)) {
                modelBatch.render(instance, environment);
                visibleCount++;
            }
        }
        modelBatch.render(instances2, environment);
        modelBatch.end();


        Gdx.gl30.glEnable(GL30.GL_DEPTH_TEST);
        spriteBatch.begin();
        font.draw(spriteBatch,sb.toString(),10,Gdx.graphics.getHeight()-10);
        spriteBatch.end();
    }

    StringBuilder sb = new StringBuilder();
    private void createDebugText(){
        sb.delete(0, sb.length());
        sb.append("Debug:");
        sb.append("\nCam Dir:"  + perCam.direction.toString());
        sb.append("\nVisible:"  + visibleCount);
        sb.append("\nCam Pos:"  + perCam.position.toString());
        sb.append("\nCam Pos comb:"  + perCam.combined.toString());
        sb.append("\nFPS: ").append(Gdx.graphics.getFramesPerSecond());
        sb.append("\nPlanet: ").append(planetInfos);
    }

    @Override
    public void dispose () {
        modelBatch.dispose();
        spriteBatch.dispose();
        instances.clear();
    }

    //region Camera Changes
    // wegen dem Inputmultiplexer wird über die bool gesagt das das event richtig verarbeitet wurde oder nicht
    @Override
    public boolean keyDown(int keycode) {
        if (keycode == Input.Keys.SPACE){
            perCam.position.set(500, 500, 500);
            perCam.lookAt(500,500,0);
            perCam.near = 1f;
            perCam.far = 10000f;
            perCam.update();
            return true;
        }
        return false;
    }
    private float startX,startY;

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {

// TODO: 04.09.2017 unproject geht nicht da es unendliche positionen im 3d space gibt...  man könnte mit raypicking herusfinden ob man ein ojec klick oder nicht, wenn ja dann rotiere um object ansonsten rotiere um aktuelle cam pos
         // start werte für mousedragged
        startX = screenX;
        startY = screenY;
        // Is a Object clicked?
        //selected = getObject(screenX, screenY);
        selected = getObject(screenX, screenY);
        if (selected >= 0){
            return true;
        }else{
            return false;
        }

    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        if (selected >= 0) {
//            if (selected == getObject(screenX, screenY)){
//                setSelected(selected);
//            }
            if (selected == getObject(screenX, screenY)){
                setSelected(selected);
            }
            selected = -1;
            return true;
        }else{
            planetInfos = "";
            return false;
        }
    }

    public void setSelected (int value) {
        if (selected == value) {
            planetInfos = instances.get(selected).toString();
        }
    }

    public int getObject(int screenX, int screenY) {
        Ray ray = perCam.getPickRay(screenX, screenY);
        int result = -1;
        float distance = -1;
        for (int i = 0; i < instances.size(); ++i) {
            final float dist2 = instances.get(i).intersects(ray);
            if (dist2 >= 0f && (distance < 0f || dist2 <= distance)) {
                result = i;
                distance = dist2;
            }
        }
        return result;
    }

    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }


    private Vector3 tmp = new Vector3(),tmp2 = new Vector3(),tmp3 = new Vector3();


    Vector3 tmpV1 = new Vector3();
    Vector3 tmpV2 = new Vector3();
    /** The target to rotate around. */
    public Vector3 target = new Vector3();
    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {

        final float deltaX = (screenX - startX) / Gdx.graphics.getWidth();
        final float deltaY = (startY - screenY) / Gdx.graphics.getHeight();
        startX = screenX;
        startY = screenY;
        tmpV1 = new Vector3();
        tmpV2 = new Vector3();

        // so is es genau die kamera (kopf drehen)
        target = new Vector3(perCam.position);

        if (Gdx.input.isButtonPressed(0)) {
            tmpV1.set(perCam.direction).crs(perCam.up).y = 0f;
            perCam.rotateAround(target, tmpV1.nor(), deltaY * rotateAngle);
            perCam.rotateAround(target, Vector3.Y, deltaX * -rotateAngle);
        } else if (Gdx.input.isButtonPressed(1)) {
            perCam.translate(tmpV1.set(perCam.direction).crs(perCam.up).nor().scl(-deltaX * translateUnits));
            perCam.translate(tmpV2.set(perCam.up).scl(-deltaY * translateUnits));
           // if (translateTarget) target.add(tmpV1).add(tmpV2);
        }
        perCam.update();
        return true;
    }
    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
    tmpV1 = new Vector3(perCam.position);
        if (amount<0)
            perCam.translate(tmpV1.set(perCam.direction).scl(amount+zoomSpeed));
        else
            perCam.translate(tmpV1.set(perCam.direction).scl(amount-zoomSpeed));
        perCam.update();
       return true;
    }
    //endregion

    // TODO: 04.09.2017 Camera handling: 1. dopperklick > zoom zum object; linksklick auf object rotieren und zoomen am objekt keine bewegen möglich,


}
