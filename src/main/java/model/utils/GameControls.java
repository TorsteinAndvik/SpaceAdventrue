package model.utils;

import java.util.LinkedHashMap;
import java.util.Map;

public class GameControls {

    // Maps of control keys to descriptions
    // LinkedHashMap is a workaround to have list-like behaviour for key-value pairs
    private static final Map<String, String> SPACE_SCREEN_CONTROLS = new LinkedHashMap<>();
    private static final Map<String, String> MENU_CONTROLS = new LinkedHashMap<>();
    private static final Map<String, String> UPGRADE_SCREEN_CONTROLS = new LinkedHashMap<>();

    static {
        SPACE_SCREEN_CONTROLS.put("W", "Accelerate forward");
        SPACE_SCREEN_CONTROLS.put("S", "Accelerate backward");
        SPACE_SCREEN_CONTROLS.put("A", "Rotate counterclockwise");
        SPACE_SCREEN_CONTROLS.put("D", "Rotate clockwise");
        SPACE_SCREEN_CONTROLS.put("SPACE", "Shoot");
        SPACE_SCREEN_CONTROLS.put("P", "Pause");
        SPACE_SCREEN_CONTROLS.put("ESC", "Return to main menu");
        SPACE_SCREEN_CONTROLS.put("U", "Open upgrade screen");

        MENU_CONTROLS.put("UP/DOWN", "Navigate menu options");
        MENU_CONTROLS.put("ENTER/SPACE", "Select options");
        MENU_CONTROLS.put("ESC", "Return to previous screen");

        UPGRADE_SCREEN_CONTROLS.put("LEFT CLICK", "Select/place upgrade");
        UPGRADE_SCREEN_CONTROLS.put("RIGHT CLICK", "Pan view");
        UPGRADE_SCREEN_CONTROLS.put("SCROLL", "Zoom in/out");
        UPGRADE_SCREEN_CONTROLS.put("T", "Toggle upgrade description");
        UPGRADE_SCREEN_CONTROLS.put("ESC/U", "Return to game");

    }

    public static Map<String, String> getSpaceScreenControls() {
        return new LinkedHashMap<>(SPACE_SCREEN_CONTROLS);
    }

    public static Map<String, String> getMenuControls() {
        return new LinkedHashMap<>(MENU_CONTROLS);
    }

    public static Map<String, String> getUpgradeScreenControls() {
        return new LinkedHashMap<>(UPGRADE_SCREEN_CONTROLS);
    }
}
