package com.panacea.RufusPyramid.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.panacea.RufusPyramid.Main;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.title = "Rufus";
		config.width = 360;
		config.height = 640;
		new LwjglApplication(new Main(), config);
	}
}
