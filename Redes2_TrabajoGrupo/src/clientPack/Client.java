package clientPack;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpClient.Version;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.Scanner;

public class Client {

	// Attributes
	//protected static final int PORT = 443;
	private static String baseURI = "https://example.com";	// URI format
	private static String selectedURI;						// URI entered by client
	private static String requestingURI;					// Converted URI
	private static String selectedMethod;
	
	private static HttpClient httpClient;
	private static HttpRequest httpRequest;
	
	// Program management function --> Look for concurrency issues and multithreading
	public void clientManagement() {
		createHttpClient();
	}
	
	// Create an HttpClient
	private void createHttpClient() {
		httpClient = HttpClient.newBuilder()
		         .version(HttpClient.Version.HTTP_2)
		         .connectTimeout(Duration.ofSeconds(10))
		         .build();
	}

	// Send request
	private void createHttpRequest() {
		try {
			httpRequest = ClientRequests.buildRequest(requestingURI, selectedMethod);
		} catch (MethodNotAllowedException e) {
			e.printStackTrace();
		}
	}
	
	// Enable client choosing an URL to be checked
	private void chooseURL() {
		
		// Ask user for new URL
		Scanner urlScanner = new Scanner(System.in);
		System.out.println("Please enter your path route");
		selectedURI = urlScanner.nextLine();
		
		// Convert the entered data into URI format
		try {
			URI auxURI = new URI(baseURI + selectedURI);
			requestingURI = auxURI.toString();
			System.out.println("Entered URI: " + requestingURI);
		} catch(URISyntaxException e) {
			System.err.println("Invalid URI: " + e.getMessage());
		}
		
	}
	
	private void selectMethod() {
		// Ask user for the request method
		Scanner methodScanner = new Scanner(System.in);
		System.out.println("Please enter a method for your request");
		selectedMethod = methodScanner.nextLine();
	}

	// Receive responses --> In another class
	private void receiveResponses() {
	
	}
	
	// Enable client authentication --> Maybe in a class
	private void clientAuthentication() {
		
	}
	
	// Enable cookies management --> Maybe in a class
	private void manageCookies() {
		
	}
	
}
