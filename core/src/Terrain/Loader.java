package Terrain;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL30;
import com.badlogic.gdx.utils.BufferUtils;

import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.List;


public class Loader {

    private List<Integer> vaos = new ArrayList<Integer>();
    private List<Integer> vbos = new ArrayList<Integer>();

    public RawModel loadVAO( float[] positions){
        int vaoID = createVAO();
        storeDataInAttributeList(0, positions);
        unbindVAO();
        return new RawModel(vaoID,positions.length/3);
    }

    public void clenUP(){

        //????
    }

    private int createVAO() {
        int vaoID = 0;
        vaos.add(vaoID);
        Gdx.gl30.glBindVertexArray(vaoID);
        return vaoID;
    }

    private void storeDataInAttributeList(int attributeNumber, float[] data) {
        int vboID = Gdx.gl30.glGenBuffer();
        vbos.add(vboID);
        Gdx.gl30.glBindBuffer(Gdx.gl30.GL_ARRAY_BUFFER, vboID);
        FloatBuffer buffer = storeDataInFloatBuffer(data);
        Gdx.gl30.glBufferData(Gdx.gl30.GL_ARRAY_BUFFER,data.length,buffer,Gdx.gl30.GL_STATIC_DRAW); // ?? size ???
        Gdx.gl30.glVertexAttribPointer(attributeNumber,3,Gdx.gl30.GL_FLOAT,false,0,0);
        Gdx.gl30.glBindBuffer(Gdx.gl30.GL_ARRAY_BUFFER,0);
    }

    private void unbindVAO() {
        Gdx.gl30.glBindVertexArray(0);
    }

    private FloatBuffer storeDataInFloatBuffer(float[] data){
        FloatBuffer buffer = BufferUtils.newFloatBuffer(data.length);
        buffer.put(data);
        buffer.flip();
        return buffer;
    }



}
