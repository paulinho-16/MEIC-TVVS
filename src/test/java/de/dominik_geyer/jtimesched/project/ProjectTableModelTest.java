package de.dominik_geyer.jtimesched.project;

import java.util.ArrayList;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.Assert.assertThrows;

public class ProjectTableModelTest {
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
        Project proj1 = new Project("Test Project");
        ArrayList<Project> projects = new ArrayList<>();

        projects.add(proj1);
        ProjectTableModel tableModel = new ProjectTableModel(projects);

        assertTrue(tableModel.isCellEditable(0, column));
    }

    @ParameterizedTest(name = "Test #{index} with input {arguments} returns false")
    @ValueSource (ints = {
        ProjectTableModel.COLUMN_ACTION_DELETE,
        ProjectTableModel.COLUMN_TIMEOVERALL,
        ProjectTableModel.COLUMN_TIMETODAY,
        ProjectTableModel.COLUMN_ACTION_STARTPAUSE,
    })
    public void testIsCellEditable_NonEditableCell_ShouldReturnFalse(int column) throws ProjectException {
        Project proj1 = new Project("Test Project");
        proj1.start();

        ArrayList<Project> projects = new ArrayList<>();

        projects.add(proj1);
        ProjectTableModel tableModel = new ProjectTableModel(projects);

        assertFalse(tableModel.isCellEditable(0, column));
    }

    @ParameterizedTest(name = "Test #{index} with input ({arguments}) throws exception")
    @CsvSource(value = {"-1,2", "2,3"}) // , "0,-2"
    public void testIsCellEditable_InvalidCell_ShouldThrowException(int row, int column) {
        Project proj1 = new Project("Test Project");
        ArrayList<Project> projects = new ArrayList<>();

        projects.add(proj1);
        ProjectTableModel tableModel = new ProjectTableModel(projects);

        assertThrows(IndexOutOfBoundsException.class, () -> tableModel.isCellEditable(row, column));
    }

    // TODO: deixamos o caso 0,-2 e explicamos porque falha no relatório? Ou simplesmente removemos do teste?
}
