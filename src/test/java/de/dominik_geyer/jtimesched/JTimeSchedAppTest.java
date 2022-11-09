package de.dominik_geyer.jtimesched;

import java.io.File;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.logging.Level;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class JTimeSchedAppTest {
    public static void deleteDirectory(File file) {
        for (File subfile : Objects.requireNonNull(file.listFiles())) {
            if (subfile.isDirectory()) {
                deleteDirectory(subfile);
            }
            subfile.delete();
        }
    }

    @Test
    public void testMain_UponStart_ShouldCreateConf() throws IOException {
        // Delete conf directory
        File conf_dir = new File(JTimeSchedApp.CONF_PATH);
        deleteDirectory(conf_dir);

        // Check that "conf" directory is empty
        Path conf_path = Paths.get(JTimeSchedApp.CONF_PATH);
        try(DirectoryStream<Path> dirStream = Files.newDirectoryStream(conf_path)) {
            assertFalse(dirStream.iterator().hasNext());
        }

        JTimeSchedApp.main(new String[0]);

        // Check that "conf" directory is not empty after creation of jTimeSched.log file
        try(DirectoryStream<Path> dirStream = Files.newDirectoryStream(conf_path)) {
            assertTrue(dirStream.iterator().hasNext());
        }

        // Verify the level of the logger after its initialization
        assertEquals(JTimeSchedApp.getLogger().getLevel(), Level.ALL);
    }
}
