package com.goods.game.Space.Stars;

import com.badlogic.gdx.graphics.g3d.Model;
import com.goods.game.Space.GameObjectModelInstance;
import com.goods.game.Space.ObjectType;

/**
 * Created by Higgy on 01.09.2017.
 */

public class Star extends StarObjectModelInstance {
    public Star(Model model, float size) {
        super(model,size, "Sun");
    }
}
