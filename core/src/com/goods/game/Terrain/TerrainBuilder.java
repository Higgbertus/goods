package com.goods.game.Terrain;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Renderable;
import com.badlogic.gdx.graphics.g3d.RenderableProvider;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.model.MeshPart;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.Pool;

import java.util.ArrayList;

/**
 * Created by $USER_NAME on 16.04.2017.
 */
public class TerrainBuilder{

    public Terrain createTerrain(String name, int width, int height, int numberOfChunks, boolean withHeight, Material material,Texture texture){
        Terrain terrain = new Terrain(name, width, height, material);
        terrain.setMaterial(new Material(ColorAttribute.createDiffuse(Color.GREEN)));
        terrain.setMesh(createMesh(width, height, numberOfChunks));
        return terrain;
    }

    private Mesh createMesh(int width, int height, int numberOfChunks){
        int offset = 0;
        float width_half = width / 2;
        float height_half = height / 2;
        int gridX = numberOfChunks;
        int gridY = numberOfChunks;
        int gridX1 = gridX + 1;
        int gridY1 = gridY + 1;

        float segment_width = width / gridX;
        float segment_height = height / gridY;

        float[] vertices = new float[gridX1 * gridY1 * 3];
        short[] indices = new short[gridX * gridY * 6];

        for (int iy = 0; iy < gridY1; iy++) {
            float y = iy * segment_height - height_half;
            for (int ix = 0; ix < gridX1; ix++) {
                float x = ix * segment_width - width_half;

                vertices[offset] = x;
                vertices[offset + 1] = -y;
                // z erstmal auf 0 setzen
                vertices[offset + 2] = 0;
                offset += 3;
            }
        }

        offset = 0;
        for (int iy = 0; iy < gridY; iy++) {
            for (int ix = 0; ix < gridX; ix++) {
                short a = (short) (ix + gridX1 * iy);
                short b = (short) (ix + gridX1 * (iy + 1));
                short c = (short) ((ix + 1) + gridX1 * (iy + 1));
                short d = (short) ((ix + 1) + gridX1 * iy);

                indices[offset] = a;
                indices[offset + 1] = b;
                indices[offset + 2] = d;

                indices[offset + 3] = b;
                indices[offset + 4] = c;
                indices[offset + 5] = d;

                offset += 6;

            }
        }

         Mesh mesh = new Mesh(true,
                vertices.length / 3,
                indices.length,
                new VertexAttribute(VertexAttributes.Usage.Position, 3, "a_position"));
        mesh.setVertices(vertices);
        mesh.setIndices(indices);
        return mesh;
    }

}