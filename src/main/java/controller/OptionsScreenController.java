package controller;

import model.GameStateModel;
import view.SpaceGame;
import view.screens.OptionsScreen;

public class OptionsScreenController extends GenericController {


    public OptionsScreenController(OptionsScreen view, GameStateModel model, SpaceGame game) {
        super(view, model, game);

    }

    @Override
    public void update(float delta) {

    }

    @Override
    protected boolean leftClick(int screenX, int screenY) {
        return false;
    }

    @Override
    protected boolean rightClick(int screenX, int screenY) {
        return false;
    }

    @Override
    protected boolean middleClick() {
        return false;
    }

    @Override
    protected boolean leftClickDragged(int screenX, int screenY) {
        return false;
    }

    @Override
    protected boolean rightClickDragged(int screenX, int screenY) {
        return false;
    }

    @Override
    protected boolean leftClickRelease() {
        return false;
    }

    @Override
    protected boolean rightClickRelease() {
        return false;
    }

    @Override
    protected void handleScroll(float amountY) {

    }
}
