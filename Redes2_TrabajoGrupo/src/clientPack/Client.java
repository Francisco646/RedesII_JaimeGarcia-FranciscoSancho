package clientPack;

import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.Scanner;

public class Client {

	// Attributes
	private static String baseURI = "https://example.com";	// URI format
	private static String requestingURI;					// URI to be requested
	private static String selectedMethod;					// Method to be requested
	
	private static HttpClient httpClient;					// Current client
	private static HttpRequest httpRequest;					// Current request
	private static HttpResponse<String> httpResponse;		// Response of current request
	
	private static URI requestURI;							// Requested URI (in URI format)
	private static URL requestURL;							// Requested URI (in URL format)
	private static HttpURLConnection httpConnection;		// Opened connection to get the response
	
	private static boolean sendAnotherRequest;				// Allow client to make another request
	private static Scanner s1 = new Scanner(System.in);		// Allow client inputs
	
	
	/**
	 * Client-side program management function 
	 * Manages the client activities, performs various HTTP requests, and handles user interactions.
	 * 
	 * The process includes creating an HttpClient, selecting HTTP method, choosing a URL, creating an HTTP request, 
	 * receiving responses, and offering the option to continue with new requests.
	 * 
	 * @throws SomeException If there is an issue with client management.
	 */
	
	public void clientManagement() {
		
		sendAnotherRequest = true;
		
		do {
			
			createHttpClient();
			selectMethod();
			chooseURL();
			createHttpRequest();
			receiveResponses();
			sendAnotherRequest = keepProgramActive();
			
		} while(sendAnotherRequest);
		
	}
	

	/**
	 * Creates an HttpClient for performing HTTP requests. 
	 * Configures the HttpClient with HTTP/2 version and a connection timeout of 10 seconds.
	 * 
	 * @return An instance of HttpClient configured for making HTTP requests.
	 */
	
	private void createHttpClient() {
		httpClient = HttpClient.newBuilder()
		         .version(HttpClient.Version.HTTP_2)
		         .connectTimeout(Duration.ofSeconds(10))
		         .build();
	}

	
	/**
	 * Constructs an HTTP request based on the selected URI and HTTP method, using the built HttpClient.
	 * 
	 * @throws MethodNotAllowedException If the selected HTTP method is not allowed.
	 */
	
	private void createHttpRequest() {
		try {
			httpRequest = ClientRequests.buildRequest(requestingURI, selectedMethod, httpClient);
		} catch (MethodNotAllowedException e) {
			e.printStackTrace();
		}
	}
	
	
	/**
	 * Allows the client to choose a URL to be checked. 
	 * Retrieves and validates user input for the URL, and creates a URI and URL object based on the input.
	 * 
	 * @throws URISyntaxException If the input URL is in an invalid format.
	 * @throws MalformedURLException If the input URL cannot be converted to a URL object.
	 */
	
	private void chooseURL() {
		
		// Ask user for new URL
		System.out.println("Please enter your path route");
		String selectedURI = s1.nextLine();
		
		// Convert the entered data into URI format
		try {
			requestURI = new URI(baseURI + selectedURI);
			requestingURI = requestURI.toString(); // URI to be requested
			System.out.println("Entered URI: " + requestingURI);
		} catch(URISyntaxException e) {
			System.err.println("Invalid URI: " + e.getMessage());
		}
		
		try {
			requestURL = requestURI.toURL();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		
	}
	
	
	/**
	 * Allows the client to select an HTTP request method. 
	 * Retrieves and validates user input for the request method.
	 */
	
	private void selectMethod() {
		// Ask user for the request method
		System.out.println("Please enter a method for your request");
		selectedMethod = s1.nextLine();
	}

	
	/**
	 * Receives and handles HTTP responses using the previously created HTTP request, HttpClient, and HTTP method.
	 * 
	 * @throws MethodNotAllowedException If the selected HTTP method is not allowed.
	 */
	
	private void receiveResponses() {
		try {
			httpResponse = ClientResponses.buildResponse(httpRequest, httpClient, selectedMethod);
			httpConnection = ClientResponses.getResponse(requestURL);
		} catch (MethodNotAllowedException e) {
			e.printStackTrace();
		}
	}
	
	
	/**
	 * Allows the user to choose whether to send more requests or close the program.
	 * 
	 * @return True if the user wants to send a new request, false otherwise.
	 */
	
	private boolean keepProgramActive() {
		System.out.println("Do you want to send a new request (true/false)? :");
		sendAnotherRequest = s1.nextBoolean();
		return sendAnotherRequest;
	}

	
	// Getters
	public static HttpClient getHttpClient() {
		return httpClient;
	}

	public static HttpRequest getHttpRequest() {
		return httpRequest;
	}

	public static HttpResponse<String> getHttpResponse() {
		return httpResponse;
	}

	public static HttpURLConnection getHttpConnection() {
		return httpConnection;
	}

	public static URI getRequestURI() {
		return requestURI;
	}

	public static URL getRequestURL() {
		return requestURL;
	}
	
	/*
	// Enable client authentication --> Maybe in a class
	// Link --> https://www.digitalocean.com/community/tutorials/java-session-management-servlet-httpsession-url-rewriting 
	private void clientAuthentication() {
		
	}
	
	// Enable cookies management --> Maybe in a class
	// Link --> https://www.digitalocean.com/community/tutorials/java-session-management-servlet-httpsession-url-rewriting 
	private void manageCookies() {
		
	}
	*/
	
}
