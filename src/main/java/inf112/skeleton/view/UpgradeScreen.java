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

import inf112.skeleton.grid.CellPosition;

public class UpgradeScreen extends InputAdapter implements Screen  {

    final SpaceGame game;
    SpriteBatch batch;
    Viewport viewport;
    BitmapFont font;
    AssetManager manager; //An assetmanager helps with loading assets and disposing them once they are no longer needed 
    Sprite obligator;
    Sprite squareRed;
    Sprite squareGreen;
    Sprite squareGray;
    Vector2 touchPos;   // Simplifies converting touch / mouse position in window-coordinates (pixels) to game-coordinates (meters x meters set in viewport)

    float gridOffsetWest;   
    float gridOffsetSouth;
    int numUpgradeOptions;
    float upgradeOffsetWest;
    float obligatorZoom = 0.8f;
    float[] cameraZoomLevels;
    int cameraCurrentZoomLevel;
    boolean obligatorGrabbed;
    int mouseX;
    int mouseY;
    boolean mouseReleased;

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

        obligator = new Sprite(manager.get("images/obligator.png", Texture.class));
        obligator.setSize(obligatorZoom, obligatorZoom);

        touchPos = new Vector2();

        cameraZoomLevels = new float[] {0.7f, 0.8f, 0.9f, 1f, 1.1f, 1.2f, 1.3f};
        cameraCurrentZoomLevel = cameraZoomLevels.length / 2;

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
        ScreenUtils.clear(new Color(0f, 64f/255f, 64f/255f, 1f)); // Always wipe screen before drawing

        viewport.apply();
        batch.setProjectionMatrix(viewport.getCamera().combined);

        batch.begin(); 

        // Draw part-options at top of screen (canon, shield, thruster)
        for(int x = 0; x < numUpgradeOptions; x++) {
            squareGray.setX((viewport.getWorldWidth() - numUpgradeOptions)/2 + x);
            squareGray.setY(viewport.getWorldHeight()-1);
            squareGray.draw(batch);

            obligator.setX((viewport.getWorldWidth() - numUpgradeOptions)/2 + x + (1f - obligatorZoom)/2);
            obligator.setY(viewport.getWorldHeight()-1 + (1f - obligatorZoom)/2);
            obligator.draw(batch);
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

        if (obligatorGrabbed) {
            //TODO: Draw obligator head at mouse coordinates.
        }

        if(mouseReleased) { //TODO: Add code for dropping obligator sprite (if it was grabbed) onto grid (if spot is available and obligator was grabbed)
            mouseReleased = false;
            obligatorGrabbed = false;
        }

        batch.end();

        // Mouse and touch:
        if (Gdx.input.justTouched()) { // If the user has clicked or tapped the screen
            //Notes: We get [X/Y] of click/touch with Gdx.input.get[X/Y](). But this is in window coordinates, not game coordinates.
            //Window coordinates depend on screen size, and also has origin in the top left, not bottom left as libGDX uses. viewport.unproject fixes this for us.
            touchPos.set(Gdx.input.getX(), Gdx.input.getY());   // Get the touch position in window coordinates.
            viewport.unproject(touchPos);                       // Convert the units to the world units of the viewport.
            
            CellPosition cpGrid = convertMouseToGrid(touchPos.x, touchPos.y);
            CellPosition cpUpgrade = convertMouseToUpgradeBar(touchPos.x, touchPos.y);
            clickedOnGrid(cpGrid);
            clickedOnUpgradeOptions(cpUpgrade);
        }
    }

    private CellPosition convertMouseToGrid(float x, float y) {
        return new CellPosition((int) Math.floor(y - gridOffsetSouth), (int) Math.floor(x - gridOffsetWest));
    }

    private CellPosition convertMouseToUpgradeBar(float x, float y) {
        return new CellPosition((int) Math.floor(y), (int)Math.floor(x - upgradeOffsetWest));
    }

    private boolean clickedOnGrid(CellPosition cp) {
        int worldX = cp.col();
        int worldY = cp.row();
        if(worldX < 0 || worldX > 4 || worldY < 0 || worldY > 4) {
            return false;
        } else {
            System.out.println("x = " + worldX + ", y = " + worldY);
            return true;
        }
    }

    private boolean clickedOnUpgradeOptions(CellPosition cp) {
        int worldX = cp.col();
        int worldY = cp.row();
        
        if((worldY != viewport.getWorldHeight()-1) || (worldX < 0) || (worldX > numUpgradeOptions-1)) {
            return false;
        } else {
            System.out.println("selected upgrade number " + worldX);
            return true;
        }
    }

    @Override 
    public boolean scrolled(float amountX, float amountY) {
        cameraCurrentZoomLevel += (int)amountY;
        if(cameraCurrentZoomLevel < 0) {cameraCurrentZoomLevel = 0;}
        else if(cameraCurrentZoomLevel >= cameraZoomLevels.length) {cameraCurrentZoomLevel = cameraZoomLevels.length - 1;}

        ((OrthographicCamera) viewport.getCamera()).zoom = cameraZoomLevels[cameraCurrentZoomLevel];
        return true;
    }

    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        if(button == 0) {
            mouseX = screenX;
            mouseY = screenY;
        }
        return true;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        mouseX = screenX;
        mouseY = screenY;
        return true;
    }

    @Override 
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        mouseX = screenX;
        mouseY = screenY;
        mouseReleased = true;
        return true;
    }

    @Override
    public void dispose() {}

    @Override
    public void hide() {
        Gdx.input.setInputProcessor(null); //Unregister this inputprocessor
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
