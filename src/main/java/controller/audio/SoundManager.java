package controller.audio;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Sound;
import java.util.HashMap;

public class SoundManager {

    protected final AssetManager manager;
    protected boolean initialized = false;
    protected boolean soundEnabled = true;

    protected final HashMap<SoundEffect, Sound> soundEffects = new HashMap<>();

    public SoundManager(AssetManager manager) {
        this.manager = manager;
    }

    public void init() {
        if (initialized) {
            return;
        }

        soundEffects.put(SoundEffect.MENU_SELECT, manager.get("audio/menu_select.wav"));
        soundEffects.put(SoundEffect.LASER_0, manager.get("audio/laser_0.mp3"));
        soundEffects.put(SoundEffect.LASER_1, manager.get("audio/laser_1.mp3"));
        soundEffects.put(SoundEffect.LASER_2, manager.get("audio/laser_2.mp3"));
        soundEffects.put(SoundEffect.SHIP_EXPLOSION_SMALL,
            manager.get("audio/ship_explosion_small.wav"));
        soundEffects.put(SoundEffect.SHIP_EXPLOSION_BIG,
            manager.get("audio/ship_explosion_big.wav"));

        initialized = true;
    }

    public void play(SoundEffect soundEffect) {
        play(soundEffect, 0.5f);
    }

    public void play(SoundEffect soundEffect, float volume) {
        if (initialized && soundEnabled) {
            soundEffects.get(soundEffect).play(volume);
        }
    }

    /**
     * Checks if sound effects are enabled.
     *
     * @return true if sound is enabled, false otherwise.
     */
    public boolean isSoundEnabled() {
        return soundEnabled;
    }

    /**
     * Sets whether sound effects should be enabled.
     *
     * @param soundEnabled true to enable sound, false to disable
     */
    public void setSoundEnabled(boolean soundEnabled) {
        this.soundEnabled = soundEnabled;
    }
}
