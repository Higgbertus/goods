package com.goods.game.Space;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;
import com.goods.game.Space.Ships.ShipObjectModelInstance;
import com.goods.game.Space.Ships.ShipType;
import com.goods.game.Space.Stars.StarObjectModelInstance;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by Higgy on 29.08.2017.
 */

public class DynamicObjectHandler {


    //Map Environment
    float shipSize = 4f;

    // Helper
    private ObjectFactory objectFactory;
    public ArrayList<ShipObjectModelInstance> shipInstances;
    private Random random;
    private ShipObjectModelInstance tmp;
    private int maxDistance = 0;
    private SpaceMap spaceMap;

    public DynamicObjectHandler(SpaceMap spaceMap) {
        this.spaceMap = spaceMap;
        objectFactory = new ObjectFactory();
        shipInstances = new ArrayList<ShipObjectModelInstance>();
        random = new Random();
    }


    private Vector3 createOrbitAngles(){
        int x = MathUtils.random(718)-359;
        int y = MathUtils.random(718)-359;
        int z = 0; //geht net da z auf stern ausgerichtet //MathUtils.random(360);
        return new Vector3(x,y,z);
    }

    public void createShip(){
        tmp = objectFactory.createShipObject(ShipType.TransporterMultiRole, shipSize, createObjectPos());
        shipInstances.add(tmp);
    }

    private Vector3 createObjectPos(){
        float x,y,z;
        int borderSpace = spaceMap.getBorderSpace();
        do {
            x = random.nextInt(spaceMap.getMapWidth() - borderSpace - borderSpace) + borderSpace;
            y = random.nextInt(spaceMap.getMapHeight() - borderSpace - borderSpace) + borderSpace;
            z = random.nextInt(spaceMap.getMapDepth() - borderSpace - borderSpace) + borderSpace;
            // Check if there is already a star in that space
        }while(isOccupied(x,y,z, maxDistance));

        Vector3 pos = new Vector3(x,y,z);
        //Vector3 pos = new Vector3(10,25,0);
        return pos;
    }

    // Check if there is enough space to other stars
    // TODO: 02.09.2017 crashed wenn zuviele stars erzeugt werden => findet keinen Platz endlosschleife
    private boolean isOccupied(float x, float y, float z, float minDistance){
        Vector3 pos1 = new Vector3();
        for (GameObjectModelInstance object :spaceMap.getStars()) {
            object.transform.getTranslation(pos1);
            if (pos1.dst(x,y,z)< minDistance)
                return true;
        }
        return false;
    }


    public ArrayList<ShipObjectModelInstance> getShips() {
        return shipInstances;
    }

    public ShipObjectModelInstance getShip(int i) {
        return shipInstances.get(i);
    }

    public void clear(){
        shipInstances.clear();
    }
}
