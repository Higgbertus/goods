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
import java.util.ArrayList;

public class TerrainBuilder {
    ModelBuilder modelBuilder;
    ImprovedNoise improvedNoise;
    float maxHeight = 20f;
    float maxDepth = -5f;
Color[] colors;
    public ArrayList<Vector3> getNormals() {
        return normals;
    }

    ArrayList<Vector3> normals;
    Model tmp;
    public Model createTerrainEachTriangleOwnVertices(int width, int length, int numberOfChunks, boolean withHeight) {
        normals = new ArrayList<Vector3>();
        // todo optimize color so only 1 float is used not 4!!!
        colors = new Color[3];
        colors[0] = new Color(0,0.3f,0,0);
        colors[1] = new Color(0,0.4f,0,0);
        colors[2] = new Color(0,0.5f,0,0);

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
        MeshPartBuilder meshPartBuilder = modelBuilder.part("terrain", Gdx.gl30.GL_TRIANGLES, VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal | VertexAttributes.Usage.ColorUnpacked, new  Material());
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
        tmp = modelBuilder.end();

        return tmp;
    }

    private void createImageFromMesh(int gridX, int gridY){

        Color colHeight = new Color();
        float height;
        Node n  = tmp.getNode("node1");
        NodePart np = n.parts.get(0);
        MeshPart mp = np.meshPart;
        Mesh mesh = mp.mesh;
        float[] vertices = new float[mesh.getMaxVertices()];
        mesh.getVertices(vertices);
        for (int i = 0; i < vertices.length; i+=10) {

            Color color = new Color(vertices[i],vertices[i+1],vertices[i+2],vertices[i+3]);
            Vector3 normal = new Vector3(vertices[i+4],vertices[i+5],vertices[i+6]);
            Vector3 position = new Vector3(vertices[i+7],vertices[i+8],vertices[i+9]);

            if (vertices[i+9] <0){
                height = Math.abs(vertices[i+9]);
            }else{
                height = Math.abs(vertices[i+9]) + Math.abs(maxDepth);
            }

            float maxHeightSum = Math.abs(maxDepth)+Math.abs(maxHeight);
            height = height/maxHeightSum;
            colHeight = new Color(height,height,height,0);
        }



        int type = BufferedImage.TYPE_INT_ARGB;

        BufferedImage image = new BufferedImage(gridX, gridY, type);

        int color = colHeight.toIntBits(); //RGBA value, each component in a byte

        for(int x = 0; x < gridX; x++) {
            for(int y = 0; y < gridY; y++) {
                image.setRGB(x, y, color);
            }
        }
    }

    private void test(){

        // get meshpart, vertices etc.
        Node n  = tmp.getNode("node1");
        NodePart np = n.parts.get(0);
        MeshPart mp = np.meshPart;
       VertexAttributes atr = mp.mesh.getVertexAttributes();
    }

    private Vector3 calculateNormal(Vector3 vec1, Vector3 vec2, Vector3 vec3) {
        Vector3 edge2 = vec2.cpy();
        Vector3 edge3 = vec3.cpy();
        edge2.sub(vec1);
        edge3.sub(vec1);
        Vector3 crsProd = edge2.crs(edge3); // Cross product between edge1 and edge2
        Vector3 normal = crsProd.nor(); // Normalization of the vector
        normals.add(normal);
        //new Vector3(1,2,3)
        return new Vector3(1,2,3);
    }

    private MeshPartBuilder.VertexInfo[] createRectangle(float posX1, float posX2, float posY1, float posY2){

        /*      Rectangle with triangles and vertex positions
         *      1_________3,4
         *      |        /|
         *      |      /  |
         *      |    /    |
         *      |  /      |
         *      |/________|
         *    2,5         6
         */

        // Create first triangle(left top) from the rectangle
        Color color = colors[MathUtils.random(2)];
        MeshPartBuilder.VertexInfo[] rectangle = new MeshPartBuilder.VertexInfo[6];
        Vector3 vec1 = new Vector3(posX1, posY2, 0);
        Vector3 vec2 = new Vector3(posX1, posY1, 0);
        Vector3 vec3 = new Vector3(posX2, posY2, 0);
        Vector3 normal = calculateNormal(vec1,vec2,vec3);
        rectangle[0] = createVertexInfo(vec1, normal, color);
        rectangle[1] = createVertexInfo(vec2, normal, color);
        rectangle[2] = createVertexInfo(vec3, normal, color);

        // Create second triangle(right bottom) from the rectangle
        color = colors[MathUtils.random(2)];
        vec1 = new Vector3(posX2, posY2, 0);
        vec2 = new Vector3(posX1, posY1, 0);
        vec3 = new Vector3(posX2, posY1, 0);
        normal = calculateNormal(vec1,vec2,vec3);
        rectangle[3] = createVertexInfo(vec1, normal, color);
        rectangle[4] = createVertexInfo(vec2, normal, color);
        rectangle[5] = createVertexInfo(vec3, normal, color);
        return rectangle;
    }

    private MeshPartBuilder.VertexInfo createVertexInfo(Vector3 position, Vector3 normal, Color color){
        MeshPartBuilder.VertexInfo info = new MeshPartBuilder.VertexInfo();
        // third corner top right
        info.setPos(position);
        info.setCol(color);
        info.setNor(normal);
        return info;
    }
}