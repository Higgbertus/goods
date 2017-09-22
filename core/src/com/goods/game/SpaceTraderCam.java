package com.goods.game;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL30;
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
import com.badlogic.gdx.graphics.g3d.particles.ParticleEffect;
import com.badlogic.gdx.graphics.g3d.particles.ParticleEffectLoader;
import com.badlogic.gdx.graphics.g3d.particles.ParticleSystem;
import com.badlogic.gdx.graphics.g3d.particles.batches.BillboardParticleBatch;
import com.badlogic.gdx.graphics.g3d.particles.influencers.DynamicsInfluencer;
import com.badlogic.gdx.graphics.g3d.particles.influencers.DynamicsModifier;
import com.badlogic.gdx.graphics.g3d.utils.CameraInputController;
import com.badlogic.gdx.graphics.g3d.utils.MeshPartBuilder;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Quaternion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.Ray;
import com.goods.game.Space.DynamicObjectHandler;
import com.goods.game.Space.GameObjectModelInstance;
import com.goods.game.Space.ObjectType;
import com.goods.game.Space.Ships.ShipObjectModelInstance;
import com.goods.game.Space.SpaceMap;
import com.goods.game.Space.Stars.StarObjectModelInstance;
import com.sun.jmx.snmp.Timestamp;

public class SpaceTraderCam extends ApplicationAdapter{

    // Engine Settings
    private Environment environment;
 //   private PerspectiveCamera perCam;
    private ModelBatch modelBatch;
    public CameraInputController camController;
    SpriteBatch spriteBatch;
    BitmapFont font;
    private ModelBuilder modelBuilder;

    // Game Settings
    public static boolean debugMode = false;
    private SpaceMap spaceMap;
    private ModelInstance[] axes;
    private DynamicObjectHandler dynamicObjectHandler;
    ParticleEffect particleEffect;
    ParticleSystem particleSystem;

    // Camera Settings
    private final int zoomSpeed = 15;
    private final int rotateAngle = 70;
    private final float translateUnits = 300f;
    private final Vector3 camPosition= new Vector3(500,500,610),camDirection= new Vector3(500,500,0);
    SpacePerspectiveCamera sPerCam;


    // Game Helper
    private String planetInfos = "";
    private int selected =-1;
    private int selectedShip =-1;
    private ObjectType selectedType;
    private float deltaTime;
    private GameObjectModelInstance activeTarget;
    private GameObjectModelInstance selectedObject;
    SpaceInputProcessor spaceInputProcessor;


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
//        perCam = new PerspectiveCamera(67, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
//        // Kamera Position im raum
//        perCam.position.set(camPosition);
//        // Gibt an wohin die Kamera schaut
//        perCam.lookAt(camDirection);
//        // Gibt an wann etwas ausgeblendet wird
//        perCam.near = 1f;
//        perCam.far = 10000000f;
//        // update camera => camera einstellungen übernehmen
//        perCam.update();
//        spaceInputProcessor = new SpaceInputProcessor(perCam);


        sPerCam = new SpacePerspectiveCamera(67, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        // Kamera Position im raum
        sPerCam.position.set(camPosition);
        // Gibt an wohin die Kamera schaut
        sPerCam.lookAt(camDirection);
        // Gibt an wann etwas ausgeblendet wird
        sPerCam.near = 1f;
        sPerCam.far = 10000000f;
        // update camera => camera einstellungen übernehmen
        sPerCam.update();
        spaceInputProcessor = new SpaceInputProcessor(sPerCam);

      //  camController = new CameraInputController(perCam);
       // Gdx.input.setInputProcessor(new InputMultiplexer(spaceInputProcessor,camController));
        Gdx.input.setInputProcessor(spaceInputProcessor);
        //Gdx.input.setInputProcessor(new InputMultiplexer(this,camController));




        particleSystem = ParticleSystem.get();
// Since our particle effects are PointSprites, we create a PointSpriteParticleBatch
        BillboardParticleBatch billboardParticleBatch = new BillboardParticleBatch();
        billboardParticleBatch.setCamera(sPerCam);
        particleSystem.add(billboardParticleBatch);

        AssetManager assets = new AssetManager();
        ParticleEffectLoader.ParticleEffectLoadParameter loadParam = new ParticleEffectLoader.ParticleEffectLoadParameter(particleSystem.getBatches());
        assets.load("particles/propulsion.p", ParticleEffect.class, loadParam);
        assets.finishLoading();

        ParticleEffect originalEffect = assets.get("particles/propulsion.p");
// we cannot use the originalEffect, we must make a copy each time we create new particle effect
        particleEffect = originalEffect.copy();
        particleEffect.init();



        particleEffect.start();  // optional: particle will begin playing immediately
        particleSystem.add(particleEffect);

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
        dynamicObjectHandler.getShip(0).setPosition(new Vector3(450,550,300));
        dynamicObjectHandler.getShip(0).setRotation(new Quaternion(Vector3.X,90));
        dynamicObjectHandler.getShip(0).updateTransform();

        spaceInputProcessor.setObjects(spaceMap.getAllObjects(),dynamicObjectHandler.getShips());
        // Debug Options
        if (SpaceTraderCam.debugMode) {
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
      //  camController.update();
        Gdx.gl30.glClearColor(0,0,0,1);
        Gdx.gl30.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        Gdx.gl30.glClear(GL30.GL_COLOR_BUFFER_BIT|GL30.GL_DEPTH_BUFFER_BIT);

       // Move Static Objects
        for (final StarObjectModelInstance star : spaceMap.getStars()) {
            star.rotateOrbitObjects(deltaTime);
        }
        // Move dynamic objects
        if (selected >= 0 && activeTarget != null && selectedShip >= 0 && selectedType != ObjectType.Ship) {
            dynamicObjectHandler.getShip(selectedShip).moveShip(spaceMap.getAllObjects().get(selected));
        }

        // move Patricle Effects
        moveParticles();

       // stage.draw();
        modelBatch.begin(sPerCam);
        visibleCount = 0;
        // Render Static Objecs, Stars, Planets
        for (final StarObjectModelInstance star : spaceMap.getStars()) {
            if (star.isVisible(sPerCam)) {
                modelBatch.render(star, environment);
                for (int i = 0; i < 3 && debugMode; i++) {
                    modelBatch.render(star.getLine(i), environment);
                }
                visibleCount++;
            }
            for (final GameObjectModelInstance planet : spaceMap.getAllPlanetsFromStar(star)) {
                if (planet.isVisible(sPerCam)) {
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
            if (ship.isVisible(sPerCam)) {
                modelBatch.render(ship, environment);
                for (int i = 0; i < 3 && debugMode; i++) {
                    modelBatch.render(ship.getLine(i), environment);
                }
                visibleCount++;
            }
        }
        //        // Render Particle Affects
        renderParticleEffects();

        modelBatch.end();
        Gdx.gl30.glEnable(GL30.GL_DEPTH_TEST);
        spriteBatch.begin();
        font.draw(spriteBatch,sb.toString(),10,Gdx.graphics.getHeight()-10);
        spriteBatch.end();


        // moveCam
//        if (isZoominActive && selected >=0){
//            moveCamToPosition();
//        }
        // move camera by user input
        spaceInputProcessor.actToPressedKeys(deltaTime);



    }

    private void moveParticles(){
//        ShipObjectModelInstance ship = dynamicObjectHandler.getShip(0);
//        Vector3 shipPos = new Vector3(ship.getPosition());
//        Vector3 shipDir = new Vector3(ship.getPosition());
//        Vector3 shipPosOffset = new Vector3(ship.getPosition());
//        shipPosOffset.sub(0,6,0);
//        Vector3 particlePos = new Vector3(shipPosOffset);
//        shipDir.nor();
//        Matrix4 transform = new Matrix4();
//        transform.setTranslation(shipPos);
//        transform.translate(0,-20,0);
//        // move Particles in Ship Position
//
//        Quaternion rot = ship.getRotation();
//
//        // rotate Particles in opposite Ship direction
//        float xAngle, yAngle;
//        xAngle = rot.getAngleAround(Vector3.Y);
//        yAngle = rot.getAngleAround(Vector3.X);
//
//        Matrix4 mat = new Matrix4();
//
//        Matrix4 rotation = new Matrix4();
//        rotation.set(rot);
//
//
//        particlePos.sub(shipPos);
//        mat.setTranslation(shipPos);
//        mat.mul(rotation);
//        mat.translate(particlePos);
        Matrix4 mat = new Matrix4();
        mat.setTranslation(new Vector3(500,500,600));
        particleEffect.setTransform(mat);

       // xAngle = ship.getRotation().getAxisAngle(Vector3.X);
        // schießt die camera ab!?!?!?!?! es geht kein links rechts rotieren mehr ?????
        //        yAngle = ship.getRotation().getAxisAngle(Vector3.Y);
        for (int i = 0; i < particleEffect.getControllers().size; i++) {
            if (particleEffect.getControllers().get(i).findInfluencer(DynamicsInfluencer.class) != null) {
//                 Gdx.app.log("INFO", "FOUND DI");
                DynamicsInfluencer di = particleEffect.getControllers().get(i).findInfluencer(DynamicsInfluencer.class);
                DynamicsModifier dm;
                for (int j = 0; j < di.velocities.size; j++) {
                    dm = (DynamicsModifier) di.velocities.get(j);
                    if (dm instanceof DynamicsModifier.PolarAcceleration) {
                        // horizontal +/- spread

                        // rotiert um X Achse
//                        float phiSpread = Math.abs(((DynamicsModifier.PolarAcceleration) dm).phiValue.getHighMax() - ((DynamicsModifier.PolarAcceleration) dm).phiValue.getHighMin());
//                        ((DynamicsModifier.PolarAcceleration) dm).phiValue.setHigh(spaceInputProcessor.angleX- 0.5f * phiSpread, spaceInputProcessor.angleX + 0.5f * phiSpread);
//
//                        // rotiert um Y Achse
//                        float thetaSpread = Math.abs(((DynamicsModifier.PolarAcceleration) dm).thetaValue.getHighMax() - ((DynamicsModifier.PolarAcceleration) dm).thetaValue.getHighMin());
//                        ((DynamicsModifier.PolarAcceleration) dm).thetaValue.setHigh(spaceInputProcessor.angleY - thetaSpread * 0.5f, spaceInputProcessor.angleY + thetaSpread * 0.5f); // rotation




                        // rotiert um z Achse
                        ((DynamicsModifier.PolarAcceleration) dm).phiValue.setHigh(spaceInputProcessor.angleX, spaceInputProcessor.angleX );

                        // rotiert um Y Achse
                        ((DynamicsModifier.PolarAcceleration) dm).thetaValue.setHigh(spaceInputProcessor.angleY,spaceInputProcessor.angleY); // rotation
                        // around
                        // y-axis
                    }
                }
            }
        }
    }

    private void renderParticleEffects() {
        particleSystem.update(); // technically not necessary for rendering
        particleSystem.begin();
        particleSystem.draw();
        particleSystem.end();
        modelBatch.render(particleSystem);
    }


    StringBuilder sb = new StringBuilder();
    private void createDebugText(){
        sb.delete(0, sb.length());
        sb.append("Debug:");
        if (selectedObject != null)
            sb.append("\n" + selectedObject.toString());
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
    public void setSelected (int value) {
        if (selected == value) {
            switch(selectedType){
                case Ship: {
                    selectedShip = value;
                    activeTarget = dynamicObjectHandler.getShip(value);
                    selectedObject = dynamicObjectHandler.getShip(value);
                    break;
                }
                case Planet: {
                    activeTarget = spaceMap.getAllObjects().get(value);
                    selectedObject = spaceMap.getAllObjects().get(value);
                    break;
                }
                case Star: {
                    activeTarget = spaceMap.getAllObjects().get(value);
                    selectedObject = spaceMap.getAllObjects().get(value);
                    break;
                }
            }
        }
    }


    // TODO: 12.09.2017 auch dynamicobjects durchlaufen

    /**
     *
     * @param screenX
     * @param screenY
     * @return gibt den Index zurück oder -1 wenn kein object angeklickt!
     */
    public int getObject2(int screenX, int screenY) {
        Ray ray = sPerCam.getPickRay(screenX, screenY);
        int result = -1;
        float distance = -1;
        for (int i = 0; i < spaceMap.getAllObjects().size(); ++i) {
            final float dist2 = spaceMap.getAllObjects().get(i).intersects(ray);
            if (dist2 >= 0f && (distance < 0f || dist2 <= distance)) {
                result = i;
                distance = dist2;
                selectedType = spaceMap.getAllObjects().get(i).getType();
            }
        }
        for (int i = 0; i < dynamicObjectHandler.getShips().size(); ++i) {
            final float dist2 = dynamicObjectHandler.getShip(i).intersects(ray);
            if (dist2 >= 0f && (distance < 0f || dist2 <= distance)) {
                result = i;
                distance = dist2;
                selectedType = dynamicObjectHandler.getShip(i).getType();
            }
        }
        return result;
    }
    //endregion
}
