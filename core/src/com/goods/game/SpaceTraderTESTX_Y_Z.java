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
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Quaternion;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;
import com.badlogic.gdx.math.collision.Ray;
import com.goods.game.Space.GameObjectModelInstance;
import com.goods.game.Space.Planets.DesertPlanet;
import com.goods.game.Space.Planets.GasPlanet;
import com.goods.game.Space.Planets.IcePlanet;
import com.goods.game.Space.Planets.TerraPlanet;
import com.goods.game.Space.Planets.VulcanoPlanet;
import com.goods.game.Space.Planets.WaterPlanet;
import com.goods.game.Space.Shapes.ObjectShape;
import com.goods.game.Space.Shapes.SphereShape;
import com.goods.game.Space.Ships.ShipObjectModelInstance;
import com.goods.game.Space.Ships.TranspoterShip;
import com.goods.game.Space.SpaceMap;
import com.goods.game.Space.Stars.Star;
import com.goods.game.Space.Stars.StarObjectModelInstance;

import java.util.ArrayList;

public class SpaceTraderTESTX_Y_Z extends ApplicationAdapter implements InputProcessor {




    Matrix4 tranfsormNEW = new Matrix4();
    Vector3 origStartPos;
    Vector3 origPlanetPos;
















    public Environment environment;
    public PerspectiveCamera perCam;
    public ModelBatch modelBatch;


    public CameraInputController camController;
    SpriteBatch spriteBatch;
    BitmapFont font;
    private ArrayList<GameObjectModelInstance> instances;
    private ArrayList<GameObjectModelInstance> planets, planets1, planets2, planets3;
    private SpaceMap spaceMap;
    private int selected = -1;
    String planetInfos = "";
    int gameSpeed = 1; // 2,5,10

    // Camera Settings
    private final int zoomSpeed = 15;
    private final int rotateAngle = 70;
    private final float translateUnits = 300f;
    private final Vector3 camPosition = new Vector3(500, 500, 1000), camDirection = new Vector3(500, 500, 0);
    ShipObjectModelInstance shipObjectModelInstance;
    private ModelInstance[] axes;
    public float deltaTime;

    private boolean rotX, rotY, rotZ, down, up, right, left;

    public void create() {
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

        Gdx.input.setInputProcessor(new InputMultiplexer(this, camController));


        instances = new ArrayList<GameObjectModelInstance>();
        planets = new ArrayList<GameObjectModelInstance>();
        modelBatch = new ModelBatch();
        Model model;
        ModelBuilder modelBuilder = new ModelBuilder();
        BoundingBox bounds = new BoundingBox();
        GameObjectModelInstance gameObjectModelInstance;
        axes = new ModelInstance[3];
        ObjectShape sphereShape;
        model = modelBuilder.createSphere(5, 5, 5, 24, 24, new Material(ColorAttribute.createDiffuse(Color.YELLOW)), VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal);
        gameObjectModelInstance = new Star(model, 5);
        gameObjectModelInstance.setPosition(new Vector3(10, 20, 30));
        gameObjectModelInstance.updateTransform();
        gameObjectModelInstance.calculateBoundingBox(bounds);
        sphereShape = new SphereShape(bounds);
        gameObjectModelInstance.setObjectShape(sphereShape);
        instances.add(gameObjectModelInstance);
        origStartPos = new Vector3(10,20,30);

        origPlanetPos = new Vector3(10, 20, 45);

        model = modelBuilder.createSphere(2, 2, 2, 24, 24, new Material(ColorAttribute.createDiffuse(Color.WHITE)), VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal);
        gameObjectModelInstance = new IcePlanet(model, 5, 0.7f);
        gameObjectModelInstance.setPosition(origPlanetPos);
        gameObjectModelInstance.updateTransform();
        gameObjectModelInstance.calculateBoundingBox(bounds);
        sphereShape = new SphereShape(bounds);
        gameObjectModelInstance.setObjectShape(sphereShape);
        planets.add(gameObjectModelInstance);
        instances.get(0).addObjectToOrbit(gameObjectModelInstance);



        model = modelBuilder.createCone(5, 5 * 3, 5, 24, new Material(ColorAttribute.createDiffuse(Color.GRAY)), VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal);
        shipObjectModelInstance = new TranspoterShip(model, 5);
        shipObjectModelInstance.setPosition(new Vector3(50, 50, 0));
        shipObjectModelInstance.updateTransform();
        shipObjectModelInstance.calculateBoundingBox(bounds);
        sphereShape = new SphereShape(bounds);
        shipObjectModelInstance.setObjectShape(sphereShape);


        createSpaceMap();
        if (SpaceTrader.debugMode) {
            createAxes();
        }

    }

    private void createSpaceMap() {
        spaceMap = new SpaceMap();
        spaceMap.createMap(1000, 1000, 1000);
        spaceMap.fillMapWithObjects();
        //spaceMap.createShip();
    }

    private ModelBuilder modelBuilder;

    private void createAxes() {
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
    private int tar = 0;
    float distance = 0f;

    @Override
    public void render() {
        // use in every move rotate translate scale etc.!!!!!
        // TODO: 07.09.2017 bei allem anwenden

        createDebugText();
        camController.update();
        Gdx.gl30.glClearColor(0, 0, 0, 1);
        Gdx.gl30.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        Gdx.gl30.glClear(GL30.GL_COLOR_BUFFER_BIT | GL30.GL_DEPTH_BUFFER_BIT);


        deltaTime = Gdx.graphics.getDeltaTime();

        // Rotiere alle Planeten des Sterns:
       // instances.get(0).rotate(deltaTime);

//
//        Vector3 orbitPosition = new Vector3(10,0,0);
//        // TODO: 07.09.2017 wie ship alle mit centralen variablen und updatetransfom lösen, alles wie bei ship ins object selber verlagern fast fertig
      for (int i = 0; i < planets.size(); i++) {

          // rotiere alle orbit Object

//
//            /*
//            * @param yaw the yaw in degrees         Y
//            * @param pitch the pitch in degrees     X
//            * @param roll the roll in degrees       Z
//            *       | Y blau
//            *       |
//            *       |
//            *       ________X grün
//            *     /
//            *   /
//            * / Z rot
//            * */

//            rotation.setFromEulerAngles(50f*deltaTime,0f*deltaTime,0f*deltaTime);
//
//            planetOffset.sub(starPos);
//            transform.setTranslation(starPos);
//            transform.mul(rotation);
//            transform.translate(planetOffset);
//
//            planets.get(i).setPosition(transform.getTranslation(new Vector3()));
//            planets.get(i).updateTransform();
//            planets.get(i).setRotation(transform.getRotation(new Quaternion()));
//            planets.get(i).updateTransform();
    }

//            Quaternion rotQ = new Quaternion();
//            Quaternion newRotQ = new Quaternion();
//            Vector3 starPos = new Vector3(instances.get(0).getPosition());
//            Vector3 planetPos = new Vector3(planets.get(0).getPosition());
//        Vector3 planetOrig = new Vector3(20,30,10);
//        Vector3 starOrig = new Vector3(10,20,30);
//            Vector3 planetOffset = new Vector3(planetPos);
//            Matrix4 transform = new Matrix4();
//            Matrix4 rotation = new Matrix4();
//        planetOffset.sub(starPos);
        // rotX,rotY,rotZ,down,moveY,moveZ
        // geht
//        if (rotY) {
//            rotQ = planets.get(0).getRotation();
//            newRotQ.setEulerAngles(50f*deltaTime,0f*deltaTime,0f*deltaTime);
//            rotQ.mul(newRotQ);
//            planets.get(0).setRotation(rotQ);
//            planets.get(0).updateTransform();
//        }
//        if (rotX) {
//            rotQ = planets.get(0).getRotation();
//            newRotQ.setEulerAngles(0f*deltaTime,50f*deltaTime,0f*deltaTime);
//            rotQ.mul(newRotQ);
//            planets.get(0).setRotation(rotQ);
//            planets.get(0).updateTransform();
//        }
//        if (rotZ) {
//            rotQ = planets.get(0).getRotation();
//            newRotQ.setEulerAngles(0f*deltaTime,0f*deltaTime,50f*deltaTime);
//            rotQ.mul(newRotQ);
//            planets.get(0).setRotation(rotQ);
//            planets.get(0).updateTransform();
//        }
//        if (rotY) {
//            planetOffset.sub(starPos);
//            newRotQ.setEulerAngles(50f*deltaTime,0f*deltaTime,0f*deltaTime);
//            transform.setTranslation(starPos);
//            rotation.set(newRotQ);
//            transform.mul(rotation);
//            transform.translate(planetOffset);
//            planets.get(0).setPosition(transform.getTranslation(new Vector3()));
//            planets.get(0).setRotation(transform.getRotation(new Quaternion()));
//            planets.get(0).updateTransform();
//        }
//        if (rotX) {
//            planetOffset.sub(starPos);
//            newRotQ.setEulerAngles(0f*deltaTime,50f*deltaTime,0f*deltaTime);
//            transform.setTranslation(starPos);
//            rotation.set(newRotQ);
//            transform.mul(rotation);
//            transform.translate(planetOffset);
//            planets.get(0).setPosition(transform.getTranslation(new Vector3()));
//            planets.get(0).setRotation(transform.getRotation(new Quaternion()));
//            planets.get(0).updateTransform();
//        }
//        if (rotZ) {
//            planetOffset.sub(starPos);
//            newRotQ.setEulerAngles(0f*deltaTime,0f*deltaTime,50f*deltaTime);
//            transform.setTranslation(starPos);
//            rotation.set(newRotQ);
//            transform.mul(rotation);
//            transform.translate(planetOffset);
//            planets.get(0).setPosition(transform.getTranslation(new Vector3()));
//            planets.get(0).setRotation(transform.getRotation(new Quaternion()));
//            planets.get(0).updateTransform();
//        }


        if (rotY) {

            instances.get(0).rotateOrbitObjects(deltaTime);
            //      planets.get(0).rotateAround(origStartPos, Vector3.Y ,deltaTime,50f);
//            Vector3 starPos = new Vector3(origStartPos);
//            Vector3 planetPos = new Vector3(origPlanetPos);
//            Vector3 planetOffset = new Vector3(planetPos);
//            Matrix4 rotation = new Matrix4();
//            rotation.setFromEulerAngles(50f*deltaTime,0f*deltaTime,0f*deltaTime);
//            planetOffset.sub(starPos);
//            tranfsormNEW.setTranslation(starPos);
//            tranfsormNEW.mul(rotation);
//            tranfsormNEW.translate(planetOffset);
        }
        if (rotX) {
            planets.get(0).rotateAround(origStartPos, Vector3.X ,deltaTime,50f);
//            Vector3 starPos = new Vector3(origStartPos);
//            Vector3 planetPos = new Vector3(origPlanetPos);
//            Vector3 planetOffset = new Vector3(planetPos);
//            Matrix4 rotation = new Matrix4();
//            rotation.setFromEulerAngles(0f*deltaTime,50f*deltaTime,0f*deltaTime);
//            planetOffset.sub(starPos);
//            tranfsormNEW.setTranslation(starPos);
//            tranfsormNEW.mul(rotation);
//            tranfsormNEW.translate(planetOffset);
        }
        if (rotZ) { // rotiert um eigene achse!?!?!? weil die grüne achse auf den stern zeigt!!!!
            Vector3 starPos = new Vector3(origStartPos);
            Vector3 planetPos = new Vector3(origPlanetPos);
            Vector3 planetOffset = new Vector3(planetPos);
            Matrix4 rotation = new Matrix4();
            rotation.setFromEulerAngles(0f*deltaTime,0f*deltaTime,50f*deltaTime);
            planetOffset.sub(starPos);
            tranfsormNEW.setTranslation(starPos);
            tranfsormNEW.mul(rotation);
            tranfsormNEW.translate(planetOffset);
        }

        if (down) {
            planets.get(0).setPosition(tranfsormNEW.getTranslation(new Vector3()));
            planets.get(0).setRotation(tranfsormNEW.getRotation(new Quaternion()));
            planets.get(0).updateTransform();
        }



        distance = instances.get(0).getPosition().dst(planets.get(0).getPosition());
//        for (int i = 0; i < planets1.size(); i++) {
//            Vector3 starPos = new Vector3(instances.get(0).getPosition());
//            Vector3 planetPos = new Vector3(planets1.get(i).getPosition());
//            Vector3 planetOffset = new Vector3(planetPos);
//            Matrix4 transform = new Matrix4();
//            Matrix4 rotation = new Matrix4();
//
//            planetOffset.sub(starPos);
//            transform.setTranslation(starPos);
//            rotation.setFromEulerAngles(0f*deltaTime,0f*deltaTime,100f*deltaTime);
//            transform.mul(rotation);
//            transform.translate(planetOffset);
//
//            planets1.get(i).setPosition(transform.getTranslation(new Vector3()));
//            planets1.get(i).setRotation(transform.getRotation(new Quaternion()));
//            planets1.get(i).updateTransform();
//        }
//        for (int i = 0; i < planets2.size(); i++) {
//            Vector3 starPos = new Vector3(instances.get(0).getPosition());
//            Vector3 planetPos = new Vector3(planets2.get(i).getPosition());
//            Vector3 planetOffset = new Vector3(planetPos);
//            Matrix4 transform = new Matrix4();
//            Matrix4 rotation = new Matrix4();
//
//            planetOffset.sub(starPos);
//            transform.setTranslation(starPos);
//            rotation.setFromEulerAngles(20f*deltaTime,-20f*deltaTime,0f*deltaTime);
//            transform.mul(rotation);
//            transform.translate(planetOffset);
//
//            planets2.get(i).setPosition(transform.getTranslation(new Vector3()));
//            planets2.get(i).setRotation(transform.getRotation(new Quaternion()));
//            planets2.get(i).updateTransform();
//        }
//        for (int i = 0; i < planets3.size(); i++) {
//            Vector3 starPos = new Vector3(instances.get(0).getPosition());
//            Vector3 planetPos = new Vector3(planets3.get(i).getPosition());
//            Vector3 planetOffset = new Vector3(planetPos);
//            Matrix4 transform = new Matrix4();
//            Matrix4 rotation = new Matrix4();
//
//            planetOffset.sub(starPos);
//            transform.setTranslation(starPos);
//            rotation.setFromEulerAngles(50f*deltaTime,-50f*deltaTime,50f*deltaTime);
//            transform.mul(rotation);
//            transform.translate(planetOffset);
//
//            planets3.get(i).setPosition(transform.getTranslation(new Vector3()));
//            planets3.get(i).setRotation(transform.getRotation(new Quaternion()));
//            planets3.get(i).updateTransform();
//        }

        if (selected >= 0) {
            shipObjectModelInstance.moveShip(instances.get(selected).transform.getTranslation(new Vector3()));
        }


        modelBatch.begin(perCam);
        visibleCount = 0;
       // modelBatch.render(shipObjectModelInstance, environment);
        //   modelBatch.render(instances, environment);
//        for (final GameObjectModelInstance instance : instances) {
//            if (instance.isVisible(perCam)) {
//                modelBatch.render(instance, environment);
//                visibleCount++;
//            }
//            for (int i = 0; i < 3; i++) {
//                modelBatch.render(instance.getLine(i), environment);
//            }
//            for (final GameObjectModelInstance orbit : instance.getOrbitObjects()) {
//                modelBatch.render(orbit, environment);
//                for (int i = 0; i < 3; i++) {
//                    modelBatch.render(orbit.getLine(i), environment);
//                }
//            }
//        }
        for (final StarObjectModelInstance star : spaceMap.getStars()) {
            star.rotateOrbitObjects(deltaTime);
        }
        for (final StarObjectModelInstance star : spaceMap.getStars()) {
            if (star.isVisible(perCam)) {
                modelBatch.render(star, environment);
                for (int i = 0; i < 3; i++) {
                    modelBatch.render(star.getLine(i), environment);
                }
                visibleCount++;
            }
            for (final GameObjectModelInstance planet : spaceMap.getAllPlanetsFromStar(star)) {
                if (planet.isVisible(perCam)) {
                    modelBatch.render(planet, environment);
                    for (int i = 0; i < 3; i++) {
                        modelBatch.render(planet.getLine(i), environment);
                    }
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

//        for (int i = 0; i < 3; i++) {
//            modelBatch.render(shipObjectModelInstance.getLine(i), environment);
//        }
//        for (final GameObjectModelInstance instance : planets) {
//            if (instance.isVisible(perCam)) {
//                modelBatch.render(instance, environment);
//                visibleCount++;
//            }
//            for (int i = 0; i < 3; i++) {
//                modelBatch.render(instance.getLine(i), environment);
//            }
//        }
//        for (final GameObjectModelInstance instance : planets1) {
//            if (instance.isVisible(perCam)) {
//                modelBatch.render(instance, environment);
//                visibleCount++;
//            }
//            for (int i = 0; i < 3; i++) {
//                modelBatch.render(instance.getLine(i), environment);
//            }
//        }
//        for (final GameObjectModelInstance instance : planets2) {
//            if (instance.isVisible(perCam)) {
//                modelBatch.render(instance, environment);
//                visibleCount++;
//            }
//            for (int i = 0; i < 3; i++) {
//                modelBatch.render(instance.getLine(i), environment);
//            }
//        }
//        for (final GameObjectModelInstance instance : planets3) {
//            if (instance.isVisible(perCam)) {
//                modelBatch.render(instance, environment);
//                visibleCount++;
//            }
//            for (int i = 0; i < 3; i++) {
//                modelBatch.render(instance.getLine(i), environment);
//            }
//        }
        for (int i = 0; i < 3; i++) {
            modelBatch.render(axes[i], environment);
        }
        modelBatch.end();


        Gdx.gl30.glEnable(GL30.GL_DEPTH_TEST);
        spriteBatch.begin();
        font.draw(spriteBatch, sb.toString(), 10, Gdx.graphics.getHeight() - 10);
        spriteBatch.end();
    }

    StringBuilder sb = new StringBuilder();

    private void createDebugText() {
        sb.delete(0, sb.length());
        sb.append("Debug:");
        sb.append(distance);

//        sb.append("\nCam Dir:" + perCam.direction.toString());
//        sb.append("\nVisible:" + visibleCount);
//        sb.append("\nCam Pos:" + perCam.position.toString());
//        sb.append("\nCam Pos comb:" + perCam.combined.toString());
//        sb.append("\nFPS: ").append(Gdx.graphics.getFramesPerSecond());
//        sb.append("\nPlanet: ").append(planetInfos);
    }

    @Override
    public void dispose() {
        modelBatch.dispose();
        spriteBatch.dispose();
        instances.clear();
    }

    //region Camera Changes
    // wegen dem Inputmultiplexer wird über die bool gesagt das das event richtig verarbeitet wurde oder nicht
    @Override
    public boolean keyDown(int keycode) {
        switch (keycode) {
            case Input.Keys.SPACE: {
                perCam.position.set(500, 500, 500);
                perCam.lookAt(500, 500, 0);
                perCam.near = 1f;
                perCam.far = 10000f;
                perCam.update();
                return true;
            }
            case Input.Keys.X: {
                rotX = true;
                return true;
            }
            case Input.Keys.Y: {
                rotY = true;
                return true;
            }
            case Input.Keys.Z: {
                rotZ = true;
                return true;
            }
            case Input.Keys.DOWN: {
                down = true;
                return true;
            }
            case Input.Keys.UP: {


                Vector3 starPos = new Vector3(origStartPos);
                Vector3 planetPos = new Vector3(origPlanetPos);
                Vector3 planetOffset = new Vector3(planetPos);
                Matrix4 rotation = new Matrix4();
                rotation.rotate(Vector3.Y, MathUtils.random(359));
                rotation.rotate(Vector3.X, MathUtils.random(359));
                planetOffset.sub(starPos);
                tranfsormNEW.setTranslation(starPos);
                tranfsormNEW.mul(rotation);


                up = true;
                return true;
            }
            case Input.Keys.LEFT: {
                tranfsormNEW.translate(new Vector3(50,25,45));
                left = true;
                return true;
            }
            case Input.Keys.RIGHT: {

planets.get(0).lookAt(origStartPos, Vector3.Z);
                right = true;
                return true;
            }
        }

        return false;
    }

    private float startX, startY;

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {

// TODO: 04.09.2017 unproject geht nicht da es unendliche positionen im 3d space gibt...  man könnte mit raypicking herusfinden ob man ein oject klick oder nicht, wenn ja dann rotiere um object ansonsten rotiere um aktuelle cam pos
        // start werte für mousedragged
        startX = screenX;
        startY = screenY;
        // Is a Object clicked?
        //selected = getObject(screenX, screenY);
        selected = getObject(screenX, screenY);
        if (selected >= 0) {
            return true;
        } else {
            return false;
        }

    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        if (selected >= 0) {
            if (selected == getObject(screenX, screenY)) {
                setSelected(selected);
            }
            //selected = -1;
            return true;
        } else {
            planetInfos = "";
            return false;
        }
    }

    public void setSelected(int value) {
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
        switch (keycode) {
            case Input.Keys.X: {
                rotX = false;
                return true;
            }
            case Input.Keys.Y: {
                rotY = false;
                return true;
            }
            case Input.Keys.Z: {
                rotZ = false;
                return true;
            }
            case Input.Keys.DOWN: {
                down = false;
                return true;
            }
            case Input.Keys.UP: {
                up = false;
                return true;
            }
            case Input.Keys.LEFT: {
                left = false;
                return true;
            }
            case Input.Keys.RIGHT: {
                right = false;
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }


    private Vector3 tmp = new Vector3(), tmp2 = new Vector3(), tmp3 = new Vector3();


    Vector3 tmpV1 = new Vector3();
    Vector3 tmpV2 = new Vector3();
    /**
     * The target to rotate around.
     */
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
        if (amount < 0)
            perCam.translate(tmpV1.set(perCam.direction).scl(amount + zoomSpeed));
        else
            perCam.translate(tmpV1.set(perCam.direction).scl(amount - zoomSpeed));
        perCam.update();
        return true;
    }
    //endregion





    // TODO: 04.09.2017 Camera handling: 1. dopperklick > zoom zum object; linksklick auf object rotieren und zoomen am objekt keine bewegen möglich,


}
