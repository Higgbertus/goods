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
import com.badlogic.gdx.graphics.g3d.utils.RenderContext;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.Ray;
import com.goods.game.Space.GameObjectModelInstance;
import com.goods.game.Space.Planets.PlanetObjectModelInstance;
import com.goods.game.Space.Ships.ShipObjectModelInstance;
import com.goods.game.Space.SpaceMap;
import com.goods.game.Space.Stars.StarObjectModelInstance;

import java.util.ArrayList;

public class SpaceTrader extends ApplicationAdapter implements InputProcessor {
    public Environment environment;
    public PerspectiveCamera perCam;
    public ModelBatch modelBatch;

    public static boolean debugMode = false;

    public ArrayList<ModelInstance> instances2;
    public CameraInputController camController;
    SpriteBatch spriteBatch;
    BitmapFont font;
    private ArrayList<GameObjectModelInstance> instances;

    public RenderContext renderContext;
    private Object model;
    private ModelBuilder modelBuilder;
    private Model planets;
    private SpaceMap spaceMap;
    private int selected =-1;
    String planetInfos = "";
    private ModelInstance modelInstance;

    // Camera Settings
    private final int zoomSpeed = 15;
    private final int rotateAngle = 70;
    private final float translateUnits = 300f;
    private final Vector3 camPosition= new Vector3(500,500,1000),camDirection= new Vector3(500,500,0);
    private ModelInstance[] axes;
    private float deltaTime;

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
        modelBatch = new ModelBatch();



        createSpaceMap();
        if (SpaceTrader.debugMode) {
            createAxes();
        }

    }

    private void createSpaceMap(){
        spaceMap = new SpaceMap();
        spaceMap.createMap(1000,1000,1000);
        spaceMap.fillMapWithObjects();
        spaceMap.createShip();
    }

    private void createAxes() {
        axes = new ModelInstance[3];
        Model model;
        Vector3 pos = new Vector3(0, 0, 0);
        modelBuilder = new ModelBuilder();
        modelBuilder.begin();
        MeshPartBuilder builder = modelBuilder.part("line", 1, 3, new Material());
        builder.setColor(Color.RED);
        builder.line(pos, new Vector3(1000, 0, 0));
        model = modelBuilder.end();
        axes[0] = new ModelInstance(model);
        modelBuilder.begin();
        builder = modelBuilder.part("line", 1, 3, new Material());
        builder.setColor(Color.BLUE);
        builder.line(pos, new Vector3(0, 1000, 0));
        model = modelBuilder.end();
        axes[1] = new ModelInstance(model);
        modelBuilder.begin();
        builder = modelBuilder.part("line", 1, 3, new Material());
        builder.setColor(Color.GREEN);
        builder.line(pos, new Vector3(0, 0, 1000));
        model = modelBuilder.end();
        axes[2] = new ModelInstance(model);
    }

    private int visibleCount;
    @Override
    public void render () {

        deltaTime = Gdx.graphics.getDeltaTime();

        createDebugText();
        camController.update();
        Gdx.gl30.glClearColor(0,0,0,1);
        Gdx.gl30.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        Gdx.gl30.glClear(GL30.GL_COLOR_BUFFER_BIT|GL30.GL_DEPTH_BUFFER_BIT);

       // Move Objects
        for (final StarObjectModelInstance star : spaceMap.getStars()) {
            star.rotateOrbitObjects(deltaTime);
        }


        modelBatch.begin(perCam);
        visibleCount = 0;
        for (final StarObjectModelInstance star : spaceMap.getStars()) {
            if (star.isVisible(perCam)) {
                modelBatch.render(star, environment);
                for (int i = 0; i < 3 && debugMode; i++) {
                    modelBatch.render(star.getLine(i), environment);
                }
                visibleCount++;
            }
            for (final GameObjectModelInstance planet : spaceMap.getAllPlanetsFromStar(star)) {
                if (planet.isVisible(perCam)) {
                    modelBatch.render(planet, environment);
                    for (int i = 0; i < 3 && debugMode; i++) {
                        modelBatch.render(planet.getLine(i), environment);
                    }
                    visibleCount++;
                }

            }
        }
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
        spaceMap.clear();
        //instances.clear();
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
        selected = getObject2(screenX, screenY);
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
            if (selected == getObject2(screenX, screenY)){
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
            planetInfos = spaceMap.getAllObjects().get(value).toString();
        }
    }

    public int getObject2(int screenX, int screenY) {
        Ray ray = perCam.getPickRay(screenX, screenY);
        int result = -1;
        float distance = -1;
        for (int i = 0; i < spaceMap.getAllObjects().size(); ++i) {
            final float dist2 = spaceMap.getAllObjects().get(i).intersects(ray);
            if (dist2 >= 0f && (distance < 0f || dist2 <= distance)) {
                result = i;
                distance = dist2;
            }
        }
        return result;
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
