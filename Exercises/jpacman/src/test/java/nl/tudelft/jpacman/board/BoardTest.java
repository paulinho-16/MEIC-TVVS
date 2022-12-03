package nl.tudelft.jpacman.board;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;

/**
 * Test various aspects of board.
 *
 * @author Jeroen Roosen 
 */
class BoardTest {

    private static final int MAX_WIDTH = 2;
    private static final int MAX_HEIGHT = 3;

    private final Square[][] grid = {
        { mock(Square.class), mock(Square.class), mock(Square.class) },
        { mock(Square.class), mock(Square.class), mock(Square.class) },
    };
    private final Board board = new Board(grid);

    /**
     * Verifies the board has the correct width.
     */
    @Test
    void verifyWidth() {
        assertThat(board.getWidth()).isEqualTo(MAX_WIDTH);
    }

    /**
     * Verifies the board has the correct height.
     */
    @Test
    void verifyHeight() {
        assertThat(board.getHeight()).isEqualTo(MAX_HEIGHT);
    }

    /**
     * Verify that squares at key positions are properly set.
     * @param x Horizontal coordinate of relevant cell.
     * @param y Vertical coordinate of relevant cell.
     */
    @ParameterizedTest
    @CsvSource({
            "0, 0",
            "1, 2",
        "0, 1"
    })
    void testSquareAt(int x, int y) {
        assertThat(board.squareAt(x, y)).isEqualTo(grid[x][y]);
    }

    @Test
    void testWithinBorders() {
        int x_within = MAX_WIDTH - 1;
        int y_within = MAX_HEIGHT - 1;
        int x_below = -1;
        int y_below = -1;
        assertTrue(board.withinBorders(x_within, y_within)); // t1
        assertFalse(board.withinBorders(x_within, MAX_HEIGHT)); // t2
        assertFalse(board.withinBorders(x_within, y_below)); // t3
        assertFalse(board.withinBorders(MAX_WIDTH, y_within)); // t5
        assertFalse(board.withinBorders(x_below, y_within)); // t9

        assertTrue(board.withinBorders(0, y_within));
        assertTrue(board.withinBorders(x_within, 0));
    }

    @Test
    void testInvariant() {
        assertTrue(board.invariant());

        Square[][] grid_null = {
            { mock(Square.class), null, mock(Square.class) },
            { mock(Square.class), mock(Square.class), mock(Square.class) },
        };
        Board board_null = new Board(grid_null);

        assertFalse(board_null.invariant());
    }
}
