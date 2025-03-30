package model.Animation;

public interface AnimationState {

    void update(float delta);

    float getX();

    float getY();

    float getRadius();

    AnimationType getAnimationType();

    float getStateTime();
}
