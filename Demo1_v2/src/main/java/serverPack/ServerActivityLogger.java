package serverPack;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

import java.time.LocalDateTime;


/**
 * The ServerActivityLogger class provides logging functionality for server
 * activities.
 */

public class ServerActivityLogger {

	private static ServerActivityLogger instance;
    private BufferedWriter writer;
    
    
	/**
	 * Private constructor to initialize the logger.
	 */
    
    private ServerActivityLogger() {
        try {
        	writer = new BufferedWriter(new FileWriter("server_activity_logs.txt", true));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    
	/**
	 * Returns the singleton instance of ServerActivityLogger.
	 *
	 * @return The singleton instance of ServerActivityLogger.
	 */

    public static synchronized ServerActivityLogger getInstance() {
        if (instance == null) {
            instance = new ServerActivityLogger();
        }
        return instance;
    }
    
    
    /**
     * Logs a server request with the current timestamp.
     *
     * @param request The request to be logged.
     */

    public void logRequest(String request) {
        String logEntry = "[REQUEST] " + LocalDateTime.now() + " - " + request;
        writeLog(logEntry);
    }
    
    
    /**
     * Logs a general server activity with the current timestamp.
     *
     * @param log The activity to be logged.
     */

    public void log(String log) {
        String logEntry = "[LOG] " + LocalDateTime.now() + " - " + log;
        writeLog(logEntry);
    }
    
    
    /**
     * Writes the log entry to the log file.
     *
     * @param logEntry The log entry to be written.
     */

    private void writeLog(String logEntry) {
        try {
        	writer.write(logEntry);
            writer.newLine();
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    
    /**
     * Closes the logger by closing the BufferedWriter.
     */
     
    public void closeLogger() {
        try {
            if (writer != null) {
                writer.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
	
}
