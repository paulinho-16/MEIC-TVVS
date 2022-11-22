package nl.tudelft.jpacman.npc.ghost;

import nl.tudelft.jpacman.board.BoardFactory;
import nl.tudelft.jpacman.board.Direction;
import nl.tudelft.jpacman.board.Square;
import nl.tudelft.jpacman.board.Unit;
import nl.tudelft.jpacman.npc.Ghost;
import nl.tudelft.jpacman.sprite.PacManSprites;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;

public class BlinkyTest {
    @Nested
    class nextAiMoveTest {
        private Ghost blinky;

        @BeforeEach
        void setUp() {
            PacManSprites sprites = mock(PacManSprites.class);
            BoardFactory factory = new BoardFactory(sprites);
            Square square = factory.createGround();
            GhostFactory ghostFact = new GhostFactory(sprites);
            blinky = ghostFact.createBlinky();
            blinky.occupy(square);
        }

        @Test
        public void nearestNull() {
            try (MockedStatic<Navigation> utilities = Mockito.mockStatic(Navigation.class)) {
                utilities.when(() -> Navigation.findNearest(Mockito.any(), Mockito.any())).thenReturn(null);

                assertEquals(Optional.empty(), blinky.nextAiMove());
            }
        }

        @Test
        public void nearestNotNull() {
            try (MockedStatic<Navigation> utilities = Mockito.mockStatic(Navigation.class)) {
                Unit unit = mock(Unit.class);
                doReturn(true).when(unit).hasSquare();
                utilities.when(() -> Navigation.findNearest(Mockito.any(), Mockito.any())).thenReturn(unit);

                assertEquals(Optional.empty(), blinky.nextAiMove());
            }
        }

        @Test
        public void pathNotNull() {
            try (MockedStatic<Navigation> utilities = Mockito.mockStatic(Navigation.class)) {
                Unit unit = mock(Unit.class);
                doReturn(true).when(unit).hasSquare();
                utilities.when(() -> Navigation.findNearest(Mockito.any(), Mockito.any())).thenReturn(unit);
                List<Direction> path = Collections.singletonList(Direction.NORTH);
                utilities.when(() -> Navigation.shortestPath(Mockito.any(), Mockito.any(), Mockito.any())).thenReturn(path);

                assertEquals(Optional.ofNullable(path.get(0)), blinky.nextAiMove());
            }
        }

        @Test
        public void pathNull() {
            try (MockedStatic<Navigation> utilities = Mockito.mockStatic(Navigation.class)) {
                Unit unit = mock(Unit.class);
                doReturn(true).when(unit).hasSquare();
                utilities.when(() -> Navigation.findNearest(Mockito.any(), Mockito.any())).thenReturn(unit);
                utilities.when(() -> Navigation.shortestPath(Mockito.any(), Mockito.any(), Mockito.any())).thenReturn(null);

                assertEquals(Optional.empty(), blinky.nextAiMove());
            }
        }
    }
}
