package controller.audio;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;

public class MusicManager {
    private final AssetManager manager;
    private Music music;
    private Music atmosphere;
    private boolean initialized = false;

    public MusicManager(AssetManager manager) {
        this.manager = manager;
    }

    public void init() {
        if (initialized) {
            return;
        }

        music = manager.get("audio/music.mp3");
        music.setLooping(true);

        atmosphere = manager.get("audio/atmosphere.mp3");
        atmosphere.setLooping(true);

        initialized = true;
    }

    public void play() {
        if (initialized) {
            music.play();
            atmosphere.play();
        }
    }

    public void pause() {
        if (initialized) {
            music.pause();
            atmosphere.pause();
        }
    }

    public void setVolume(float volume) {
        if (initialized) {
            music.setVolume(volume);
            atmosphere.setVolume(0.25f * volume);
        }
    }
}