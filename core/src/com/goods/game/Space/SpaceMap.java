package com.goods.game.Space;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL30;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.utils.MeshPartBuilder;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by Higgy on 29.08.2017.
 */

public class SpaceMap {

    private ModelBuilder modelBuilder;
    //Map Settings
    private int mapHeight, mapWidth, difficulty;
    PlanetType[] pType = PlanetType.values();
    private int borderSpace = 50;
    ObjectType oType;
    private int maxPlanetSize = 7, maxStarSize =30, minStarSize =12, minPlanetSize =2;
    //Map Environment
    private boolean hasPirates = false;
    private int pirateStrength, maxPirateSize;
    private int maxPlanetperStar = 6;


    // Helper
    private PlanetFactory planetFactory;
    private ObjectFactory objectFactory;
    public ArrayList<GameObjectModelInstance> gameObjects;
    private Random random;
    private Vector3 spacer;
    private float distance = 5f;

    public SpaceMap( ) {
        planetFactory = new PlanetFactory();
        modelBuilder = new ModelBuilder();
        objectFactory = new ObjectFactory();
        gameObjects = new ArrayList<GameObjectModelInstance>();
        random = new Random();
    }

    public void createMap(int mapHeight, int mapWidth, int difficulty, boolean hasPirates, int pirateStrength, int maxPirateSize) {
        this.mapHeight = mapHeight;
        this.mapWidth = mapWidth;
        this.difficulty = difficulty;
        this.hasPirates = hasPirates;
        this.pirateStrength = pirateStrength;
        this.maxPirateSize = maxPirateSize;
        //fillMapWithObjects();
    }

    public ModelInstance createFrame(){
        modelBuilder.begin();
        MeshPartBuilder meshPartBuilder = modelBuilder.part("terrain", Gdx.gl30.GL_TRIANGLES, VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal | VertexAttributes.Usage.ColorUnpacked, new  Material());

                for (MeshPartBuilder.VertexInfo info:createRectangle(0, 50, 0, 50)) {
                    meshPartBuilder.index(meshPartBuilder.vertex(info));
                }

        Model tmp = modelBuilder.end();
        ModelInstance tmp2 = new ModelInstance(tmp);
        return tmp2;
    }

    private MeshPartBuilder.VertexInfo[] createRectangle(float posX1, float posX2, float posY1, float posY2){

        /*      Rectangle with triangles and vertex positions
         *      1_________3
         *      |        /|
         *      |      /  |
         *      |    /    |
         *      |  /      |
         *      |/________|
         *      2         4
         */


        MeshPartBuilder.VertexInfo[] rectangle = new MeshPartBuilder.VertexInfo[6];
        Vector3 vec1 = new Vector3(posX1, posY2, 0);
        Vector3 vec2 = new Vector3(posX1, posY1, 0);
        Vector3 vec3 = new Vector3(posX2, posY2, 0);
        rectangle[0] = new MeshPartBuilder.VertexInfo().setPos(vec1);
        rectangle[1] = new MeshPartBuilder.VertexInfo().setPos(vec2);
        rectangle[2] = new MeshPartBuilder.VertexInfo().setPos(vec3);

        Vector3 vec4 = new Vector3(posX2, posY1, 0);
        rectangle[3] = new MeshPartBuilder.VertexInfo().setPos(vec3);
        rectangle[4] = new MeshPartBuilder.VertexInfo().setPos(vec2);
        rectangle[5] = new MeshPartBuilder.VertexInfo().setPos(vec4);

        return rectangle;
    }
    float starSize, planetSize;
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

    private void createPlanets() {

        for (int i = 0; i < 100; i++) {

            // less Terra Planets and smaller Planets
            if(difficulty >8){

            }else if (difficulty > 6){

            }else if (difficulty > 3){

            }else{ // many Terra Planets and big Planets

            }
           // planetFactory.createPlanet(createPlanetPos(), pType[random.nextInt(pType.length)], MathUtils.random(maxPlanetSize));
        }
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
