package controller;

import app.TestSpaceGame;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Vector3;
import controller.audio.SoundEffect;
import java.util.List;
import model.GameStateModel;
import model.constants.GameState;
import model.utils.MenuButton;
import view.SpaceGame;
import view.screens.OptionsScreen;

public class OptionsScreenController extends GenericController {

    private final OptionsScreen view;

    public OptionsScreenController(OptionsScreen view, GameStateModel model, SpaceGame game) {
        super(view, model, game);
        this.view = view;

    }

    @Override
    protected boolean handleKeyDown(int keyCode) {
        List<MenuButton> buttons = view.getOptionButtons();
        if (buttons.isEmpty()) {
            return false;
        }

        switch (keyCode) {
            case Input.Keys.UP:
                gameStateModel.setSelectedButtonIndex(
                    Math.max(0, gameStateModel.getSelectedButtonIndex() - 1));
                soundManager.play(SoundEffect.MENU_SELECT, 0.4f);
                return true;
            case Input.Keys.DOWN:
                gameStateModel.setSelectedButtonIndex(
                    Math.min(buttons.size() - 1, gameStateModel.getSelectedButtonIndex() + 1));
                soundManager.play(SoundEffect.MENU_SELECT, 0.4f);
                return true;
            case Input.Keys.ENTER:
            case Input.Keys.SPACE:
                activateButton(gameStateModel.getSelectedButtonIndex());
                return true;
            case Input.Keys.ESCAPE:
                returnToPreviousScreen();
                return true;
            case Input.Keys.P:
                if (gameStateModel.getPreviousState() == GameState.PLAYING) {
                    returnToPreviousScreen();
                    return true;
                }
                return false;
        }
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        Vector3 worldCoords = view.unprojectScreenCoords(screenX, screenY);

        //Check for button clicks
        List<MenuButton> buttons = view.getOptionButtons();
        for (int i = 0; i < buttons.size(); i++) {
            if (buttons.get(i).getBounds().contains(worldCoords.x, worldCoords.y)) {
                gameStateModel.setSelectedButtonIndex(i);
                activateButton(i);
                return true;
            }
        }
        return false;
    }

    public void activateButton(int buttonIndex) {
        soundManager.play(SoundEffect.MENU_SELECT, 0.7f);
        List<MenuButton> buttons = view.getOptionButtons();

        switch (buttonIndex) {
            case 0:
                toggleSound();
                break;
            case 1:
                toggleMusic();
                break;
            case 2:
                returnToPreviousScreen();
                break;
        }
    }

    private void toggleSound() {
        boolean soundEnabled = soundManager.isSoundEnabled();
        soundManager.setSoundEnabled(!soundEnabled);

        //Update button text
        List<MenuButton> buttons = view.getOptionButtons();
        MenuButton soundButton = buttons.get(0);
        soundButton.setText("SOUND: " + (soundManager.isSoundEnabled() ? "ON" : "OFF"));
    }

    private void toggleMusic() {
        boolean musicEnabled = musicManager.isMusicEnabled();
        musicManager.setMusicEnabled(!musicEnabled);

        //Update button text
        List<MenuButton> buttons = view.getOptionButtons();
        MenuButton musicButton = buttons.get(1);
        musicButton.setText("MUSIC: " + (musicManager.isMusicEnabled() ? "ON" : "OFF"));
    }

    private void returnToPreviousScreen() {
        GameState prevState = gameStateModel.getPreviousState();
        if (prevState == GameState.PLAYING) {
            game.setSpaceScreen();
        } else {
            ((TestSpaceGame) game).setStartScreen();
        }
    }


    @Override
    public void update(float delta) {

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

        //check hover over buttons
        List<MenuButton> buttons = view.getOptionButtons();
        for (int i = 0; i < buttons.size(); i++) {
            if (buttons.get(i).getBounds().contains(worldCoords.x, worldCoords.y)) {
                if (gameStateModel.getSelectedButtonIndex() != i) {
                    gameStateModel.setSelectedButtonIndex(i);
                    soundManager.play(SoundEffect.MENU_SELECT, 0.2f);
                }
                return true;
            }

        }
        return false;
    }
}
