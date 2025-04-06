package controller;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Vector2;
import model.SpaceGameModel;
import view.SpaceGame;
import view.screens.SpaceScreen;

public class SpaceGameScreenController extends GenericController {

    private final SpaceGameModel model;
    private final SpaceScreen view;
    private final SpaceGame game;
    private final Vector2 touchPos;

    public SpaceGameScreenController(SpaceScreen view, SpaceGameModel model, SpaceGame game) {
        this.model = model;
        this.view = view;
        this.game = game;
        this.touchPos = new Vector2();
    }

    public void update(float delta) {
        model.update(delta);
    }

    @Override
    protected boolean handleKeyDown(int keycode) {
        return switch (keycode) {
            case Input.Keys.W -> {
                model.setAccelerateForward(true);
                model.setAccelerateBackward(false);
                yield true;
            }
            case Input.Keys.S -> {
                model.setAccelerateBackward(true);
                model.setAccelerateForward(false);
                yield true;
            }
            case Input.Keys.A -> {
                model.setAccelerateCounterClockwise(true);
                model.setAccelerateClockwise(false);
                yield true;
            }
            case Input.Keys.D -> {
                model.setAccelerateClockwise(true);
                model.setAccelerateCounterClockwise(false);
                yield true;
            }
            case Input.Keys.SPACE -> {
                model.playerShoot();
                yield true;
            }
            case Input.Keys.ESCAPE -> {
                game.setUpgradeScreen();
                yield true;
            }
            default -> false;
        };
    }

    @Override
    protected boolean handleKeyUp(int keycode) {
        return switch (keycode) {
            case Input.Keys.W -> {
                model.setAccelerateForward(false);
                yield true;
            }
            case Input.Keys.S -> {
                model.setAccelerateBackward(false);
                yield true;
            }
            case Input.Keys.A -> {
                model.setAccelerateCounterClockwise(false);
                yield true;
            }
            case Input.Keys.D -> {
                model.setAccelerateClockwise(false);
                yield true;
            }
            default -> false;
        };
    }

    public void reset() {
        model.setAccelerateForward(false);
        model.setAccelerateBackward(false);
        model.setAccelerateClockwise(false);
        model.setAccelerateCounterClockwise(false);
    }

    @Override
    protected boolean leftClick(int screenX, int screenY) {
        return false;
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
}
