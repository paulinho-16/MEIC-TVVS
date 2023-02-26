package de.dominik_geyer.jtimesched.project;

import java.awt.*;
import java.lang.reflect.Field;
import java.text.ParseException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;
import java.util.stream.Stream;

import de.dominik_geyer.jtimesched.JTimeSchedApp;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import static de.dominik_geyer.jtimesched.project.ProjectTableModel.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class ProjectTableModelTest {
    private final ArrayList<Project> projects = new ArrayList<>();
    private ProjectTableModel tableModel;

    @BeforeEach
    void setup() throws ProjectException, NoSuchFieldException, IllegalAccessException {
        Project proj1 = new Project("Test Project 1");
        Project proj2 = new Project("Test Project 2");
        Project proj3 = new Project("Test Project 3");

        proj3.start();
        proj3.setChecked(true);

        projects.add(proj1);
        projects.add(proj2);
        projects.add(proj3);
        tableModel = new ProjectTableModel(projects);
        tableModel = spy(tableModel);

        Field reader = JTimeSchedApp.class.getDeclaredField("LOGGER");
        reader.setAccessible(true);
        JTimeSchedApp mainClass = new JTimeSchedApp();
        Logger l = Logger.getLogger("JTimeSched");
        l.setLevel(Level.ALL);
        reader.set(mainClass, l);
    }

    @Test
    public void testGetColumnCount_InputNumberOfColumns_ShouldReturnTheCurrentAmount() {
        assertEquals(8, tableModel.getColumnCount());
    }

    @Test
    public void testGetRowCount_InputNumberOfProjects_ShouldBeThree() {
        assertEquals(3, tableModel.getRowCount());
    }

    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    @Nested
    public class GetValueAtTest {
        Stream<Arguments> arguments() {
            return Stream.of(
                Arguments.of(0, 7, false),
                Arguments.of(2, 7, true),
                Arguments.of(0, 8, "wtf?")
            );
        }

        @ParameterizedTest
        @MethodSource("arguments")
        public void testGetValueAt_InputCellAndValue_ShouldReturnCellValue(int row, int column, Object value) {
            Object o = tableModel.getValueAt(row, column);
            assertEquals(value, o);
        }
    }

    @Test
    @DisplayName("Test GetProjectAt method with a created project")
    public void testGetProjectAt_CreateAndAddProject_ShouldHaveSameAttributes() {
        Project prj = new Project("Test Project 4");
        int rows = tableModel.getRowCount();

        tableModel.addProject(prj);
        Project returned = tableModel.getProjectAt(3);

        assertAll("Compare both Projects",
            () -> assertEquals(prj.getTitle(), returned.getTitle()),
            () -> assertEquals(prj.getNotes(), returned.getNotes()),
            () -> assertEquals(prj.getTimeCreated(), returned.getTimeCreated()),
            () -> assertEquals(prj.getSecondsToday(), returned.getSecondsToday()),
            () -> assertEquals(prj.getSecondsOverall(), returned.getSecondsOverall()),
            () -> assertEquals(prj.getQuotaOverall(), returned.getQuotaOverall()),
            () -> assertEquals(prj.getQuotaToday(), returned.getQuotaToday()),
            () -> assertEquals(prj.getTimeStart(), returned.getTimeStart()),
            () -> assertEquals(prj.getColor(), returned.getColor())
        );

        assertEquals(tableModel.getRowCount(), rows + 1);
    }

    @ParameterizedTest(name = "Test #{index} with input {arguments} returns true")
    @CsvSource(value = {"'',0","'',1","'Title',2","'',3","'Created',4","'Time Overall',5","'Time Today',6","'',7"})
    public void testGetColumnName_InputColumnAndName_ShouldReturnProvidedName(String title, int column) {
        assertEquals(title,tableModel.getColumnName(column));
    }

    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    @Nested
    public class GetColumnClassTest {
        Stream<Arguments> arguments() {
            return Stream.of(
                Arguments.of(COLUMN_COLOR, Color.class),
                Arguments.of(COLUMN_CREATED, Date.class),
                Arguments.of(COLUMN_TIMEOVERALL, Integer.class),
                Arguments.of(COLUMN_TIMETODAY, Integer.class),
                Arguments.of(COLUMN_CHECK, Boolean.class),
                Arguments.of(COLUMN_ACTION_DELETE, Boolean.class),
                Arguments.of(COLUMN_ACTION_STARTPAUSE, Boolean.class),
                Arguments.of(10, String.class)
            );
        }

        @ParameterizedTest
        @MethodSource("arguments")
        public void testGetColumnClass_ColumnInput_ShouldReturnClass(int column, Class cl) {
            assertEquals(cl, tableModel.getColumnClass(column));
        }
    }

    @ParameterizedTest(name = "Test #{index} with input {arguments} returns true")
    @ValueSource (ints = {
        COLUMN_CHECK,
        COLUMN_TITLE,
        COLUMN_COLOR,
        COLUMN_CREATED,
        COLUMN_TIMEOVERALL,
        COLUMN_TIMETODAY
    })
    public void testIsCellEditable_EditableCell_ShouldReturnTrue(int column) {
        assertTrue(tableModel.isCellEditable(0, column));
        assertTrue(tableModel.isCellEditable(1, column));
    }

    @ParameterizedTest(name = "Test #{index} with input {arguments} returns false")
    @ValueSource (ints = {
        ProjectTableModel.COLUMN_ACTION_DELETE,
        COLUMN_TIMEOVERALL,
        ProjectTableModel.COLUMN_TIMETODAY,
        ProjectTableModel.COLUMN_ACTION_STARTPAUSE,
    })
    public void testIsCellEditable_NonEditableCell_ShouldReturnFalse(int column) throws ProjectException {
        tableModel.getProjectAt(0).start();
        assertFalse(tableModel.isCellEditable(0, column));
        tableModel.getProjectAt(1).start();
        assertFalse(tableModel.isCellEditable(1, column));
    }

    @ParameterizedTest(name = "Test #{index} with input ({arguments}) throws exception due to invalid row")
    @CsvSource(value = {"-2,2", "3,3", "-1,2", "3,1"})
    public void testIsCellEditable_InvalidRow_ShouldThrowException(int row, int column) {
        assertThrows(IndexOutOfBoundsException.class, () -> tableModel.isCellEditable(row, column));
    }

    @ParameterizedTest(name = "Test #{index} with input ({arguments}) throws exception due to invalid column")
    @CsvSource(value = {"0,-2", "0,9", "1,-1", "1,8"})
    public void testIsCellEditable_InvalidColumn_ShouldThrowException(int row, int column) {
        assertThrows(IllegalStateException.class, () -> tableModel.isCellEditable(row, column));
    }

    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    @Nested
    public class SetValueAtTest {
        Stream<Arguments> arguments() {

            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDateTime now = LocalDateTime.now();

            return Stream.of(
                Arguments.of(true, COLUMN_CHECK, 1, "Set check for project 'Test Project 1'"),
                Arguments.of(false, COLUMN_CHECK, 1, "Unset check for project 'Test Project 1'"),
                Arguments.of("The cake is a lie", COLUMN_TITLE, 1, "Renamed project 'Test Project 1' to 'The cake is a lie'"),
                Arguments.of(Color.yellow, COLUMN_COLOR, 0, ""),
                Arguments.of(new Date(2021-12-25), COLUMN_CREATED, 1, "Manually set create date for project 'The cake is a lie' from " + dtf.format(now) + " to 1970-01-01"),
                Arguments.of(new Integer(128), COLUMN_TIMEOVERALL, 1, "Manually set time overall for project 'The cake is a lie' from 0:00:00 to 0:02:08"),
                Arguments.of(new Integer(56), COLUMN_TIMETODAY, 1, "Manually set time today for project 'The cake is a lie' from 0:00:00 to 0:00:56")
            );
        }

        @ParameterizedTest
        @MethodSource("arguments")
        public void testSetValueAt_InputValueAndColumn_ShouldReturnChangedCell(Object value, int column, int numLogs, String logMessage) {
            ArrayList<LogRecord> logRecords = new ArrayList<>();

            JTimeSchedApp.getLogger().setFilter(logRecord -> {
                if (logRecord.getLevel().intValue() == Level.INFO.intValue()) {
                    logRecords.add(logRecord);
                }
                return false;
            });

            tableModel.setValueAt(value, 0, column);

            assertEquals(value, tableModel.getValueAt(0, column));
            assertEquals(numLogs, logRecords.size());
            if (logRecords.size() > 0) {
                assertEquals(logMessage, logRecords.get(0).getMessage());
            }
        }

        @Test
        public void testSetValueAt_InputOutOfBounds_ShouldThrowException() {
            assertThrows(IllegalStateException.class, () -> tableModel.setValueAt(null, 0, 10));
        }

        @Test
        public void testSetValueAt_NegativeTimeToday_ShouldCatchException() {
            ParseException ex = mock(ParseException.class);

            try (MockedStatic<ProjectTime> utilities = Mockito.mockStatic(ProjectTime.class)) {
                utilities.when(() -> ProjectTime.formatSeconds(-1)).thenThrow(ex);

                // Call method
                assertDoesNotThrow(() -> tableModel.setValueAt(-1, 0, COLUMN_TIMETODAY));

                // Verify that the exception stacktrace was printed
                verify(ex).printStackTrace();
            }
        }
    }

    @Test
    public void testAddProject_AddingProject_ShouldUpdateTable() {
        Project proj = new Project("Test Project 4");

        tableModel.addProject(proj);

        verify(tableModel, times(1)).fireTableRowsInserted(3, 3);
    }

    @Test
    public void testRemoveProject_DeletingProject_ShouldRemoveIt() throws ParseException {
        int initial_count = tableModel.getRowCount();

        tableModel.removeProject(0);

        assertEquals(initial_count - 1, tableModel.getRowCount());
        verify(tableModel, times(1)).fireTableRowsDeleted(0, 0);
    }
}
