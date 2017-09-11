package com.goods.game.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.badlogic.gdx.graphics.Color;
import com.goods.game.SpaceTrader;
import com.goods.game.SpaceTraderTEST;
import com.goods.game.SpaceTraderTESTX_Y_Z;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        config.title = "Goods";
        config.resizable = true;
        config.fullscreen = false;
		config.useGL30 = true;
		config.width = 1600;
		config.height = 1200;
        config.initialBackgroundColor= Color.WHITE;
        // new LwjglApplication(new Main(), config);
        //new LwjglApplication(new MeshExample(), config);
       // new LwjglApplication(new Main(), config);
		new LwjglApplication(new SpaceTrader(), config);
//		new LwjglApplication(new SpaceTraderTESTX_Y_Z(), config);
		// gradlew desktop:dis
	}
}
