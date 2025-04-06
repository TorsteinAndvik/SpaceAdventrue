package controller.audio;

import java.util.HashMap;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Sound;

public class SoundManager {
    private final AssetManager manager;
    private boolean initialized = false;

    private final HashMap<SoundEffect, Sound> soundEffects = new HashMap<>();

    public SoundManager(AssetManager manager) {
        this.manager = manager;
    }

    public void init() {
        if (initialized) {
            return;
        }

        soundEffects.put(SoundEffect.BLIPP, manager.get("audio/blipp.ogg"));
        soundEffects.put(SoundEffect.LASER, manager.get("audio/laser_shot_0.wav"));
        soundEffects.put(SoundEffect.SHIP_EXPLOSION_SMALL, manager.get("audio/ship_explosion_small.wav"));
        soundEffects.put(SoundEffect.SHIP_EXPLOSION_BIG, manager.get("audio/ship_explosion_big.wav"));

        initialized = true;
    }

    public void play(SoundEffect soundEffect) {
        play(soundEffect, 1f);
    }

    public void play(SoundEffect soundEffect, float volume) {
        if (initialized) {
            soundEffects.get(soundEffect).play(volume);
        }
    }
}
