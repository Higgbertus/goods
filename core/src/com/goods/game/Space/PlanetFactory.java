package com.goods.game.Space;

import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.math.Vector3;
import com.goods.game.Space.Planets.DesertPlanet;
import com.goods.game.Space.Planets.GasPlanet;
import com.goods.game.Space.Planets.IcePlanet;
import com.goods.game.Space.Planets.PlanetObjectModelInstance;
import com.goods.game.Space.Planets.TerraPlanet;
import com.goods.game.Space.Planets.VulcanoPlanet;
import com.goods.game.Space.Planets.WaterPlanet;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by Higgy on 29.08.2017.
 */

public class PlanetFactory {


    // Helper
    ModelBuilder modelBuilder;
    private ArrayList<PlanetObjectModelInstance> planets;
    private Random random;

    public PlanetFactory() {
        modelBuilder = new ModelBuilder();
        planets = new ArrayList<PlanetObjectModelInstance>();
        random = new Random();
    }

    public void createPlanet(Vector3 position, PlanetType type, float size){
        Model model = createPlanetModel(size, type);
        planets.add(createPlanetModelInstance(model, position, type, size));
    }

    private Model createPlanetModel(float size, PlanetType type){
        return modelBuilder.createSphere(size,size,size,24,24,new Material(ColorAttribute.createDiffuse(type.getColor())), VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal);
    }

    private PlanetObjectModelInstance createPlanetModelInstance(Model model, Vector3 position, PlanetType type, float size){
        PlanetObjectModelInstance planetModelInstance = null;
        // Model model, PlanetType type, float size, float ressourceDepositeFactor)
      /*  switch (type){
            case Ice:{
                planetModelInstance = new IcePlanet(model, type, size,0.6f);
            }
            break;
            case Desert:{
                planetModelInstance = new DesertPlanet(model, type, size,0.6f);
            }
            break;
            case Water:{
                planetModelInstance = new WaterPlanet(model, type, size,0.5f);
            }
            break;
            case Terrastic:{
                planetModelInstance = new TerraPlanet(model, type, size,0.2f);
            }
            break;
            case Vulcano:{
                planetModelInstance = new VulcanoPlanet(model, type, size,0.8f);
            }
            break;
            case Gas:{
                planetModelInstance = new GasPlanet(model, type, size,0.6f);
            }
            break;
        }
*/
        planetModelInstance.transform.setToTranslation(position);
        return planetModelInstance;
    }

    public ArrayList<PlanetObjectModelInstance> getPlanets() {
        return planets;
    }
}
