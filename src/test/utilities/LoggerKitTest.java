package utilities;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.*;

public class LoggerKitTest {

    private LoggerKit loggerKit;
    private FileHandler fileHandler;

    @BeforeEach
    public void setUp() throws Exception {
        fileHandler = new FileHandler("test.log");
        loggerKit = new LoggerKit(fileHandler);
    }

    @Test
    public void testAddLogger() throws NoSuchFieldException, IllegalAccessException {
        Class<?> testClass = LoggerKitTest.class;
        loggerKit.addLogger(testClass);
        Field field = loggerKit.getClass().getDeclaredField("loggerMap");
        field.setAccessible(true);
        @SuppressWarnings("unchecked")
        Map<String, Logger> loggerMap = (Map<String, Logger>) field.get(loggerKit);
        field.setAccessible(false);

        Logger logger = loggerMap.get(testClass.getSimpleName());
        assertNotNull(logger);
        assertEquals(1, logger.getHandlers().length);
        assertEquals(fileHandler, logger.getHandlers()[0]);
        assertEquals(Level.ALL, logger.getLevel());
    }

    @Test
    public void testDoLogWithException() {
        Class<?> testClass = LoggerKitTest.class;
        Exception exception = new NullPointerException("Test Exception");

        assertThrows(NullPointerException.class, () -> loggerKit.doLog(testClass, exception, Level.SEVERE));
    }

    @Test
    public void testDoLogWithMessage() {
        Class<?> testClass = LoggerKitTest.class;
        String message = "Test Message";
        loggerKit.addLogger(testClass);

        assertDoesNotThrow(() -> loggerKit.doLog(testClass, message, Level.INFO));
    }
}