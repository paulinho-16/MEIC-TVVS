package de.dominik_geyer.jtimesched.project;

import java.awt.*;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Stream;

import de.dominik_geyer.jtimesched.JTimeSchedApp;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import static de.dominik_geyer.jtimesched.project.ProjectTableModel.*;
import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.*;


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


        Field reader = JTimeSchedApp.class.getDeclaredField("LOGGER");
        reader.setAccessible(true);
        JTimeSchedApp mainClass = new JTimeSchedApp();
        Logger l = Logger.getLogger("JTimeSched");
        l.setLevel(Level.ALL);
        reader.set(mainClass, l);
    }

    @Test
    public void testGetColumnCount(){
        assertEquals(8,tableModel.getColumnCount());
    }

    @Test
    public void testGetRowCount(){
        assertEquals(2,tableModel.getRowCount());
    }

    @ParameterizedTest(name = "Test #{index} with input ({arguments})")
    @CsvSource(value = {"0,1", "0,2", "0,3","0,4", "0,5", "0,6", "0,7", "0,8","2,1","2,7"})
    public void testGetValueAt(int row, int column){
        tableModel.getValueAt(row,column);

        // Last 2 tests are for branch if conditions on project3
        // Assert the result of the getter as an object

    }

    @Test
    public void testGetProjectAt(){
        Project proj3 = new Project("Test Project 3");

        // tableModel.addProject(prj);
        Project returned = tableModel.getProjectAt(2);



        assertAll("Test obj1 with obj2 equality",
            () -> assertEquals(proj3.getTitle(), returned.getTitle()),
            () -> assertEquals(proj3.getNotes(), returned.getNotes()),
            () -> assertEquals(proj3.getTimeCreated(), returned.getTimeCreated()));


        // More fields can be created, but addProject is not working, which sucks

    }

    @Test
    public void testGetColumnName(){
        assertEquals("Title",tableModel.getColumnName(2));
    }

    @Test
    public void testGetColumnClass(){

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
        ProjectTableModel.COLUMN_TIMETODAY
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
    @CsvSource(value = {"-2,2", "3,3", "-1,2", "2,1"})
    public void testIsCellEditable_InvalidRow_ShouldThrowException(int row, int column) {
        assertThrows(IndexOutOfBoundsException.class, () -> tableModel.isCellEditable(row, column));
    }

    @ParameterizedTest(name = "Test #{index} with input ({arguments}) throws exception due to invalid column")
    @CsvSource(value = {"0,-2", "0,9", "1,-1", "1,8"})
    public void testIsCellEditable_InvalidColumn_ShouldThrowException(int row, int column) {
        assertThrows(IllegalStateException.class, () -> tableModel.isCellEditable(row, column));
    }


    @Nested
    public class SetValueAtTest{


        @Test
        public void testSetCheck(){

            tableModel.setValueAt(true,0,COLUMN_CHECK);
            assertTrue((Boolean) tableModel.getValueAt(0,COLUMN_CHECK));

        }

        @Test
        public void testSetTitle(){
            tableModel.setValueAt("The cake is a lie",0,COLUMN_TITLE);
            assertEquals("The cake is a lie", tableModel.getValueAt(0,COLUMN_TITLE));
        }

        @Test
        public void testSetColor(){
            tableModel.setValueAt(Color.yellow,0,COLUMN_COLOR);
            assertEquals(Color.yellow, tableModel.getValueAt(0,COLUMN_COLOR));
        }


        @Test
        public void testSetCreated(){
            tableModel.setValueAt(new Date(2021-12-25),0,COLUMN_CREATED);
            assertEquals(new Date(2021-12-25), tableModel.getValueAt(0,COLUMN_CREATED));
        }

        @Test
        public void testSetTimeOverall(){
            tableModel.setValueAt(new Integer(128),0,COLUMN_TIMEOVERALL);
            assertEquals(new Integer(128), tableModel.getValueAt(0,COLUMN_TIMEOVERALL));
        }

        @Test
        public void testSetTimeToday(){
            tableModel.setValueAt(new Integer(56),0,COLUMN_TIMETODAY);
            assertEquals(new Integer(56), tableModel.getValueAt(0,COLUMN_TIMETODAY));
        }

        @Test
        public void testException(){

            assertThrows(IllegalStateException.class, () -> tableModel.setValueAt(null,0,10));
        }









    }

    @Test
    public void testAddProject() {

        

        int initial_count = tableModel.getRowCount();
        Project prj = new Project("New project");

        tableModel.addProject(prj);

        int final_count = tableModel.getRowCount();
        assertEquals(initial_count + 1, final_count);

    }

    @Test
    public void testRemoveProject(){

        int initial_count = tableModel.getRowCount();

        tableModel.removeProject(0);

        int final_count = tableModel.getRowCount();

        assertEquals(initial_count -1, final_count);

    }





    @Test
    public void getColumnAt(){
        assertEquals(COLUMN_CHECK,tableModel.getProjectAt(1));
    }



}
