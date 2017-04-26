package com.goods.game.TerrainInet;

/**
 * Created by $USER_NAME on 15.04.2017.
 */
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;

import java.util.Random;

public class GeometryBuilder {
    public static Mesh buildGrid(int size, int step) {
        Random rnd = new Random();
        float[] vertices = new float[size * size * 12];
        int offset = 0;
        float y = 0;

        for (int i = -size; i <= size; i += step) {
            vertices[offset] = -size;
            vertices[offset + 1] = y;
            vertices[offset + 2] = i;

            vertices[offset + 3] = size;
            vertices[offset + 4] = y;
            vertices[offset + 5] = i;

            vertices[offset + 6] = i;
            vertices[offset + 7] = y;
            vertices[offset + 8] = -size;

            vertices[offset + 9] = i;
            vertices[offset + 10] = y;
            vertices[offset + 11] = size;

            offset += 12;
        }

        Mesh mesh = new Mesh(true, vertices.length / 3, 0, new VertexAttribute(
                VertexAttributes.Usage.Position, 3, ShaderProgram.POSITION_ATTRIBUTE));
        mesh.setVertices(vertices);

        return mesh;
    }

    /**
     *
     * @param width
     * @param height
     * @param numberOfChunks
     * @param withHeight flat or with hils etc.
     * @return
     */
    // 2,2,2,false
    public static Mesh buildPlane(int width, int height, int numberOfChunks, boolean withHeight) {
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


        if (withHeight) {
            float[] d = ImprovedNoise.heightData(numberOfChunks);
            offset = 2;
            for (int f = 0; f < vertices.length / 3; f++) {
                vertices[offset] = d[f];
                offset += 3;
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

    /**
     *
     * @param width breite
     * @param height höhe
     * @param numberOfChunks anzahl von rechtecken
     * @return mesh
     */
    public static Mesh buildPlane2(int width, int height, int numberOfChunks) {
        int count = numberOfChunks * numberOfChunks;
        float[] vertices = new float[count * 3];
        float[] normals = new float[count * 3];
        float[] textureCoords = new float[count*2];
        short[] indices = new short[6*(numberOfChunks-1)*(numberOfChunks-1)];
        int vertexPointer = 0;
        for(int i=0;i<numberOfChunks;i++){
            for(int j=0;j<numberOfChunks;j++){
                vertices[vertexPointer*3] = (float)j/((float)numberOfChunks - 1) * width;
                vertices[vertexPointer*3+1] = 0;
                vertices[vertexPointer*3+2] = (float)i/((float)numberOfChunks - 1) * height;
                normals[vertexPointer*3] = 0;
                normals[vertexPointer*3+1] = 1;
                normals[vertexPointer*3+2] = 0;
                textureCoords[vertexPointer*2] = (float)j/((float)numberOfChunks - 1);
                textureCoords[vertexPointer*2+1] = (float)i/((float)numberOfChunks - 1);
                vertexPointer++;
            }
        }
        int pointer = 0;
        for(int gz=0;gz<numberOfChunks-1;gz++){
            for(int gx=0;gx<numberOfChunks-1;gx++){
                short topLeft =  (short)((gz*numberOfChunks)+gx);
                short topRight =  (short)(topLeft + 1);
                short bottomLeft =  (short)(((gz+1)*numberOfChunks)+gx);
                short bottomRight =  (short)(bottomLeft + 1);
                indices[pointer++] = topLeft;
                indices[pointer++] = bottomLeft;
                indices[pointer++] = topRight;
                indices[pointer++] = topRight;
                indices[pointer++] = bottomLeft;
                indices[pointer++] = bottomRight;
            }
        }
       // return loader.loadToVAO(vertices, textureCoords, normals, indices);

        Mesh mesh = new Mesh(true,
                vertices.length / 3,
                indices.length,
                new VertexAttribute(VertexAttributes.Usage.Position, 3, "a_position"));
        mesh.setVertices(vertices);
        mesh.setIndices(indices);

        return mesh;
    }

}