package logger;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.logging.*;
import java.time.format.DateTimeFormatter;

public class LoggerService {

    private static Logger logger;

    static {
        try {
            logger = Logger.getLogger(LoggerService.class.getName());
            FileHandler fh = new FileHandler("application.log");
            fh.setFormatter(new SimpleFormatter());
            logger.addHandler(fh);
            logger.setUseParentHandlers(false);
        } catch (IOException e){
            System.out.println("Error " + e);
        }
    }

    public static void info(String message) {
        log("INFO", message);
    }

    public static void error(String message) {
        log("ERROR", message);
    }

    private static void log(String level, String message) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy HH:mm");
        String timestamp = LocalDateTime.now().format(formatter);

        String logMessage = String.format("%s [%s] %s", timestamp, level, message);

        logger.info(logMessage);

        FileHandler fileHandler = (FileHandler) logger.getHandlers()[0];
        fileHandler.setFormatter(new SimpleFormatter());

        LogRecord logRecord = new LogRecord(Level.INFO, logMessage);
        fileHandler.publish(logRecord);
    }
}
