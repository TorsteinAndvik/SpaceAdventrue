package model.Animation;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import model.Globals.Collidable;
import model.SpaceCharacters.Asteroid;

public class AnimationStateImplTest {

    private Collidable collideable;
    private AnimationStateImpl anim1;
    private AnimationStateImpl anim2;

    @BeforeEach
    private void setup() {
        collideable = new Asteroid();
        anim1 = new AnimationStateImpl(1f, 2f, 3f, AnimationType.EXPLOSION);
        anim2 = new AnimationStateImpl(collideable, AnimationType.EXPLOSION);
    }

    @Test
    public void getterTest() {
        assertEquals(1f, anim1.getX());
        assertEquals(2f, anim1.getY());
        assertEquals(3f, anim1.getRadius());
        assertEquals(AnimationType.EXPLOSION, anim1.getAnimationType());

        assertEquals(collideable.getX(), anim2.getX());
        assertEquals(collideable.getY(), anim2.getY());
        assertEquals(collideable.getRadius(), anim2.getRadius());
        assertEquals(AnimationType.EXPLOSION, anim2.getAnimationType());
    }

    @Test
    public void updateTest() {
        assertEquals(0f, anim1.getStateTime());
        anim1.update(2f);
        assertEquals(2f, anim1.getStateTime());
    }
}
