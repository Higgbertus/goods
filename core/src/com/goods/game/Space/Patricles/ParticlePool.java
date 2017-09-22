package com.goods.game.Space.Patricles;

import com.badlogic.gdx.graphics.g3d.particles.ParticleEffect;
import com.badlogic.gdx.utils.Pool;

/**
 * Created by Higgy on 20.09.2017.
 */

public class ParticlePool extends Pool<ParticleEffect> {
    private ParticleEffect sourceEffect;

    public ParticlePool(ParticleEffect sourceEffect) {
        this.sourceEffect = sourceEffect;
    }

    @Override
    public void free(ParticleEffect pfx) {
        pfx.reset();
        super.free(pfx);
    }

    @Override
    protected ParticleEffect newObject() {
        return sourceEffect.copy();
    }
}