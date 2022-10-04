package de.dominik_geyer.jtimesched.project;

import java.text.ParseException;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import static de.dominik_geyer.jtimesched.project.ProjectTime.parseSeconds;
import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class ProjectTimeTest {

    @ParameterizedTest
    @CsvSource(value = {"4:21:16,15676", "0:0:0,0", "59:59:59,215999", "06:09:03,22143"})
    public void testFormatSeconds_CorrectDateFormat_ShouldReturnSeconds(String format, int value) throws ParseException {
        // Test correct time format
        assertEquals(value, parseSeconds(format));
    }

    @ParameterizedTest
    @ValueSource(strings = {"4.21.16", "1:11:11:11","1:11","1","00:60:00","24:00:60", "8:-42:09"})
    public void testFormatSeconds_IncorrectDateFormat_ShouldReturnException(String format) {
        // Test wrong time format with exception being thrown
        assertThrows(ParseException.class, () -> parseSeconds(format));
    }
}