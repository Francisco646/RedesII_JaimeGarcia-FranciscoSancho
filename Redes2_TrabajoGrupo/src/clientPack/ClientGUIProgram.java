package clientPack;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

@SuppressWarnings("serial")
public class ClientGUIProgram extends JFrame {

    private JFrame clientFrame;
    private JPanel clientMainPanel;
    private JPanel payloadPanel;
    private JPanel responsePanel;

    private JTextField urlField;
    private JComboBox<String> httpMethodsComboBox;

    private JLabel enterUrlLabel;
    private JButton sendButton;
    private String jsonPayload;

    private String requestingURL;
    private String selectedMethod;
    private URI requestURI;
    private URL requestURL;

    private HttpClient httpClient;
    private HttpRequest httpRequest;
    private HttpResponse<String> httpResponse;
    private ClientResponseInfo responseInfo;
    
    private JLabel codeLabel;
    private JLabel messageLabel;
    private JLabel bodyLabel;

    public ClientGUIProgram() {
        configureHttpClient();
        manageGUI();
    }

    private void configureHttpClient() {
        // Creates a client to be used during the session
        httpClient = HttpClient.newBuilder()
                .version(HttpClient.Version.HTTP_1_1)
                .connectTimeout(Duration.ofSeconds(10))
                .build();
    }

    private void manageGUI() {
        clientFrame = new JFrame("HTTP connection manager");
        clientFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        clientFrame.setSize(600, 300);
        clientFrame.setLocationRelativeTo(null);

        clientMainPanel = new JPanel(new GridBagLayout());
        responsePanel = new JPanel();

        enterUrlLabel = new JLabel();
        enterUrlLabel.setText("Enter an URL to be requested");

        urlField = new JTextField();
        urlField.setPreferredSize(new Dimension(400, 40));

        String[] httpMethods = {"GET", "HEAD", "POST", "PUT", "DELETE"};
        httpMethodsComboBox = new JComboBox<>(httpMethods);
        httpMethodsComboBox.setPreferredSize(new Dimension(75, 25));
       
        sendButton = new JButton("Send");
        sendButton.addActionListener(e -> {
            
        	selectedMethod = (String) httpMethodsComboBox.getSelectedItem();
        	
        	if (selectedMethod.equals("POST") || selectedMethod.equals("PUT")) {
                configureJsonPayload();
            } else {
                jsonPayload = "";
                processRequest();
            }
        	
        });
        
        configureResponseItems();
        setItemsLayoutPosition();
        
        clientFrame.add(clientMainPanel);
        clientFrame.setVisible(true);
    }

    private void processRequest() {
        try {
            requestURI = new URI("https://" + urlField.getText());
            requestingURL = requestURI.toString();
            requestURL = requestURI.toURL();
            
            httpRequest = ClientRequests.buildRequest(requestingURL, selectedMethod, httpClient, jsonPayload);
            httpResponse = ClientResponses.buildResponse(httpRequest, httpClient, selectedMethod);
            responseInfo = ClientResponses.getResponse(requestURL);
            
            // Update response labels with received information
            updateResponseLabels(httpResponse.statusCode(), responseInfo.getResponseMessage(), httpResponse.body());

        } catch (URISyntaxException | MalformedURLException e) {
            e.printStackTrace();
        } catch (MethodNotAllowedException e) {
            e.printStackTrace();
        }
    }

    private void configureJsonPayload() {
        // Create the frame for configuring JSON payload
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Configure JSON payload");
            frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            frame.setSize(400, 200);
            frame.setLocationRelativeTo(clientFrame);

            payloadPanel = new JPanel(new GridBagLayout());

            JLabel label = new JLabel("Enter the JSON payload for your request");
            JTextField textField = new JTextField();
            JButton button = new JButton("Send payload");
            button.addActionListener(event -> {
                jsonPayload = textField.getText();
                processRequest(); // Process request after payload is configured
                frame.dispose();
            });

            configurePayloadLayout(label, textField, button);

            frame.add(payloadPanel);
            frame.setVisible(true);
        });
    }
    
    private void configureResponseItems() {
    	
    	// Create labels for response information
        codeLabel = new JLabel("Response Code: ");
        messageLabel = new JLabel("Response Message: ");
        bodyLabel = new JLabel("Response Body: ");

        // Set layout and add labels to the responsePanel
        responsePanel.setLayout(new BoxLayout(responsePanel, BoxLayout.Y_AXIS));
        responsePanel.add(codeLabel);
        responsePanel.add(messageLabel);
        responsePanel.add(bodyLabel);
    }
    
    private void setItemsLayoutPosition() {
    	GridBagConstraints gbc = new GridBagConstraints();
    	gbc.fill = GridBagConstraints.HORIZONTAL;
    	gbc.insets = new Insets(10, 10, 10, 10);

    	// enterUrlLabel
    	gbc.gridx = 0;
    	gbc.gridy = 0;
    	gbc.gridwidth = 3;
    	clientMainPanel.add(enterUrlLabel, gbc);

    	// urlField
    	gbc.gridx = 0;
    	gbc.gridy = 1;
    	gbc.gridwidth = 1;
    	gbc.weightx = 0.67; // Occupies 2/3 of the width
	   	clientMainPanel.add(urlField, gbc);

	   	// httpMethodsComboBox
	   	gbc.gridx = 1;
	    gbc.gridwidth = 1;
	    gbc.weightx = 0.33; // Occupies 1/3 of the width
	   	clientMainPanel.add(httpMethodsComboBox, gbc);

	   	// sendButton
	   	gbc.gridy = 2;
	    gbc.gridx = 0;
	    gbc.gridwidth = 2;
	   	gbc.anchor = GridBagConstraints.CENTER; // Center the button
	   	clientMainPanel.add(sendButton, gbc);
	   	
	   	// responsePanel
	    gbc.gridy = 3;
	    gbc.gridx = 0;
	    gbc.gridwidth = 3;
	    gbc.anchor = GridBagConstraints.NORTHWEST; // Align responsePanel to top-left
	    clientMainPanel.add(responsePanel, gbc);
    }
    
    private void configurePayloadLayout(JLabel label, JTextField textField, JButton button) {
    	GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 10, 10, 10);

        // Label
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.CENTER; // Center the label
        payloadPanel.add(label, gbc);

        // Text field
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        gbc.weightx = 0.67; // Occupies 2/3 of the width
        payloadPanel.add(textField, gbc);

        // Button
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        gbc.weightx = 0.33; // Occupies 1/3 of the width
        payloadPanel.add(button, gbc);
    }
    
    private void updateResponseLabels(int code, String message, String body) {
        codeLabel.setText("Response Code: " + code);
        messageLabel.setText("Response Message: " + message);
        bodyLabel.setText("Response Body: " + body);
    }

}