package com.goods.game.Space;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by Higgy on 29.08.2017.
 */

public class SpaceMap {

    //Map Settings
    private int mapHeight, mapWidth;
    PlanetType[] pType = PlanetType.values();
    private int borderSpace = 50;
    private int maxPlanetSize = 7, maxStarSize =30, minStarSize =12, minPlanetSize =2;
    //Map Environment
    private int maxPlanetperStar = 6;
    float starSize, planetSize;

    // Helper
    private ObjectFactory objectFactory;
    public ArrayList<GameObjectModelInstance> gameObjects;
    private Random random;

    public SpaceMap( ) {
        objectFactory = new ObjectFactory();
        gameObjects = new ArrayList<GameObjectModelInstance>();
        random = new Random();
    }

    public void createMap(int mapHeight, int mapWidth) {
        this.mapHeight = mapHeight;
        this.mapWidth = mapWidth;
        //fillMapWithObjects();
    }

    public boolean fillMapWithObjects() {
        GameObjectModelInstance tmp;
        // create Stars
        for (int i = 0; i < 6; i++) {
            starSize = MathUtils.random(maxStarSize-minStarSize)+minStarSize;
            // create Star at Position
            tmp = objectFactory.createGameObject(ObjectType.Star, starSize, createObjectPos());

            // Add Star to ArrayList
            gameObjects.add(tmp);

            // create Planets
            for (int j = 1; j < maxPlanetperStar+1; j++) {
                // create Planet at position of the star with a factor
                planetSize = MathUtils.random(maxPlanetSize-minPlanetSize)+minPlanetSize;
                Vector3 starPos = new Vector3();
                gameObjects.get(i).transform.getTranslation(starPos);
                Vector3 position = new Vector3(starPos);
                position.add((starSize)*j,0,0);
                tmp = objectFactory.createGameObject(PlanetType.Random, planetSize, position, starPos);

                // Add Planet to Star ArrayList
                gameObjects.get(i).addObjectToOrbit(tmp);
            }

        }
        return true;
    }

    private Vector3 createObjectPos(){
        float x,y;
        do {
            x = random.nextInt(mapWidth - borderSpace - borderSpace) + borderSpace;
            y = random.nextInt(mapHeight - borderSpace - borderSpace) + borderSpace;
            // Check if there is already a star in that space
        }while(isOccupied(x,y));

        Vector3 pos = new Vector3(x,y,0);
        //Vector3 pos = new Vector3(10,25,0);
        return pos;
    }

    // Check if there is enough space to other stars
    // TODO: 02.09.2017 crashed wenn zuviele stars erzeugt werden => findet keinen Platz endlosschleife
    private boolean isOccupied(float x, float y){
        Vector3 pos1 = new Vector3();
        for (GameObjectModelInstance object :gameObjects) {
            object.transform.getTranslation(pos1);
            if (pos1.dst(x,y,0)< (maxStarSize*(maxPlanetperStar+1)))
                return true;
        }
        return false;
    }

    public ArrayList<GameObjectModelInstance> getAllObjects() {
        ArrayList<GameObjectModelInstance> allGameObjects = new ArrayList<GameObjectModelInstance>();
        for (GameObjectModelInstance tmp:gameObjects) {
            allGameObjects.add(tmp);
            if(tmp.hasChildObjects()){
                for (GameObjectModelInstance tmp1:tmp.getOrbitObjects()) {
                    allGameObjects.add(tmp1);
                }
            }
        }
        return allGameObjects;
    }
}
