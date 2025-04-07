package model;

import model.SpaceCharacters.Ships.ViewablePlayer;
import model.constants.GameState;

public class GameStateModel {

    private GameState currentState;
    private SpaceGameModel spaceGameModel;
    private UpgradeScreenModel upgradeScreenModel;

    private int selectedButtonIndex = 0;

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
            ViewablePlayer playerShip = spaceGameModel.getPlayer();
            if (upgradeScreenModel == null) {
                upgradeScreenModel = new UpgradeScreenModel(playerShip);
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


    public int getSelectedButtonIndex() {
        return selectedButtonIndex;
    }

    public void setSelectedButtonIndex(int selectedButtonIndex) {
        this.selectedButtonIndex = selectedButtonIndex;
    }

    public boolean hasActiveGame() {
        return spaceGameModel != null && spaceGameModel.getPlayer() != null;
    }


}
