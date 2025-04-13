package controller.audio;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyFloat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Sound;

public class SoundManagerTest {

    SoundManager soundManager;
    Sound menuSelectMock;
    Sound laser0Mock;
    Sound laser1Mock;
    Sound laser2Mock;
    Sound shipExplosionSmallMock;
    Sound shipExplosionBigMock;

    @BeforeEach
    public void setup() {
        AssetManager managerMock = mock(AssetManager.class);
        menuSelectMock = mock(Sound.class);
        laser0Mock = mock(Sound.class);
        laser1Mock = mock(Sound.class);
        laser2Mock = mock(Sound.class);
        shipExplosionSmallMock = mock(Sound.class);
        shipExplosionBigMock = mock(Sound.class);

        when(managerMock.get("audio/menu_select.wav")).thenReturn(menuSelectMock);
        when(managerMock.get("audio/laser_0.mp3")).thenReturn(laser0Mock);
        when(managerMock.get("audio/laser_1.mp3")).thenReturn(laser1Mock);
        when(managerMock.get("audio/laser_2.mp3")).thenReturn(laser2Mock);
        when(managerMock.get("audio/ship_explosion_small.wav")).thenReturn(shipExplosionSmallMock);
        when(managerMock.get("audio/ship_explosion_big.wav")).thenReturn(shipExplosionBigMock);

        soundManager = new SoundManager(managerMock);
    }

    @Test
    public void constructorTest() {
        assertNotNull(soundManager.manager);
        assertNotNull(soundManager.soundEffects);
    }

    @Test
    public void initTest() {
        assertFalse(soundManager.initialized);

        assertNull(soundManager.soundEffects.get(SoundEffect.MENU_SELECT));
        assertNull(soundManager.soundEffects.get(SoundEffect.LASER_0));
        assertNull(soundManager.soundEffects.get(SoundEffect.LASER_1));
        assertNull(soundManager.soundEffects.get(SoundEffect.LASER_2));
        assertNull(soundManager.soundEffects.get(SoundEffect.SHIP_EXPLOSION_SMALL));
        assertNull(soundManager.soundEffects.get(SoundEffect.SHIP_EXPLOSION_BIG));

        soundManager.init();

        assertNotNull(soundManager.soundEffects.get(SoundEffect.MENU_SELECT));
        assertNotNull(soundManager.soundEffects.get(SoundEffect.LASER_0));
        assertNotNull(soundManager.soundEffects.get(SoundEffect.LASER_1));
        assertNotNull(soundManager.soundEffects.get(SoundEffect.LASER_2));
        assertNotNull(soundManager.soundEffects.get(SoundEffect.SHIP_EXPLOSION_SMALL));
        assertNotNull(soundManager.soundEffects.get(SoundEffect.SHIP_EXPLOSION_BIG));
        assertTrue(soundManager.initialized);

        soundManager.init();

        assertNotNull(soundManager.soundEffects.get(SoundEffect.MENU_SELECT));
        assertNotNull(soundManager.soundEffects.get(SoundEffect.LASER_0));
        assertNotNull(soundManager.soundEffects.get(SoundEffect.LASER_1));
        assertNotNull(soundManager.soundEffects.get(SoundEffect.LASER_2));
        assertNotNull(soundManager.soundEffects.get(SoundEffect.SHIP_EXPLOSION_SMALL));
        assertNotNull(soundManager.soundEffects.get(SoundEffect.SHIP_EXPLOSION_BIG));
        assertTrue(soundManager.initialized);
    }

    @Test
    public void playTest() {
        soundManager.play(SoundEffect.LASER_0);
        verify(laser0Mock, never()).play(anyFloat());

        soundManager.init();

        soundManager.play(SoundEffect.LASER_0);
        verify(laser0Mock).play(anyFloat());
    }
}
