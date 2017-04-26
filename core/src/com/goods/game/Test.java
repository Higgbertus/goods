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
import com.goods.game.Terrain.Terrain;
import com.goods.game.Terrain.TerrainBuilder;


import java.util.ArrayList;

public class Test extends ApplicationAdapter {
    public Environment environment;
    public DirectionalLight light;
    public PerspectiveCamera perCam;
    public ModelBatch modelBatch;
    public Model model, map;
    public Mesh mesh;
    public ModelInstance instance, instanceArea;
    public ArrayList<ModelInstance> instances,instances2;
    public CameraInputController camController;
    public Material material;
    private MeshBuilder meshBuilder;
    Terrain terrainBig, terrainSmall;
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
        environment.add(new DirectionalLight().set(0.8f, 0.8f, 0.8f, -1f, -0.8f, -0.2f));

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
        //map = createTerrainWithShardeVertices(1,1,1,false,new Material(ColorAttribute.createDiffuse(Color.RED)));
        TerrainBuilder terrainBuilder = new TerrainBuilder();
        map = terrainBuilder.createTerrainEachTriangleOwnVertices(70,70,70,false);

        instanceArea = new ModelInstance(map);
        NodePart blockPart = map.nodes.get(0).parts.get(0);

        renderable = new Renderable();
        blockPart.setRenderable(renderable);
        renderable.environment = null;
        renderable.worldTransform.idt();
        String vert = Gdx.files.internal("shader/vertexShader.glsl").readString();
        String frag = Gdx.files.internal("shader/fragmentShader.glsl").readString();
        shader = new DefaultShader(renderable, new DefaultShader.Config(vert, frag));
        shader.init();

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


    /**
     *
     * @param width Width of the terrain
     * @param length Length of the terrain
     * @param numberOfChunks Number of Chunks that will be generated in the square of length*width
     * @param withHeight generate heights
     * @param material Material useless?
     * @return
     */
    private Model createTerrainWithShardeVertices(int width, int length, int numberOfChunks, boolean withHeight, Material material) {
        int offset = 0;
        float width_half = width / 2;
        float height_half = length / 2;
        int gridX = numberOfChunks;
        int gridY = numberOfChunks;
        int gridX1 = gridX + 1;
        int gridY1 = gridY + 1;

        float segment_width = width / gridX;
        float segment_height = length / gridY;



        ModelBuilder builder = new ModelBuilder();
        builder.begin();
        MeshPartBuilder meshPart = builder.part("top", Gdx.gl30.GL_TRIANGLES, VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal | VertexAttributes.Usage.ColorPacked, material);
float z = 0f;
        int counter= 0;

        Color color = new Color();
        for (int iy = 0; iy < gridY1; iy++) {
            float y = iy * segment_height - height_half;
            for (int ix = 0; ix < gridX1; ix++) {
                float x = ix * segment_width - width_half;

                MeshPartBuilder.VertexInfo info = Pools.obtain(MeshPartBuilder.VertexInfo.class);
                //info.setCol(MathUtils.random(),MathUtils.random(),MathUtils.random(),0);

                if (withHeight) {
                    z = MathUtils.random(20);
                    info.setPos(x, -y, 0);
                    info.setCol(Color.WHITE);
                } else {
                    if (counter == 0){
                        color = new Color(0,MathUtils.random(1f),0,0);
                        info.setCol(color);
                        info.setPos(x, -y, 0);
                    }else{
                        info.setCol(color);
                        info.setPos(x, -y, 0);
                    }
                    if(counter++ == 2){
                        counter =0;
                    }
                }


                //info.setPos(x,-y,0);
                info.setNor(0, 0, 1f);

                //info.setUV(0,1);
                meshPart.vertex(info);
                Pools.free(info);

                //todo abcd berechnen siehe skizze...

                //if(iy !=gridY1-1)
                //  meshPart.index(a,b,d,b,c,d);
                // Indexierung bei jedem Chunk reicht aus
                // meshPart.index(0,2,3,3,1,0);
                // meshPart.index(a,c,d,d,b,a);
                // meshPart.index(a,d,c,b,d,d);
            }
        }
        for (int iy = 0; iy < gridY; iy++) {
            for (int ix = 0; ix < gridX; ix++) {
                short a = (short) (ix + gridX1 * iy);
                short b = (short) (ix + gridX1 * (iy + 1));
                short c = (short) ((ix + 1) + gridX1 * (iy + 1));
                short d = (short) ((ix + 1) + gridX1 * iy);
                meshPart.index(a,b,d,b,c,d);
            }
        }



        return builder.end();

    }

    private Model createTerrainEachTriangleOwnVertices(int width, int length, int numberOfChunks, boolean withHeight, Material material) {
        Vector3 corner1;
        Vector3 corner2;
        Vector3 corner3;
        Vector3 corner4;

        float chunkWidth = width / numberOfChunks;
        float chunkLength = length / numberOfChunks;

        int gridX =numberOfChunks +1;
        int gridY =numberOfChunks +1;

        for (int iX = 0; iX < gridX; iX++) {
            for (int iY = 0; iY < gridY; iY++) {

            }
        }
        
        ModelBuilder builder = new ModelBuilder();
        builder.begin();
        MeshPartBuilder meshPart = builder.part("top", Gdx.gl30.GL_TRIANGLES, VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal | VertexAttributes.Usage.ColorUnpacked, material);

        MeshPartBuilder.VertexInfo info = Pools.obtain(MeshPartBuilder.VertexInfo.class);
        //info.setCol(MathUtils.random(),MathUtils.random(),MathUtils.random(),0);



        return builder.end();

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


        renderContext.begin();
        shader.begin(perCam, renderContext);
        shader.render(renderable);
        shader.end();
        renderContext.end();

        //modelBatch.begin(perCam);
        //modelBatch.render(terrainBig);
        //modelBatch.render(instanceArea);
        //modelBatch.render(instance, environment);
       // modelBatch.render(renderable);
        //modelBatch.end();


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
