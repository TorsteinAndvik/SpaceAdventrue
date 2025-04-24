package controller;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.Input.Keys;
import controller.audio.AudioCallback;
import controller.audio.SoundEffect;
import model.GameStateModel;
import model.SpaceGameModel;
import view.SpaceGame;
import view.screens.SpaceScreen;

public class SpaceScreenController extends GenericController implements AudioCallback {

    private final SpaceGameModel model;

    public SpaceScreenController(SpaceScreen view, GameStateModel gameStateModel, SpaceGame game) {
        super(view, gameStateModel, game);
        this.model = gameStateModel.getSpaceGameModel();
        soundManager.init();
        model.setAudioCallback(this);
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
            case Input.Keys.U -> {
                game.setUpgradeScreen();
                yield true;
            }
            case Keys.ESCAPE -> {
                // TODO: Do we keep this?
                game.setStartScreen();
                yield true;
            }
            case Input.Keys.P -> {
                game.setOptionsScreen();
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

    @Override
    public void play(SoundEffect soundEffect) {
        soundManager.play(soundEffect);
    }
}
