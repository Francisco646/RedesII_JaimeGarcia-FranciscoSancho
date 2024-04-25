package clientPack;

import java.net.http.HttpClient;
import java.time.Duration;

import javax.swing.JFrame;


/**
 * This class represents the main GUI program for the client application.
 * It extends JFrame to create a graphical window for the application.
 */

@SuppressWarnings("serial")
public class ClientGUIProgram extends JFrame {

    private HttpClient httpClient;

    
    /**
     * Constructs a new instance of ClientGUIProgram.
     * This constructor configures the HttpClient instance and initializes the GUI for the client program.
     */
    
    public ClientGUIProgram() {
        configureHttpClient();
        ClientLayoutConfiguration.manageGUI(httpClient);
    }

    
    /**
     * Configures the HttpClient instance for the session.
     * This method creates a HttpClient instance with specified settings such as HTTP version
     * and connection timeout duration.
     */
    
    private void configureHttpClient() {
        httpClient = HttpClient.newBuilder()
                .version(HttpClient.Version.HTTP_1_1)
                .connectTimeout(Duration.ofSeconds(10))
                .build();
    }

}