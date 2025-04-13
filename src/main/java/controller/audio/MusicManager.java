package controller.audio;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;

public class MusicManager {

    protected final AssetManager manager;
    protected Music music;
    protected Music atmosphere;
    protected boolean initialized = false;
    protected boolean enabled = true;
    protected float volume = 0.5f;

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
        if (initialized && enabled) {
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

    /**
     * Checks if music is enabled.
     *
     * @return true if enabled, false otherwise.
     */
    public boolean isMusicEnabled() {
        return enabled;
    }

    /**
     * Sets whether music should be enabled.
     *
     * @param musicEnabled true to enable music, false to disable
     */
    public void setMusicEnabled(boolean musicEnabled) {
        this.enabled = musicEnabled;

        // Update volume immediately based on new setting
        if (initialized) {
            if (enabled) {
                setVolume(volume);
                play();

            } else {
                pause();
            }
        }
    }
}