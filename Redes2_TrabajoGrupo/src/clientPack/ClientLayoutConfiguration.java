package clientPack;

import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;


/**
 * This class manages the layout configuration of the client GUI program for interacting with an HTTP client.
 * It provides methods for initializing the GUI, configuring JSON payload, processing HTTP requests,
 * and updating response labels.
 */

public class ClientLayoutConfiguration {

	private static JFrame frame;
	private static JFrame clientFrame;
    private static JPanel clientMainPanel;
    private static JPanel responsePanel;
    
    private static JLabel enterUrlLabel;
    private static JTextField urlField;
    private static JComboBox<String> httpMethodsComboBox;
    private static JButton sendButton;
    
    private static String selectedMethod;
    private static String jsonPayload;
    
    private static URI requestURI;
    private static String requestingURL;
    private static URL requestURL;
    
    private static JLabel codeLabel;
    private static JLabel messageLabel;
    private static JLabel bodyLabel;
    
    private static HttpClient httpClient;
    private static HttpRequest httpRequest;
    private static HttpResponse<String> httpResponse;
    private static ClientResponseInfo responseInfo;
    
    
    /**
     * Initializes and manages the GUI for interacting with an HTTP client.
     *
     * @param client The HttpClient instance to be used for sending HTTP requests.
     */
    
	protected static void manageGUI(HttpClient client) {
        
		// Set the HttpClient instance for the client layout configuration
	    ClientLayoutConfiguration.httpClient = client;
	    
	    // Create and configure the items of the main GUI
	    configureMainGUI();
	    
	    sendButton.addActionListener(e -> {
	        selectedMethod = (String) httpMethodsComboBox.getSelectedItem();
	        
	        if (selectedMethod.equals("POST") || selectedMethod.equals("PUT")) {
	            configureJsonPayload();
	        } else {
	            jsonPayload = "";
	            processRequest();
	        }
	    });
	    
	    // Update labels according to response and set layout position of each component
	    updateLabels();
	    ClientLayoutItems.setItemsLayoutPosition(clientMainPanel, enterUrlLabel, urlField, httpMethodsComboBox, sendButton, responsePanel);
	    
	    clientFrame.add(clientMainPanel);
	    clientFrame.setVisible(true);
    }
	
	
	/**
	 * Configures the main graphical user interface (GUI) for the HTTP connection manager.
	 * This method creates and configures the main JFrame, panels for content and response display,
	 * and UI components including the URL label, URL field, HTTP methods combo box, and send button.
	 */
	
	private static void configureMainGUI() {
		// Create and configure the main JFrame
	    clientFrame = new JFrame("HTTP connection manager");
	    clientFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    clientFrame.setSize(600, 300);
	    clientFrame.setLocationRelativeTo(null);

	    // Create panels for main content and response display
	    clientMainPanel = new JPanel(new GridBagLayout());
	    responsePanel = new JPanel();

	    // Create and configure UI components
	    enterUrlLabel = new JLabel();
	    enterUrlLabel.setText("Enter an URL to be requested");

	    urlField = new JTextField();
	    urlField.setPreferredSize(new Dimension(400, 40));

	    // Define HTTP methods for selection
	    String[] httpMethods = {"GET", "HEAD", "POST", "PUT", "DELETE"};
	    httpMethodsComboBox = new JComboBox<>(httpMethods);
	    httpMethodsComboBox.setPreferredSize(new Dimension(75, 25));
	    
	    // Create and configure the "Send" button
	    sendButton = new JButton("Send");
	}
	
	
	/**
	 * Configures the JSON payload for an HTTP request through a graphical user interface (GUI).
	 * This method creates a JFrame for configuring the JSON payload, allowing users to enter
	 * the JSON data and send it with the request.
	 */
	
	private static void configureJsonPayload() {
        // Create the frame for configuring JSON payload
        SwingUtilities.invokeLater(() -> {
            frame = new JFrame("Configure JSON payload");
            frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            frame.setSize(400, 200);
            frame.setLocationRelativeTo(clientFrame);

            JPanel payloadPanel = new JPanel(new GridBagLayout());
            JLabel label = new JLabel("Enter the JSON payload for your request");
            JTextField textField = new JTextField();
            
            JButton button = new JButton("Send payload");
            button.addActionListener(event -> {
                jsonPayload = textField.getText();
                processRequest(); 
                frame.dispose();
            });

            ClientLayoutItems.configurePayloadLayout(label, textField, button, payloadPanel);

            frame.add(payloadPanel);
            frame.setVisible(true);
        });
    }
	
	
	/**
	 * Processes the HTTP request based on the user input and updates the GUI with response information.
	 * This method constructs the request URI and URL, builds the HTTP request, sends it using the provided
	 * HttpClient, receives the HTTP response, and updates the GUI with response status code, message,
	 * and body.
	 */
	
	private static void processRequest() {
        try {
            // Make conversions
        	requestURI = new URI("https://" + urlField.getText());
            requestingURL = requestURI.toString();
            requestURL = requestURI.toURL();
            
            // Process requests and responses
            httpRequest = ClientRequests.buildRequest(requestingURL, selectedMethod, httpClient, jsonPayload);
            httpResponse = ClientResponses.buildResponse(httpRequest, httpClient, selectedMethod);
            responseInfo = ClientResponses.getResponse(requestURL);
            
         	// Update response labels with received information
            codeLabel.setText("Response Code: " + httpResponse.statusCode());
            messageLabel.setText("Response Message: " + responseInfo.getResponseMessage());
            bodyLabel.setText("Response Body: " + httpResponse.body());

        } catch (URISyntaxException | MalformedURLException e) {
            e.printStackTrace();
        } 
    }
	
	
	/**
	 * Updates the labels in the response panel of the GUI.
	 * This method creates JLabel instances for displaying response code, message, and body.
	 * It sets the layout of the response panel to BoxLayout and adds the labels to the panel
	 * in a vertical arrangement.
	 */
	
	private static void updateLabels() {
        codeLabel = new JLabel("Response Code: ");
        messageLabel = new JLabel("Response Message: ");
        bodyLabel = new JLabel("Response Body: ");

        responsePanel.setLayout(new BoxLayout(responsePanel, BoxLayout.Y_AXIS));
        responsePanel.add(codeLabel);
        responsePanel.add(messageLabel);
        responsePanel.add(bodyLabel);
	}
	
}
