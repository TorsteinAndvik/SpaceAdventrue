package app;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.*;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.headless.HeadlessApplication;
import com.badlogic.gdx.backends.headless.HeadlessApplicationConfiguration;

/**
 * Integration tests (example).
 * <p>
 * (Run using `mvn verify`)
 */
public class ExampleIT {

    /**
     * Static method run before everything else
     */
    @BeforeAll
    static void setUpBeforeAll() {
        HeadlessApplicationConfiguration config = new HeadlessApplicationConfiguration();
        ApplicationListener listener = new ApplicationListener() {

            @Override
            public void create() {
            }

            @Override
            public void resize(int width, int height) {

            }

            @Override
            public void render() {

            }

            @Override
            public void pause() {
            }

            @Override
            public void resume() {
            }

            @Override
            public void dispose() {
            }
        };
        new HeadlessApplication(listener, config);
    }

    /**
     * Setup method called before each of the test methods
     */
    @BeforeEach
    void setUpBeforeEach() {
    }

    /**
     * Simple test case
     */
    @Test
    void dummy1() {
        // check that we can find a file using the LibGDX file API
        assertNotNull(Gdx.files.internal("images/obligator.png"));
    }
}