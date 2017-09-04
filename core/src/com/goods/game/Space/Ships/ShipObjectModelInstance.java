package com.goods.game.Space.Ships;

import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.math.Vector3;
import com.goods.game.Space.GameObjectModelInstance;
import com.goods.game.Space.ObjectType;

/**
 * Created by Higgy on 31.08.2017.
 */

public class ShipObjectModelInstance extends GameObjectModelInstance{
    private static ObjectType oType = ObjectType.Ship;

    // Ship Settings
    private Vector3 direction;
    private boolean isMoving;

    public Vector3 getDirection() {
        return direction;
    }

    public void setDirection(Vector3 direction) {
        this.direction = direction;
    }

    public boolean isMoving() {
        return isMoving;
    }

    public void setMoving(boolean moving) {
        isMoving = moving;
    }

    public Vector3 getDestination() {
        return destination;
    }

    public void setDestination(Vector3 destination) {
        this.destination = destination;
    }

    private Vector3 destination;

    // Helper
    public ShipObjectModelInstance(Model model, float size) {
        super(model, size, oType);
    }

    @Override
    public String toString() {
        return oType.name()+super.getId()+"; Pos:"+this.transform.getTranslation(new Vector3());
    }

}
