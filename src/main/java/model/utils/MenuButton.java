package model.utils;

import com.badlogic.gdx.math.Rectangle;

public class MenuButton {

    private final String text;
    private float x;
    private float y;
    Rectangle bounds = new Rectangle();

    public String getText() {
        return text;
    }

    public void setX(float x) {
        this.x = x;
    }

    public void setY(float y) {
        this.y = y;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public Rectangle getBounds() {
        return bounds;
    }

    public void setBounds(Rectangle bounds) {
        this.bounds = bounds;
    }

    public MenuButton(String text, float x, float y) {
        this.text = text;
        this.x = x;
        this.y = y;
    }

}
