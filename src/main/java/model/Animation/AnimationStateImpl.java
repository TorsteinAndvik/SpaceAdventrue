package model.Animation;

import model.Globals.Collideable;

public class AnimationStateImpl implements AnimationState {
    private final float x;
    private final float y;
    private final float radius;
    private final AnimationType type;
    private float stateTime = 0f;

    public AnimationStateImpl(float x, float y, float radius, AnimationType type) {
        this.x = x;
        this.y = y;
        this.radius = radius;
        this.type = type;
    }

    public AnimationStateImpl(Collideable collideable, AnimationType type) {
        this.x = collideable.getX();
        this.y = collideable.getY();
        this.radius = collideable.getRadius();
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
    public float getRadius() {
        return this.radius;
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
