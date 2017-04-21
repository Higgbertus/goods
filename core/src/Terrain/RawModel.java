package Terrain;

/**
 * Created by Fabian Naaß on 14.04.2017.
 * Einstein Motorsport 2017.
 * + Copyrights by Einstein Motorsport e.V
 */
public class RawModel {

    private int vaoID;
    private int vertexCount;

    public RawModel(int vaoID, int vertexCount) {
        this.vaoID = vaoID;
        this.vertexCount = vertexCount;
    }

    public int getVaoID() {
        return vaoID;
    }

    public int getVertexCount() {
        return vertexCount;
    }
}
