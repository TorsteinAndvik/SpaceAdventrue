package inf112.skeleton.controller;

import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import inf112.skeleton.model.SpaceGameModel;
import inf112.skeleton.model.UpgradeScreenModel;
import inf112.skeleton.view.SpaceScreen;

public class SpaceGameScreenController extends InputAdapter {
  private final SpaceGameModel model;
  private final SpaceScreen view;
  private final Vector2 touchPos;

  public SpaceGameScreenController(SpaceScreen view, SpaceGameModel model) {
    this.model = model;
    this.view = view;
    this.touchPos = new Vector2();
  }

}
