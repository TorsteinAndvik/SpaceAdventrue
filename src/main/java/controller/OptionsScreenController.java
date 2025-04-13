package controller;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import controller.audio.SoundEffect;
import java.util.List;
import java.util.Map;
import model.GameStateModel;
import model.constants.GameState;
import model.utils.GameControls;
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
        List<String> categories = view.getControlCategories();
        if (buttons.isEmpty()) {
            return false;
        }

        switch (keyCode) {
            case Input.Keys.UP:
                if (view.isShowingControls()) {
                    return false;
                }
                gameStateModel.setSelectedButtonIndex(
                        Math.max(0, gameStateModel.getSelectedButtonIndex() - 1));
                soundManager.play(SoundEffect.MENU_SELECT, 0.2f);
                return true;
            case Input.Keys.DOWN:
                if (view.isShowingControls()) {
                    return false;
                }
                gameStateModel.setSelectedButtonIndex(
                        Math.min(buttons.size() - 1, gameStateModel.getSelectedButtonIndex() + 1));
                soundManager.play(SoundEffect.MENU_SELECT, 0.2f);
                return true;
            case Input.Keys.RIGHT:
                if (!view.isShowingControls()) {
                    return false;
                }
                gameStateModel.setSelectedControlCategoryIndex(
                        Math.min(categories.size() - 1, gameStateModel.getSelectedControlCategoryIndex() + 1));
                view.selectControlCategory(gameStateModel.getSelectedControlCategoryIndex());
                soundManager.play(SoundEffect.MENU_SELECT, 0.2f);
                return true;
            case Input.Keys.LEFT:
                if (!view.isShowingControls()) {
                    return false;
                }
                gameStateModel.setSelectedControlCategoryIndex(
                        Math.max(0, gameStateModel.getSelectedControlCategoryIndex() - 1));
                view.selectControlCategory(gameStateModel.getSelectedControlCategoryIndex());
                soundManager.play(SoundEffect.MENU_SELECT, 0.2f);
                return true;
            case Input.Keys.ENTER:
            case Input.Keys.SPACE:
                activateButton(gameStateModel.getSelectedButtonIndex());
                return true;
            case Input.Keys.ESCAPE:
            case Input.Keys.P:
                game.setSpaceScreen();
                return true;
        }
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        Vector3 worldCoords = view.unprojectScreenCoords(screenX, screenY);

        // if showing controls screen check if back button is clicked
        if (view.isShowingControls()) {

            // check if category tab clicked
            List<Rectangle> categoryBounds = view.getCategoryBounds();
            for (int i = 0; i < categoryBounds.size(); i++) {
                if (categoryBounds.get(i).contains(worldCoords.x, worldCoords.y)) {
                    view.selectControlCategory(i);
                    soundManager.play(SoundEffect.MENU_SELECT, 0.2f);
                    return true;
                }
            }

            MenuButton backButton = view.getControlsBackButton();
            if (backButton != null && backButton.getBounds()
                    .contains(worldCoords.x, worldCoords.y)) {
                if (view.getBackButtonAction() != null) {
                    view.getBackButtonAction().run();
                }
                return true;
            }
            return false;
        }

        // Check for button clicks
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
        soundManager.play(SoundEffect.MENU_SELECT, 0.2f);

        switch (buttonIndex) {
            case 0:
                toggleSound();
                break;
            case 1:
                toggleMusic();
                break;
            case 2:
                pullUpControls();
                break;
            case 3:
                returnToPreviousScreen();
                break;
        }
    }

    private void pullUpControls() {
        view.showControlsScreen();

        // add categories
        view.addControlCategory("GAME CONTROLS");
        view.addControlCategory("MENU CONTROLS");
        view.addControlCategory("UPGRADE SCREEN");

        // get control maps, one for each tab
        Map<String, String> spaceControls = GameControls.getSpaceScreenControls();
        Map<String, String> menuControls = GameControls.getMenuControls();
        Map<String, String> upgradeControls = GameControls.getUpgradeScreenControls();

        // add controls for each category
        view.addControlsForCategory(0, spaceControls);
        view.addControlsForCategory(1, menuControls);
        view.addControlsForCategory(2, upgradeControls);

        // add back button
        view.addControlsBackButton("BACK", view::resetToOptionsMenu);
    }

    private void toggleSound() {
        boolean soundEnabled = soundManager.isSoundEnabled();
        soundManager.setSoundEnabled(!soundEnabled);

        // Update button text
        List<MenuButton> buttons = view.getOptionButtons();
        MenuButton soundButton = buttons.get(0);
        soundButton.setText("SOUND: " + (soundManager.isSoundEnabled() ? "ON" : "OFF"));
    }

    private void toggleMusic() {
        boolean musicEnabled = musicManager.isMusicEnabled();
        musicManager.setMusicEnabled(!musicEnabled);

        // Update button text
        List<MenuButton> buttons = view.getOptionButtons();
        MenuButton musicButton = buttons.get(1);
        musicButton.setText("MUSIC: " + (musicManager.isMusicEnabled() ? "ON" : "OFF"));
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

        // check hover over buttons
        List<MenuButton> buttons = view.getOptionButtons();
        for (int i = 0; i < buttons.size(); i++) {
            if (buttons.get(i).getBounds().contains(worldCoords.x, worldCoords.y)) {
                if (!view.isShowingControls() && gameStateModel.getSelectedButtonIndex() != i) {
                    gameStateModel.setSelectedButtonIndex(i);
                    soundManager.play(SoundEffect.MENU_SELECT, 0.2f);
                }
                return true;
            }
        }
        return false;
    }
}
