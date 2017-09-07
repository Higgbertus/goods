package com.goods.game.Space.Ships;

import com.badlogic.gdx.graphics.g3d.Model;

/**
 * Created by Higgy on 04.09.2017.
 */

public class TranspoterShip extends ShipObjectModelInstance {
    private static float shipTravelSpeed =100f, shipRotationSpeed=2f, shipWarpTravelSpeed = 500f;
    private static float accelerationPower =100f, decelerationPower =20f, rotationPower = 10f;
    public TranspoterShip(Model model, float size) {
        super(model, size, shipTravelSpeed, shipWarpTravelSpeed, shipRotationSpeed, accelerationPower, decelerationPower, rotationPower);
    }

}
