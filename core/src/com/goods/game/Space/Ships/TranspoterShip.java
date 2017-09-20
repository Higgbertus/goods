package com.goods.game.Space.Ships;

import com.badlogic.gdx.graphics.g3d.Model;
import com.goods.game.Space.Ressources.Form;

/**
 * Created by Higgy on 04.09.2017.
 */

public class TranspoterShip extends ShipObjectModelInstance {
    private static float shipTravelSpeed =100f, shipRotationSpeed=2f, shipWarpTravelSpeed = 500f;
    private static float accelerationPower =100f, decelerationPower =20f, rotationPower = 10f;
    private static ShipType sType = ShipType.TransporterMultiRole;
    private static int storages = 3;
    public TranspoterShip(Model model, float size) {
        super(model, size, sType, "TX2000", storages, shipTravelSpeed, shipWarpTravelSpeed, shipRotationSpeed, accelerationPower, decelerationPower, rotationPower);
        setCargo();
    }

    public void setCargo(){

        setStorageType(0, Form.Loose,1000);
        setStorageType(1, Form.Liquid,1000);
        setStorageType(2, Form.Gas,1000);

    }
}
