package nl.tudelft.jpacman.game;

import nl.tudelft.jpacman.level.Level;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

public class GameTest {
    @Nested
    class startTest {
        private Game game;
        private Level level;

        @BeforeEach
        void setUp() {
            game = spy(Game.class);
            level = mock(Level.class);
            when(game.getLevel()).thenReturn(level);
        }

        @Test // t1
        public void startGame() {
            assertFalse(game.isInProgress());
            when(level.isAnyPlayerAlive()).thenReturn(true);
            when(level.remainingPellets()).thenReturn(1);
            game.start();
            assertTrue(game.isInProgress());
        }

        @Test // t2
        public void noRemainingPellets() {
            assertFalse(game.isInProgress());
            when(level.isAnyPlayerAlive()).thenReturn(true);
            when(level.remainingPellets()).thenReturn(0);
            game.start();
            assertFalse(game.isInProgress());
        }

        @Test // t3
        public void noPlayersAlive() {
            assertFalse(game.isInProgress());
            when(level.isAnyPlayerAlive()).thenReturn(false);
            when(level.remainingPellets()).thenReturn(1);
            game.start();
            assertFalse(game.isInProgress());
        }

        @Test // t5
        public void gameInProgress() {
            assertFalse(game.isInProgress());
            when(level.isAnyPlayerAlive()).thenReturn(true);
            when(level.remainingPellets()).thenReturn(1);
            game.start();
            assertTrue(game.isInProgress());
            game.start();
            assertDoesNotThrow(() -> game.start());
            assertTrue(game.isInProgress());
        }
    }
}
