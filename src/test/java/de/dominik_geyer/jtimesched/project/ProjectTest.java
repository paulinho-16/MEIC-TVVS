package de.dominik_geyer.jtimesched.project;

import java.util.Date;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class ProjectTest {
    @Nested
    class startTest {
        @Test
        public void testStart_IdleProject_ShouldStart() throws ProjectException {
            // Create new Project
            Project prj = new Project("Test project");
            // Project should not be running before it is started
            Assertions.assertFalse(prj.isRunning());
            Date beforeStart = new Date();
            // Starting project
            prj.start();
            // Project should be running after it is started
            Assertions.assertTrue(prj.isRunning());
            // Checking that the project start date has been set during the running test
            Assertions.assertTrue(Math.abs(beforeStart.getTime() - prj.getTimeStart().getTime()) < 1000, "Dates aren't close enough to each other!");
        }

        @Test
        public void testStart_RunningProject_ShouldReturnException() throws ProjectException {
            // Create new Project
            Project prj = new Project("Test project");
            // Starting project
            prj.start();
            // Starting project again
            assertThrows(ProjectException.class, prj::start);
        }
    }

    @Nested
    class adjustSecondsTodayTest {
        @Test
        public void testAdjustSecondsToday_LargerInput_ShouldIncreaseOverallTime() {
            Project proj = new Project("Test Project");
            proj.setSecondsToday(10);
            proj.setSecondsOverall(50);
            proj.adjustSecondsToday(20);

            assertEquals(20,proj.getSecondsToday());
            assertEquals(60,proj.getSecondsOverall());
        }

        @Test
        public void testAdjustSecondsToday_SmallerInput_ShouldReduceOverallTime() {
            Project proj = new Project("Test Project");
            proj.setSecondsToday(20);
            proj.setSecondsOverall(50);
            proj.adjustSecondsToday(10);

            assertEquals(10,proj.getSecondsToday());
            assertEquals(40,proj.getSecondsOverall());
        }

        @Test
        public void testAdjustSecondsToday_NegativeInput_ShouldBecomeZero() {
            Project proj = new Project("Test Project");
            proj.setSecondsToday(10);
            proj.setSecondsOverall(50);
            proj.adjustSecondsToday(-10);

            assertEquals(0,proj.getSecondsToday());
            assertEquals(40,proj.getSecondsOverall());
        }
    }
}