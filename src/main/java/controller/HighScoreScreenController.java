package controller;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.math.Vector3;
import controller.audio.SoundEffect;
import model.GameStateModel;
import view.SpaceGame;
import view.screens.HighScoreScreen;

public class HighScoreScreenController extends GenericController {

    private final HighScoreScreen view;
    private final int backButtonIndex = 0;

    public HighScoreScreenController(HighScoreScreen view, GameStateModel model, SpaceGame game) {
        super(view, model, game);
        this.view = view;

    }

    @Override
    protected boolean handleKeyDown(int keyCode) {
        if (keyCode == Keys.ESCAPE || keyCode == Keys.ENTER || keyCode == Keys.SPACE) {
            activateButton(backButtonIndex);
            return true;
        }
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        Vector3 worldCoords = view.unprojectScreenCoords(screenX, screenY);

        if (view.getBackButton().getBounds().contains(worldCoords.x, worldCoords.y)) {
            gameStateModel.setSelectedButtonIndex(backButtonIndex);
            activateButton(button);
            return true;
        }

        return false;
    }

    @Override
    public void update(float delta) { }


    public void activateButton(int buttonIndex) {
        soundManager.play(SoundEffect.MENU_SELECT, 0.2f);

        if (buttonIndex == backButtonIndex) {
            game.setStartScreen();
        }
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
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        Vector3 worldCoords = view.unprojectScreenCoords(screenX, screenY);

        if (view.getBackButton().getBounds().contains(worldCoords.x, worldCoords.y)) {
            if (gameStateModel.getSelectedButtonIndex() != backButtonIndex) {
                gameStateModel.setSelectedButtonIndex(backButtonIndex);
                soundManager.play(SoundEffect.MENU_SELECT, 0.2f);
            }
            return true;
        }

        return false;
    }
}
