package com.goods.game.Space;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;
import com.goods.game.Space.Stars.StarObjectModelInstance;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by Higgy on 29.08.2017.
 */

public class SpaceMap {


    //Map Settings
    private int mapHeight, mapWidth, mapDepth;
    private int borderSpace = 50;
    private int maxPlanetSize = 8, maxStarSize =35, minStarSize =15, minPlanetSize =3;
    //Map Environment
    private int maxPlanetperStar = 5;
    float starSize, planetSize, shipSize = 4f;

    // Helper
    private ObjectFactory objectFactory;
    public ArrayList<StarObjectModelInstance> starInstances;
    public ArrayList<GameObjectModelInstance> gameObjects;
    private Random random;
    private int maxDistance = 0;

    public SpaceMap( ) {
        objectFactory = new ObjectFactory();
        gameObjects = new ArrayList<GameObjectModelInstance>();
        starInstances = new ArrayList<StarObjectModelInstance>();
        random = new Random();
    }

    public void createMap(int mapHeight, int mapWidth, int mapDepth) {
        this.mapHeight = mapHeight;
        this.mapWidth = mapWidth;
        this.mapDepth = mapDepth;
        //fillMapWithObjects();
    }
    private GameObjectModelInstance tmp;
    public boolean fillMapWithObjects() {

        // create Stars
        for (int i = 0; i < 10; i++) {
            starSize = MathUtils.random(maxStarSize-minStarSize)+minStarSize;
            // create Star at Position
            tmp = objectFactory.createGameObject(ObjectType.Star, starSize, createObjectPos((maxStarSize*(maxPlanetperStar+1))));
            Vector3 initRotAngles = createOrbitAngles();
            // Add Star to ArrayList
            starInstances.add((StarObjectModelInstance) tmp);
            gameObjects.add(tmp);

            // create Planets
            for (int j = 1; j < maxPlanetperStar+1; j++) {
                // create Planet at position of the star with a factor
                planetSize = MathUtils.random(maxPlanetSize-minPlanetSize)+minPlanetSize;
                // old Vector3 starPos = new Vector3();
                // old gameObjects.get(i).transform.getTranslation(starPos);
                Vector3 starPos = new Vector3(gameObjects.get(i).getPosition());
                Vector3 position = new Vector3(starPos);
                position.add(0,0,(starSize)*j);
                tmp = objectFactory.createGameObject(com.goods.game.Space.Planets.PlanetType.Random, planetSize, position, starPos);
                tmp.setInitialRotation(initRotAngles, starPos);
                starInstances.get(i).addObjectToOrbit(tmp);
                // Add Planet to Star ArrayList
                gameObjects.get(i).addObjectToOrbit(tmp);
            }

        }
        return true;
    }

    private Vector3 createOrbitAngles(){
        int x = MathUtils.random(718)-359;
        int y = MathUtils.random(718)-359;
        int z = 0; //geht net da z auf stern ausgerichtet //MathUtils.random(360);
        return new Vector3(x,y,z);
    }

    private Vector3 createObjectPos(int minDistance){
        // save maxDisstance for further usage
        if (maxDistance <minDistance){
            maxDistance = minDistance;
        }

        float x,y,z;
        do {
            x = random.nextInt(mapWidth - borderSpace - borderSpace) + borderSpace;
            y = random.nextInt(mapHeight - borderSpace - borderSpace) + borderSpace;
            z = random.nextInt(mapDepth - borderSpace - borderSpace) + borderSpace;
            // Check if there is already a star in that space
        }while(isOccupied(x,y,z, minDistance));

        Vector3 pos = new Vector3(x,y,z);
        //Vector3 pos = new Vector3(10,25,0);
        return pos;
    }

    // Check if there is enough space to other stars
    // TODO: 02.09.2017 crashed wenn zuviele stars erzeugt werden => findet keinen Platz endlosschleife
    private boolean isOccupied(float x, float y, float z, float minDistance){
        Vector3 pos1 = new Vector3();
        for (GameObjectModelInstance object :gameObjects) {
            pos1 = object.getPosition();
            if (pos1.dst(x,y,z)< minDistance)
                return true;
        }
        return false;
    }

    public ArrayList<StarObjectModelInstance> getStars() {
        return starInstances;
    }

    public StarObjectModelInstance getStar(int i) {
        return starInstances.get(i);
    }

    public  ArrayList<GameObjectModelInstance> getAllPlanetsFromStar(int i){
       return starInstances.get(i).getOrbitObjects();
    }
    public  ArrayList<GameObjectModelInstance> getAllPlanetsFromStar(StarObjectModelInstance i){
        return starInstances.get(starInstances.indexOf(i)).getOrbitObjects();
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

    public int getMaxDistance() {
        return maxDistance;
    }
    public int getMapHeight() {
        return mapHeight;
    }

    public int getMapWidth() {
        return mapWidth;
    }

    public int getMapDepth() {
        return mapDepth;
    }

    public int getBorderSpace() {
        return borderSpace;
    }

    public void clear(){
        starInstances.clear();
        gameObjects.clear();
    }
}
