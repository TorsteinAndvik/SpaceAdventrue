package inf112.skeleton.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.Viewport;

public class UpgradeScreen extends InputAdapter implements Screen  {

    final SpaceGame game;
    SpriteBatch batch;
    Viewport viewport;
    BitmapFont font;
    AssetManager manager; //An assetmanager helps with loading assets and disposing them once they are no longer needed 
    Sprite squareRed;
    Sprite squareGreen;
    Sprite squareGray;
    Vector2 touchPos;   // Simplifies converting touch / mouse position in window-coordinates (pixels) to game-coordinates (meters x meters set in viewport)

    float gridOffsetWest;   
    float gridOffsetSouth;
    int numUpgradeOptions;
    float upgradeOffsetWest;

    public UpgradeScreen(final SpaceGame game) {
        this.game = game;
        this.batch = this.game.getSpriteBatch();
        this.manager = this.game.getAssetManager();
        this.viewport = game.getViewport();

        squareRed = new Sprite(manager.get("images/upgrade_grid_tile_red.png", Texture.class));
        squareRed.setSize(1, 1);

        squareGreen = new Sprite(manager.get("images/upgrade_grid_tile_green.png", Texture.class));
        squareGreen.setSize(1, 1);

        squareGray = new Sprite(manager.get("images/upgrade_grid_tile_gray.png", Texture.class));
        squareGray.setSize(1, 1);

        touchPos = new Vector2();

        registerOffsets();
    }

    private void registerOffsets() {
        gridOffsetWest = 1;   
        gridOffsetSouth = 0;
        numUpgradeOptions = 4;
        upgradeOffsetWest = (viewport.getWorldWidth() - numUpgradeOptions)/2;
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(Color.BLACK); // Always wipe screen before drawing

        viewport.apply();
        batch.setProjectionMatrix(viewport.getCamera().combined);

        batch.begin(); 

        // Draw part-options at top of screen (canon, shield, thruster)
        for(int x = 0; x < numUpgradeOptions; x++) {
            squareGray.setX((viewport.getWorldWidth() - numUpgradeOptions)/2 + x);
            squareGray.setY(viewport.getWorldHeight()-1);
            squareGray.draw(batch);
        }

        // Draw upgrade grid
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
            //Notes: We get [X/Y] of click/touch with Gdx.input.get[X/Y](). But this is in window coordinates, not game coordinates.
            //Window coordinates depend on screen size, and also has origin in the top left, not bottom left as libGDX uses. viewport.unproject fixes this for us.
            touchPos.set(Gdx.input.getX(), Gdx.input.getY());   // Get the touch position in window coordinates.
            viewport.unproject(touchPos);                       // Convert the units to the world units of the viewport.
            clickedOnGrid(touchPos.x, touchPos.y);
            clickedOnUpgradeOptions(touchPos.x, touchPos.y);
        }
    }

    private boolean clickedOnGrid(float x, float y) {
        int worldX = (int) Math.floor(x - gridOffsetWest);
        int worldY = (int) Math.floor(y - gridOffsetSouth);
        if(worldX < 0 || worldX > 4 || worldY < 0 || worldY > 4) {
            return false;
        } else {
            System.out.println("x = " + worldX + " (" + x +" ), y = " + worldY + " (" + y +" )");
            return true;
        }
    }

    private boolean clickedOnUpgradeOptions(float x, float y) {
        int worldY = (int) Math.floor(y);
        float rightBound = upgradeOffsetWest + numUpgradeOptions;
        
        if((worldY != viewport.getWorldHeight()-1) || (x < upgradeOffsetWest) || (x > rightBound)) {
            return false;
        } else {
            int upgradeX = (int)Math.floor(x - upgradeOffsetWest);
            System.out.println("selected upgrade number " + upgradeX);
            //((OrthographicCamera) viewport.getCamera()).zoom = 2f;
            return true;
        }
    }

    @Override 
    public boolean scrolled(float amountX, float amountY) {
        System.out.println("amountX = " + amountX + ", amountY = " + amountY);
        
        return true;
    }

    @Override
    public void dispose() {
        this.dispose();
    }

    @Override
    public void hide() {
        Gdx.input.setInputProcessor(null); //Unregister inputprocessor
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
        Gdx.input.setInputProcessor(this); //Register new inputProcessor 
    }
    
}
