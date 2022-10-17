package de.dominik_geyer.jtimesched.project;

import java.util.ArrayList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.Assert.assertThrows;

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

    @ParameterizedTest(name = "Test #{index} with input {arguments} returns true")
    @ValueSource (ints = {
        ProjectTableModel.COLUMN_CHECK,
        ProjectTableModel.COLUMN_TITLE,
        ProjectTableModel.COLUMN_COLOR,
        ProjectTableModel.COLUMN_CREATED,
        ProjectTableModel.COLUMN_TIMEOVERALL,
        ProjectTableModel.COLUMN_TIMETODAY
    })
    public void testIsCellEditable_EditableCell_ShouldReturnTrue(int column) {
        assertTrue(tableModel.isCellEditable(0, column));
        assertTrue(tableModel.isCellEditable(1, column));
    }

    @ParameterizedTest(name = "Test #{index} with input {arguments} returns false")
    @ValueSource (ints = {
        ProjectTableModel.COLUMN_ACTION_DELETE,
        ProjectTableModel.COLUMN_TIMEOVERALL,
        ProjectTableModel.COLUMN_TIMETODAY,
        ProjectTableModel.COLUMN_ACTION_STARTPAUSE,
    })
    public void testIsCellEditable_NonEditableCell_ShouldReturnFalse(int column) throws ProjectException {
        tableModel.getProjectAt(0).start();
        assertFalse(tableModel.isCellEditable(0, column));
        tableModel.getProjectAt(1).start();
        assertFalse(tableModel.isCellEditable(1, column));
    }

    @ParameterizedTest(name = "Test #{index} with input ({arguments}) throws exception")
    @CsvSource(value = {"-2,2", "3,3", "-1,2", "2,1", "0,-2", "0,9", "1,-1", "1,8"})
    public void testIsCellEditable_InvalidCell_ShouldThrowException(int row, int column) {
        assertThrows(IndexOutOfBoundsException.class, () -> tableModel.isCellEditable(row, column));
    }
}
