package view.Animation;

public interface AnimationState {
    public void update(float delta);

    public float getX();

    public float getY();

    public AnimationType getAnimationType();

    public float getStateTime();
}
