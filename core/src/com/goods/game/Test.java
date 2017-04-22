package com.goods.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL30;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.Texture;
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
import com.badlogic.gdx.graphics.g3d.attributes.TextureAttribute;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.graphics.g3d.utils.CameraInputController;
import com.badlogic.gdx.graphics.g3d.utils.MeshBuilder;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.math.Matrix4;
import com.goods.game.Terrain.Terrain;
import com.goods.game.Terrain.TerrainBuilder;
import com.goods.game.Terrain.TerrainWithObjects;


import java.util.ArrayList;

public class Test extends ApplicationAdapter {
    public Environment lights;
    public PerspectiveCamera perCam;
    public ModelBatch modelBatch;
    public Model model;
    public Mesh mesh;
    public ModelInstance instance, instanceArea;
    public ArrayList<ModelInstance> instances,instances2;
    public CameraInputController camController;
    public Material material;
    TerrainBuilder terrainBuilder;
    private MeshBuilder meshBuilder;
    Terrain terrainBig, terrainSmall;
    AssetManager assetManager;
   SpriteBatch spriteBatch;
    BitmapFont font;
    Texture texture;
    TerrainWithObjects terrainWithObjects;
    public boolean loading;
    @Override
    public void create () {
        spriteBatch = new SpriteBatch();
        assetManager = new AssetManager();
       // assetManager.load("models/Leuchtturm.g3db",Model.class);

        FreeTypeFontGenerator generator2 = new FreeTypeFontGenerator(Gdx.files.internal("fonts/BRLNSR.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 25;
        parameter.color = Color.RED;

        font = generator2.generateFont(parameter); // font size 12 pixels
        generator2.dispose(); // don't forget to dispose to avoid memory leaks!



        instances = new ArrayList<ModelInstance>();
        instances2 = new ArrayList<ModelInstance>();
        lights = new Environment();
        // set ambient light ?? stärke vom licht?
        lights.set(new ColorAttribute(ColorAttribute.AmbientLight, 0.4f, 0.4f, 0.4f, 1f));
        // erste wert ist die farbe??? , 2 wert die richtung
        lights.add(new DirectionalLight().set(0.8f, 0.8f, 0.8f, 0f, 0f, -5f));

        modelBatch = new ModelBatch();

        // Kamera 67Grad, aspect ratio
        perCam = new PerspectiveCamera(67, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        // Position rechtsX  hochY  zurückZ
        // Da aber nicht die kamera sonder alles was gerendet wird verschoben wird bedeuten 0,0,10 das alles plus 10 verschpben wird kamera bleibt 0,0,0 ?
        perCam.position.set(0f, 0f, 300f);
        // 0,0,0 also mitte vom koordinatensys
        perCam.lookAt(0,0,0);
        // ???
        perCam.near = 1f;
        perCam.far = 1000f;
        // update camera => camera einstellungen übernehmen

        perCam.update();

        camController = new CameraInputController(perCam);
        Gdx.input.setInputProcessor(camController);

        texture = new Texture("badlogic.jpg");
        material = new Material(TextureAttribute.createDiffuse(texture));
        terrainBuilder = new TerrainBuilder();
        terrainBig = terrainBuilder.createTerrain("Big", 1000, 1000, 100, true, material,lights);
        terrainSmall = terrainBuilder.createTerrain("Small", 10, 10, 10, false, material, lights);
        terrainWithObjects = new TerrainWithObjects(terrainBig);
        ModelBuilder modelBuilder = new ModelBuilder();
        model = modelBuilder.createBox(5f, 5f, 5f, new Material(ColorAttribute.createDiffuse(Color.GREEN)), VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal);
        instances.add(new ModelInstance(model));
        instance = new ModelInstance(model);

        //textTransformation.idt().scl(0.2f).rotate(0, 0, 1, 45).translate(-50, 2, 25f);
        assetManager.load("models/leaftree_1.g3db",Model.class);
        loading = true;
    }

    Matrix4 textTransformation = new Matrix4();
    private void doneLoading() {
        Model ship = assetManager.get("models/leaftree_1.g3db", Model.class);
        ModelInstance shipInstance = new ModelInstance(ship);
        instance = shipInstance;
        loading = false;
    }

    @Override
    public void render () {
            if (loading && assetManager.update())
                doneLoading();
        createDebugText();
        camController.update();

        Gdx.gl30.glClearColor(0,0,0,1);
        Gdx.gl30.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        Gdx.gl30.glClear(GL30.GL_COLOR_BUFFER_BIT|GL30.GL_DEPTH_BUFFER_BIT);

        modelBatch.begin(perCam);
        texture.bind();
        modelBatch.render(terrainBig);
        modelBatch.end();

        modelBatch.begin(perCam);
        modelBatch.render(instance, lights);
        modelBatch.end();


        Gdx.gl30.glEnable(GL30.GL_DEPTH_TEST);
        //spriteBatch.setProjectionMatrix(perCam.combined.mul(textTransformation));
        spriteBatch.begin();
       // assetManager.get("size10.ttf");
        font.draw(spriteBatch,sb.toString(),10,Gdx.graphics.getHeight()-10);
        spriteBatch.end();
    }

    StringBuilder sb = new StringBuilder();
    private void createDebugText(){
        sb.delete(0, sb.length());
        sb.append("Debug:");
        sb.append("\nCam Dir:"  + perCam.direction.toString());
        sb.append("\nCam Pos:"  + perCam.position.toString());
        sb.append("\nCam Pos:"  + perCam.combined.toString());
    }

    @Override
    public void dispose () {
        modelBatch.dispose();
        terrainSmall.getMeshPart().mesh.dispose();
        model.dispose();
        spriteBatch.dispose();
        assetManager.dispose();
//        terrainWithObjects.dispose();
    }
}
