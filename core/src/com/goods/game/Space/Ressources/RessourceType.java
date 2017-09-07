package com.goods.game.Space.Ressources;

import com.badlogic.gdx.graphics.Color;

/**
 * Created by Higgy on 31.08.2017.
 */

public enum RessourceType {

    Crystal (Color.GREEN, 2, Form.Solid),
    Metal (Color.RED, 3, Form.Solid),
    Gas (Color.BROWN, 0.5f, Form.Gas),
    Water (Color.BLUE,1, Form.Liquid);

    private final Color color;
    private final float weight;
    private Form form;

    RessourceType(Color color, float weight, Form form) {
        this.color = color;
        this.weight = weight;
        this.form = form;
    }

    public Color getColor() {
        return color;
    }

    public float getWeight() {
        return weight;
    }

    public Form getForm() {
        return form;
    }
}
