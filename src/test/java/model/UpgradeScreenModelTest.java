package model;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.backends.headless.HeadlessApplication;
import com.badlogic.gdx.backends.headless.HeadlessApplicationConfiguration;
import model.ShipComponents.Components.ShipStructure;
import model.ShipComponents.ShipFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class UpgradeScreenModelTest {

    // TODO: Finish testing as we keep implementing methods in spaceGame.
    private UpgradeScreenModel gameModel;

    // Borrowing from the Wiki here:
    // https://git.app.uib.no/inf112/25v/inf112-25v/-/wikis/notater/Testing-og-Mocking
    // [accessed 14.03.25]
    @BeforeAll
    static void setup() {
        HeadlessApplicationConfiguration config = new HeadlessApplicationConfiguration();
        ApplicationListener listener = new ApplicationAdapter() {
        };
        new HeadlessApplication(listener, config);
    }

    @BeforeEach
    public void setUp() {
        setup();
        ShipStructure structure = ShipFactory.createShipFromJson("enemy1.json");
        gameModel = new UpgradeScreenModel(structure);

    }

    @Test
    public void testInitialization() {
        assertNotNull(gameModel.getExpandedGrid());

    }

}