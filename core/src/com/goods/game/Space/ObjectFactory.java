package com.goods.game.Space;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;
import com.goods.game.Space.Planets.*;
import com.goods.game.Space.Shapes.*;
import com.goods.game.Space.Ships.ShipObjectModelInstance;
import com.goods.game.Space.Ships.ShipType;
import com.goods.game.Space.Ships.TranspoterShip;
import com.goods.game.Space.Stars.Star;

import java.util.Random;

/**
 * Created by Higgy on 01.09.2017.
 */

public class ObjectFactory {


    private Model model;
    private ModelBuilder modelBuilder;
    private Random random;
    private float ressourceDepositeFactor;
    private GameObjectModelInstance gameObjectModelInstance;
    private BoundingBox bounds = new BoundingBox();
    protected ObjectShape sphereShape;
    protected ObjectShape ressourceShape;
    protected ObjectShape coneShape;
    private Vector3 front, back;


    public ObjectFactory() {
        modelBuilder = new ModelBuilder();
        random = new Random();
    }

    public GameObjectModelInstance createGameObject (ObjectType oType, float size, Vector3 position){
        switch (oType){
            case Star:{

                model = modelBuilder.createSphere(size,size,size,24,24,new Material(ColorAttribute.createDiffuse(Color.YELLOW)), VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal);
                gameObjectModelInstance = new Star(model,size);
                gameObjectModelInstance.setPosition(position);
                gameObjectModelInstance.updateTransform();
                gameObjectModelInstance.calculateBoundingBox(bounds);
                sphereShape = new SphereShape(bounds);
                gameObjectModelInstance.setObjectShape(sphereShape);
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
                model = modelBuilder.createCone(size,size*3,size,24,new Material(ColorAttribute.createDiffuse(Color.GRAY)), VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal);
                gameObjectModelInstance = new TranspoterShip(model,size);
                gameObjectModelInstance.setPosition(position);
                gameObjectModelInstance.updateTransform();
                gameObjectModelInstance.calculateBoundingBox(bounds);
                sphereShape = new ConeShape(bounds);
                gameObjectModelInstance.setObjectShape(sphereShape);
                return gameObjectModelInstance;
            }
        }

        return null;
    }

    public ShipObjectModelInstance createShipObject (ShipType sType, float size, Vector3 position){
        ShipObjectModelInstance shipObjectModelInstance;
        switch (sType){
            case Fighter:{

            }
            case TransporterGas:{

            }
            case TransporterLooseFreight:{

            }
            case TransporterMultiRole:{
                model = modelBuilder.createCone(size,size*3,size,24,new Material(ColorAttribute.createDiffuse(Color.GRAY)), VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal);
                shipObjectModelInstance = new TranspoterShip(model,size);
                shipObjectModelInstance.setPosition(position);
                shipObjectModelInstance.updateTransform();
                shipObjectModelInstance.calculateBoundingBox(bounds);
                sphereShape = new ConeShape(bounds);
                shipObjectModelInstance.setObjectShape(sphereShape);
                return shipObjectModelInstance;
            }
            case Battleship:{

            }
            case Frigate:{
                model = modelBuilder.createCone(size,size*3,size,24,new Material(ColorAttribute.createDiffuse(Color.GRAY)), VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal);
                shipObjectModelInstance = new TranspoterShip(model,size);
                shipObjectModelInstance.setPosition(position);
                shipObjectModelInstance.updateTransform();
                shipObjectModelInstance.calculateBoundingBox(bounds);
                sphereShape = new ConeShape(bounds);
                shipObjectModelInstance.setObjectShape(sphereShape);
                return shipObjectModelInstance;
            }
        }

        return null;
    }

    public PlanetObjectModelInstance createGameObject (com.goods.game.Space.Planets.PlanetType pType, float size, Vector3 position, Vector3 starPos){
        PlanetObjectModelInstance planetObjectModelInstance;
        ressourceDepositeFactor = random.nextFloat();

        do{
            switch (pType) {
                case Ice: {
                    model = modelBuilder.createSphere(size, size, size, 24, 24, new Material(ColorAttribute.createDiffuse(pType.getColor())), VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal);
                    planetObjectModelInstance = new IcePlanet(model, size, ressourceDepositeFactor);
                    planetObjectModelInstance.setPosition(position);
                    planetObjectModelInstance.updateTransform();
                    planetObjectModelInstance.calculateBoundingBox(bounds);
                    sphereShape = new SphereShape(bounds);
                    planetObjectModelInstance.setObjectShape(sphereShape);
                    return planetObjectModelInstance;
                }
                case Desert: {
                    model = modelBuilder.createSphere(size, size, size, 24, 24, new Material(ColorAttribute.createDiffuse(pType.getColor())), VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal);
                    planetObjectModelInstance = new DesertPlanet(model, size, ressourceDepositeFactor);
                    planetObjectModelInstance.setPosition(position);
                    planetObjectModelInstance.updateTransform();
                    planetObjectModelInstance.calculateBoundingBox(bounds);
                    sphereShape = new SphereShape(bounds);
                    planetObjectModelInstance.setObjectShape(sphereShape);
                    return planetObjectModelInstance;
                }
                case Water: {
                    model = modelBuilder.createSphere(size, size, size, 24, 24, new Material(ColorAttribute.createDiffuse(pType.getColor())), VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal);
                    planetObjectModelInstance = new WaterPlanet(model, size, ressourceDepositeFactor);
                    planetObjectModelInstance.setPosition(position);
                    planetObjectModelInstance.updateTransform();
                    planetObjectModelInstance.calculateBoundingBox(bounds);
                    sphereShape = new SphereShape(bounds);
                    planetObjectModelInstance.setObjectShape(sphereShape);
                    return planetObjectModelInstance;
                }
                case Terrastic: {
                    model = modelBuilder.createSphere(size, size, size, 24, 24, new Material(ColorAttribute.createDiffuse(pType.getColor())), VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal);
                    planetObjectModelInstance = new TerraPlanet(model, size, ressourceDepositeFactor);
                    planetObjectModelInstance.setPosition(position);
                    planetObjectModelInstance.updateTransform();
                    planetObjectModelInstance.calculateBoundingBox(bounds);
                    sphereShape = new SphereShape(bounds);
                    planetObjectModelInstance.setObjectShape(sphereShape);
                    return planetObjectModelInstance;
                }
                case Vulcano: {
                    model = modelBuilder.createSphere(size, size, size, 24, 24, new Material(ColorAttribute.createDiffuse(pType.getColor())), VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal);
                    planetObjectModelInstance = new VulcanoPlanet(model, size, ressourceDepositeFactor);
                    planetObjectModelInstance.setPosition(position);
                    planetObjectModelInstance.updateTransform();
                    planetObjectModelInstance.calculateBoundingBox(bounds);
                    sphereShape = new SphereShape(bounds);
                    planetObjectModelInstance.setObjectShape(sphereShape);
                    return planetObjectModelInstance;
                }
                case Gas: {
                    model = modelBuilder.createSphere(size, size, size, 24, 24, new Material(ColorAttribute.createDiffuse(pType.getColor())), VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal);
                    planetObjectModelInstance = new GasPlanet(model, size, ressourceDepositeFactor);
                    planetObjectModelInstance.setPosition(position);
                    planetObjectModelInstance.updateTransform();
                    planetObjectModelInstance.calculateBoundingBox(bounds);
                    sphereShape = new SphereShape(bounds);
                    planetObjectModelInstance.setObjectShape(sphereShape);
                    return planetObjectModelInstance;
                }
                case Random: {
                    com.goods.game.Space.Planets.PlanetType[] pTypeArray = com.goods.game.Space.Planets.PlanetType.values();
                    pType = pTypeArray[random.nextInt(pTypeArray.length - 1)];
                }
            }
        }while (true);
    }
}
