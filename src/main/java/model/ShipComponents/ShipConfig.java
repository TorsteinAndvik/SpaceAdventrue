package model.ShipComponents;

import grid.CellPosition;
import java.util.List;
import java.util.StringJoiner;

public class ShipConfig {

    public int width;
    public int height;
    public List<ShipComponent> components;

    @Override
    public String toString() {
        StringJoiner sj = new StringJoiner(", ", "[", "]");
        for (ShipComponent component : components) {
            sj.add(component.toString());
        }
        return "ShipConfig{"
                + "width="
                + width
                + ", height="
                + height
                + ", components="
                + sj
                + '}';
    }

    public static class ShipComponent {

        public int x;
        public int y;
        public Upgrade upgrade;

        /**
         * Get the cell position of the ship component.
         *
         * @return the CellPosition.
         */
        public CellPosition getPosition() {
            return new CellPosition(y, x);
        }

        @Override
        public String toString() {
            return String.format("(%d,%d) = %s", x, y, upgrade);
        }
    }

    public static class Upgrade {

        public UpgradeType type;
        public int level;

        @Override
        public String toString() {
            return String.format("%s, lvl= %d", type, level);
        }
    }
}
