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
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.graphics.g3d.utils.CameraInputController;
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
    }

    private void createSpaceMap(){
        spaceMap = new SpaceMap();
        spaceMap.createMap(1000,1000,1000);
        spaceMap.fillMapWithObjects();
        spaceMap.createShip();
    }

    private int visibleCount;
    @Override
    public void render () {
        createDebugText();
        camController.update();
        Gdx.gl30.glClearColor(0,0,0,1);
        Gdx.gl30.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        Gdx.gl30.glClear(GL30.GL_COLOR_BUFFER_BIT|GL30.GL_DEPTH_BUFFER_BIT);

        // move
        // working
        // instances.get(0).transform.translate(1,0,0);


        //working
        //instances.get(0).transform.rotate(new Vector3(1,1,1), 1);

        // So rotiert der planet in der umlaufbahn vom stern, alle 3 gehen unterschiedlich Stern = 0,0,0
        //instances.get(1).transform.setTranslation(0,0,0).rotate(new Vector3(0,1,0), 1).translate(10,0,0);
        //instances.get(1).transform.setTranslation(0,0,0).rotate(new Vector3(0,0,1), 1).translate(0,10,0);
        //instances.get(1).transform.setTranslation(0,0,0).rotate(new Vector3(1,1,0), 1).translate(0,0,10);

        // So rotiert der planet in der umlaufbahn vom stern, alle 3 gehen unterschiedlich Stern = 10,25,0
        //instances.get(1).transform.setTranslation(10,25,0).rotate(new Vector3(0,1,0), 1).translate(10,0,0);
        //instances.get(1).transform.setTranslation(10,25,0).rotate(new Vector3(0,0,1), 1).translate(0,10,0);
       // instances.get(1).transform.setTranslation(10,25,0).rotate(new Vector3(1,1,0), 1).translate(0,0,10);

        // Rotate planets (self rotation on the own center)
//        for (GameObjectModelInstance instance :instances) {
//            instance.transform.rotate(new Vector3(1,1,0), instance.getSelfRotationSpeed());
//        }

        // TODO: 04.09.2017 planeten drehen durch nach umstellung getpos() zu transform.getTranslation... komischerweise gehen feste werte wie 1f auch nicht wird alles aufsummiert 

        // rotate and translate Planets around Star
        for (StarObjectModelInstance star :spaceMap.getStars()) {

            for (GameObjectModelInstance planet : spaceMap.getAllPlanetsFromStar(star)) {

//                PlanetObjectModelInstance planetInstance = (PlanetObjectModelInstance) instance;
//              Vector3 starPos = planet.getParentPosition();
//                Vector3 planetOffset = new Vector3();
//                planetInstance.transform.getTranslation(planetOffset);
//                planetOffset.sub(starPos);
                // // TODO: 04.09.2017 problem wennn die starpos nicht ein fixer wert ist sonder mit transform.getTranslation immer die vorrige rotation mit einbezogen wird bringt ein ständiges beschleunigen
                //instance.transform.setTranslation(starPos).rotate(planetInstance.getFace(), planetInstance.getOrbitRotationSpeed()).translate(planetOffset);
                //instance.transform.setTranslation(starPos).setFromEulerAngles(1,1,1).translate(planetOffset);

                // TODO: 04.09.2017 es sind nur 2 rotationsarten möglich im Star object erzeugt
                Vector3 starPos = planet.getParentPosition();
                Vector3 planetOffset = new Vector3();
                planet.transform.getTranslation(planetOffset);
                planetOffset.sub(starPos);
                Vector3 rot = new Vector3(star.getFace());
                float a = ((PlanetObjectModelInstance) planet).getOrbitRotationSpeed();
                rot.scl(a);
                Matrix4 transform = new Matrix4();
                Matrix4 tmp = new Matrix4();
                transform.setTranslation(starPos);
                tmp.setFromEulerAngles(rot.x,rot.y,rot.z);
                transform.mul(tmp);
                transform.translate(planetOffset);
                planet.transform.set(transform);
            }


                ShipObjectModelInstance shipInstance = (ShipObjectModelInstance) spaceMap.getShip(0);
           if (shipInstance.isMoving()){
               if (shipInstance.transform.getTranslation(new Vector3()).dst(shipInstance.getDestination())<10){
                   shipInstance.setMoving(false);
               }else{
                   Vector3 baseDirection = shipInstance.getDestination();

                   Vector3 direction = new Vector3();
                   //Quaternion rotation = your_quaternion;
                   direction.set(baseDirection);
                   //direction.mul(rotation);

                   final float speed = 5f; // 5 units per second
                   Vector3 translation = new Vector3();
                   translation.set(direction);
                   translation.scl(speed);

                   shipInstance.transform.trn(translation);

//                   Vector3 dest = shipInstance.getDestination();
//                   shipInstance.transform.trn(dest.x*2,dest.y*2,dest.z*2);
               }
            //   shipInstance.transform.translate(1,1,1);
           }else{
                shipInstance.setMoving(true);
                // get a random star as target
                GameObjectModelInstance planet;
                planet = spaceMap.getStar(MathUtils.random(spaceMap.getStars().size()-1));
                Vector3 target = new Vector3();
                planet.transform.getTranslation(target);
               shipInstance.setDestination(target.nor());
               shipInstance.setMoving(true);

           }
            // move Ship

        }


        modelBatch.begin(perCam);
        visibleCount = 0;
        for (final StarObjectModelInstance star : spaceMap.getStars()) {
            if (star.isVisible(perCam)) {
                modelBatch.render(star, environment);
                visibleCount++;
            }
            for (final GameObjectModelInstance planet : spaceMap.getAllPlanetsFromStar(star)) {
                if (planet.isVisible(perCam)) {
                    modelBatch.render(planet, environment);
                    visibleCount++;
                }
            }
        }
        for (final GameObjectModelInstance ship : spaceMap.getShips()) {
            if (ship.isVisible(perCam)) {
                modelBatch.render(ship, environment);
                visibleCount++;
            }
        }
        //modelBatch.render(instances, environment);
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
