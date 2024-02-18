package stas.utilities;

import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

public class LoggerKit {

    private final ConcurrentHashMap<String, Logger> loggerMap;
    private final FileHandler fileHandler;


    public LoggerKit(FileHandler fileHandler) {
        this.loggerMap = new ConcurrentHashMap<>();
        this.fileHandler = fileHandler;
        Logger logger = Logger.getLogger(this.getClass().getName());
        loggerMap.put(this.getClass().getSimpleName(), logger);
    }


    public static String buildStackTraceMessage(Exception e) {
        StringBuilder str = new StringBuilder();
        str.append(e.getMessage()).append('\n');
        for (StackTraceElement ste : e.getStackTrace()) {
            str.append(ste.toString()).append('\n');
        }
        return str.toString();
    }

    public static String buildStackTraceMessage(StackTraceElement[] stackTrace) {
        StringBuilder str = new StringBuilder();
        for (StackTraceElement e : stackTrace) {
            str.append(e.toString()).append("\n");
        }
        return str.toString();
    }

    public static String getStackTraceLines(StackTraceElement[] stackTrace, int linesNumber) {
        StringBuilder str = new StringBuilder();
        int i = 0;
        while (i < linesNumber) {
            str.append(stackTrace[i++].toString()).append('\n');
        }
        return str.toString();
    }


    public void addLogger(Class<?> clas) {
        Logger logger = Logger.getLogger(clas.getName());
        logger.addHandler(fileHandler);
        logger.setLevel(Level.ALL);
        loggerMap.put(clas.getSimpleName(), logger);
    }

    public void doLog(Class<?> clas, Exception e, Level level) throws NullPointerException {
        Logger logger = loggerMap.get(clas.getSimpleName());
        if (logger == null) {
            NullPointerException nullPointerException = new NullPointerException("logger is not found");
            doLog(this.getClass(), nullPointerException, Level.SEVERE);
            throw nullPointerException;
        } else {
            logger.log(level, buildStackTraceMessage(e));
        }
    }

    public void doLog(Class<?> clas, String message, Level level) throws NullPointerException {
        Logger logger = loggerMap.get(clas.getSimpleName());
        if (logger == null) {
            NullPointerException nullPointerException = new NullPointerException("logger is not found");
            doLog(this.getClass(), nullPointerException, Level.SEVERE);
            throw nullPointerException;
        } else {
            logger.log(level, message);
        }
    }
}
