package inf112.skeleton.view;

import com.badlogic.gdx.Gdx;
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

    int gridWidth = 5;
    int gridHeight = 5;
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
    int rightClickDragX;
    int rightClickDragY;
    float cameraX;
    float cameraY;
    boolean releaseGrabbedItem;
    boolean leftClickLocked;    //touchDragged() doesn't discern between left and right clicks, but we want to disable left-clicking while right clicking and vice versa
    boolean rightClickLocked;

    public UpgradeScreen(final SpaceGame spaceGame) {
        game = spaceGame;
        batch = game.getSpriteBatch();
        manager = game.getAssetManager();
        viewport = game.getScreenViewport();

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
        ScreenUtils.clear(new Color(0f, 64f/255f, 64f/255f, 1f)); 
        
        viewport.apply(false);
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
        for(int x = 0; x < gridWidth; x++){
            for(int y = 0; y < gridHeight; y++){
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
            ViewportPosition vpPos = worldToGameCoordinates(mouseX, mouseY);
            obligator.setX(vpPos.x() - 0.5f * obligatorZoom);
            obligator.setY(vpPos.y() - 0.5f * obligatorZoom);
            obligator.draw(batch);
        }

        if(releaseGrabbedItem && obligatorGrabbed) { //TODO: Add code for dropping obligator sprite (if it was grabbed) onto grid (if spot is available and obligator was grabbed)
            releaseGrabbedItem = false;
            obligatorGrabbed = false;
        }

        batch.end();
    }

    private ViewportPosition worldToGameCoordinates(int worldX, int worldY) {
        touchPos.set(worldX, worldY);   // Set the position in window coordinates.
        viewport.unproject(touchPos);   // Convert the touch position to the game units of the viewport.
        return new ViewportPosition(touchPos.x, touchPos.y);
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
        return !(worldX < 0 || worldX > gridWidth-1 || worldY < 0 || worldY > gridHeight-1);
    }

    private boolean clickedOnUpgradeOptions(CellPosition cp) {
        int worldX = cp.col();
        int worldY = cp.row();
        return !((worldY != viewport.getWorldHeight()-1) || (worldX < 0) || (worldX > numUpgradeOptions-1));
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
        if(button == 0) {return leftClick(screenX, screenY);}
        else if(button == 1) {return rightClick(screenX, screenY);}
        else {return false;}
    }

    private boolean leftClick(int screenX, int screenY) {
        if (leftClickLocked) {return true;}
        rightClickLocked = true;
        releaseGrabbedItem = false;
        mouseX = screenX;
        mouseY = screenY;

        //Notes: We get [X/Y] of click/touch with Gdx.input.get[X/Y](). But this is in window coordinates, not game coordinates.
        //Window coordinates depend on screen size, and also has origin in the top left, not bottom left as libGDX uses. viewport.unproject fixes this for us.
        touchPos.set(mouseX, mouseY);   // Set the touch position in window coordinates.
        viewport.unproject(touchPos);   // Convert the touch position to the game units of the viewport.
            
        CellPosition cpGrid = convertMouseToGrid(touchPos.x, touchPos.y);
        CellPosition cpUpgrade = convertMouseToUpgradeBar(touchPos.x, touchPos.y);

        if (clickedOnGrid(cpGrid)) {System.out.println("x = " + cpGrid.col() + ", y = " + cpGrid.row());   }
        if (clickedOnUpgradeOptions(cpUpgrade)) {
            System.out.println("selected upgrade number " + cpUpgrade.col());
            obligatorGrabbed = true;
        }

        return true;
    }

    private boolean leftClickRelease(int screenX, int screenY) {
        if (leftClickLocked) {return true;}
        rightClickLocked = false;
        releaseGrabbedItem = true;
        return true;
    }

    private boolean leftClickDragged(int screenX, int screenY) {
        mouseX = screenX;
        mouseY = screenY;
        return true;
    }

    private boolean rightClick(int screenX, int screenY) {
        if (rightClickLocked) {return true;}
        leftClickLocked = true;
        rightClickDragX = screenX;
        rightClickDragY = screenY;
        //System.out.println("x=" + screenX + ", y=" + screenY);
        return true;
    }

    private boolean rightClickRelease(int screenX, int screenY) {
        if (rightClickLocked) {return true;}
        leftClickLocked = false;
        return true;
    }

    private boolean rightClickDragged(int screenX, int screenY) {
        int offsetX = rightClickDragX - screenX;
        int offsetY = rightClickDragY - screenY;

        rightClickDragX = screenX;
        rightClickDragY = screenY;

        setCameraPosition(offsetX, offsetY);
        return true;
    }

    private void setCameraPosition(int offsetX, int offsetY) {
        cameraX = viewport.getScreenWidth()/2 + offsetX;
        cameraY = viewport.getScreenHeight()/2 + offsetY;

        touchPos.set(cameraX, cameraY);
        viewport.unproject(touchPos);   // Convert the touch position to the game units of the viewport.
        clampVector(touchPos, 0f, viewport.getWorldWidth(), 0f, viewport.getWorldHeight());
        
        viewport.getCamera().position.set(touchPos, 0);
    }

    private void setCameraPosition(float botleftX, float botleftY) {
        touchPos.set(viewport.getWorldWidth()/2 + botleftX, viewport.getWorldHeight()/2 + botleftY);
        clampVector(touchPos, 0f, viewport.getWorldWidth(), 0f, viewport.getWorldHeight());
        viewport.getCamera().position.set(touchPos, 0);
    }

    private void clampVector(Vector2 v, float minX, float maxX, float minY, float maxY) {
        //System.out.println("x = " + v.x + ", y = " + v.y + ", minX = " + minX + ", minY = " + minY + ", maxX = " + maxX + ", maxY = " + maxY);
        touchPos.x = Math.max(Math.min(touchPos.x, maxX), minX);
        touchPos.y = Math.max(Math.min(touchPos.y, maxY), minY);   
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        if (leftClickLocked) {return rightClickDragged(screenX, screenY);}
        else if (rightClickLocked) {return leftClickDragged(screenX, screenY);}
        return true;
    }

    @Override 
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        if(button == 0) {return leftClickRelease(screenX, screenY);}
        else if(button == 1) {return rightClickRelease(screenX, screenY);}
        else {return false;}
    }

    @Override
    public void resize(int width, int height) {
        if(viewport.getScreenWidth() == 0 || viewport.getScreenHeight() == 0) {
            viewport.update(width, height, false);
            setCameraPosition(0f, 0f);
        } else {viewport.update(width, height, false);}
    }

    @Override
    public void hide() {
        Gdx.input.setInputProcessor(null); //Unregister this inputprocessor
    }

    @Override
    public void dispose() {}

    @Override
    public void pause() {
        // TODO Auto-generated method stub
    }

    @Override
    public void resume() {
        // TODO Auto-generated method stub
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(this); //Register new inputProcessor 
        viewport.apply(true);
    }
}
