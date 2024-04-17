package clientPack;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;

public class ClientResponses {

	// Attributes
	private static HttpResponse<String> clientResponse;
	private static HttpURLConnection connection;
	private static String usedMethod;
	private static ClientResponseInfo responseInfo;
	
	
	/**
	 * Send an HTTP request using the specified method and return the corresponding HTTP response.
	 *
	 * @param inputRequest    The HTTP request to be sent.
	 * @param requestingClient   The HttpClient that will send the request.
	 * @param selectedMethod  The selected HTTP method (GET, HEAD, POST, PUT, DELETE).
	 * @return The HTTP response received from the server.
	 * @throws MethodNotAllowedException If the selected HTTP method is not allowed.
	 */
	
	protected static HttpResponse<String> buildResponse(HttpRequest inputRequest, HttpClient requestingClient, String selectedMethod) throws MethodNotAllowedException {
		
		usedMethod = selectedMethod;
		
		switch (selectedMethod){
		case "GET", "HEAD", "POST", "PUT", "DELETE":
			try {
				clientResponse = requestingClient.send(inputRequest, BodyHandlers.ofString());
			} catch (IOException | InterruptedException e) {
				e.getMessage();
			}
			break;
			
		default:
			throw new MethodNotAllowedException("The method " + selectedMethod + " is not permitted in HTTP");
		
		}
		
		return clientResponse;
	}
	
	
	/**
	 * Open a connection to the specified URL, send a request, and retrieve the HTTP response.
	 *
	 * @param url The URL to which the HTTP request will be sent.
	 * @return The HttpURLConnection representing the connection to the specified URL.
	 */
	
	protected static ClientResponseInfo getResponse(URL url) {
		responseInfo = new ClientResponseInfo();

	    try {
	        connection = (HttpURLConnection) url.openConnection();
	        connection.setRequestMethod(usedMethod);

	        // Set request headers if needed

	        // Get code and message of the response
	        int responseCode = connection.getResponseCode();
	        String responseMessage = connection.getResponseMessage();

	        // Read the response body
	        try (BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
	            StringBuilder response = new StringBuilder();
	            String inputLine;
	            while ((inputLine = in.readLine()) != null) {
	                response.append(inputLine);
	            }

	            responseInfo.setResponseCode(responseCode);
	            responseInfo.setResponseMessage(responseMessage);
	            responseInfo.setResponseBody(response.toString());
	        } catch (IOException ex) {
	            responseInfo.setErrorMessage("Error reading response body: " + ex.getMessage());
	        }

	        System.out.println("Response code: " + responseCode);
	        System.out.println("Response message: " + responseMessage);
	        System.out.println("Response body: " + responseInfo.getResponseBody());

	    } catch (IOException e) {
	        responseInfo.setErrorMessage("Error connecting to URL: " + e.getMessage());
	    } finally {
	        if (connection != null) {
	            connection.disconnect();
	        }
	    }

	    return responseInfo;
	}
	
}
