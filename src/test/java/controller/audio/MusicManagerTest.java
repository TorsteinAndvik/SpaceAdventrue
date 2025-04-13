package controller.audio;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;

public class MusicManagerTest {

    MusicManager musicManager;
    Music musicMock;
    Music atmosphereMock;

    @BeforeEach
    public void setup() {
        AssetManager managerMock = mock(AssetManager.class);
        musicMock = mock(Music.class);
        atmosphereMock = mock(Music.class);
        when(managerMock.get("audio/music.mp3")).thenReturn(musicMock);
        when(managerMock.get("audio/atmosphere.mp3")).thenReturn(atmosphereMock);
        musicManager = new MusicManager(managerMock);
    }

    @Test
    public void constructorTest() {
        assertNotNull(musicManager.manager);
    }

    @Test
    public void initTest() {
        assertNull(musicManager.music);
        assertNull(musicManager.atmosphere);
        assertFalse(musicManager.initialized);

        musicManager.init();

        assertNotNull(musicManager.music);
        assertNotNull(musicManager.atmosphere);
        assertTrue(musicManager.initialized);

        musicManager.init();

        assertNotNull(musicManager.music);
        assertNotNull(musicManager.atmosphere);
        assertTrue(musicManager.initialized);
    }

    @Test
    public void playTest() {
        musicManager.play();
        verify(musicMock, never()).play();
        verify(atmosphereMock, never()).play();

        musicManager.init();

        musicManager.play();
        verify(musicMock).play();
        verify(atmosphereMock).play();
    }

    @Test
    public void pauseTest() {
        musicManager.pause();
        verify(musicMock, never()).pause();
        verify(atmosphereMock, never()).pause();

        musicManager.init();

        musicManager.pause();
        verify(musicMock).pause();
        verify(atmosphereMock).pause();
    }

    @Test
    public void setVolumeTest() {
        musicManager.setVolume(0.5f);
        verify(musicMock, never()).setVolume(0.5f);
        verify(atmosphereMock, never()).setVolume(0.5f);

        musicManager.init();

        musicManager.setVolume(0.5f);
        verify(musicMock).setVolume(0.5f);
        verify(atmosphereMock).setVolume(0.25f * 0.5f);
    }
}
