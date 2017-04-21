package com.goods.game.Terrain;

import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Renderable;
import com.badlogic.gdx.graphics.g3d.RenderableProvider;
import com.badlogic.gdx.graphics.g3d.utils.MeshBuilder;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.ObjectSet;
import com.badlogic.gdx.utils.Pool;

/**
 * Created by $USER_NAME on 16.04.2017.
 */
public class TerrainBatch extends ObjectSet<Terrain> implements RenderableProvider, Disposable{
    Renderable renderable;
    Mesh mesh;
    MeshBuilder meshBuilder;


    public TerrainBatch(Material material) {

        meshBuilder = new MeshBuilder();
        renderable = new Renderable();
        renderable.material = material;
    }

    @Override
    public void getRenderables(Array<Renderable> renderables, Pool<Renderable> pool) {

    }

    @Override
    public void dispose() {

    }
}
