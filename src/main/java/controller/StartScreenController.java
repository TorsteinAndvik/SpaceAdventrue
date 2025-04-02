package controller;


import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Vector3;
import java.util.List;
import model.GameStateModel;
import model.utils.MenuButton;
import view.SpaceGame;
import view.StartGameScreen;

public class StartScreenController extends GenericController {


    private final GameStateModel model;
    private final StartGameScreen view;
    private final SpaceGame game;

    public StartScreenController(StartGameScreen view, GameStateModel model,
        SpaceGame game) {
        this.model = model;
        this.view = view;
        this.game = game;
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
                model.setSelectedButtonIndex(Math.max(0, model.getSelectedButtonIndex() - 1));
                view.playBlipSound(0.4f);
                System.out.println("play blip");
                return true;
            case Input.Keys.DOWN:
                model.setSelectedButtonIndex(
                    Math.min(menuButtons.size() - 1, model.getSelectedButtonIndex() + 1));
                view.playBlipSound(0.4f);
                return true;
            case Input.Keys.ENTER:
            case Input.Keys.SPACE:
                activateButton(model.getSelectedButtonIndex());
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
                model.setSelectedButtonIndex(i);
                activateButton(i);
                return true;
            }
        }
        return false;
    }

    public void activateButton(int index) {
        view.playBlipSound(0.7f);

        switch (index) {
            case 0:
                startGame();
                break;
            case 1:
                showOptionsScreen();
                break;
            case 2:
                exitGame();
                break;
        }
    }

    private void startGame() {
        model.startNewGame();
        game.setSpaceScreen();
    }

    private void showOptionsScreen() {
        //TODO: Implement options screen
    }

    private void exitGame() {
        com.badlogic.gdx.Gdx.app.exit();
    }

    public boolean mouseMoved(int screenX, int screenY) {
        Vector3 worldCoords = view.unprojectScreenCoords(screenX, screenY);

        //check if mouse hovers over any button
        List<MenuButton> menuButtons = view.getMenuButtons();
        for (int i = 0; i < menuButtons.size(); i++) {
            if (menuButtons.get(i).getBounds().contains(worldCoords.x, worldCoords.y)) {
                if (model.getSelectedButtonIndex() != i) {
                    model.setSelectedButtonIndex(i);
                    view.playBlipSound(0.2f);
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
