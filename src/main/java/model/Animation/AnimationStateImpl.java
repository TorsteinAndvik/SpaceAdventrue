package model.Animation;

import model.Globals.Collideable;

public class AnimationStateImpl implements AnimationState {
    private final float x;
    private final float y;
    private final float width;
    private final float height;
    private final AnimationType type;
    private float stateTime = 0f;

    public AnimationStateImpl(float x, float y, float width, float height, AnimationType type) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.type = type;
    }

    public AnimationStateImpl(Collideable collideable, AnimationType type) {
        this.x = collideable.getX();
        this.y = collideable.getY();
        this.width = 2f * collideable.getRadius();
        this.height = 2f * collideable.getRadius();
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
    public float getWidth() {
        return this.width;
    }

    @Override
    public float getHeight() {
        return this.height;
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
