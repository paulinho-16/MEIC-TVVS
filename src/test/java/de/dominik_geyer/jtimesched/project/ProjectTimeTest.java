package de.dominik_geyer.jtimesched.project;

import java.text.ParseException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import static de.dominik_geyer.jtimesched.project.ProjectTime.parseSeconds;
import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class ProjectTimeTest {
    @Test
    @DisplayName("Test with null input throws exception")
    public void testParseSeconds_NullInput_ShouldThrowException() {
        assertThrows(NullPointerException.class, () -> parseSeconds(null));
    }

    @ParameterizedTest(name = "Test #{index} with input {0} results in {1} seconds")
    @CsvSource(value = {"0:0:0,0", "12:15:0,44100", "7:0:9,25209", "0:17:05,1025", "20:02:0,72120", "4:21:16,15676", "100:59:59,363599", "18:59:14,68354", "9:21:59,33719", "06:09:03,22143", "13:21:12,48072"})
    public void testParseSeconds_CorrectDateFormat_ShouldReturnSeconds(String format, int value) throws ParseException {
        assertEquals(value, parseSeconds(format));
    }

    @ParameterizedTest(name = "Test #{index} with input {arguments} throws exception")
    @ValueSource(strings = {"", "4.21.16", "1:11:11:11","1:11","1","00:60:00","24:00:60", "-2:13:09", "8:-42:09", "07:5:-15", "aa:19:23", "14:bb:34", "04:25:cc", "14:021:34", "04:25:123"})
    public void testParseSeconds_IncorrectDateFormat_ShouldThrowException(String format) {
        assertThrows(ParseException.class, () -> parseSeconds(format));
    }
}