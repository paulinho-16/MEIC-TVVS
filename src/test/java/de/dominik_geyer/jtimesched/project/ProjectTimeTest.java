package de.dominik_geyer.jtimesched.project;

import java.text.ParseException;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import static de.dominik_geyer.jtimesched.project.ProjectTime.parseSeconds;
import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class ProjectTimeTest {
    @ParameterizedTest(name = "Test #{index} with input {0} results in {1} seconds")
    @CsvSource(value = {"0:0:0,0", "0:0:15,15", "0:17:0,1020", "20:0:0,72000", "4:21:16,15676", "59:59:59,215999", "06:09:03,22143"})
    public void testParseSeconds_CorrectDateFormat_ShouldReturnSeconds(String format, int value) throws ParseException {
        assertEquals(value, parseSeconds(format));
    }

    @ParameterizedTest(name = "Test #{index} with input {arguments} throws exception")
    @ValueSource(strings = {"", "4.21.16", "1:11:11:11","1:11","1","00:60:00","24:00:60", "8:-42:09", "aa:bb:cc"})
    public void testParseSeconds_IncorrectDateFormat_ShouldThrowException(String format) {
        assertThrows(ParseException.class, () -> parseSeconds(format));
    }
}