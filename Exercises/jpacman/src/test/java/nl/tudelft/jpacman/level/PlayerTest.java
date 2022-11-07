package nl.tudelft.jpacman.level;

import nl.tudelft.jpacman.sprite.PacManSprites;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class PlayerTest {
    private Player player;

    @BeforeEach
    void setUp() {
        PacManSprites sprites = new PacManSprites();
        PlayerFactory playerCreator = new PlayerFactory(sprites);
        player = playerCreator.createPacMan();
    }

    @Nested
    class setAliveTest {
        @Test
        public void isAlive_true() {
            player.setAlive(true);
            assertTrue(player.isAlive());
        }

        @Test
        public void isAlive_false() {
            player.setAlive(false);
            assertFalse(player.isAlive());
        }
    }
}
