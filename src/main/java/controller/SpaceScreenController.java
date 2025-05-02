package controller;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.Input.Keys;
import controller.audio.AudioCallback;
import controller.audio.SoundEffect;
import model.GameStateModel;
import model.SpaceGameModel;
import model.constants.GameState;
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
        if (model.isGameOver()) {
            gameStateModel.changeState(GameState.GAME_OVER);
        }
    }

    @Override
    protected boolean handleKeyDown(int keycode) {
        return switch (keycode) {
            case Input.Keys.W -> {
                if (gameStateModel.getCurrentState() == GameState.GAME_OVER) {
                    yield true;
                }
                model.setAccelerateForward(true);
                model.setAccelerateBackward(false);
                yield true;
            }
            case Input.Keys.S -> {
                if (gameStateModel.getCurrentState() == GameState.GAME_OVER) {
                    yield true;
                }
                model.setAccelerateBackward(true);
                model.setAccelerateForward(false);
                yield true;
            }
            case Input.Keys.A -> {
                if (gameStateModel.getCurrentState() == GameState.GAME_OVER) {
                    yield true;
                }
                model.setAccelerateCounterClockwise(true);
                model.setAccelerateClockwise(false);
                yield true;
            }
            case Input.Keys.D -> {
                if (gameStateModel.getCurrentState() == GameState.GAME_OVER) {
                    yield true;
                }
                model.setAccelerateClockwise(true);
                model.setAccelerateCounterClockwise(false);
                yield true;
            }
            case Input.Keys.SPACE -> {
                if (gameStateModel.getCurrentState() == GameState.GAME_OVER) {
                    yield true;
                }
                model.playerShoot();
                yield true;
            }
            case Input.Keys.U -> {
                if (gameStateModel.getCurrentState() == GameState.GAME_OVER) {
                    yield true;
                }
                game.setUpgradeScreen();
                yield true;
            }
            case Keys.ESCAPE -> {
                game.setStartScreen();
                yield true;
            }
            case Input.Keys.P -> {
                if (gameStateModel.getCurrentState() == GameState.GAME_OVER) {
                    yield true;
                }
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
                if (gameStateModel.getCurrentState() == GameState.GAME_OVER) {
                    yield true;
                }
                model.setAccelerateForward(false);
                yield true;
            }
            case Input.Keys.S -> {
                if (gameStateModel.getCurrentState() == GameState.GAME_OVER) {
                    yield true;
                }
                model.setAccelerateBackward(false);
                yield true;
            }
            case Input.Keys.A -> {
                if (gameStateModel.getCurrentState() == GameState.GAME_OVER) {
                    yield true;
                }
                model.setAccelerateCounterClockwise(false);
                yield true;
            }
            case Input.Keys.D -> {
                if (gameStateModel.getCurrentState() == GameState.GAME_OVER) {
                    yield true;
                }
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
