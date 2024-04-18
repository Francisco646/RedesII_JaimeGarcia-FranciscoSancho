package clientPack;

import java.awt.GridBagConstraints;
import java.awt.Insets;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;


/**
 * This class provides methods for configuring the layout of UI components within the client GUI.
 * It includes methods for arranging components such as URL label, URL field, HTTP methods combo box,
 * send button, and response panel using GridBagConstraints for layout.
 */

public class ClientLayoutItems {
	
	private static JPanel responsePanel;
	
	
	/**
	 * Sets the layout position of UI components within the clientMainPanel of the GUI.
	 * This method arranges the UI components including the URL label, URL field, HTTP methods combo box,
	 * send button, and response panel within the clientMainPanel using GridBagConstraints for layout.
	 *
	 * @param clientMainPanel       The JPanel where the UI components will be arranged.
	 * @param enterUrlLabel         The JLabel for entering the URL.
	 * @param urlField              The JTextField for entering the URL.
	 * @param httpMethodsComboBox   The JComboBox for selecting HTTP methods.
	 * @param sendButton            The JButton for sending the HTTP request.
	 * @param respPanel             The JPanel for displaying HTTP response information.
	 */
	
	protected static void setItemsLayoutPosition(JPanel clientMainPanel, JLabel enterUrlLabel, 
			JTextField urlField, JComboBox<String> httpMethodsComboBox, JButton sendButton, JPanel respPanel) {
		
		responsePanel = respPanel;
		
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
    
	
	/**
	 * Configures the layout of components for entering JSON payload within the payloadPanel.
	 * This method arranges the label, text field, and button for entering and sending JSON payload
	 * within the specified payloadPanel using GridBagConstraints for layout.
	 *
	 * @param label         The JLabel for describing the purpose of the text field.
	 * @param textField     The JTextField for entering the JSON payload.
	 * @param button        The JButton for sending the JSON payload.
	 * @param payloadPanel  The JPanel where the components will be arranged.
	 */
	
    protected static void configurePayloadLayout(JLabel label, JTextField textField, JButton button, JPanel payloadPanel) {
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
		
}
