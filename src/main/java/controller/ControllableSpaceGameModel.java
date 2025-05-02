package controller;

import controller.audio.AudioCallback;
import model.Animation.AnimationCallback;
import model.ScreenBoundsProvider;

public interface ControllableSpaceGameModel {

    /**
     * Updates the game model by the given delta time in seconds.
     *
     * @param delta delta time in seconds
     */
    void update(float delta);

    /**
     * Set ship to accelerate forwards.
     */
    void setAccelerateForward(boolean accelerate);

    /**
     * Set ship to accelerate backwards.
     */
    void setAccelerateBackward(boolean accelerate);

    /**
     * Set ship rotational acceleration to counter clockwise.
     */
    void setAccelerateCounterClockwise(boolean accelerate);

    /**
     * Set ship rotational acceleration to clockwise.
     */
    void setAccelerateClockwise(boolean accelerate);

    /**
     * Sets the audio callback object.
     *
     * @param audioCallback the <code>AudioCallback</code> object.
     */
    void setAudioCallback(AudioCallback audioCallback);

    /**
     * Sets the <code>AnimationCallback</code> for the model.
     *
     * @param animationCallback an AnimationCallback object.
     */
    void setAnimationCallback(AnimationCallback animationCallback);

    /**
     * Sets the <code>ScreenBoundsProvider</code> for the model.
     *
     * @param screenBoundsProvider a ScreenBoundsProvider object.
     */
    void setScreenBoundsProvider(ScreenBoundsProvider screenBoundsProvider);
}
