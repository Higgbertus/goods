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
import com.badlogic.gdx.graphics.g3d.Renderable;
import com.badlogic.gdx.graphics.g3d.Shader;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.attributes.TextureAttribute;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.graphics.g3d.model.NodePart;
import com.badlogic.gdx.graphics.g3d.shaders.DefaultShader;
import com.badlogic.gdx.graphics.g3d.utils.CameraInputController;
import com.badlogic.gdx.graphics.g3d.utils.DefaultTextureBinder;
import com.badlogic.gdx.graphics.g3d.utils.MeshBuilder;
import com.badlogic.gdx.graphics.g3d.utils.MeshPartBuilder;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.graphics.g3d.utils.RenderContext;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Pools;
import com.goods.game.Terrain.TerrainBuilder;


import java.util.ArrayList;

public class Main extends ApplicationAdapter {
    public Environment environment;
    public PerspectiveCamera perCam;
    public ModelBatch modelBatch;
    public Model model, map;
    public ModelInstance instance, instanceArea;
    public ArrayList<ModelInstance> instances,instances2;
    public CameraInputController camController;
    public Material material;
    AssetManager assetManager;
   SpriteBatch spriteBatch;
    BitmapFont font;
    Texture texture;
    public Shader shader;

    public RenderContext renderContext;
Renderable renderable;

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

        // Lichteinstellungen
        environment = new Environment();

        environment.set(new ColorAttribute(ColorAttribute.AmbientLight, 0.4f, 0.4f, 0.4f, 1f));
        // rgb + position (x,y,z)
        environment.add(new DirectionalLight().set(0.8f, 0.8f, 0.8f, -1f, -0.8f, 10f));

        modelBatch = new ModelBatch();

        // Kamera 67Grad, aspect ratio
        perCam = new PerspectiveCamera(67, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        // Position rechtsX  hochY  zurückZ
        // Da aber nicht die kamera sonder alles was gerendet wird verschoben wird bedeuten 0,0,10 das alles plus 10 verschpben wird kamera bleibt 0,0,0 ?
        perCam.position.set(0f, 0f, 10);
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

        ModelBuilder modelBuilder = new ModelBuilder();
        model = modelBuilder.createBox(5f, 5f, 5f, new Material(ColorAttribute.createDiffuse(Color.GREEN)), VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal);
        renderContext = new RenderContext(new DefaultTextureBinder(DefaultTextureBinder.WEIGHTED, 1));
        instances.add(new ModelInstance(model));
        instance = new ModelInstance(model);
        TerrainBuilder terrainBuilder = new TerrainBuilder();
        map = terrainBuilder.createTerrainEachTriangleOwnVertices(30,30,30,false);

        instanceArea = new ModelInstance(map);
        NodePart blockPart = map.nodes.get(0).parts.get(0);

        renderable = new Renderable();
        blockPart.setRenderable(renderable);
        renderable.environment = environment;
        renderable.worldTransform.idt();
        String vert = Gdx.files.internal("shader/vertexShader.glsl").readString();
        String frag = Gdx.files.internal("shader/fragmentShader.glsl").readString();
        shader = new DefaultShader(renderable, new DefaultShader.Config(vert, frag));
        shader.init();

        //textTransformation.idt().scl(0.2f).rotate(0, 0, 1, 45).translate(-50, 2, 25f);
        assetManager.load("models/House.g3db",Model.class);
        loading = true;
    }

    Matrix4 textTransformation = new Matrix4();
    private void doneLoading() {
        Model ship = assetManager.get("models/House.g3db", Model.class);
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

//        renderContext.begin();
//        shader.begin(perCam, renderContext);
//        shader.render(renderable);
//        shader.end();
//        renderContext.end();

        modelBatch.begin(perCam);
        //modelBatch.render(terrainBig);
        modelBatch.render(instanceArea, environment);
        //modelBatch.render(instance, environment);
        modelBatch.render(renderable);
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
     //   terrainSmall.getMeshPart().mesh.dispose();
        model.dispose();
        map.dispose();
        spriteBatch.dispose();
        assetManager.dispose();
//        terrainWithObjects.dispose();
    }
}
