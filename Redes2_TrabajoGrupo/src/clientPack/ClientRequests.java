package clientPack;

import java.net.http.HttpClient;
import java.net.http.HttpRequest;

import java.util.Map;

/**
 * This class handles building HTTP requests, including specifying the URL, HTTP method, and additional headers.
 */

public class ClientRequests {
	
	// Attributes
	private static HttpRequest clientRequest;
	
	
	/**
	 * Constructs and returns an HttpRequest object based on the specified URL, HTTP method, and additional headers.
	 *
	 * @param url               The URL to which the request will be sent.
	 * @param selectedMethod    The HTTP method to be used for the request (GET, HEAD, POST, PUT, DELETE).
	 * @param requestingClient  The HttpClient used to execute the request.
	 * @return                  An HttpRequest object representing the constructed request.
	 */
	
	protected static HttpRequest buildRequest(String url, String selectedMethod, 
			HttpClient requestingClient, String jsonPayload, Map <String, String> additionalHeaders) {
		
		// Handle required HTTP methods
		switch (selectedMethod){
			case "GET":
				clientRequest = ClientRequestMethods.buildGetRequest(url, requestingClient, additionalHeaders);
				break;
				
			case "HEAD":
				clientRequest = ClientRequestMethods.buildHeadRequest(url, requestingClient, additionalHeaders);
				break;
				
			case "POST":
				clientRequest = ClientRequestMethods.buildPostRequest(url, requestingClient, additionalHeaders, jsonPayload);
				break;
				
			case "PUT":
				clientRequest = ClientRequestMethods.buildPutRequest(url, requestingClient, additionalHeaders, jsonPayload);
				break;
				
			case "DELETE":
				clientRequest = ClientRequestMethods.buildDeleteRequest(url, requestingClient, additionalHeaders);
				break;
				
			default:
				// Client is unable to set another method, so nothing is done here
			
		}
		
		return clientRequest;
	}

}
