package com.goods.game;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputMultiplexer;
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
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.graphics.g3d.utils.RenderContext;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Quaternion;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.Ray;
import com.goods.game.Space.GameObjectModelInstance;
import com.goods.game.Space.Planets.PlanetObjectModelInstance;
import com.goods.game.Space.SpaceMap;

import java.util.ArrayList;

public class SpaceTrader extends InputAdapter implements ApplicationListener {
    public Environment environment;
    public PerspectiveCamera perCam;
    public ModelBatch modelBatch;
    public ArrayList<GameObjectModelInstance> instances;
    public ArrayList<ModelInstance> instances2;
    public CameraInputController camController;
    SpriteBatch spriteBatch;
    BitmapFont font;

    public RenderContext renderContext;
    private Object model;
    private ModelBuilder modelBuilder;
    private Model planets;
    private SpaceMap spaceMap;
    private int selected =-1;
    String planetInfos = "";
    private ModelInstance modelInstance;
    public void create () {
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

        instances = new ArrayList<GameObjectModelInstance>();
        spaceMap = new SpaceMap();
        spaceMap.createMap(1000,1000,4,false,0,0);
        spaceMap.fillMapWithObjects();

       // modelInstance = spaceMap.createFrame();


        for (GameObjectModelInstance gameObjects : spaceMap.getAllObjects()) {
            instances.add(gameObjects);
        }

//        for (GameObjectModelInstance gameObjects : spaceMap.getAllObjects()) {
//            instances.add(gameObjects);
//        }





/*
        for (PlanetObjectModelInstance planetsTMP : spaceMap.getPlanets()) {
                 instances.add(planetsTMP);
        }



        instances = new ArrayList<ModelInstance>();
        ModelBuilder modelBuilder = new ModelBuilder();
        model = modelBuilder.createBox(5f, 5f, 5f, new Material(ColorAttribute.createDiffuse(Color.GREEN)), VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal);
        renderContext = new RenderContext(new DefaultTextureBinder(DefaultTextureBinder.WEIGHTED, 1));
        //instances.add(new ModelInstance(model));
        planets = modelBuilder.createSphere(4f,4f,4f,24,24,new Material(ColorAttribute.createDiffuse(Color.RED)), VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal);
        ModelInstance modelinstance = new ModelInstance(planets);
        // modelinstance.transform.setToTranslation(5f,5f,5f);
        instances.add(modelinstance);
        planets = modelBuilder.createSphere(6f,6f,6f,24,24,new Material(ColorAttribute.createDiffuse(Color.GREEN)), VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal);
        modelinstance = new ModelInstance(planets);
        modelinstance.transform.setToTranslation(5f,5f,0f);
        instances.add(modelinstance);
        // 3 floats= size, 2 int = used vertices
        //createSphere(2f,2f,2f,100,100,new Material(ColorAttribute.createDiffuse(Color.YELLOW)), VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal);
        planets = modelBuilder.createSphere(2f,2f,2f,100,100,new Material(ColorAttribute.createDiffuse(Color.YELLOW)), VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal);
        modelinstance = new ModelInstance(planets);
        modelinstance.transform.setToTranslation(-10f,-5f,0f);
        instances.add(modelinstance);
*/
        // Kamera 67Grad, aspect ratio
        perCam = new PerspectiveCamera(67, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        // Position rechtsX  hochY  zurückZ
        // Da aber nicht die kamera sonder alles was gerendet wird verschoben wird bedeuten 0,0,10 das alles plus 10 verschoben wird kamera bleibt 0,0,0 ?
        perCam.position.set(500, 500, 500);
        // 0,0,0 also mitte vom koordinatensys
        perCam.lookAt(500,500,0);
        // ???
        perCam.near = 1f;
        perCam.far = 10000f;
        // update camera => camera einstellungen übernehmen
        perCam.update();
        camController = new CameraInputController(perCam);

        Gdx.input.setInputProcessor(new InputMultiplexer(this,camController));



        modelBatch = new ModelBatch();
    }

    @Override
    public void resize(int width, int height) {

    }
int a = 1;
    Matrix4 mtx = new Matrix4();
    protected Quaternion rotation = new Quaternion();
    protected static Quaternion q = new Quaternion();
    @Override
    public void render () {
        createDebugText();
        camController.update();

        Gdx.gl30.glClearColor(0,0,0,1);
        Gdx.gl30.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        Gdx.gl30.glClear(GL30.GL_COLOR_BUFFER_BIT|GL30.GL_DEPTH_BUFFER_BIT);




        // Wichtig alles mit SetTo überschreibt alle vorherigen werte! es wird also ein neuer wert festgelegt!

        // move
        // working
        // instances.get(0).transform.translate(1,0,0);


        //working
        instances.get(0).transform.rotate(new Vector3(1,1,1), 1);

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

        // Rotate planets around Star

        for (GameObjectModelInstance instance :instances) {
            if (instance.getParentPosition() != null){
                Vector3 starPos = instance.getParentPosition();
                Vector3 planetOffset = new Vector3(instance.getPos());
                planetOffset.sub(starPos);
                instance.transform.setTranslation(starPos).rotate(instance.getRotation(), instance.getOrbitRotationSpeed()).translate(planetOffset);
            }
        }





        modelBatch.begin(perCam);
        modelBatch.render(instances, environment);
        //modelBatch.render(modelInstance, environment);

        modelBatch.end();


        Gdx.gl30.glEnable(GL30.GL_DEPTH_TEST);
        spriteBatch.begin();
        font.draw(spriteBatch,sb.toString(),10,Gdx.graphics.getHeight()-10);
        spriteBatch.end();
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    StringBuilder sb = new StringBuilder();
    private void createDebugText(){
        sb.delete(0, sb.length());
        sb.append("Debug:");
        sb.append("\nCam Dir:"  + perCam.direction.toString());
        sb.append("\nCam Pos:"  + perCam.position.toString());
        sb.append("\nCam Pos comb:"  + perCam.combined.toString());
        sb.append("\nFPS: ").append(Gdx.graphics.getFramesPerSecond());
        sb.append("\nPlanet: ").append(planetInfos);
    }


    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        selected = getObject(screenX, screenY);
        return selected >= 0;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        if (selected >= 0) {
            if (selected == getObject(screenX, screenY)){
                setSelected(selected);
            }
            selected = -1;
            return true;
        }else{
            planetInfos = "";
        }
        return false;
    }

    public void setSelected (int value) {
        if (selected == value) {
            planetInfos = instances.get(value).toString();
        }

    }

    @Override
    public void dispose () {
        modelBatch.dispose();
        spriteBatch.dispose();
        instances.clear();
    }

    public int getObject(int screenX, int screenY) {
        Vector3 position = new Vector3();
        Ray ray = perCam.getPickRay(screenX, screenY);
        int result = -1;
        float distance = -1;
        for (int i = 0; i < instances.size(); ++i) {
            final GameObjectModelInstance instance = instances.get(i);
            instance.transform.getTranslation(position);
            position.add(instance.getCenter());
            float dist2 = ray.origin.dst2(position);
            if (distance >= 0f && dist2 > distance) continue;
            if (Intersector.intersectRaySphere(ray, position, instance.radius, null)) {
                result = i;
                distance = dist2;
            }
        }
        return result;
    }
}
