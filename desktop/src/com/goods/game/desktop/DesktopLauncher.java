package com.goods.game.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.goods.game.Main;
import com.goods.game.MeshExample;
import com.goods.game.Test;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        config.title = "Goods";
        config.resizable = true;
        config.fullscreen = false;
		config.useGL30 = true;
		config.width = 1600;
		config.height = 1200;
		//new LwjglApplication(new Main(), config);
        //new LwjglApplication(new MeshExample(), config);
        new LwjglApplication(new Test(), config);
	}
}
