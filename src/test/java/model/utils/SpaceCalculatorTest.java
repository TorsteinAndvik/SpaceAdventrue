package model.utils;

import static model.utils.SpaceCalculator.orthogonallyAdjacent;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import grid.CellPosition;
import org.junit.jupiter.api.Test;

public class SpaceCalculatorTest {

    @Test
    void CellPositionAdjacencyTest() {
        CellPosition a = new CellPosition(0, 0);
        CellPosition b = new CellPosition(1, 0);
        assertTrue(orthogonallyAdjacent(a, b));

        a = new CellPosition(0, 1);
        b = new CellPosition(0, 0);
        assertTrue(orthogonallyAdjacent(a, b));

        a = new CellPosition(0, 1);
        b = new CellPosition(1, 0);
        assertFalse(orthogonallyAdjacent(a, b));

        a = new CellPosition(1, 0);
        b = new CellPosition(0, 1);
        assertFalse(orthogonallyAdjacent(a, b));

    }
}
