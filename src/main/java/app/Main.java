package app;

import org.lwjgl.system.Configuration;

import com.badlogic.gdx.Graphics.DisplayMode;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.badlogic.gdx.utils.Os;
import com.badlogic.gdx.utils.SharedLibraryLoader;

public class Main {

    public static void main(String[] args) {
        if (SharedLibraryLoader.os == Os.MacOsX) {
            Configuration.GLFW_LIBRARY_NAME.set("glfw_async");
        }

        Lwjgl3ApplicationConfiguration cfg = new Lwjgl3ApplicationConfiguration();
        DisplayMode dm = Lwjgl3ApplicationConfiguration.getDisplayMode();

        float aspectRatio = (float) dm.width / (float) dm.height;

        cfg.setTitle("TestSpaceGame");
        cfg.setWindowedMode(dm.width / 2, (int) (aspectRatio * (float) dm.height / 2f));

        // cfg.setFullscreenMode(Lwjgl3ApplicationConfiguration.getDisplayMode());
        new Lwjgl3Application(new TestSpaceGame(), cfg);
    }
}