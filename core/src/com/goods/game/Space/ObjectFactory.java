package com.goods.game.Space;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;
import com.goods.game.Space.Planets.DesertPlanet;
import com.goods.game.Space.Planets.GasPlanet;
import com.goods.game.Space.Planets.IcePlanet;
import com.goods.game.Space.Planets.TerraPlanet;
import com.goods.game.Space.Planets.VulcanoPlanet;
import com.goods.game.Space.Planets.WaterPlanet;
import com.goods.game.Space.Stars.Star;

import java.util.HashMap;
import java.util.Random;
import java.util.TreeSet;

/**
 * Created by Higgy on 01.09.2017.
 */

public class ObjectFactory {


    private Model model;
    private ModelBuilder modelBuilder;
    private Random random;
    private float ressourceDepositeFactor;
    private GameObjectModelInstance gameObjectModelInstance;


    public ObjectFactory() {
        modelBuilder = new ModelBuilder();
        random = new Random();
    }

    public GameObjectModelInstance createGameObject (ObjectType oType, float size, Vector3 position){
        switch (oType){
            case Star:{
                model = modelBuilder.createSphere(size,size,size,24,24,new Material(ColorAttribute.createDiffuse(Color.YELLOW)), VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal);
                gameObjectModelInstance = new Star(model,size);
                gameObjectModelInstance.transform.setTranslation(position);
                gameObjectModelInstance.setPos(position);
                return gameObjectModelInstance;
            }
            case Asteroid:{

            }
            case Wormhole:{

            }
            case Blackhole:{

            }
            case Ressource:{

            }
            case Ship:{

            }
        }

        return null;
    }

    public GameObjectModelInstance createGameObject (PlanetType pType, float size, Vector3 position, Vector3 starPos){
        ressourceDepositeFactor = random.nextFloat();

        do{
            switch (pType) {
                case Ice: {
                    model = modelBuilder.createSphere(size, size, size, 24, 24, new Material(ColorAttribute.createDiffuse(pType.getColor())), VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal);
                    gameObjectModelInstance = new IcePlanet(model, size, ressourceDepositeFactor);
                    gameObjectModelInstance.transform.setTranslation(position);
                    gameObjectModelInstance.setPos(position);
                    gameObjectModelInstance.setParentPosition(starPos);
                    gameObjectModelInstance.setOrbitRotationSpeed();
                    return gameObjectModelInstance;
                }
                case Desert: {
                    model = modelBuilder.createSphere(size, size, size, 24, 24, new Material(ColorAttribute.createDiffuse(pType.getColor())), VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal);
                    gameObjectModelInstance = new DesertPlanet(model, size, ressourceDepositeFactor);
                    gameObjectModelInstance.transform.setTranslation(position);
                    gameObjectModelInstance.setPos(position);
                    gameObjectModelInstance.setParentPosition(starPos);
                    gameObjectModelInstance.setOrbitRotationSpeed();
                    return gameObjectModelInstance;
                }
                case Water: {
                    model = modelBuilder.createSphere(size, size, size, 24, 24, new Material(ColorAttribute.createDiffuse(pType.getColor())), VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal);
                    gameObjectModelInstance = new WaterPlanet(model, size, ressourceDepositeFactor);
                    gameObjectModelInstance.transform.setTranslation(position);
                    gameObjectModelInstance.setPos(position);
                    gameObjectModelInstance.setParentPosition(starPos);
                    gameObjectModelInstance.setOrbitRotationSpeed();
                    return gameObjectModelInstance;
                }
                case Terrastic: {
                    model = modelBuilder.createSphere(size, size, size, 24, 24, new Material(ColorAttribute.createDiffuse(pType.getColor())), VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal);
                    gameObjectModelInstance = new TerraPlanet(model, size, ressourceDepositeFactor);
                    gameObjectModelInstance.transform.setTranslation(position);
                    gameObjectModelInstance.setPos(position);
                    gameObjectModelInstance.setParentPosition(starPos);
                    gameObjectModelInstance.setOrbitRotationSpeed();
                    return gameObjectModelInstance;
                }
                case Vulcano: {
                    model = modelBuilder.createSphere(size, size, size, 24, 24, new Material(ColorAttribute.createDiffuse(pType.getColor())), VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal);
                    gameObjectModelInstance = new VulcanoPlanet(model, size, ressourceDepositeFactor);
                    gameObjectModelInstance.transform.setTranslation(position);
                    gameObjectModelInstance.setPos(position);
                    gameObjectModelInstance.setParentPosition(starPos);
                    gameObjectModelInstance.setOrbitRotationSpeed();
                    return gameObjectModelInstance;
                }
                case Gas: {
                    model = modelBuilder.createSphere(size, size, size, 24, 24, new Material(ColorAttribute.createDiffuse(pType.getColor())), VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal);
                    gameObjectModelInstance = new GasPlanet(model, size, ressourceDepositeFactor);
                    gameObjectModelInstance.transform.setTranslation(position);
                    gameObjectModelInstance.setPos(position);
                    gameObjectModelInstance.setParentPosition(starPos);
                    gameObjectModelInstance.setOrbitRotationSpeed();
                    return gameObjectModelInstance;
                }
                case Random: {
                    PlanetType[] pTypeArray = PlanetType.values();
                    pType = pTypeArray[random.nextInt(pTypeArray.length - 1)];
                }
            }
        }while (true);
    }
}
