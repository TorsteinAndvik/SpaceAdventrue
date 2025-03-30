package model.Animation;

public interface AnimationState {
    public void update(float delta);

    public float getX();

    public float getY();

    public float getRadius();

    public AnimationType getAnimationType();

    public float getStateTime();
}
