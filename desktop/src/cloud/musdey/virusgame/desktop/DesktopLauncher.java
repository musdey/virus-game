package cloud.musdey.virusgame.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

import cloud.musdey.virusgame.VirusGame;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.width = 576;
		config.height = 1024;
		config.title = VirusGame.TITLE;
		new LwjglApplication(new VirusGame(null,null), config);
	}
}
