package nl.tudelft.jpacman.level;

import nl.tudelft.jpacman.PacmanConfigurationException;
import nl.tudelft.jpacman.board.BoardFactory;
import nl.tudelft.jpacman.board.Square;
import nl.tudelft.jpacman.npc.Ghost;
import nl.tudelft.jpacman.npc.ghost.GhostFactory;
import nl.tudelft.jpacman.sprite.PacManSprites;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;

public class MapParserTest {
    private MapParser parser;

    @BeforeEach
    void setUp() {
        PacManSprites sprites = mock(PacManSprites.class);
        LevelFactory levelCreator = new LevelFactory(sprites, new GhostFactory(sprites));
        parser = new MapParser(levelCreator, new BoardFactory(sprites));
    }

    @Nested
    class addSquareTest {
        private final Square[][] grid = {
            {mock(Square.class), mock(Square.class), mock(Square.class)},
            {mock(Square.class), mock(Square.class), mock(Square.class)},
            {mock(Square.class), mock(Square.class), mock(Square.class)}
        };
        private final List<Ghost> ghosts = new ArrayList<>();
        private final List<Square> startPositions = new ArrayList<>();

        @ParameterizedTest
        @ValueSource(chars = {' ', '#', '.', 'G', 'P'})
        public void valid(char c) {
            assertDoesNotThrow(() -> parser.addSquare(grid, ghosts, startPositions, 0, 0, c));
        }

        @ParameterizedTest
        @ValueSource(chars = {'A', ':', 'L'})
        public void invalid() {
            assertThrows(PacmanConfigurationException.class, () -> parser.addSquare(grid, ghosts, startPositions, 0, 0, 'A'));
        }
    }

    @Nested
    class parseMapTest {
        @Test
        public void null_text() {
            assertThrows(PacmanConfigurationException.class, () -> parser.parseMap((List<String>) null));
        }

        @Test
        public void empty_text() {
            assertThrows(PacmanConfigurationException.class, () -> parser.parseMap(new ArrayList<>()));
        }

        @Test
        public void zero_width() {
            List<String> text = Arrays.asList("", "", "");
            assertThrows(PacmanConfigurationException.class, () -> parser.parseMap(text));
        }

        @Test
        public void different_width() {
            List<String> text = Arrays.asList("a", "ab", "abc");
            assertThrows(PacmanConfigurationException.class, () -> parser.parseMap(text));
        }

        @Test
        public void missing_resource() {
            assertThrows(PacmanConfigurationException.class, () -> parser.parseMap("testMap"));
        }
    }

    @Nested
    class getBoardCreatorTest {
        @Test
        public void valid() {
            assertNotNull(parser.getBoardCreator());
        }
    }
}
