package inf112.skeleton.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.Viewport;

public class UpgradeScreen implements Screen {

    final SpaceGame game;
    SpriteBatch batch;
    Viewport viewport;
    BitmapFont font;
    AssetManager manager; //An assetmanager helps with loading assets and disposing them once they are no longer needed 
    Sprite squareRed;
    Sprite squareGreen;
    Vector2 touchPos;   // Simplifies converting touch / mouse position in window-coordinates (pixels) to game-coordinates (meters x meters set in viewport)

    float gridOffsetWest = 1;   
    float gridOffsetSouth = 1;

    public UpgradeScreen(final SpaceGame game) {
        this.game = game;
        this.batch = this.game.getSpriteBatch();
        this.manager = this.game.getAssetManager();

        squareRed = new Sprite(manager.get("src/main/resources/images/upgrade_grid_tile_red.png", Texture.class));
        squareRed.setSize(1, 1);

        squareGreen = new Sprite(manager.get("src/main/resources/images/upgrade_grid_tile_green.png", Texture.class));
        squareGreen.setSize(1, 1);

        touchPos = new Vector2();
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(Color.BLACK); // Always wipe screen before drawing

        viewport = game.getViewport();
        viewport.apply();
        batch.setProjectionMatrix(viewport.getCamera().combined);

        batch.begin(); 
        for(int x = 0; x < 5; x++){
            for(int y = 0; y < 5; y++){
                if ((x == 1 || x==3) || (y < 2 || y==4)) { //just testing some patterns
                    squareGreen.setX(gridOffsetWest + x);
                    squareGreen.setY(gridOffsetSouth + y);
                    squareGreen.draw(batch);
                } else {
                    squareRed.setX(gridOffsetWest + x);
                    squareRed.setY(gridOffsetSouth + y);
                    squareRed.draw(batch);
                }
                
            }
        }
        batch.end();

        // Mouse and touch:
        if (Gdx.input.justTouched()) { // If the user has clicked or tapped the screen
            //Notes: We get [X/Y] of click/touch with Gdx.input.get[X/Y](). But this is in window coordinates, not our selected pixels per GU.
            //Window coordinates depend on screen size, and also has origin in the top left, not bottom left as libGDX uses.
            touchPos.set(Gdx.input.getX(), Gdx.input.getY());   // Get the touch position in window coordinates.
            viewport.unproject(touchPos);                       // Convert the units to the world units of the viewport.
            printClickedGrid(touchPos.x, touchPos.y);
        }
    }

    private void printClickedGrid(float x, float y) {
        int gridX = (int) Math.floor(x - gridOffsetWest);
        int gridY = (int) Math.floor(y - gridOffsetSouth);
        if(gridX < 0 || gridX > 4 || gridY < 0 || gridY > 4) {
            System.out.println("Not on grid");
        } else {
            System.out.println("x = " + gridX + ", y = " + gridY);
        }
    }

    @Override
    public void dispose() {
        this.dispose();
    }

    @Override
    public void hide() {
        // TODO: If using per-Screen InputProcessors (recommended), need to *unregister* it here
    }

    @Override
    public void pause() {
        // TODO Auto-generated method stub
    }

    @Override
    public void resize(int width, int height) {
        game.getViewport().update(width, height, true);
    }

    @Override
    public void resume() {
        // TODO Auto-generated method stub
    }

    @Override
    public void show() {
        // TODO: If using per-Screen InputProcessors (recommended), need to *register* it here
    }
    
}
