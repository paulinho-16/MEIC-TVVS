package de.dominik_geyer.jtimesched.project;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

public class ProjectTest {
    @Nested
    public class SetTitleTest {
        private Project proj;

        @BeforeEach
        void setup() {
            proj = new Project("Test Project Before");
        }

        @Test
        public void testSetTitle_UponCall_ShouldChangeTitle() {
            assertEquals("Test Project Before", proj.getTitle());
            proj.setTitle("Test Project After");
            assertEquals("Test Project After", proj.getTitle());
        }

        @Test
        public void testSetTitle_NullInput_ShouldSetTitleNull() {
            assertEquals("Test Project Before", proj.getTitle());
            proj.setTitle(null);
            assertNull(proj.getTitle());
        }
    }

    @Nested
    public class SetRunningTest {
        @Test
        public void testSetRunning_BooleanInput_ShouldChangeState() {
            Project proj = new Project();
            Assertions.assertFalse(proj.isRunning());
            proj.setRunning(true);
            Assertions.assertTrue(proj.isRunning());
            proj.setRunning(false);
            Assertions.assertFalse(proj.isRunning());
        }
    }

    @Nested
    public class GetElapsedSecondsTest {
        private Project proj;
        private final CountDownLatch waiter = new CountDownLatch(1);

        @BeforeEach
        void setup() {
            proj = new Project();
        }

        @Test
        public void testGetElapsedSeconds_RunningProject_ShouldReturnSeconds() throws ProjectException, InterruptedException {
            Date beforeStart = new Date();
            proj.start();
            waiter.await(2*1000*1000*1000, TimeUnit.NANOSECONDS); // 2s
            Date afterRunning = new Date();
            int elapsedSeconds = proj.getElapsedSeconds();
            // Checking that the project start date has been set during the running test
            Assertions.assertTrue(Math.abs(beforeStart.getTime() - proj.getTimeStart().getTime()) < 1000, "Times aren't close enough to each other!");
            // Checking that the value returned by the method and the actual elapsed seconds are similar
            int actualSeconds = (int) Math.abs(beforeStart.getTime() - afterRunning.getTime()) / 1000;
            assertEquals(actualSeconds, elapsedSeconds, "Elapsed seconds aren't close enough to each other!");
        }

        @Test
        public void testGetElapsedSeconds_IdleProject_ShouldThrowException() {
            // Project should not be running before it is started
            Assertions.assertFalse(proj.isRunning());
            assertThrows(ProjectException.class, proj::getElapsedSeconds);
        }
    }

    @Nested
    class StartTest {
        @Test
        public void testStart_IdleProject_ShouldStart() throws ProjectException {
            // Create new Project
            Project proj = new Project();
            // Project should not be running before it is started
            Assertions.assertFalse(proj.isRunning());
            Date beforeStart = new Date();
            // Starting project
            proj.start();
            // Project should be running after it is started
            Assertions.assertTrue(proj.isRunning());
            // Checking that the project start date has been set during the running test
            Assertions.assertTrue(Math.abs(beforeStart.getTime() - proj.getTimeStart().getTime()) < 1000, "Times aren't close enough to each other!");
        }

        @Test
        public void testStart_RunningProject_ShouldReturnException() throws ProjectException {
            // Create new Project
            Project proj = new Project();
            // Starting project
            proj.start();
            // Starting project again
            assertThrows(ProjectException.class, proj::start);
        }
    }

    @Nested
    class PauseTest {
        @Test
        public void testPause_RunningProject_ShouldPause() throws ProjectException {
            // Create new Project
            Project proj = new Project();
            // Project should not be running before it is started
            Assertions.assertFalse(proj.isRunning());
            Date beforeStart = new Date();
            // Starting project
            proj.start();
            // Project should be running after it is started
            Assertions.assertTrue(proj.isRunning());
            // Checking that the project start date has been set during the running test
            Assertions.assertTrue(Math.abs(beforeStart.getTime() - proj.getTimeStart().getTime()) < 1000, "Times aren't close enough to each other!");
            // Pausing project
            proj.pause();
            // Project should not be running after it is paused
            Assertions.assertFalse(proj.isRunning());
        }

        @Test
        public void testPause_IdleProject_ShouldReturnException() {
            // Create new Project
            Project proj = new Project();
            // Project should not be running after it is created
            Assertions.assertFalse(proj.isRunning());
            // Pausing idle project again
            assertThrows(ProjectException.class, proj::pause);
        }
    }

    @Nested
    public class ToggleTest {
        private Project proj;

        @BeforeEach
        void setup() {
            proj = new Project("Test Project");
        }

        @Test
        public void testToggle_IdleProject_ShouldStartIt() {
            Assertions.assertFalse(proj.isRunning());
            proj.toggle();
            Assertions.assertTrue(proj.isRunning());
        }

        @Test
        public void testToggle_RunningProject_ShouldPauseIt() throws ProjectException {
            proj.start();
            Assertions.assertTrue(proj.isRunning());
            proj.toggle();
            Assertions.assertFalse(proj.isRunning());
        }
    }

    @Nested
    public class SetSecondsTodayTest {
        private Project proj;

        @BeforeEach
        void setup() {
            proj = new Project();
        }

        @ParameterizedTest(name = "Test #{index} with Positive input {arguments}")
        @ValueSource(ints = {1,10,60,120})
        public void testSetSecondsToday_PositiveInput_ShouldUpdateSeconds(int seconds) {
            assertEquals(0, proj.getSecondsToday());
            proj.setSecondsToday(seconds);
            assertEquals(seconds, proj.getSecondsToday());
        }

        @ParameterizedTest(name = "Test #{index} with Non-Positive input {arguments}")
        @ValueSource(ints = {-60,-10,-1,0})
        public void testSetSecondsToday_NegativeInput_ShouldBecomeZero(int seconds) {
            assertEquals(0, proj.getSecondsToday());
            proj.setSecondsToday(seconds);
            assertEquals(0, proj.getSecondsToday());
        }
    }

    @Nested
    public class SetSecondsOverallTest {
        private Project proj;

        @BeforeEach
        void setup() {
            proj = new Project();
        }

        @ParameterizedTest(name = "Test #{index} with Positive input {arguments}")
        @ValueSource(ints = {1,10,60,120})
        public void testSetSecondsOverall_PositiveInput_ShouldUpdateSeconds(int seconds) {
            assertEquals(0, proj.getSecondsOverall());
            proj.setSecondsOverall(seconds);
            assertEquals(seconds, proj.getSecondsOverall());
        }

        @ParameterizedTest(name = "Test #{index} with Non-Positive input {arguments}")
        @ValueSource(ints = {-60,-10,-1,0})
        public void testSetSecondsOverall_NegativeInput_ShouldBecomeZero(int seconds) {
            assertEquals(0, proj.getSecondsOverall());
            proj.setSecondsOverall(seconds);
            assertEquals(0, proj.getSecondsOverall());
        }
    }

    @Nested
    public class AdjustSecondsTodayTest {
        private Project proj;
        private final int overallTime = 50;
        private final int previousValue = 4;
        
        @BeforeEach
        void setup() {
            proj = new Project();
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

    @Nested
    public class ResetTodayTest {
        @Test
        public void testResetToday_UponReset_ShouldZeroAttributes() throws ParseException {
            Project proj = new Project();
            int secondsToday = 4;
            int quotaToday = 120;
            String startDate = "07-11-2022";

            proj.setSecondsToday(secondsToday);
            proj.setQuotaToday(quotaToday);
            Date date = new SimpleDateFormat("dd-MM-yyyy").parse(startDate);
            proj.setTimeStart(date);

            assertEquals(secondsToday, proj.getSecondsToday());
            assertEquals(quotaToday, proj.getQuotaToday());
            Date beforeReset = new Date();
            proj.resetToday();
            assertEquals(0, proj.getSecondsToday());
            assertEquals(0, proj.getQuotaToday());

            // Checking that the project start date has been set during the running test
            Assertions.assertTrue(Math.abs(beforeReset.getTime() - proj.getTimeStart().getTime()) < 1000, "Times aren't close enough to each other!");
        }
    }

    @Nested
    public class SetQuotaOverallTest {
        @Test
        public void testSetQuotaOverall_IntInput_ShouldUpdateQuota() {
            Project proj = new Project();
            assertEquals(0, proj.getQuotaOverall());
            proj.setQuotaOverall(120);
            assertEquals(120, proj.getQuotaOverall());
        }
    }

    @Nested
    public class ToStringTest {
        private Project proj;

        @BeforeEach
        void setup() {
            proj = new Project("Test Project");
            proj.setSecondsToday(4);
            proj.setSecondsOverall(10);
        }

        @Test
        public void testToString_IdleCheckedProject_ShouldPrintAttributes() {
            proj.setChecked(true);
            assertEquals("Project [title=Test Project, running=no, secondsOverall=10, secondsToday=4, checked=yes]", proj.toString());
        }

        @Test
        public void testToString_RunningUncheckedProject_ShouldPrintAttributes() throws ProjectException {
            proj.start();
            assertEquals("Project [title=Test Project, running=yes, secondsOverall=10, secondsToday=4, checked=no]", proj.toString());
        }
    }
}