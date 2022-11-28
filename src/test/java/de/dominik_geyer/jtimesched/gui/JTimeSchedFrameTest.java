package de.dominik_geyer.jtimesched.gui;

import de.dominik_geyer.jtimesched.JTimeSchedApp;
import de.dominik_geyer.jtimesched.project.Project;
import de.dominik_geyer.jtimesched.project.ProjectException;
import java.lang.reflect.Field;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class JTimeSchedFrameTest {
    @Nested
    public class HandleStartPauseTest {
        private JTimeSchedFrame jTimeSchedFrame;

        @BeforeEach
        void setup() throws NoSuchFieldException, IllegalAccessException {
            Field reader = JTimeSchedApp.class.getDeclaredField("LOGGER");
            reader.setAccessible(true);
            JTimeSchedApp mainClass = new JTimeSchedApp();
            Logger l = Logger.getLogger("JTimeSched");
            l.setLevel(Level.ALL);
            reader.set(mainClass, l);

            jTimeSchedFrame = spy(JTimeSchedFrame.class);
        }

        @Test
        public void testHandleStartPause_PrjRunning_ShouldPauseIt() throws ProjectException, ParseException, NoSuchFieldException, IllegalAccessException {
            // Create project
            Project prj = new Project("Running Project");
            prj.start();
            assertTrue(prj.isRunning());

            // Call method
            jTimeSchedFrame.handleStartPause(prj);

            // Verify that prj is now idle
            assertFalse(prj.isRunning());
            Field currentProject = JTimeSchedFrame.class.getDeclaredField("currentProject");
            currentProject.setAccessible(true);
            assertEquals(currentProject.get(jTimeSchedFrame), prj);

            // Verify that methods are indeed being called
            verify(jTimeSchedFrame, times(1)).updateGUI();
            verify(jTimeSchedFrame, times(1)).updateTrayCurrentProject();
        }

        @Test
        public void testHandleStartPause_PrjIdle_ShouldStartIt() throws ParseException, NoSuchFieldException, IllegalAccessException {
            // Create project
            Project prj = new Project("Idle Project");
            assertFalse(prj.isRunning());

            // Call method
            jTimeSchedFrame.handleStartPause(prj);

            // Verify that prj is now running
            assertTrue(prj.isRunning());
            Field currentProject = JTimeSchedFrame.class.getDeclaredField("currentProject");
            currentProject.setAccessible(true);
            assertEquals(currentProject.get(jTimeSchedFrame), prj);

            // Verify that methods are indeed being called
            verify(jTimeSchedFrame, times(1)).updateGUI();
            verify(jTimeSchedFrame, times(1)).updateTrayCurrentProject();
        }

        @Test
        public void testHandleStartPause_PrjIdlePRunning_ShouldPauseP() throws ProjectException, ParseException, NoSuchFieldException, IllegalAccessException {
            // Create projects
            Project prj = new Project("Idle Project");
            Project p = new Project("Running Project");
            p.start();
            assertFalse(prj.isRunning());
            assertTrue(p.isRunning());

            // Initialize project list with a running project
            Field arPrj = JTimeSchedFrame.class.getDeclaredField("arPrj");
            arPrj.setAccessible(true);
            arPrj.set(jTimeSchedFrame, new ArrayList<>(Collections.singletonList(p)));

            // Call method
            jTimeSchedFrame.handleStartPause(prj);

            // Verify that p has now stopped and prj is now running
            assertFalse(p.isRunning());
            assertTrue(prj.isRunning());
            Field currentProject = JTimeSchedFrame.class.getDeclaredField("currentProject");
            currentProject.setAccessible(true);
            assertEquals(currentProject.get(jTimeSchedFrame), prj);
        }

        @Test
        public void testHandleStartPause_PrjIdlePIdle_ShouldStartPrj() throws ParseException, NoSuchFieldException, IllegalAccessException {
            // Create projects
            Project prj = new Project("Idle Project");
            Project p = new Project("Idle Project");
            assertFalse(prj.isRunning());
            assertFalse(p.isRunning());

            // Initialize project list with a running project
            Field arPrj = JTimeSchedFrame.class.getDeclaredField("arPrj");
            arPrj.setAccessible(true);
            arPrj.set(jTimeSchedFrame, new ArrayList<>(Collections.singletonList(p)));

            // Call method
            jTimeSchedFrame.handleStartPause(prj);

            // Verify that p has now stopped and prj is now running
            assertFalse(p.isRunning());
            assertTrue(prj.isRunning());
            Field currentProject = JTimeSchedFrame.class.getDeclaredField("currentProject");
            currentProject.setAccessible(true);
            assertEquals(currentProject.get(jTimeSchedFrame), prj);
        }

        @Test
        public void testHandleStartPause_UponException_ShouldCatchIt() throws ProjectException, ParseException {
            // Create project
            Project prj = mock(Project.class);
            ProjectException ex = mock(ProjectException.class);
            doThrow(ex).when(prj).start();

            // Call method
            jTimeSchedFrame.handleStartPause(prj);

            // Verify that the exception stacktrace was printed
            verify(ex).printStackTrace();
        }
    }
}
