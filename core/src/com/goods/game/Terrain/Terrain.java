package com.goods.game.Terrain;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Renderable;
import com.badlogic.gdx.graphics.g3d.RenderableProvider;
import com.badlogic.gdx.graphics.g3d.model.MeshPart;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.Pool;

/**
 * Created by $USER_NAME on 16.04.2017.
 */

public class Terrain extends Renderable{
    private int width, heigth;
    private boolean withHeights = false;
    private Material material;
    private Texture texture;
    private String name;
    public final Matrix4 transform = new Matrix4();
    private float[] vertices;
    private float[] indices;

    public Terrain(String Name,int width, int height, Material material) {
        this.name = name;
        this.width = width;
        this.heigth = height;
        this.material = material;
    }

    public boolean isWithHeights() {
        return withHeights;
    }

    public void setWithHeights(boolean withHeights) {
        this.withHeights = withHeights;
    }

    public Material getMaterial() {
        return material;
    }

    public void setMaterial(Material material) {
        this.material = material;
    }

    public Texture getTexture() {
        return texture;
    }

    public void setTexture(Texture texture) {
        this.texture = texture;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float[] getVertices() {
        return vertices;
    }

    public void setVertices(float[] vertices) {
        this.vertices = vertices;
    }

    public float[] getIndices() {
        return indices;
    }

    public void setIndices(float[] indices) {
        this.indices = indices;
    }


    public MeshPart getMeshPart() {
        return meshPart;
    }

    public void setMesh(Mesh mesh) {
        meshPart.mesh = mesh;
        meshPart.offset = 0;
        meshPart.size = mesh.getNumIndices();
        meshPart.primitiveType = Gdx.gl30.GL_LINES;
        meshPart.update();
    }


}
