package nl.tudelft.jpacman;

import nl.tudelft.jpacman.board.Board;
import nl.tudelft.jpacman.board.BoardFactory;
import nl.tudelft.jpacman.board.Square;
import nl.tudelft.jpacman.level.LevelFactory;
import nl.tudelft.jpacman.level.MapParser;
import nl.tudelft.jpacman.npc.Ghost;
import nl.tudelft.jpacman.npc.ghost.GhostFactory;
import nl.tudelft.jpacman.sprite.ImageSprite;
import nl.tudelft.jpacman.sprite.PacManSprites;
import nl.tudelft.jpacman.sprite.SpriteStore;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;

public class BoundaryValueAnalysisTest {
    @Nested
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    class squareAtTest {
        private final Square[][] grid = {
            { mock(Square.class), mock(Square.class), mock(Square.class) },
            { mock(Square.class), mock(Square.class), mock(Square.class) },
            { mock(Square.class), mock(Square.class), mock(Square.class) }
        };

        private final Board board = new Board(grid);

        Stream<Arguments> valid_x_y() {
            return Stream.of(
                Arguments.of(0, 1),
                Arguments.of(1, 2),
                Arguments.of(2, 0)
            );
        }

        @ParameterizedTest
        @MethodSource("valid_x_y")
        public void valid_x_y(int x, int y) {
            assertDoesNotThrow(() -> board.squareAt(x, y));
        }

        @ParameterizedTest
        @ValueSource(ints = {-1, 3, 4})
        public void invalid_x(int x) {
            assertThrows(AssertionError.class, () -> board.squareAt(x, 1));
        }

        @ParameterizedTest
        @ValueSource(ints = {-1, 3, 4})
        public void invalid_y(int y) {
            assertThrows(AssertionError.class, () -> board.squareAt(2, y));
        }
    }

    @Nested
    class createBoardTest {
        private BoardFactory boardCreator;

        @BeforeEach
        void setUp() {
            PacManSprites sprites = mock(PacManSprites.class);
            boardCreator = new BoardFactory(sprites);
        }

        @Test
        public void grid_array() {
            assertDoesNotThrow(() -> boardCreator.createBoard(new Square[][]{{}}));
        }

        @Test
        public void null_grid() {
            assertThrows(AssertionError.class, () -> boardCreator.createBoard(null));
        }
    }

    @Nested
    class createLevelTest {
        private LevelFactory levelCreator;
        private final Board board = mock(Board.class);
        private final List<Ghost> ghosts = Arrays.asList(mock(Ghost.class), mock(Ghost.class), mock(Ghost.class));
        private final List<Square> startPositions = new ArrayList<>();

        @BeforeEach
        void setUp() {
            PacManSprites sprites = mock(PacManSprites.class);
            levelCreator = new LevelFactory(sprites, new GhostFactory(sprites));
        }

        @Test
        public void valid_level() {
            assertDoesNotThrow(() -> levelCreator.createLevel(board, ghosts, startPositions));
        }

        @Test
        public void null_board() {
            assertThrows(AssertionError.class, () -> levelCreator.createLevel(null, ghosts, startPositions));
        }

        @Test
        public void null_ghosts() {
            assertThrows(AssertionError.class, () -> levelCreator.createLevel(board, null, startPositions));
        }

        @Test
        public void null_positions() {
            assertThrows(AssertionError.class, () -> levelCreator.createLevel(board, ghosts, null));
        }
    }

    @Nested
    class makeGhostSquareTest {
        private MapParser parser;
        private List<Ghost> ghosts;
        private LevelFactory levelCreator;

        @BeforeEach
        void setUp() {
            PacManSprites sprites = mock(PacManSprites.class);
            parser = new MapParser(new LevelFactory(sprites, new GhostFactory(sprites)), new BoardFactory(sprites));
            levelCreator = new LevelFactory(sprites, new GhostFactory(sprites));
            ghosts = new ArrayList<>(Arrays.asList(levelCreator.createGhost(), levelCreator.createGhost(), levelCreator.createGhost()));
        }

        @Test
        public void valid_level() {
            assertDoesNotThrow(() -> parser.makeGhostSquare(ghosts, levelCreator.createGhost()));
        }

        @Test
        public void null_ghosts() {
            assertThrows(NullPointerException.class, () -> parser.makeGhostSquare(null, levelCreator.createGhost()));
        }

        @Test
        public void null_ghost() {
            assertThrows(NullPointerException.class, () -> parser.makeGhostSquare(ghosts, null));
        }
    }

    @Nested
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    class drawTest {
        private ImageSprite sprite;
        private BufferedImage image;

        Stream<Arguments> valid() {
            return Stream.of(
                Arguments.of(0, 2, 1, 1),
                Arguments.of(1, 1, 2, 3),
                Arguments.of(2, 0, 1, 1)
            );
        }

        @BeforeEach
        void setUp() throws IOException {
            SpriteStore spriteStore = new SpriteStore();
            sprite = (ImageSprite) spriteStore.loadSpriteFromResource("/sprite/floor.png");
            image = sprite.newImage(3, 3);
        }

        @ParameterizedTest
        @MethodSource("valid")
        public void valid_draw(int x, int y, int width, int height) {
            assertDoesNotThrow(() -> sprite.draw(image.createGraphics(), x, y, width, height));
        }

        @Test
        public void null_graphics() {
            assertThrows(NullPointerException.class, () -> sprite.draw(null, 0, 1, 1, 1));
        }

        @ParameterizedTest
        @ValueSource(ints = {-1, 3, 4})
        public void invalid_x(int x) {
            assertThrows(AssertionError.class, () -> sprite.draw(image.createGraphics(), x, 1, 1, 1));
        }

        @ParameterizedTest
        @ValueSource(ints = {-1, 3, 4})
        public void invalid_y(int y) {
            assertThrows(AssertionError.class, () -> sprite.draw(image.createGraphics(), 0, y, 1, 1));
        }

        @ParameterizedTest
        @ValueSource(ints = {0, -1, 3, 4})
        public void invalid_width(int width) {
            assertThrows(AssertionError.class, () -> sprite.draw(image.createGraphics(), 0, 0, width, 1));
        }

        @ParameterizedTest
        @ValueSource(ints = {0, -1, 3, 4})
        public void invalid_height(int height) {
            assertThrows(AssertionError.class, () -> sprite.draw(image.createGraphics(), 0, 0, 1, height));
        }
    }
}
