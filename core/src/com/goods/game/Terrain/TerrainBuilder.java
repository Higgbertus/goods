package com.goods.game.Terrain;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.model.MeshPart;
import com.badlogic.gdx.graphics.g3d.model.Node;
import com.badlogic.gdx.graphics.g3d.model.NodePart;
import com.badlogic.gdx.graphics.g3d.utils.MeshBuilder;
import com.badlogic.gdx.graphics.g3d.utils.MeshPartBuilder;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Pools;

import java.awt.image.BufferedImage;

public class TerrainBuilder {
    ModelBuilder modelBuilder;


Model tmp;
    public Model createTerrainEachTriangleOwnVertices(int width, int length, int numberOfChunks, boolean withHeight) {

        modelBuilder = new ModelBuilder();
        Color color;
        float chunkWidth = width / numberOfChunks;
        float chunkLength = length / numberOfChunks;
        int gridX =numberOfChunks+1;
        int gridY =numberOfChunks+1;

        float[] heights = new float[(int)Math.pow((numberOfChunks+1),2)];
        heights[0] = MathUtils.random(-5f,20f);
        //todo es reicht glaub au je 1 pos für x und y
        float posX1 = 0f;
        float posX2 = chunkWidth;
        float posY1 = 0f;
        float posY2 = chunkLength;

        modelBuilder.begin();
// todo crashed bei ca 80,80,80 => zuviele vertices (>32k)... Aufteilen in mehrere parts???!?!?
        MeshPartBuilder meshPartBuilder = modelBuilder.part("terrain", Gdx.gl30.GL_TRIANGLES, VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal | VertexAttributes.Usage.ColorPacked, new Material(ColorAttribute.createDiffuse(Color.RED)));
        for (int iY = 1; iY < gridY; iY++) {
            for (int iX = 1; iX < gridX; iX++) {
                for (MeshPartBuilder.VertexInfo info:createRectangle(posX1, posX2, posY1, posY2)) {
                    meshPartBuilder.index(meshPartBuilder.vertex(info));
                }
                posX1 = chunkWidth*iX;
                posX2 = chunkWidth*(iX+1);
            }
            posX1 = 0f;
            posX2 = chunkWidth;
            posY1 = chunkLength*iY;
            posY2 = chunkLength*(iY+1);
        }
        return modelBuilder.end();
    }

    private void createImageFromMesh(int width, int length, float[] heights){

        int type = BufferedImage.TYPE_INT_ARGB;

        BufferedImage image = new BufferedImage(width, length, type);

        int color = 255; // RGBA value, each component in a byte

        for(int x = 0; x < width; x++) {
            for(int y = 0; y < length; y++) {
                image.setRGB(x, y, color);
            }
        }
    }

    private void info(){
        /*
        gett meshpart, vertices etc.
        Node n  = tmp.getNode("node1");
        NodePart np = n.parts.get(0);
        MeshPart mp = np.meshPart;
        */
    }

    private MeshPartBuilder.VertexInfo[] createRectangle(float posX1, float posX2, float posY1, float posY2){
        Color color = new Color(0,MathUtils.random(1f),0,0);
        MeshPartBuilder.VertexInfo[] rectangle = new MeshPartBuilder.VertexInfo[6];
        rectangle[0] = createVertexInfo(posX1, posY2, color);
        rectangle[1] = createVertexInfo(posX1, posY1, color);
        rectangle[2] = createVertexInfo(posX2, posY2, color);
        color = new Color(0,MathUtils.random(1f),0,0);
        rectangle[3] = createVertexInfo(posX2, posY2, color);
        rectangle[4] = createVertexInfo(posX1, posY1, color);
        rectangle[5] = createVertexInfo(posX2, posY1, color);
        return rectangle;
    }

    private MeshPartBuilder.VertexInfo createVertexInfo(float posX, float posY, Color color){
        MeshPartBuilder.VertexInfo info = new MeshPartBuilder.VertexInfo();
        // third corner top right
        info.setPos(posX,posY,0);
        info.setCol(color);
        info.setNor(0, 0, 1f);
        return info;
    }

}