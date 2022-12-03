package nl.tudelft.jpacman.board;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;

/**
 * Test suite to confirm the public API of {@link Square} works as desired.
 *
 * @author Jeroen Roosen 
 */
class SquareTest {

    /**
     * The square under test.
     */
    private Square square;
    private Unit o1, o2;

    /**
     * Resets the square under test.
     */
    @BeforeEach
    void setUp() {
        square = new BasicSquare();

        o1 = mock(Unit.class);
        o2 = mock(Unit.class);
    }

    /**
     * Assert that the square holds the occupant once it has occupied it.
     */
    @Test
    void testOccupy() {
        Unit occupant = mock(Unit.class);
        square.put(occupant);

        assertThat(square.getOccupants()).contains(occupant);
    }

    /**
     * Assert that the square no longer holds the occupant after it has left the
     * square.
     */
    @Test
    void testLeave() {
        Unit occupant = mock(Unit.class);
        square.put(occupant);
        square.remove(occupant);

        assertThat(square.getOccupants()).doesNotContain(occupant);
    }

    /**
     * Assert that the order in which units entered the square is preserved.
     */
    @Test
    void testOrder() {
        square.put(o1);
        square.put(o2);

        assertThat(square.getOccupants()).containsSequence(o1, o2);
        assertTrue(square.invariant());
    }

    @Test
    void testInvariant() {
        assertTrue(square.invariant());

        doReturn(true).when(o1).hasSquare();
        doReturn(true).when(o2).hasSquare();
        doReturn(mock(Square.class)).when(o2).getSquare();

        square.put(o1);
        square.put(o2);

        assertFalse(square.invariant());
    }

    @Test
    void testInvariantCondition() {
        doReturn(true).when(o1).hasSquare();
        doReturn(false).when(o2).hasSquare();
        doReturn(square).when(o1).getSquare();
        doReturn(mock(Square.class)).when(o2).getSquare();

        square.put(o1);
        square.put(o2);

        assertTrue(square.invariant());
    }
}
