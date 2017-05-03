package com.goods.game.Terrain;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.model.MeshPart;
import com.badlogic.gdx.graphics.g3d.model.Node;
import com.badlogic.gdx.graphics.g3d.model.NodePart;
import com.badlogic.gdx.graphics.g3d.utils.MeshPartBuilder;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

public class TerrainBuilder {
    ModelBuilder modelBuilder;
    ImprovedNoise improvedNoise;
    float maxHeight = 1f;
    float maxDepth = -0.2f;
    float[][] heightsArray2dim;
    Color[] colors;
    float[] heights;
    public ArrayList<Vector3> getNormals() {
        return normals;
    }
    ArrayList<Vector3> normals;
    Model tmp;
    private int numberOfChunks;
    public Model createTerrainEachTriangleOwnVertices(int width, int length, int numberOfChunks, boolean withHeight) {
        normals = new ArrayList<Vector3>();
        // todo optimize color so only 1 float is used not 4!!!
        colors = new Color[3];
        colors[0] = new Color(0,0.3f,0,0);
        colors[1] = new Color(0,0.3f,0,0);
        colors[2] = new Color(0,0.3f,0,0);
        this.numberOfChunks = numberOfChunks;
        modelBuilder = new ModelBuilder();
        Color color;
        float chunkWidth = width / numberOfChunks;
        float chunkLength = length / numberOfChunks;
        int gridX =numberOfChunks+1;
        int gridY =numberOfChunks+1;
        heightsArray2dim = new float[gridX][gridY];

        heights = new float[(int)Math.pow((numberOfChunks+1),2)];
        //heights = improvedNoise.heightData(numberOfChunks);
        for (int i = 0; i < heights.length; i++) {
            heights[i] = MathUtils.random(maxDepth,maxHeight);
        }
        //heights = new float[] {1,2,4,3,3,2.2f,2,4.5f,2.1f,5,1.8f,3,0,3,2,9};
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
                for (MeshPartBuilder.VertexInfo info:createRectangle(posX1, posX2, posY1, posY2,iX,iY)) {
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

    private void createImageFromMesh(int gridX, int gridY) throws IOException {

        Color colHeight = new Color();
        /*
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

        */





        int type = BufferedImage.TYPE_INT_ARGB;

        BufferedImage image = new BufferedImage(gridX, gridY, type);

        int color; //RGBA value, each component in a byte

        for(int x = 0; x < gridX; x++) {
            for(int y = 0; y < gridY; y++) {
                float height;
                if (heights[x+y*(numberOfChunks+1)] <0){
                    height = Math.abs(heights[x+y*(numberOfChunks+1)]);
                }else{
                    height = Math.abs(heights[x+y*(numberOfChunks+1)]) + Math.abs(maxDepth);
                }

                float maxHeightSum = Math.abs(maxDepth)+Math.abs(maxHeight);
                height = height/maxHeightSum;
                colHeight = new Color(height,height,height,0);
                color = colHeight.toIntBits();
                image.setRGB(x, y, color);
            }
        }



        File outputfile = new File("image.jpg");
        try {
            ImageIO.write(image, "jpg", outputfile);
        } catch (IOException e) {
            e.printStackTrace();
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
private int next =0;
    private MeshPartBuilder.VertexInfo[] createRectangle(float posX1, float posX2, float posY1, float posY2, int iX, int iY){

        /*      Rectangle with triangles and vertex positions
         *      1_________3
         *      |        /|
         *      |      /  |
         *      |    /    |
         *      |  /      |
         *      |/________|
         *      2         4
         */


        float h1 = heights[numberOfChunks+iY+next];
        float h2 = heights[next+iY-1];
        float h3 = heights[numberOfChunks+iY+1+next];
        float h4 = heights[iY+next];

        // Create first triangle(left top) from the rectangle
        Color color = colors[MathUtils.random(2)];
        MeshPartBuilder.VertexInfo[] rectangle = new MeshPartBuilder.VertexInfo[6];
        Vector3 vec1 = new Vector3(posX1, posY2, h1);
        Vector3 vec2 = new Vector3(posX1, posY1, h2);
        Vector3 vec3 = new Vector3(posX2, posY2, h3);
        Vector3 normal = calculateNormal(vec1,vec2,vec3);
        rectangle[0] = createVertexInfo(vec1, normal, color);
        rectangle[1] = createVertexInfo(vec2, normal, color);
        rectangle[2] = createVertexInfo(vec3, normal, color);

        // Create second triangle(right bottom) from the rectangle
        color = colors[MathUtils.random(2)];
        Vector3 vec4 = new Vector3(posX2, posY1, h4);
        normal = calculateNormal(vec1,vec2,vec3);
        rectangle[3] = createVertexInfo(vec3, normal, color);
        rectangle[4] = createVertexInfo(vec2, normal, color);
        rectangle[5] = createVertexInfo(vec4, normal, color);
        next++;
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