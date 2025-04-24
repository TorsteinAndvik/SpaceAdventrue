package controller;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Vector3;
import controller.audio.SoundEffect;
import java.util.List;
import model.GameStateModel;
import model.utils.MenuButton;
import view.SpaceGame;
import view.screens.StartGameScreen;

public class StartScreenController extends GenericController {

    private final StartGameScreen view;

    public StartScreenController(StartGameScreen view, GameStateModel model, SpaceGame game) {
        super(view, model, game);
        this.view = view;
    }

    @Override
    public void update(float delta) {
    }

    @Override
    protected boolean handleKeyDown(int keycode) {
        List<MenuButton> menuButtons = view.getMenuButtons();
        if (menuButtons.isEmpty()) {
            return false;
        }

        switch (keycode) {
            case Input.Keys.UP:
                gameStateModel.setSelectedButtonIndex(
                        Math.max(0, gameStateModel.getSelectedButtonIndex() - 1));
                soundManager.play(SoundEffect.MENU_SELECT, 0.4f);
                return true;
            case Input.Keys.DOWN:
                gameStateModel.setSelectedButtonIndex(
                        Math.min(menuButtons.size() - 1, gameStateModel.getSelectedButtonIndex() + 1));
                soundManager.play(SoundEffect.MENU_SELECT, 0.4f);
                return true;
            case Input.Keys.ENTER:
            case Input.Keys.SPACE:
                activateButton(gameStateModel.getSelectedButtonIndex());
                return true;
        }
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        // Convert screen coordinates to world coordinates
        Vector3 worldCoords = view.unprojectScreenCoords(screenX, screenY);

        // Check if any button was clicked
        List<MenuButton> menuButtons = view.getMenuButtons();
        for (int i = 0; i < menuButtons.size(); i++) {
            if (menuButtons.get(i).getBounds().contains(worldCoords.x, worldCoords.y)) {
                gameStateModel.setSelectedButtonIndex(i);
                activateButton(i);
                return true;
            }
        }
        return false;
    }

    public void activateButton(int index) {
        soundManager.play(SoundEffect.MENU_SELECT, 0.7f);

        switch (index) {
            case 0 -> startGame();
            case 1 -> showHighScoreScreen();
            case 2 -> showOptionsScreen();
            case 3 -> exitGame();
        }
    }

    private void startGame() {
        gameStateModel.startNewGame();
        game.setUpgradeScreen();
    }

    private void showHighScoreScreen() { game.setHighScoreScreen(); }

    private void showOptionsScreen() {
        game.setOptionsScreen();
    }

    private void exitGame() {
        com.badlogic.gdx.Gdx.app.exit();
    }

    public boolean mouseMoved(int screenX, int screenY) {
        Vector3 worldCoords = view.unprojectScreenCoords(screenX, screenY);

        // check if mouse hovers over any button
        List<MenuButton> menuButtons = view.getMenuButtons();
        for (int i = 0; i < menuButtons.size(); i++) {
            if (menuButtons.get(i).getBounds().contains(worldCoords.x, worldCoords.y)) {
                if (gameStateModel.getSelectedButtonIndex() != i) {
                    gameStateModel.setSelectedButtonIndex(i);
                    soundManager.play(SoundEffect.MENU_SELECT, 0.2f);
                }
                return true;
            }
        }
        return false;
    }

    @Override
    protected boolean leftClick(int screenX, int screenY) {
        return touchDown(screenX, screenY, 0, 0);
    }

    @Override
    protected boolean rightClick(int screenX, int screenY) {
        return false; // no right click action in this screen
    }

    @Override
    protected boolean middleClick() {
        return false; // no middle click action in this screen
    }

    @Override
    protected boolean leftClickDragged(int screenX, int screenY) {
        return false; // no drag action in this screen
    }

    @Override
    protected boolean rightClickDragged(int screenX, int screenY) {
        return false; // no drag action in this screen
    }

    @Override
    protected boolean leftClickRelease() {
        return false; // no release action in this screen
    }

    @Override
    protected boolean rightClickRelease() {
        return false; // no release action in this screen
    }

    @Override
    protected void handleScroll(float amountY) {
        // no scroll action in this screen
    }
}
