package com.panacea.RufusPyramid.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.panacea.RufusPyramid.main;
import com.panacea.RufusPyramid.test.TiledMapDirectLoaderTest;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		new LwjglApplication(new TiledMapDirectLoaderTest(), config);
	}
}
