package clientPack;

import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class ClientRequests {
	
	// Attributes
	private static HttpRequest clientRequest;
	private static ArrayList<String> requestHeadersList;
	private static Map<String, String> additionalHeaders;
	private static Scanner s1 = new Scanner(System.in);
	private static boolean moreHeaders;
	
	
	/**
	 * Constructs and returns an HttpRequest object based on the specified URL, HTTP method, and additional headers.
	 *
	 * @param url               The URL to which the request will be sent.
	 * @param selectedMethod    The HTTP method to be used for the request (GET, HEAD, POST, PUT, DELETE).
	 * @param requestingClient  The HttpClient used to execute the request.
	 * @return                  An HttpRequest object representing the constructed request.
	 * @throws MethodNotAllowedException If the specified HTTP method is not permitted.
	 */
	
	protected static HttpRequest buildRequest(String url, String selectedMethod, HttpClient requestingClient, String jsonPayload) throws MethodNotAllowedException {
		
		// Create the headers list if it is the first request
		if(additionalHeaders == null && requestHeadersList == null) {
			requestHeadersList = new ArrayList<>();
			createHeadersList();
		}
		
		// Managing extra headers
		additionalHeaders = new HashMap<>();
		addingExtraHeaders();
		
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
				throw new MethodNotAllowedException("The method " + selectedMethod + " is not permitted in HTTP");
			
		}
		
		return clientRequest;
	}
	
	/**
	 * Initializes the request headers list with several common HTTP headers.
	 * 
	 * This method populates the requestHeadersList with common HTTP headers
	 *
	 * Note: This method should be called to initialize the request headers list before making the HTTP request.
	 */
	
	private static void createHeadersList() {
		requestHeadersList.add("Accept - Specifies the media types that are acceptable for the response (\"application/json, text/plain, /\").");
		requestHeadersList.add("Accept-Charset - Indicates the character sets that are acceptable for the response (\"utf-8, iso-8859-1\").");
		requestHeadersList.add("Accept-Encoding - Defines the encoding formats that the client can process (\"gzip, deflate, br\").");
		requestHeadersList.add("Authorization - Contains credentials for authenticating the client with the server (\"Basic encoded_credentials\").");
		requestHeadersList.add("Cache-Control - Directives for caching mechanisms in both requests and responses (\"no-cache, max-age=3600\").");
		requestHeadersList.add("Content-Type - Describes the media type of the request body sent to the server (\"application/json; charset=utf-8\").");
		requestHeadersList.add("Content-Length - Indicates the size of the request body in bytes (\"1234\").");
		requestHeadersList.add("Cookie -  Contains stored HTTP cookies to be sent with the request (\"session_id=abc123\").");
		requestHeadersList.add("User-Agent - Provides information about the client application making the request (Mozilla, Chrome, Safari, etc).");
		requestHeadersList.add("Connection - Specifies preferences regarding the handling of the connection (\"keep-alive\").");
		requestHeadersList.add("Date - Sends the current date and time of the request (\"Mon, 08 Apr 2024 15:30:00 GMT\").");
		requestHeadersList.add("Host - Indicates the target host of the request (\"www.example.com\").");
		requestHeadersList.add("If-Modified-Since - Sends the date/time the client last received this resource (\"Sat, 06 Apr 2024 12:00:00 GMT\").");
		requestHeadersList.add("If-None-Match - Used to make a conditional request with an entity tag (\"W/\"123456789\"\").");
		requestHeadersList.add("Referer - Specifies the URL of the previous web page that linked to the request (\"http://www.previouspage.com\").");
		requestHeadersList.add("Origin - Indicates the server from which the request initiated (\"https://www.origin-server.com\").");
		requestHeadersList.add("X-Forwarded-For - Contains the client's original IP address in the case of proxies (\"client_ip_address\").");
		requestHeadersList.add("X-Requested-With - Used to identify Ajax requests (\"XMLHttpRequest\").");
	}
	
	
	/**
	* This method adds extra headers to the existing requestHeadersList based on user input.
	* It prompts the user to add their own headers and stores them in the additionalHeaders map.
	*/
	
	private static void addingExtraHeaders() {
		
		// Display existing headers
		for(String header : requestHeadersList) {
			System.out.println(header);
		}
		
		// Prompt user to add headers
		System.out.println("Do you want to add your own headers? (true/false): ");
		moreHeaders = s1.nextBoolean();
		
		// User input for additional headers
		while(moreHeaders) {
			System.out.println("Name of header: ");
			String headerName = s1.nextLine();
			System.out.println("Value of header: ");
			String headerValue = s1.nextLine();
			
			additionalHeaders.put(headerName, headerValue);
			System.out.println("Do you want to add more headers? (true/false): ");
			moreHeaders = "true".equalsIgnoreCase(s1.nextLine()); // Allow the user to input 'true' or 'false' to continue adding headers
		}
	}
	
}
