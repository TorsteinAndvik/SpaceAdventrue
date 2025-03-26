package view.Animation;

public class AnimationStateImpl implements AnimationState {
    private final float x;
    private final float y;
    private final AnimationType type;
    private float stateTime = 0f;

    public AnimationStateImpl(float x, float y, AnimationType type) {
        this.x = x;
        this.y = y;
        this.type = type;
    }

    @Override
    public void update(float delta) {
        this.stateTime += delta;
    }

    @Override
    public float getX() {
        return this.x;
    }

    @Override
    public float getY() {
        return this.y;
    }

    @Override
    public AnimationType getAnimationType() {
        return this.type;
    }

    @Override
    public float getStateTime() {
        return this.stateTime;
    }
}
