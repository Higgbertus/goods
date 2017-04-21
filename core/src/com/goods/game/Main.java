package com.goods.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL30;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.VertexAttributes;
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
import com.badlogic.gdx.graphics.g3d.utils.MeshPartBuilder;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.ui.List;
import com.badlogic.gdx.utils.Pools;

import java.util.ArrayList;

public class Main extends ApplicationAdapter {
    public Environment lights;
    public PerspectiveCamera cam;
    public ModelBatch modelBatch;
    public Model model;
    public Mesh mesh;
    public ModelInstance instance, instanceArea;
    public ArrayList<ModelInstance> instances,instances2;
    public CameraInputController camController;
	
	@Override
	public void create () {
        instances = new ArrayList<ModelInstance>();
        instances2 = new ArrayList<ModelInstance>();
        lights = new Environment();
        // set ambient light ?? stärke vom licht?
        lights.set(new ColorAttribute(ColorAttribute.AmbientLight, 0.4f, 0.4f, 0.4f, 1f));
        // erste wert ist die farbe??? , 2 wert die richtung
        lights.add(new DirectionalLight().set(0.8f, 0.8f, 0.8f, 0f, 0f, -5f));

        modelBatch = new ModelBatch();

        // Kamera 67Grad, aspect ratio
        cam = new PerspectiveCamera(67, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        // Position rechtsX  hochY  zurückZ
        // Da aber nicht die kamera sonder alles was gerendet wird verschoben wird bedeuten 0,0,10 das alles plus 10 verschpben wird kamera bleibt 0,0,0 ?
        cam.position.set(0f, 0f, 10f);
        // 0,0,0 also mitte vom koordinatensys
        cam.lookAt(0,0,0);
        // ???
        cam.near = 1f;
        // Wie weit die camera sehen kann, was also noch dargestellt wird und was nicht
        cam.far = 300f;
        // update camera => camera einstellungen übernehmen
        cam.update();
        camController = new CameraInputController(cam);
        Gdx.input.setInputProcessor(camController);


        // Cube
        //ModelBuilder modelBuilder = new ModelBuilder();
        // Model contains everything on what to render and it keeps track of the resources.
        // aber nicht wo es gerendet werden soll => Instance
        // Cube erstellen mit größe 5,5,5, farbe grün, position??, normal fügt normals der box hinzu
        //model = modelBuilder.createBox(5f, 5f, 5f, new Material(ColorAttribute.createDiffuse(Color.GREEN)), VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal);

        // ModelInstance contains the location, rotation and scale the model should be rendered at.
        // By default this is at (0,0,0), so we just create a ModelInstance which should be rendered at (0,0,0)
        //instanceArea = new ModelInstance(model);




        //Texture texture = new Texture(Gdx.files.internal("assets/badlogic.jpg"));
        //Texture texture = new Texture("badlogic.jpg");
       // Material material = new Material(TextureAttribute.createDiffuse(texture));
    /*    Material material = new Material();
        MeshBuilder meshBuilder = new MeshBuilder();
        meshBuilder.begin(VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal | VertexAttributes.Usage.ColorPacked | VertexAttributes.Usage.TextureCoordinates, Gdx.gl30.GL_TRIANGLES);
        meshBuilder.box(5f, 1f, 1f);
        Mesh mesh = new Mesh(true, meshBuilder.getNumVertices(), meshBuilder.getNumIndices(), meshBuilder.getAttributes());
        mesh = meshBuilder.end(mesh);

        ModelBuilder modelBuilder = new ModelBuilder();
        modelBuilder.begin();
        //modelBuilder.manage(texture);

        modelBuilder.node().id = "box";
        MeshPartBuilder mpb = modelBuilder.part("box", Gdx.gl30.GL_TRIANGLES, VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal | VertexAttributes.Usage.TextureCoordinates
                | VertexAttributes.Usage.ColorPacked, material);
        mpb.setColor(Color.RED);
        mpb.box(1f, 1f, 1f);

        modelBuilder.node().id = "sphere";
        mpb = modelBuilder.part("sphere", Gdx.gl30.GL_TRIANGLES, VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal | VertexAttributes.Usage.TextureCoordinates
                | VertexAttributes.Usage.ColorPacked, material);
        mpb.sphere(2f, 2f, 2f, 10, 5);

        modelBuilder.node().id = "cone";
        mpb = modelBuilder.part("cone", Gdx.gl30.GL_TRIANGLES, VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal | VertexAttributes.Usage.TextureCoordinates
                | VertexAttributes.Usage.ColorPacked, material);
        mpb.setVertexTransform(new Matrix4().rotate(Vector3.X, -45f));
        mpb.cone(2f, 3f, 1f, 8);

        modelBuilder.node().id = "cylinder";
        mpb = modelBuilder.part("cylinder", Gdx.gl30.GL_TRIANGLES, VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal | VertexAttributes.Usage.TextureCoordinates
                | VertexAttributes.Usage.ColorPacked, material);
        mpb.setUVRange(1f, 1f, 0f, 0f);
        mpb.cylinder(2f, 4f, 3f, 15);

        modelBuilder.node().id = "mesh";
        mpb = modelBuilder.part("mesh", Gdx.gl30.GL_TRIANGLES, mesh.getVertexAttributes(), material);
        Matrix4 transform = new Matrix4();
        mpb.setVertexTransform(transform.setToTranslation(0, 2, 0));
        mpb.addMesh(mesh);
        mpb.setColor(Color.BLUE);
        mpb.setVertexTransform(transform.setToTranslation(1, 1, 0));
        mpb.addMesh(mesh);
        mpb.setColor(null);
        mpb.setVertexTransform(transform.setToTranslation(-1, 1, 0).rotate(Vector3.X, 45));
        mpb.addMesh(mesh);
        mpb.setVertexTransform(transform.setToTranslation(0, 1, 1));
        mpb.setUVRange(0.75f, 0.75f, 0.25f, 0.25f);
        mpb.addMesh(mesh);

        model = modelBuilder.end();

        instances.add(new ModelInstance(model, new Matrix4().trn(0f, 0f, 0f), "mesh", true));
        instances.add(new ModelInstance(model, new Matrix4().trn(-5f, 0f, -5f), "box", true));
        instances.add(new ModelInstance(model, new Matrix4().trn(5f, 0f, -5f), "sphere", true));
        instances.add(new ModelInstance(model, new Matrix4().trn(-5f, 0f, 5f), "cone", true));
        instances.add(new ModelInstance(model, new Matrix4().trn(5f, 0f, 5f), "cylinder", true));
*/


        model = makeTerrain(new Material(ColorAttribute.createDiffuse(Color.GREEN)),5,4,1f);
        instance = new ModelInstance(model);

	}

    private static Model makeTerrain(Material ground, int width, int length, float tileSize)
    {
        ModelBuilder builder = new ModelBuilder();

        builder.begin();
        MeshPartBuilder meshPart = builder.part("top", Gdx.gl30.GL_TRIANGLES, VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal | VertexAttributes.Usage.TextureCoordinates, ground);

        for(int i = 0; i <= width; i++)
        {
            for(int j = 0; j <= length; j++)
            {
                float x = i - width * tileSize / 2f - tileSize / 2f;
                float y = j - length * tileSize / 2f - tileSize / 2f;


                MeshPartBuilder.VertexInfo info = Pools.obtain(MeshPartBuilder.VertexInfo.class);
                info.setPos(x, 0f, y);
                //info.setUV(0f, 0f); ???
                meshPart.vertex(info);
                Pools.free(info);


                if(i < width && j < length) {

                    meshPart.index(meshPart.lastIndex(), (short) (i * (length + 1) + j + 1), (short) ((i + 1) * (length + 1) + j));
                }
                if(i > 0 && j > 0) {
                    meshPart.index(meshPart.lastIndex(), (short) (i * (length + 1) + j - 1), (short) ((i - 1) * (length + 1) + j));
                }
            }
        }

        return builder.end();
    }

	@Override
	public void render () {

        camController.update();

        //
        Gdx.gl30.glClearColor(0,0,0,1);
        Gdx.gl30.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		Gdx.gl30.glClear(GL30.GL_COLOR_BUFFER_BIT|GL30.GL_DEPTH_BUFFER_BIT);


        modelBatch.begin(cam);
        modelBatch.render(instance, lights);
        //modelBatch.render(instances2, lights);
        modelBatch.end();
	}
	
	@Override
	public void dispose () {
        modelBatch.dispose();
        model.dispose();
	}
}
