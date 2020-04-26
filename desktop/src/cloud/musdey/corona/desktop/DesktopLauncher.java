package cloud.musdey.corona.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

import cloud.musdey.corona.CoronaGame;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.width = 576;
		config.height = 1024;
		config.title = CoronaGame.TITLE;
		new LwjglApplication(new CoronaGame(null,null), config);
	}
}
