package com.goods.game.Space.Planets;

import com.badlogic.gdx.graphics.Color;

/**
 * Created by Higgy on 31.08.2017.
 */

public enum PlanetType {
    Terrastic (Color.GREEN), Vulcano (Color.RED), Gas (Color.BROWN), Ice (Color.WHITE), Desert (Color.ORANGE), Water (Color.BLUE), Random (null);

    private final Color color;
    PlanetType(Color color) {
        this.color = color;
    }

    public Color getColor() {
        return color;
    }
}

