package de.dominik_geyer.jtimesched.project;

import java.util.Date;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

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
            Assertions.assertTrue(Math.abs(beforeStart.getTime() - prj.getTimeStart().getTime()) < 1000, "Times aren't close enough to each other!");
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
    public class adjustSecondsTodayTest {
        Project proj;
        final int overallTime = 50;
        final int previousValue = 4;
        
        @BeforeEach
        void setup() {
            proj = new Project("Test Project");

            proj.setSecondsToday(previousValue);
            proj.setSecondsOverall(overallTime);
        }
        
        @ParameterizedTest(name = "Test #{index} with Positive input {arguments}")
        @ValueSource(ints = {1,2,3,4,5,6})
        public void testAdjustSecondsToday_PositiveInput_ShouldReturnOverallTime(int value) {
            proj.adjustSecondsToday(value);
            assertEquals(value, proj.getSecondsToday());
            assertEquals(overallTime + value - previousValue, proj.getSecondsOverall());
        }

        @ParameterizedTest(name = "Test #{index} with Non-Positive input {arguments}")
        @ValueSource(ints = {-2,0})
        public void testAdjustSecondsToday_NegativeInput_ShouldBecomeZero(int value) {
            proj.adjustSecondsToday(value);
            assertEquals(0, proj.getSecondsToday());
            assertEquals(overallTime - previousValue, proj.getSecondsOverall());
        }
    }
}