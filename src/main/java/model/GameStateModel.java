package model;

import model.ShipComponents.Components.ShipStructure;
import model.SpaceCharacters.SpaceShip;
import model.constants.GameState;

public class GameStateModel {

    private GameState currentState;
    private SpaceGameModel spaceGameModel;
    private UpgradeScreenModel upgradeScreenModel;


    /**
     * Creates a new GameStateModel that will manage game states
     */
    public GameStateModel() {

        this.currentState = GameState.LOADING;
    }

    public void changeState(GameState newState) {
        if (newState == GameState.PLAYING && spaceGameModel == null) {
            spaceGameModel = new SpaceGameModel();
        } else if (newState == GameState.UPGRADE && spaceGameModel != null) {
            SpaceShip playerShip = spaceGameModel.getPlayer();
            ShipStructure playerShipStructure = playerShip.getShipStructure();
            if (upgradeScreenModel == null) {
                upgradeScreenModel = new UpgradeScreenModel(playerShipStructure);
            }
        }
        this.currentState = newState;
    }

    public void onAssetsLoaded() {
        changeState(GameState.START_GAME);
    }

    public void startNewGame() {
        this.spaceGameModel = new SpaceGameModel();
        this.upgradeScreenModel = null;
        changeState(GameState.PLAYING);
    }

    public void showUpgradeScreen() {
        changeState(GameState.UPGRADE);
    }

    public void returnToGame() {
        changeState(GameState.PLAYING);
    }

    public GameState getCurrentState() {
        return this.currentState;
    }

    public SpaceGameModel getSpaceGameModel() {
        return this.spaceGameModel;
    }

    public UpgradeScreenModel getUpgradeScreenModel() {
        return this.upgradeScreenModel;
    }


}
