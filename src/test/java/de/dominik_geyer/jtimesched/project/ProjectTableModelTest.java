package de.dominik_geyer.jtimesched.project;

import java.awt.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.stream.Stream;

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
    void setup() {
        Project proj1 = new Project("Test Project 1");
        Project proj2 = new Project("Test Project 2");

        projects.add(proj1);
        projects.add(proj2);
        tableModel = new ProjectTableModel(projects);
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
    @CsvSource(value = {"0,1", "0,2", "0,3","0,4", "0,5", "0,6", "0,7", "0,8"})
    public void testGetValueAt(int row, int column){
        tableModel.getValueAt(row,column);

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
        // Este teste tem muitas cahamdas ao Logger e não estamos a conseguir tratar disso
        private Project proj;

        @BeforeEach
        void setup() {
            proj = new Project();

            proj.setChecked(true);/*
            proj.setSecondsOverall(10);
            proj.setSecondsToday(5);
            proj.setTitle("Third Project");
            proj.setColor(Color.green);
            proj.setNotes("hello notes");
            proj.setQuotaOverall(2);
            proj.setQuotaToday(1);
            proj.setRunning(true);*/

        }

        @Test
        public void testSetCheck(){

            tableModel.setValueAt(false,0,COLUMN_CHECK);

            assertFalse(proj.isChecked());

        }



    }

    @Test
    public void testAddProject(){

        int initial_count = tableModel.getRowCount();
        Project prj = new Project("New project");

        // Field.set() // -> Descobrir como se consegue criar o Logger que está complicado

        // tableModel.addProject(prj);

        int final_count = tableModel.getRowCount();
        assertEquals(initial_count + 1, final_count);

    }

    @Test
    public void testRemoveProject(){

        int initial_count = tableModel.getRowCount();

        //tableModel.removeProject(0);

        int final_count = tableModel.getRowCount();

        assertEquals(initial_count -1, final_count);

    }





    @Test
    public void getColumnAt(){
        assertEquals(COLUMN_CHECK,tableModel.getProjectAt(1));
    }



}
