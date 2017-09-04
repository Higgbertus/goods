package com.goods.game.Space.Shapes;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.collision.BoundingBox;
import com.badlogic.gdx.math.collision.Ray;

/**
 * Created by Higgy on 04.09.2017.
 */

public class ConeShape extends BaseShape {
    public float radius;

    public ConeShape(BoundingBox bounds) {
        super(bounds);
       // radius = dimensions.len() / 2f;
        radius = 0.5f * dimensions.z;
    }

    @Override
    public boolean isVisible(Matrix4 transform, Camera cam) {
        return cam.frustum.sphereInFrustum(transform.getTranslation(position).add(center), radius);
    }

    @Override
    public float intersects(Matrix4 transform, Ray ray) {
        transform.getTranslation(position).add(center);
        final float len = ray.direction.dot(position.x-ray.origin.x, position.y-ray.origin.y, position.z-ray.origin.z);
        if (len < 0f)
            return -1f;
        float dist2 = position.dst2(ray.origin.x+ray.direction.x*len, ray.origin.y+ray.direction.y*len, ray.origin.z+ray.direction.z*len);
        return (dist2 <= radius * radius) ? dist2 : -1f;
    }
}
