package nl.tudelft.jpacman.ui;

import nl.tudelft.jpacman.Launcher;
import nl.tudelft.jpacman.board.Board;
import nl.tudelft.jpacman.board.BoardFactory;
import nl.tudelft.jpacman.board.Square;
import nl.tudelft.jpacman.game.Game;
import nl.tudelft.jpacman.sprite.PacManSprites;
import nl.tudelft.jpacman.sprite.Sprite;
import java.awt.Dimension;
import java.awt.Graphics;
import java.lang.reflect.Method;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;

public class BoardPanelTest {
    private Method method;
    private BoardPanel boardPanel;
    private Board board;

    @Nested
    class renderTest {
        @BeforeEach
        void setUp() throws NoSuchMethodException {
            Launcher launcher = new Launcher();
            launcher.launch();
            Game game = launcher.getGame();
            method = BoardPanel.class.getDeclaredMethod("render", Board.class, Graphics.class, Dimension.class);
            method.setAccessible(true);
            boardPanel = new BoardPanel(game);
            boardPanel = spy(boardPanel);

            PacManSprites sprites = mock(PacManSprites.class);
            BoardFactory factory = new BoardFactory(sprites);
            Square square = factory.createGround();
            Square spySquare = spy(square);

            board = mock(Board.class);
            doReturn(3).when(board).getWidth();
            doReturn(3).when(board).getHeight();
            Sprite sprite = mock(Sprite.class);
            doReturn(sprite).when(spySquare).getSprite();
            doReturn(spySquare).when(board).squareAt(Mockito.anyInt(), Mockito.anyInt());
        }

        @Test
        public void renderCycles() throws Exception {
            Graphics graphics = mock(Graphics.class);

            method.invoke(boardPanel, board, graphics, mock(Dimension.class));
        }
    }
}
