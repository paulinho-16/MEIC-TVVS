package nl.tudelft.jpacman.board;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import nl.tudelft.jpacman.sprite.PacManSprites;
import nl.tudelft.jpacman.sprite.Sprite;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

/**
 * Tests the linking of squares done by the board factory.
 *
 * @author Jeroen Roosen 
 */
class BoardFactoryTest {

    /**
     * The factory under test.
     */
    private BoardFactory factory;

    /**
     * Squares on the board to test.
     */
    private Square s1, s2, s3;

    /**
     * Resets the factory under test.
     */
    @BeforeEach
    void setUp() {
        PacManSprites sprites = mock(PacManSprites.class);
        doReturn(mock(Sprite.class)).when(sprites).getWallSprite();
        doReturn(mock(Sprite.class)).when(sprites).getGroundSprite();
        factory = new BoardFactory(sprites);

        s1 = new BasicSquare();
        s2 = new BasicSquare();
        s3 = new BasicSquare();
    }

    /**
     * Verifies that a single cell is connected to itself on all sides.
     */
    @Test
    void worldIsRound() {
        factory.createBoard(new Square[][]{{s1}});
        assertThat(Arrays.stream(Direction.values()).map(s1::getSquareAt)).containsOnly(s1);
    }

    /**
     * Verifies a chain of cells is connected to the east.
     */
    @Test
    void connectedEast() {
        factory.createBoard(new Square[][]{{s1}, {s2}});
        assertThat(s1.getSquareAt(Direction.EAST)).isEqualTo(s2);
        assertThat(s2.getSquareAt(Direction.EAST)).isEqualTo(s1);
    }

    /**
     * Verifies a chain of cells is connected to the west.
     */
    @Test
    void connectedWest() {
        factory.createBoard(new Square[][]{{s1}, {s2}});
        assertThat(s1.getSquareAt(Direction.WEST)).isEqualTo(s2);
        assertThat(s2.getSquareAt(Direction.WEST)).isEqualTo(s1);
    }

    /**
     * Verifies a chain of cells is connected to the north.
     */
    @Test
    void connectedNorth() {
        factory.createBoard(new Square[][]{{s1, s2}});
        assertThat(s1.getSquareAt(Direction.NORTH)).isEqualTo(s2);
        assertThat(s2.getSquareAt(Direction.NORTH)).isEqualTo(s1);
    }

    /**
     * Verifies a chain of cells is connected to the south.
     */
    @Test
    void connectedSouth() {
        factory.createBoard(new Square[][]{{s1, s2}});
        assertThat(s1.getSquareAt(Direction.SOUTH)).isEqualTo(s2);
        assertThat(s2.getSquareAt(Direction.SOUTH)).isEqualTo(s1);
    }

    @Test
    void largerHeight() {
        Board board = factory.createBoard(new Square[][]{{s1, s2, s3}, {s1, s2, s3}});
        assertThat(s1.getSquareAt(Direction.SOUTH)).isEqualTo(s2);
        assertThat(s2.getSquareAt(Direction.SOUTH)).isEqualTo(s3);

        assertNotNull(board);
    }

    @Test
    void largerWidth() {
        Board board = factory.createBoard(new Square[][]{{s1}, {s2}, {s3}});
        assertThat(s1.getSquareAt(Direction.WEST)).isEqualTo(s3);
        assertThat(s3.getSquareAt(Direction.WEST)).isEqualTo(s2);

        assertNotNull(board);
    }

    @Test
    void createWall() {
        assertNotNull(factory.createWall());
        assertFalse(factory.createWall().isAccessibleTo(mock(Unit.class)));
        assertNotNull(factory.createWall().getSprite());
    }

    @Test
    void createGround() {
        assertNotNull(factory.createGround());
        assertTrue(factory.createGround().isAccessibleTo(mock(Unit.class)));
        assertNotNull(factory.createGround().getSprite());
    }
}
