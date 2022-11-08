package de.dominik_geyer.jtimesched.project;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import static de.dominik_geyer.jtimesched.project.ProjectTime.formatDate;
import static de.dominik_geyer.jtimesched.project.ProjectTime.formatSeconds;
import static de.dominik_geyer.jtimesched.project.ProjectTime.parseSeconds;
import static de.dominik_geyer.jtimesched.project.ProjectTime.parseDate;
import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class ProjectTimeTest {
    @Nested
    public class FormatSecondsTest {
        @ParameterizedTest(name = "Test #{index} with input of {0} seconds results in time string {1}")
        @CsvSource(value = {"0,0:00:00", "22143,6:09:03", "25209,7:00:09", "15676,4:21:16", "363599,100:59:59"})
        public void testFormatSeconds_ValidInput_ShouldReturnTime(int seconds, String format) {
            assertEquals(format, formatSeconds(seconds));
        }

        @ParameterizedTest(name = "Test #{index} with input {arguments} throws exception")
        @ValueSource(ints = {-1, -15, -256})
        public void testFormatSeconds_NegativeInput_ShouldThrowException(int seconds) {
            assertThrows(ParseException.class, () -> formatSeconds(seconds));
        }
    }

    @Nested
    public class ParseSecondsTest {
        @Test
        @DisplayName("Test with null input throws exception")
        public void testParseSeconds_NullInput_ShouldThrowException() {
            assertThrows(NullPointerException.class, () -> parseSeconds(null));
        }

        @ParameterizedTest(name = "Test #{index} with input {0} results in {1} seconds")
        @CsvSource(value = {"0:0:0,0", "12:15:0,44100", "7:0:9,25209", "0:17:05,1025", "20:02:0,72120", "4:21:16,15676", "100:59:59,363599", "18:59:14,68354", "9:21:59,33719", "06:09:03,22143", "13:21:12,48072"})
        public void testParseSeconds_CorrectDateFormat_ShouldReturnSeconds(String format, int seconds) throws ParseException {
            assertEquals(seconds, parseSeconds(format));
        }

        @ParameterizedTest(name = "Test #{index} with input {arguments} throws exception")
        @ValueSource(strings = {"", "4.21.16", "1:11:11:11", "1:11", "1", "00:60:00", "24:00:60", "-2:13:09", "8:-42:09", "07:5:-15", "aa:19:23", "14:bb:34", "04:25:cc", "14:021:34", "04:25:123"})
        public void testParseSeconds_IncorrectDateFormat_ShouldThrowException(String format) {
            assertThrows(ParseException.class, () -> parseSeconds(format));
        }
    }

    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    @Nested
    public class FormatDateTest {
        Stream<Arguments> arguments() {
            return Stream.of(
                Arguments.of(2022, Calendar.NOVEMBER, 8, "2022-11-08"),
                Arguments.of(2010, Calendar.OCTOBER, 16, "2010-10-16"),
                Arguments.of(2016, Calendar.APRIL, 1, "2016-04-01")
            );
        }

        @Test
        @DisplayName("Test with null input throws exception")
        public void testFormatDate_NullInput_ShouldThrowException() {
            assertThrows(NullPointerException.class, () -> formatDate(null));
        }

        @ParameterizedTest
        @MethodSource("arguments")
        public void testFormatDate_DateInput_ShouldReturnString(int year, int month, int day, String parsedDate) {
            Date date = new GregorianCalendar(year, month, day).getTime();
            assertEquals(parsedDate, formatDate(date));
        }
    }

    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    @Nested
    public class ParseDateTest {
        Stream<Arguments> arguments() {
            return Stream.of(
                Arguments.of("2022-11-8", "Tue Nov 08 00:00:00 GMT 2022"),
                Arguments.of("2010-10-16", "Sat Oct 16 00:00:00 BST 2010"),
                Arguments.of("2016-4-01", "Fri Apr 01 00:00:00 BST 2016")
            );
        }

        @Test
        @DisplayName("Test with null input throws exception")
        public void testParseDate_NullInput_ShouldThrowException() {
            assertThrows(NullPointerException.class, () -> parseDate(null));
        }

        @ParameterizedTest(name = "Test #{index} with input {arguments} returns formatted date")
        @MethodSource("arguments")
        public void testParseDate_CorrectDateFormat_ShouldReturnFormattedDate(String strDate, String dateFormat) throws ParseException {
            Date date = parseDate(strDate);
            assertEquals(dateFormat, date.toString());
        }

        @ParameterizedTest(name = "Test #{index} with input {arguments} throws exception")
        @ValueSource(strings = {"2022:11:8", "16/10/2010", "2k16-4-1"})
        public void testParseDate_IncorrectDateFormat_ShouldThrowException(String strDate) {
            assertThrows(ParseException.class, () -> parseDate(strDate));
        }
    }
}