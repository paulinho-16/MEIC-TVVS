package de.dominik_geyer.jtimesched.misc;

import java.text.SimpleDateFormat;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class PlainTextFormatterTest {
    PlainTextFormatter plainTextFormatter = new PlainTextFormatter();

    @Test
    public void testFormat_LogRecord_ShouldReturnString() {
        LogRecord record = new LogRecord(Level.INFO, "Test message in Log");
        String time = new SimpleDateFormat("yyyy-MM-dd (E) HH:mm:ss").format(record.getMillis());
        String expected = time.concat(" [INFO]: Test message in Log");

        String format = plainTextFormatter.format(record).trim();

        assertEquals(expected, format);
    }

    @Test
    public void testFormat_NullInput_ShouldThrowException() {
        assertThrows(NullPointerException.class, () -> plainTextFormatter.format(null));
    }
}
