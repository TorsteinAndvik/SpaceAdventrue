package controller;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.math.Vector3;
import controller.audio.SoundEffect;
import model.GameStateModel;
import model.constants.GameState;
import view.SpaceGame;
import view.screens.HighScoreScreen;

public class HighScoreScreenController extends GenericController {

    private final HighScoreScreen view;

    public HighScoreScreenController(HighScoreScreen view, GameStateModel model, SpaceGame game) {
        super(view, model, game);
        this.view = view;
        gameStateModel.setSelectedButtonIndex(0);
    }

    @Override
    protected boolean handleKeyDown(int keyCode) {
        if (keyCode == Keys.ESCAPE) {
            activateButton(gameStateModel.getSelectedButtonIndex());
            game.setStartScreen();
            return true;
        }

        if (keyCode == Keys.ENTER || keyCode == Keys.SPACE) {
            activateButton(gameStateModel.getSelectedButtonIndex());
            return true;
        }

        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        Vector3 worldCoords = view.unprojectScreenCoords(screenX, screenY);

        if (view.getBackButton().getBounds().contains(worldCoords.x, worldCoords.y)) {
            gameStateModel.setSelectedButtonIndex(0);
            activateButton(0);
            return true;
        }

        return false;
    }

    public void activateButton(int buttonIndex) {
        soundManager.play(SoundEffect.MENU_SELECT, 0.2f);

        if (buttonIndex == 0) {
            returnToPreviousScreen();
        }
    }

    private void returnToPreviousScreen() {
        GameState prevState = gameStateModel.getPreviousState();
        if (prevState == GameState.PLAYING) {
            game.setSpaceScreen();
        } else {
            game.setStartScreen();
        }
    }

    @Override
    public void update(float delta) {
        // No update logic currently needed
    }

    @Override
    protected boolean leftClick(int screenX, int screenY) {
        return touchDown(screenX, screenY, 0, 0);
    }

    @Override
    protected boolean rightClick(int screenX, int screenY) {
        return false;
    }

    @Override
    protected boolean middleClick() {
        return false;
    }

    @Override
    protected boolean leftClickDragged(int screenX, int screenY) {
        return false;
    }

    @Override
    protected boolean rightClickDragged(int screenX, int screenY) {
        return false;
    }

    @Override
    protected boolean leftClickRelease() {
        return false;
    }

    @Override
    protected boolean rightClickRelease() {
        return false;
    }

    @Override
    protected void handleScroll(float amountY) {
        // No scroll handling needed
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        Vector3 worldCoords = view.unprojectScreenCoords(screenX, screenY);

        if (view.getBackButton().getBounds().contains(worldCoords.x, worldCoords.y)) {
            if (gameStateModel.getSelectedButtonIndex() != 0) {
                gameStateModel.setSelectedButtonIndex(0);
                soundManager.play(SoundEffect.MENU_SELECT, 0.2f);
            }
            return true;
        }

        return false;
    }
}
