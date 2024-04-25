package clientPack;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.Map;

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
    private static Map<String, String> additionalHeaders;
    
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
	        configureClientSecondLayout();
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
	 * Configures the layout of the secondary client panel for configuring JSON payload and headers.
	 * This method sets up a frame with header and payload panels based on the selected HTTP method.
	 * When the client selects POST or PUT methods, both header and payload panels are displayed.
	 * 
	 * - The header panel includes fields for entering header name and value, with a button to send headers.
	 * - The payload panel allows the user to input the JSON payload for the request and a button to send it.
	 * 
	 * If the POST or PUT method is selected:
	 * - The header panel is placed at the top (North) of the frame.
	 * - The payload panel is placed in the center of the frame.
	 * 
	 * If other methods are selected:
	 * - Only the header panel is displayed at the top of the frame.
	 * 
	 * The "End configuration" button triggers the processing of the request and closes the frame.
	 */
	
	private static void configureClientSecondLayout() {
        // Create the frame for configuring JSON payload
        SwingUtilities.invokeLater(() -> {
            frame = new JFrame("Secondary panel");
            frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            frame.setSize(600, 300);
            frame.setLayout(new BorderLayout());
            frame.setLocationRelativeTo(clientFrame);
            
            // Header panel
            additionalHeaders = new HashMap<>();
            JPanel headerPanel = new JPanel(new GridBagLayout());
            JLabel headerNameLabel = new JLabel("Header name");
            JTextField headerNameTextField = new JTextField();
        	JLabel headerValueLabel = new JLabel("Header value");
        	JTextField headerValueTextField = new JTextField();
        	JButton sendHeaderButton = new JButton("Send header");
        	
        	sendHeaderButton.addActionListener(event -> {
        		String headerName = headerNameTextField.getText();
        		String headerValue = headerValueTextField.getText();
        		additionalHeaders.put(headerName, headerValue);
        	});
        	
        	ClientLayoutItems.configureHeaderPanel(headerNameLabel, headerNameTextField, 
        			headerValueLabel, headerValueTextField, sendHeaderButton, headerPanel);
            
        	// If client selects POST or PUT methods
            if(selectedMethod == "POST" || selectedMethod == "PUT") {
	            
            	// Payload panel
	        	JPanel payloadPanel = new JPanel(new GridBagLayout());
	            JLabel label = new JLabel("Enter the JSON payload for your request");
	            JTextField textField = new JTextField();
	            JButton payloadPanelButton = new JButton("Send payload");
            	
            	payloadPanelButton.addActionListener(event -> {
            		jsonPayload = textField.getText();
            	});
            	
            	ClientLayoutItems.configurePayloadPanel(label, textField, payloadPanelButton, payloadPanel);
            	
                frame.add(headerPanel, BorderLayout.NORTH);
                frame.add(payloadPanel, BorderLayout.CENTER);	            
            } else {
            	frame.add(headerPanel);
            }
            
            // General layout button
            JButton secLayoutButton = new JButton("End configuration");
            secLayoutButton.addActionListener(event -> {
            	processRequest(); 
	            frame.dispose();
            });
            
            frame.add(secLayoutButton, BorderLayout.SOUTH);
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
            httpRequest = ClientRequests.buildRequest(requestingURL, selectedMethod, httpClient, jsonPayload, additionalHeaders);
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
