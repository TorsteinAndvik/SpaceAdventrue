package controller.audio;

import java.util.HashMap;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Sound;

public class SoundManager {
    protected final AssetManager manager;
    protected boolean initialized = false;

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
        soundEffects.put(SoundEffect.SHIP_EXPLOSION_SMALL, manager.get("audio/ship_explosion_small.wav"));
        soundEffects.put(SoundEffect.SHIP_EXPLOSION_BIG, manager.get("audio/ship_explosion_big.wav"));

        initialized = true;
    }

    public void play(SoundEffect soundEffect) {
        play(soundEffect, 0.5f);
    }

    public void play(SoundEffect soundEffect, float volume) {
        if (initialized) {
            soundEffects.get(soundEffect).play(volume);
        }
    }
}
