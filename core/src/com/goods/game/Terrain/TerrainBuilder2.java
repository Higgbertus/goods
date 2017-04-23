package com.goods.game.Terrain;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.utils.MeshBuilder;
import com.badlogic.gdx.graphics.g3d.utils.MeshPartBuilder;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Pools;

/**
 * Created by $USER_NAME on 16.04.2017.
 */
public class TerrainBuilder2 {

    MeshBuilder meshBuilder;

    public TerrainBuilder2() {
        meshBuilder = new MeshBuilder();
    }

    public Terrain createTerrain(String name, int width, int height, int numberOfChunks, boolean withHeight, Material material, Environment environment){
        Terrain terrain = new Terrain(name, width, height, material, environment);
        //terrain.setMesh(createMesh(width, height, numberOfChunks, withHeight));
        return terrain;
    }

    private Model createMesh(int width, int height, int numberOfChunks, boolean withHeight, Material material) {
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

        ModelBuilder builder = new ModelBuilder();
        builder.begin();
        MeshPartBuilder meshPart = builder.part("top", Gdx.gl30.GL_TRIANGLES, VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal, null);


        for (int iy = 0; iy < gridY1; iy++) {
            float y = iy * segment_height - height_half;
            for (int ix = 0; ix < gridX1; ix++) {
                float x = ix * segment_width - width_half;

                vertices[offset] = x;
                vertices[offset + 1] = -y;
                vertices[offset + 2] = 0;
                offset += 3;

                MeshPartBuilder.VertexInfo info = Pools.obtain(MeshPartBuilder.VertexInfo.class);
                info.setCol(MathUtils.random(),MathUtils.random(),MathUtils.random(),0);
                info.setPos(x,-y,0);
                info.setNor(0,0,1f);
                info.setUV(null);
                meshPart.vertex(info);
                Pools.free(info);
            }
        }
        return builder.end();

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
                info.setUV(0f, 0f);
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


}