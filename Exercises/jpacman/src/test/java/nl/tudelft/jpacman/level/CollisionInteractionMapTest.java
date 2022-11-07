package nl.tudelft.jpacman.level;

import nl.tudelft.jpacman.Launcher;
import nl.tudelft.jpacman.board.Unit;
import nl.tudelft.jpacman.game.Game;
import nl.tudelft.jpacman.npc.Ghost;
import nl.tudelft.jpacman.npc.ghost.GhostFactory;
import nl.tudelft.jpacman.sprite.PacManSprites;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;

public class CollisionInteractionMapTest {
    @Nested
    class createBoardTest {
        private Method method;
        private Method playerMethod;
        private Launcher launcher;

        @BeforeEach
        void setUp() throws NoSuchMethodException {
            launcher = new Launcher();
            launcher.launch();
            Game game = launcher.getGame();
            game.start();

            method = CollisionInteractionMap.class.getDeclaredMethod("getInheritance", Class.class);
            method.setAccessible(true);

            playerMethod = Launcher.class.getDeclaredMethod("getSinglePlayer", Game.class);
            playerMethod.setAccessible(true);
        }

        @Test
        public void null_parameter() {
            CollisionInteractionMap collisionInteractionMap = new CollisionInteractionMap();
            assertThrows(InvocationTargetException.class, () -> method.invoke(collisionInteractionMap, (Object) null));
        }

        @Test
        public void collisionInteractionMap_parameter() throws InvocationTargetException, IllegalAccessException {
            CollisionInteractionMap collisionInteractionMap = new CollisionInteractionMap();
            List<Class<? extends Unit>> found = (List<Class<? extends Unit>>) method.invoke(collisionInteractionMap, collisionInteractionMap.getClass());
            assertEquals(1, found.size());
        }

        @Test
        public void player_parameter() throws InvocationTargetException, IllegalAccessException {
            CollisionInteractionMap collisionInteractionMap = new CollisionInteractionMap();
            Player singlePlayer = (Player) playerMethod.invoke(launcher, launcher.getGame());
            List<Class<? extends Unit>> found = (List<Class<? extends Unit>>) method.invoke(collisionInteractionMap, singlePlayer.getClass());
            assertEquals(2, found.size());
        }

        @Test
        public void blinky_parameter() throws InvocationTargetException, IllegalAccessException {
            CollisionInteractionMap collisionInteractionMap = new CollisionInteractionMap();
            PacManSprites sprites = mock(PacManSprites.class);
            GhostFactory ghostFact = new GhostFactory(sprites);
            Ghost blinky = ghostFact.createBlinky();
            List<Class<? extends Unit>> found = (List<Class<? extends Unit>>) method.invoke(collisionInteractionMap, blinky.getClass());
            assertEquals(3, found.size());
        }

        @Test
        public void object_parameter() throws InvocationTargetException, IllegalAccessException {
            CollisionInteractionMap collisionInteractionMap = new CollisionInteractionMap();
            Object object = new Object();
            List<Class<? extends Unit>> found = (List<Class<? extends Unit>>) method.invoke(collisionInteractionMap, object.getClass());
            assertEquals(1, found.size());
        }
    }
}
