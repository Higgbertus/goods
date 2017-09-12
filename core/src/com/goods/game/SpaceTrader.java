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
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.Ray;
import com.goods.game.Space.DynamicObjectHandler;
import com.goods.game.Space.GameObjectModelInstance;
import com.goods.game.Space.ObjectType;
import com.goods.game.Space.Ships.ShipObjectModelInstance;
import com.goods.game.Space.SpaceMap;
import com.goods.game.Space.Stars.StarObjectModelInstance;
import com.sun.jmx.snmp.Timestamp;

public class SpaceTrader extends ApplicationAdapter implements InputProcessor {

    // Engine Settings
    private Environment environment;
    private PerspectiveCamera perCam;
    private ModelBatch modelBatch;
    public CameraInputController camController;
    SpriteBatch spriteBatch;
    BitmapFont font;
    private ModelBuilder modelBuilder;



    // Game Settings
    public static boolean debugMode = false;
    private String planetInfos = "";
    private SpaceMap spaceMap;
    private int selected =-1;
    private float deltaTime;
    private ModelInstance[] axes;
    private DynamicObjectHandler dynamicObjectHandler;



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

        initGame();

    }


    // Initiate Game, create Map, create Start Settings
    private void initGame(){
        // Create Map
        spaceMap = new SpaceMap();
        spaceMap.createMap(1000,1000,1000);
        spaceMap.fillMapWithObjects();

        // Create first Ship
        dynamicObjectHandler = new DynamicObjectHandler(spaceMap);
        dynamicObjectHandler.createShip();

        // Debug Options
        if (SpaceTrader.debugMode) {
            createAxes();
        }
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

       // Move Static Objects
        for (final StarObjectModelInstance star : spaceMap.getStars()) {
            star.rotateOrbitObjects(deltaTime);
        }
        // Move dynamic objects
        if (selected >= 0) {
            dynamicObjectHandler.getShip(0).moveShip(spaceMap.getAllObjects().get(selected).getPosition());
        }

        modelBatch.begin(perCam);
        visibleCount = 0;
        // Render Static Objecs, Stars, Planets
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
        // Render dynamic Objects like Ships
        for (final ShipObjectModelInstance ship : dynamicObjectHandler.getShips()) {
            if (ship.isVisible(perCam)) {
                modelBatch.render(ship, environment);
                for (int i = 0; i < 3 && debugMode; i++) {
                    modelBatch.render(ship.getLine(i), environment);
                }
                visibleCount++;
            }
        }
        modelBatch.end();

        // Camera Adjustments
        if(stickToPosition && selected > 0){
            stickCameraToPosition();
        }



        Gdx.gl30.glEnable(GL30.GL_DEPTH_TEST);
        spriteBatch.begin();
        font.draw(spriteBatch,sb.toString(),10,Gdx.graphics.getHeight()-10);
        spriteBatch.end();
    }

    StringBuilder sb = new StringBuilder();
    private void createDebugText(){
        sb.delete(0, sb.length());
        sb.append("Debug:");
//        sb.append("\nCam Dir:"  + perCam.direction.toString());
        sb.append("\nVisible:"  + visibleCount);
        sb.append("\nCam Pos:"  + perCam.position.toString());
//        sb.append("\nCam Pos comb:"  + perCam.combined.toString());
        sb.append("\nFPS: ").append(Gdx.graphics.getFramesPerSecond());
        sb.append("\nPlanet: ").append(planetInfos);
    }

    @Override
    public void dispose () {
        modelBatch.dispose();
        spriteBatch.dispose();
        spaceMap.clear();
        dynamicObjectHandler.clear();
        //instances.clear();
    }

    //region Camera Changes
    // wegen dem Inputmultiplexer wird über die bool gesagt das das event richtig verarbeitet wurde oder nicht
    @Override
    public boolean keyDown(int keycode) {
        stickToPosition = false;
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


    private Timestamp timestamp, timestampNEW;
    private boolean doubleClicked = false;
    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {

// TODO: 04.09.2017 unproject geht nicht da es unendliche positionen im 3d space gibt...  man könnte mit raypicking herusfinden ob man ein ojec klick oder nicht, wenn ja dann rotiere um object ansonsten rotiere um aktuelle cam pos

        // Double Click: Zoomt zu Objekt und fixiert position, mann kann um Objekt herum rotieren und hin bzw weg zoomen. erst wenn mousedragged benützt verliert man die fixierung
        if(button == Input.Buttons.LEFT){
            if(timestampNEW == null){
                timestampNEW = new Timestamp(System.currentTimeMillis());
            }else{
                timestamp = new Timestamp(System.currentTimeMillis());
                long timeSpan = timestamp.getDateTime()-timestampNEW.getDateTime();
                if (timeSpan < 500){
                    doubleClicked = true;
                }
                timestampNEW = null;
            }
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
        }else{
            if (button == Input.Buttons.RIGHT){
                stickToPosition = false;
                return true;
            }
            return false;
        }








    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        if (selected >= 0) {
            if (selected == getObject2(screenX, screenY)){
                setSelected(selected);
            }
//            selected = -1;
            return true;
        }else{
            planetInfos = "";
            return false;
        }
    }

    boolean stickToPosition = false;
    public void setSelected (int value) {
        if (selected == value) {
            planetInfos = spaceMap.getAllObjects().get(value).toString();
        }
        if (selected == value && doubleClicked) {
            doubleClicked = false;
            // TODO: 12.09.2017 so drehen das der blick auf die umlaufbahn fällt damit man alles sieht und keine Planeten sich überlapppen
            if (spaceMap.getAllObjects().get(value).getType() == ObjectType.Star){
                Vector3 pos = spaceMap.getAllObjects().get(value).getPosition();
                perCam.position.set(pos.x,pos.y,pos.z+200);
                perCam.lookAt(pos.x,pos.y,0);
                perCam.near = 1f;
                perCam.far = 1000f;
                perCam.update();
            }else {
                stickToPosition = true;
                Vector3 pos = spaceMap.getAllObjects().get(value).getPosition();
                perCam.position.set(pos.x,pos.y,pos.z+50);
                perCam.lookAt(pos.x,pos.y,0);
                perCam.near = 1f;
                perCam.far = 1000f;
                perCam.update();
            }
        }
    }

    private void stickCameraToPosition(){
        Vector3 pos = spaceMap.getAllObjects().get(selected).getPosition();
        perCam.position.set(pos.x,pos.y,pos.z+50);
        perCam.lookAt(pos.x,pos.y,0);
        perCam.near = 1f;
        perCam.far = 1000f;
        perCam.update();
    }

    // TODO: 12.09.2017 auch dynamicobjects durchlaufen
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

    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    Vector3 tmpV1 = new Vector3();
    Vector3 tmpV2 = new Vector3();
    /** The target to rotate around. */
    public Vector3 target = new Vector3();
    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        stickToPosition = false;
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


}
