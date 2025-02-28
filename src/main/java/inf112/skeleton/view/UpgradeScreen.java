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
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import inf112.skeleton.grid.CellPosition;

public class UpgradeScreen extends InputAdapter implements Screen {

    final SpaceGame game;
    SpriteBatch batch;
    ScreenViewport viewportGame;
    ScreenViewport viewportUI;
    BitmapFont fontBold;    // Agency FB Bold
    BitmapFont fontRegular; // Agency FB Regular
    AssetManager manager;   // An assetmanager helps with loading assets and disposing them once they are no longer needed 
    Vector2 touchPos;       // Simplifies converting touch / mouse position in window-coordinates (pixels) to game-coordinates (meters x meters set in viewport)

    Sprite[] upgradeIcons;
    Sprite fuselage;
    Sprite turret;
    Sprite rocket;
    Sprite shield;
    Sprite squareRed;
    Sprite squareGreen;
    Sprite squareGray;
    //ui sprites:
    Sprite msLeft;
    Sprite msMiddle;
    Sprite msRight;
    Sprite kbT;

    String[] upgradeStrings;

    int gridWidth;
    int gridHeight;
    int numUpgradeOptions;
 
    float gridOffsetX;
    float gridOffsetY;
    
    float upgradeOffsetX;
    float upgradeOffsetY;

    float upgradeIconZoom;
    float uiIconZoom;

    float[] cameraZoomLevels;
    int cameraCurrentZoomLevel;
    float cameraZoomDeltaTime;
    float cameraZoomTextFadeCutoffTime;
    boolean cameraZoomRecently;

    int leftClickDragX;
    int leftClickDragY;
    int rightClickDragX;
    int rightClickDragY;

    boolean upgradeGrabbed;
    boolean releaseGrabbedUpgrade;
    int grabbedUpgradeIndex;

    boolean leftClickLocked;    //touchDragged() doesn't discern between left and right clicks, but we want to disable left-clicking while right clicking and vice versa
    boolean rightClickLocked;

    public UpgradeScreen(final SpaceGame spaceGame) {
        game = spaceGame;
        batch = game.getSpriteBatch();
        manager = game.getAssetManager();
        viewportGame = game.getScreenViewport();
        viewportUI = new ScreenViewport();
        viewportUI.setUnitsPerPixel(viewportGame.getUnitsPerPixel());

        setupFonts();
        loadSprites();
        
        cameraZoomLevels = new float[] {0.5f, 0.6f, 0.7f, 0.8f, 0.9f, 1f, 1.1f, 1.2f, 1.3f, 1.4f, 1.5f};
        cameraCurrentZoomLevel = cameraZoomLevels.length / 2;
        
        touchPos = new Vector2();

        setupFields();
        setupUpgradeStrings();
    }

    private void loadSprites() {
        squareRed = new Sprite(manager.get("images/upgrade_grid_tile_red.png", Texture.class));
        squareRed.setSize(1, 1);

        squareGreen = new Sprite(manager.get("images/upgrade_grid_tile_green.png", Texture.class));
        squareGreen.setSize(1, 1);

        squareGray = new Sprite(manager.get("images/upgrade_grid_tile_gray.png", Texture.class));
        squareGray.setSize(1, 1);

        upgradeIconZoom = 0.8f;

        fuselage = new Sprite(manager.get("images/upgrades/fuselage_alt_stage_0.png", Texture.class));
        fuselage.setSize(upgradeIconZoom, upgradeIconZoom);

        turret = new Sprite(manager.get("images/upgrades/turret_laser_stage_0.png", Texture.class));
        turret.setSize(upgradeIconZoom, upgradeIconZoom);

        rocket = new Sprite(manager.get("images/upgrades/rocket_stage_0.png", Texture.class));
        rocket.setSize(upgradeIconZoom, upgradeIconZoom);

        shield = new Sprite(manager.get("images/upgrades/shield_stage_0.png", Texture.class));
        shield.setSize(upgradeIconZoom, upgradeIconZoom);

        upgradeIcons = new Sprite[] {fuselage, turret, rocket, shield};

        //ui sprites:
        uiIconZoom = fontRegular.getData().lineHeight;

        msLeft = new Sprite(manager.get("images/ui/Mouse_Left_Key_Light.png", Texture.class));
        msLeft.setSize(uiIconZoom, uiIconZoom);

        msMiddle = new Sprite(manager.get("images/ui/Mouse_Middle_Key_Light.png", Texture.class));
        msMiddle.setSize(uiIconZoom, uiIconZoom);

        msRight = new Sprite(manager.get("images/ui/Mouse_Right_Key_Light.png", Texture.class));
        msRight.setSize(uiIconZoom, uiIconZoom);

        kbT = new Sprite(manager.get("images/ui/T_Key_Light.png", Texture.class));
        kbT.setSize(uiIconZoom, uiIconZoom);
    }

    private void setupFields() {
        numUpgradeOptions = 4;
        gridWidth = 5;
        gridHeight = 5;

        cameraZoomTextFadeCutoffTime = 0.5f;
    }

    private void setupUpgradeStrings() {
        upgradeStrings = new String[] {
            "Fuselage:\nUsed to expand the ship. Any new\npart must be attached to a piece\nof Fuselage.",
            "Turret:\nFires lasers at enemies and\nasteroids.",
            "Obligator 1:\nPlaceholder for other upgrades.",
            "Obligator 2:\nYet another placeholder for\nother upgrades."
        };
    }

    private void setupFonts() {
        fontBold = manager.get("fonts/AGENCYB.ttf", BitmapFont.class);
        fontRegular = manager.get("fonts/AGENCYR.ttf", BitmapFont.class);

        // font are set as [integer]pt, need to scale them to our viewport by ratio of viewport height to screen height in order to use world-unit sized font
        fontBold.setUseIntegerPositions(false);
        fontBold.getData().setScale(viewportGame.getUnitsPerPixel());

        fontRegular.setUseIntegerPositions(false);
        fontRegular.getData().setScale(viewportGame.getUnitsPerPixel());
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(new Color(0f, 64f/255f, 64f/255f, 1f)); 
        
        viewportGame.apply(false);
        batch.setProjectionMatrix(viewportGame.getCamera().combined);
        batch.begin(); 

        // Draw upgrade options at top of screen (canon, shield, thruster, etc.)
        for(int x = 0; x < numUpgradeOptions; x++) {
            drawUpgradeSquare(x);
        }

        // Draw ship grid
        for(int x = 0; x < gridWidth; x++) {
            for(int y = 0; y < gridHeight; y++) {
                if ((x == 1 || x==3) || (y < 2 || y==4)) {drawGridSquare(squareGreen, x, y);} 
                else {drawGridSquare(squareRed, x, y);}
            }
        }

        if (upgradeGrabbed) {
            // draw a ghost copy of upgrade if hovering a grid cell
            CellPosition cpGrid = convertMouseToGrid(touchPos.x, touchPos.y);
            if (cellPositionOnGrid(cpGrid)) {
                upgradeIcons[grabbedUpgradeIndex].setX(gridOffsetX + cpGrid.col() + 0.5f * (1f - upgradeIconZoom));
                upgradeIcons[grabbedUpgradeIndex].setY(gridOffsetY + cpGrid.row() + 0.5f * (1f - upgradeIconZoom));
                upgradeIcons[grabbedUpgradeIndex].draw(batch, 0.5f);
            }

            // draw held upgrade
            ViewportPosition vpPos = worldToGameCoordinates(leftClickDragX, leftClickDragY);
            upgradeIcons[grabbedUpgradeIndex].setX(vpPos.x() - 0.5f * upgradeIconZoom);
            upgradeIcons[grabbedUpgradeIndex].setY(vpPos.y() - 0.5f * upgradeIconZoom);
            upgradeIcons[grabbedUpgradeIndex].draw(batch);
        }

        if(releaseGrabbedUpgrade && upgradeGrabbed) {
            releaseGrabbedUpgrade = false;
            upgradeGrabbed = false;
        }

        batch.end();

        // Draw UI: Need to swap viewport (because of camera position being set in rightClickDragged we need a separate viewport for centered camera) 
            // TODO: Refactor if possible such that setCamera is only ever called inside render() (does not seem like a simple fix). 
        
            viewportUI.apply(true);
        batch.setProjectionMatrix(viewportUI.getCamera().combined);

        batch.begin();

        // Draw ui elements
        fontRegular.setColor(Color.WHITE);

        // zoom (mouse wheel)
        msMiddle.setX(0f);
        msMiddle.setY(0.15f * fontRegular.getData().lineHeight);
        msMiddle.draw(batch);
        fontRegular.draw(batch, "Adjust zoom", fontRegular.getData().lineHeight, fontRegular.getData().lineHeight);
        
        // move camera (right click)
        msRight.setX(0f);
        msRight.setY(1.15f * fontRegular.getData().lineHeight);
        msRight.draw(batch);
        fontRegular.draw(batch, "Move camera", fontRegular.getData().lineHeight, 2*fontRegular.getData().lineHeight);
        
        // grab upgrade (left click)
        msLeft.setX(0f);
        msLeft.setY(2.15f * fontRegular.getData().lineHeight);
        msLeft.draw(batch);
        fontRegular.draw(batch, "Grab upgrade", fontRegular.getData().lineHeight, 3*fontRegular.getData().lineHeight);
        
        // inspect upgrade (T key)
        kbT.setX(0f);
        kbT.setY(3.15f * fontRegular.getData().lineHeight);
        kbT.draw(batch);
        fontRegular.draw(batch, "Inspect upgrade", fontRegular.getData().lineHeight, 4*fontRegular.getData().lineHeight);

        // display zoom level if recently scrolled / middle mouse clicked
        if (cameraZoomRecently) {
            float deltaTime = Gdx.graphics.getDeltaTime();
            cameraZoomDeltaTime += deltaTime;
            //float a = cameraZoomDeltaTime < cameraZoomTextFadeCutoffTime ? 1f : 1f + cameraZoomTextFadeCutoffTime - cameraZoomDeltaTime/cameraZoomTextFadeCutoffTime;
            float a = cameraZoomDeltaTime < cameraZoomTextFadeCutoffTime ? 1f : 1f - (float)Math.pow((cameraZoomDeltaTime - cameraZoomTextFadeCutoffTime), 2);
            if (a < 0) {
                cameraZoomRecently = false;
            } else {
                Color fontColor = new Color(1f, 0.47f, 0.55f, a);
                fontRegular.setColor(fontColor);
                fontRegular.draw(batch, "Zoom = x" + cameraZoomLevels[cameraCurrentZoomLevel], 0.1f, 5*fontRegular.getData().lineHeight);
            }
        }

        batch.end();
    }

    private void drawUpgradeSquare(int x) {
        squareGray.setX(upgradeOffsetX + x);
        squareGray.setY(upgradeOffsetY);
        squareGray.draw(batch);

        upgradeIcons[x].setX(upgradeOffsetX + x + (1f - upgradeIconZoom)/2f);
        upgradeIcons[x].setY(upgradeOffsetY + (1f - upgradeIconZoom)/2f);
        upgradeIcons[x].draw(batch);
    }

    private void drawGridSquare(Sprite squareSprite, int x, int y) {
        squareSprite.setX(gridOffsetX + x);
        squareSprite.setY(gridOffsetY + y);
        squareSprite.draw(batch);
    }

    private ViewportPosition worldToGameCoordinates(int worldX, int worldY) {
        touchPos.set(worldX, worldY);   // Set the position in window coordinates.
        viewportGame.unproject(touchPos);   // Convert the touch position to the game units of the viewport.
        return new ViewportPosition(touchPos.x, touchPos.y);
    }

    private CellPosition convertMouseToGrid(float x, float y) {
        return new CellPosition((int) Math.floor(y - gridOffsetY), (int) Math.floor(x - gridOffsetX));
    }

    private CellPosition convertMouseToUpgradeBar(float x, float y) {
        return new CellPosition((int) Math.floor(y - upgradeOffsetY), (int) Math.floor(x - upgradeOffsetX));
    }

    private boolean cellPositionOnGrid(CellPosition cp) {
        int gridX = cp.col();
        int gridY = cp.row();
        return !(gridX < 0 || gridX > gridWidth-1 || gridY < 0 || gridY > gridHeight-1);
    }

    private boolean cellPositionOnUpgradeOptions(CellPosition cp) {
        int upgradeX = cp.col();
        int upgradeY = cp.row();
        return !((upgradeY != 0) || (upgradeX < 0) || (upgradeX > numUpgradeOptions-1));
    }

    @Override 
    public boolean scrolled(float amountX, float amountY) {
        cameraCurrentZoomLevel += (int)amountY;
        if(cameraCurrentZoomLevel < 0) {cameraCurrentZoomLevel = 0;}
        else if(cameraCurrentZoomLevel >= cameraZoomLevels.length) {cameraCurrentZoomLevel = cameraZoomLevels.length - 1;}

        ((OrthographicCamera) viewportGame.getCamera()).zoom = cameraZoomLevels[cameraCurrentZoomLevel];
        cameraZoomRecently = true;
        cameraZoomDeltaTime = 0f;
        return true;
    }

    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        if(button == 0) {return leftClick(screenX, screenY);}
        else if(button == 1) {return rightClick(screenX, screenY);}
        else if(button == 2) {return middleClick();}
        else {return false;}
    }

    private boolean leftClick(int screenX, int screenY) {
        if (leftClickLocked) {return true;}
        rightClickLocked = true;
        releaseGrabbedUpgrade = false;
        leftClickDragX = screenX;
        leftClickDragY = screenY;

        //Notes: We get [X/Y] of click/touch with Gdx.input.get[X/Y](). But this is in window coordinates, not game coordinates.
        //Window coordinates depend on screen size, and also has origin in the top left, not bottom left as libGDX uses. viewport.unproject fixes this for us.
        touchPos.set(leftClickDragX, leftClickDragY);   // Set the touch position in window coordinates.
        viewportGame.unproject(touchPos);   // Convert the touch position to the game units of the viewport.
            
        CellPosition cpGrid = convertMouseToGrid(touchPos.x, touchPos.y);
        CellPosition cpUpgrade = convertMouseToUpgradeBar(touchPos.x, touchPos.y);

        if (cellPositionOnGrid(cpGrid)) {System.out.println("x = " + cpGrid.col() + ", y = " + cpGrid.row());}
        if (cellPositionOnUpgradeOptions(cpUpgrade)) {
            //System.out.println("selected upgrade number " + cpUpgrade.col());
            grabbedUpgradeIndex = cpUpgrade.col();
            upgradeGrabbed = true;
        }

        return true;
    }

    private boolean leftClickRelease(int screenX, int screenY) {
        if (leftClickLocked) {return true;}
        rightClickLocked = false;
        releaseGrabbedUpgrade = true;
        return true;
    }

    private boolean leftClickDragged(int screenX, int screenY) {
        leftClickDragX = screenX;
        leftClickDragY = screenY;
        return true;
    }

    private boolean rightClick(int screenX, int screenY) {
        if (rightClickLocked) {return true;}
        leftClickLocked = true;
        rightClickDragX = screenX;
        rightClickDragY = screenY;
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

    private boolean middleClick() {
        cameraZoomRecently = true;
        cameraZoomDeltaTime = 0f;
        return true;
    }

    /**
     * Set camera position in screen coordinates
     * 
     * @param screenX
     * @param screenY
     */
    private void setCameraPosition(int screenX, int screenY) {
        float cameraX = viewportGame.getScreenWidth()/2f + screenX;
        float cameraY = viewportGame.getScreenHeight()/2f + screenY;

        touchPos.set(cameraX, cameraY);
        viewportGame.unproject(touchPos);   // Convert the touch position to the game units of the viewport.
        clampVector(touchPos, 0f, viewportGame.getWorldWidth(), 0f, viewportGame.getWorldHeight());
        
        viewportGame.getCamera().position.set(touchPos, 0);
    }

    /**
     * Set camera position in world coordinates
     * 
     * @param worldX
     * @param worldY
     */
    private void setCameraPosition(float worldX, float worldY) {
        touchPos.set(viewportGame.getWorldWidth()/2f + worldX, viewportGame.getWorldHeight()/2f + worldY);
        clampVector(touchPos, 0f, viewportGame.getWorldWidth(), 0f, viewportGame.getWorldHeight());
        viewportGame.getCamera().position.set(touchPos, 0);
    }

    private void clampVector(Vector2 v, float minX, float maxX, float minY, float maxY) {
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
        int oldWidth = viewportGame.getScreenWidth();
        int oldHeight = viewportGame.getScreenHeight();
        viewportGame.update(width, height, false);
        if(viewportGame.getScreenWidth() == 0 || viewportGame.getScreenHeight() == 0) {//camera only centered on show, this if-split is necessary to set camera position correctly
            setCameraPosition(0f, 0f); //adjust camera position when window is opened from minimized / first created
        } else {
            //"evenly distribute" window resizing so that stuff in camera appear to center
                //For some reason using touchPos and viewport.unproject causes *massive* issues, have to unproject manually using unitsPerPixel
            float screenDeltaX = (width - oldWidth)/2f;
            float screenDeltaY = (height - oldHeight)/2f;

            float deltaX = viewportGame.getUnitsPerPixel()*screenDeltaX;
            float deltaY = viewportGame.getUnitsPerPixel()*screenDeltaY;
            
            viewportGame.getCamera().translate(deltaX, deltaY, 0f);
            
        }
        updateOffsets();
        viewportUI.update(width, height, true);
    }

    private void updateOffsets() { 
        float upgradeToGridDelta = 2f;    // 2: 1 from upgrade bar itself, 1 for space between upgrade bar and ship grid
        gridOffsetX = (viewportGame.getWorldWidth() - gridWidth)/2f;   
        gridOffsetY = (viewportGame.getWorldHeight() - gridHeight - upgradeToGridDelta)/2f; 
        upgradeOffsetX = (viewportGame.getWorldWidth() - numUpgradeOptions)/2f;
        upgradeOffsetY = gridOffsetY + gridHeight + upgradeToGridDelta/2f;
    }

    @Override
    public void hide() {
        Gdx.input.setInputProcessor(null); //Unregister this inputprocessor
    }

    @Override
    public void dispose() {}

    @Override
    public void pause() {}

    @Override
    public void resume() {}

    @Override
    public void show() {
        Gdx.input.setInputProcessor(this);  //Register new inputProcessor 
        viewportGame.apply(true);           //Camera is centered on show, but never on resize
        viewportUI.apply(true);
    }
}
