package com.goods.game;

/**
 * Created by $USER_NAME on 15.04.2017.
 */

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.GL30;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.Renderable;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.graphics.g3d.model.MeshPart;
import com.badlogic.gdx.graphics.g3d.utils.CameraInputController;
import com.badlogic.gdx.graphics.g3d.utils.MeshBuilder;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.goods.game.TerrainInet.GeometryBuilder;

public class MeshExample extends ApplicationAdapter {
    ShaderProgram shader;
    Mesh mesh;
    Matrix4 matrix = new Matrix4();
    PerspectiveCamera camera;
    Model model;
    ModelInstance instance;
    public Environment lights;
    public ModelBatch modelBatch;
    Renderable renderable;
    MeshPart mp;
    MeshBuilder meshBuilder;
    @Override
    public void create() {
        String vertexShader = "attribute vec4 a_position;    \n"
                + "attribute vec4 a_color;\n"
                + "attribute vec2 a_texCoord0;\n"
                + "uniform mat4 u_worldView;\n"
                + "varying vec4 v_color;"
                + "varying vec2 v_texCoords;"
                + "void main()                  \n" + "{                            \n"
                + "   v_color = vec4(0.5,0.5,0.5,1); \n"
                + "   v_texCoords = a_texCoord0; \n"
                + "   gl_Position =  u_worldView * a_position;  \n"
                + "}                            \n";
        String fragmentShader = "#ifdef GL_ES\n"
                + "precision mediump float;\n"
                + "#endif\n"
                + "varying vec4 v_color;\n"
                + "varying vec2 v_texCoords;\n"
                + "uniform sampler2D u_texture;\n"
                + "void main()                                  \n"
                + "{                                            \n"
                + "  gl_FragColor = v_color;\n"
                + "}";

        shader = new ShaderProgram(vertexShader, fragmentShader);
        if (!shader.isCompiled()) {
            Gdx.app.log("ShaderTest", shader.getLog());
            Gdx.app.exit();
        }

        camera = new PerspectiveCamera(50f, Gdx.graphics.getWidth(),
                Gdx.graphics.getHeight());
        camera.far = 10000;
        camera.position.set(0, 10f, 10);
        camera.up.set(0, 1, 0);
        camera.direction.set(0, 0, -1);
        camera.rotateAround(new Vector3(), Vector3.X, 60);
        camera.update();

        CameraInputController camController = new CameraInputController(camera);
        Gdx.input.setInputProcessor(camController);
        lights = new Environment();
        // set ambient light ?? stärke vom licht?
        lights.set(new ColorAttribute(ColorAttribute.AmbientLight, 0.4f, 0.4f, 0.4f, 1f));
        // erste wert ist die farbe??? , 2 wert die richtung
        lights.add(new DirectionalLight().set(0.8f, 0.8f, 0.8f, 0f, 0f, -5f));

        modelBatch = new ModelBatch();
        ModelBuilder modelBuilder = new ModelBuilder();
        // Model contains everything on what to render and it keeps track of the resources.
        // aber nicht wo es gerendet werden soll => Instance
        // Cube erstellen mit größe 5,5,5, farbe grün, position??, normal fügt normals der box hinzu
         model = modelBuilder.createBox(5f, 5f, 5f, new Material(ColorAttribute.createDiffuse(Color.GREEN)), VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal);
         instance = new ModelInstance(model);
         mesh = GeometryBuilder.buildPlane(2, 2, 2, false);
        //mesh = GeometryBuilder.buildPlane2(2, 2, 3);
        //mesh =  GeometryBuilder.buildGrid(10,1);


        meshBuilder = new MeshBuilder();

        meshBuilder.addMesh(mesh);
        for(Mesh m:model.meshes){
            meshBuilder.addMesh(m);
        }
        mesh = meshBuilder.end();
    }


    @Override
    public void render() {
       camera.update();
        Gdx.gl30.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        Gdx.gl30.glClearColor(0f, 0f, 0f, 1);
        Gdx.gl30.glClear(GL30.GL_COLOR_BUFFER_BIT);
        Gdx.gl30.glEnable(GL30.GL_BLEND);
        Gdx.gl30.glBlendFunc(GL30.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

        shader.begin();
        shader.setUniformMatrix("u_worldView", camera.combined);
        mesh.render(shader, GL30.GL_LINES);

        shader.end();
    }

    @Override
    public void dispose() {
        mesh.dispose();
        shader.dispose();
    }

}