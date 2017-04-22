package com.goods.game.Terrain;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.Renderable;
import com.badlogic.gdx.graphics.g3d.RenderableProvider;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.model.MeshPart;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.Pool;

import java.util.ArrayList;

/**
 * Created by $USER_NAME on 22.04.2017.
 */
public class TerrainWithObjects implements RenderableProvider, Disposable{

    Terrain terrain;
    ArrayList<Tree> trees;
    ModelInstance instance;
    Model model;
    ModelBuilder modelBuilder;
    public TerrainWithObjects(Terrain terrain) {
        this.terrain = terrain;
        trees = new ArrayList<Tree>();
    }

    @Override
    public void getRenderables(Array<Renderable> renderables, Pool<Renderable> pool) {
        renderables.add(terrain);
Renderable r = new Renderable();
        ModelBuilder modelBuilder = new ModelBuilder();
        model = modelBuilder.createBox(5f, 5f, 5f,
                new Material(ColorAttribute.createDiffuse(Color.GREEN)),
                VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal);
        instance = new ModelInstance(model);

r.set(instance.getRenderable(r));
        renderables.add(r);

    }

    @Override
    public void dispose() {
        terrain.getMeshPart().mesh.dispose();
        model.dispose();
    }
}
