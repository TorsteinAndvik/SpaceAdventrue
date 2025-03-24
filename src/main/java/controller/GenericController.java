package controller;

import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.math.Vector2;

/**
 * Abstract controller for implementing input handling. Provides a common structure for handling
 * mouse and keyboard input across different game screens
 */
abstract class GenericController extends InputAdapter {

    protected final Vector2 touchPos;
    protected boolean leftClickLocked;
    protected boolean rightClickLocked;

    public GenericController() {
        this.touchPos = new Vector2();
    }

    @Override
    public boolean scrolled(float amountX, float amountY) {
        handleScroll(amountY);
        return true;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        return switch (button) {
            case 0 -> leftClick(screenX, screenY);
            case 1 -> rightClick(screenX, screenY);
            case 2 -> middleClick();
            default -> false;
        };
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return switch (button) {
            case 0 -> leftClickRelease();
            case 1 -> rightClickRelease();
            default -> false;
        };
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        if (isLeftClickLocked()) {
            return rightClickDragged(screenX, screenY);
        } else if (isRightClickLocked()) {
            return leftClickDragged(screenX, screenY);
        }
        return false;
    }

    @Override
    public boolean keyDown(int keycode) {
        return handleKeyDown(keycode);
    }

    @Override
    public boolean keyUp(int keycode) {
        return handleKeyUp(keycode);
    }

    /**
     * Used to pass delta-time (time elapsed since the previous frame) from the view to the model.
     *
     * @param delta Time elapsed since previous rendered frame in seconds.
     */
    public abstract void update(float delta);

    /**
     * Template for handling key press event. Default returns false to indicate that input wasn't
     * handled. Child classes can override this to implement keyboard controls.
     *
     * @param keycode The LibGDX keycode of the pressed key.
     * @return true if input was handled, false otherwise.
     */
    protected boolean handleKeyDown(int keycode) {
        return false;
    }

    /**
     * Template for handling key release event. Default returns false to indicate that input wasn't
     * handled. Child classes can override this to implement keyboard controls.
     *
     * @param keycode The LibGDX keycode of the pressed key.
     * @return true if input was handled, false otherwise.
     */
    protected boolean handleKeyUp(int keycode) {
        return false;
    }

    /**
     * Checks if left click actions are currently locked. Left click is locked when right click is
     * being handled to prevent simultaneous actions.
     *
     * @return true if left click is locked, false otherwise.
     */
    protected boolean isLeftClickLocked() {
        return leftClickLocked;
    }

    /**
     * Checks if right click actions are currently locked. Left click is locked when right click is
     * being handled to prevent simultaneous actions.
     *
     * @return true if right click is locked, false otherwise.
     */
    protected boolean isRightClickLocked() {
        return rightClickLocked;
    }

    /**
     * Locks left click actions. Used to prevent left click handling when right click is active.
     *
     * @param locked true to lock left click, false to unlock.
     */
    protected void setLeftClickLocked(boolean locked) {
        this.leftClickLocked = locked;
    }

    /**
     * Locks right click actions. Used to prevent left click handling when right click is active.
     *
     * @param locked true to lock right click, false to unlock.
     */
    protected void setRightClickLocked(boolean locked) {
        this.rightClickLocked = locked;
    }

    /**
     * Template for handling left click events. Implemented by child classes to define specific left
     * click behaviour.
     *
     * @param screenX The x-coordinate of the click in screen coordinates.
     * @param screenY The y-coordinate of the click in screen coordinates.
     * @return true if the input was handled, false otherwise.
     */
    protected abstract boolean leftClick(int screenX, int screenY);

    /**
     * Template for handling right click events. Implemented by child classes to define specific
     * right click behaviour.
     *
     * @param screenX The x-coordinate of the click in screen coordinates.
     * @param screenY The y-coordinate of the click in screen coordinates.
     * @return true if the input was handled, false otherwise.
     */
    protected abstract boolean rightClick(int screenX, int screenY);

    /**
     * Template for handling middle click events. Implemented by child classes to define specific
     * middle click behaviour.
     *
     * @return true if the input was handled, false otherwise.
     */
    protected abstract boolean middleClick();

    /**
     * Template for handling left click drag events. Called when mouse is dragged while left button
     * is held.
     *
     * @param screenX The current x-coordinate in screen coordinates.
     * @param screenY The current y-coordinate in screen coordinates.
     * @return true if the input ws handled, false otherwise.
     */
    protected abstract boolean leftClickDragged(int screenX, int screenY);

    /**
     * Template for handling right click drag events. Called when mouse is dragged while right
     * button is held.
     *
     * @param screenX The current x-coordinate in screen coordinates.
     * @param screenY The current y-coordinate in screen coordinates.
     * @return true if the input ws handled, false otherwise.
     */
    protected abstract boolean rightClickDragged(int screenX, int screenY);

    /**
     * Template for handling left click release events. Called when left mouse button is released.
     *
     * @return true if the input was handled, false otherwise.
     */
    protected abstract boolean leftClickRelease();

    /**
     * Template for handling right click release events. Called when right mouse button is
     * released.
     *
     * @return true if the input was handled, false otherwise.
     */
    protected abstract boolean rightClickRelease();

    /**
     * Template for handling vertical scroll input. Typically used for zoom or scroll operations.
     *
     * @param amountY The amount of vertical scroll (negative = up, positive = down).
     */
    protected abstract void handleScroll(float amountY);

}
