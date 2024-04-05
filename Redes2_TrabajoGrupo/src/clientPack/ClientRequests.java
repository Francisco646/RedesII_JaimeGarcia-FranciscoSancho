package clientPack;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpRequest.Builder;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class ClientRequests {
	
	// Attributes
	private static HttpRequest clientRequest;
	private static ArrayList<String> requestHeadersList = new ArrayList<>();
	private static Scanner s1 = new Scanner(System.in);
	
	
	/**
	 * Constructs and returns an HttpRequest object based on the specified URL, HTTP method, and additional headers.
	 *
	 * @param url               The URL to which the request will be sent.
	 * @param selectedMethod    The HTTP method to be used for the request (GET, HEAD, POST, PUT, DELETE).
	 * @param requestingClient  The HttpClient used to execute the request.
	 * @return                  An HttpRequest object representing the constructed request.
	 * @throws MethodNotAllowedException If the specified HTTP method is not permitted.
	 */
	
	protected static HttpRequest buildRequest(String url, String selectedMethod, HttpClient requestingClient) throws MethodNotAllowedException {
		
		Map<String, String> additionalHeaders = new HashMap<>();
		boolean moreHeaders = true;
		
		// Create and show list of headers, to facilitate user the choosing of new headers
		createHeadersList();
		for(String header : requestHeadersList) {
			System.out.println(header);
		}
		
		while(moreHeaders) {
			System.out.println("Name of header: ");
			String headerName = s1.nextLine();
			System.out.println("Value of header: ");
			String headerValue = s1.nextLine();
			
			additionalHeaders.put(headerName, headerValue);
			System.out.println("Do you want to add more headers? (true/false): ");
			moreHeaders = "true".equalsIgnoreCase(s1.nextLine()); // Allow the user to input 'true' or 'false' to continue adding headers
		}
		
		// Handle required HTTP methods
		switch (selectedMethod){
			case "GET":
				buildGetRequest(url, requestingClient, additionalHeaders);
				break;
				
			case "HEAD":
				buildHeadRequest(url, requestingClient, additionalHeaders);
				break;
				
			case "POST":
				buildPostRequest(url, requestingClient, additionalHeaders);
				break;
				
			case "PUT":
				buildPutRequest(url, requestingClient, additionalHeaders);
				break;
				
			case "DELETE":
				buildDeleteRequest(url, requestingClient, additionalHeaders);
				break;
				
			default:
				throw new MethodNotAllowedException("The method " + selectedMethod + " is not permitted in HTTP");
			
		}
		
		return clientRequest;
	}
	
	private static void createHeadersList() {
		requestHeadersList.add("Accept");
		requestHeadersList.add("Accept-Charset");
		requestHeadersList.add("Accept-Encoding");
		requestHeadersList.add("Authorization");
		requestHeadersList.add("Cache-Control");
		requestHeadersList.add("Content-Type");
		requestHeadersList.add("Content-Length");
		requestHeadersList.add("Cookie");
		requestHeadersList.add("User-Agent");
		requestHeadersList.add("Connection");
		requestHeadersList.add("Date");
		requestHeadersList.add("Host");
		requestHeadersList.add("If-Modified-Since");
		requestHeadersList.add("If-None-Match");
		requestHeadersList.add("Referer");
		requestHeadersList.add("Origin");
		requestHeadersList.add("X-Forwarded-For");
		requestHeadersList.add("X-Requested-With");
	}
	
	
	/**
	 * Constructs and builds an HTTP GET request with the specified URL, HttpClient, and additional user headers.
	 *
	 * @param url               The URL for the GET request.
	 * @param requestingClient  The HttpClient to be used for the request.
	 * @param userHeaders       Additional headers provided by the user for customization.
	 */
	
	private static void buildGetRequest(String url, HttpClient requestingClient, Map<String, String> userHeaders) {
		
		// Doing the request
		HttpRequest.Builder requestBuilder = (Builder) HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("User-Agent", requestingClient.toString())
                .header("Accept-Language", "en-US")
                .header("Cache-Control", "no-cache")
                .header("Connection", "keep-alive")
                .header("Date", ZonedDateTime.now().format(DateTimeFormatter.RFC_1123_DATE_TIME))
                .GET();
		
		// Add the headers entered by user, and build the request
		userHeaders.forEach(requestBuilder::header);
		clientRequest = requestBuilder.build();
	}
	
	
	/**
	 * Constructs and builds an HTTP HEAD request with the specified URL, HttpClient, and additional user headers.
	 *
	 * @param url               The URL for the HEAD request.
	 * @param requestingClient  The HttpClient used to execute the request.
	 * @param userHeaders       Additional headers provided by the user for customization.
	 */
	
	private static void buildHeadRequest(String url, HttpClient requestingClient, Map<String, String> userHeaders) {
		
		// Doing the request
		HttpRequest.Builder requestBuilder = (Builder) HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("User-Agent", requestingClient.toString())
                .header("Accept-Language", "en-US")
                .header("Cache-Control", "no-cache")
                .header("Connection", "keep-alive")
                .header("Date", ZonedDateTime.now().format(DateTimeFormatter.RFC_1123_DATE_TIME))
                .method("HEAD", HttpRequest.BodyPublishers.noBody());
		
		// Add the headers entered by user, and build the request
		userHeaders.forEach(requestBuilder::header);
		clientRequest = requestBuilder.build();
	}
	
	
	/**
	 * Constructs and builds an HTTP POST request with the specified URL, HttpClient, JSON payload, and additional user headers.
	 *
	 * @param url               The URL for the POST request.
	 * @param requestingClient  The HttpClient used to execute the request.
	 * @param userHeaders       Additional headers provided by the user for customization.
	 */
	
	private static void buildPostRequest(String url, HttpClient requestingClient, Map<String, String> userHeaders) {
		
		// Selecting a JSON payload to be included in the POST request
		@SuppressWarnings("resource")
		Scanner postScanner = new Scanner(System.in);
		System.out.println("Enter the JSON payload for the POST request body: ");
		String postRequestBody = postScanner.nextLine();
		
		// Doing the request
		HttpRequest.Builder requestBuilder = (Builder) HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Content-Type", "application/json")
                .header("Accept", "application/json")
                .header("User-Agent", requestingClient.toString())
                .header("Accept-Language", "en-US")
                .header("Cache-Control", "no-cache")
                .header("Connection", "keep-alive")
                .header("Date", ZonedDateTime.now().format(DateTimeFormatter.RFC_1123_DATE_TIME))
                .header("Content-Length", String.valueOf(postRequestBody.length()))
                .POST(BodyPublishers.ofString(postRequestBody));
		
		// Add the headers entered by user, and build the request
		userHeaders.forEach(requestBuilder::header);
		clientRequest = requestBuilder.build();
	}
	
	
	/**
	 * Constructs and builds an HTTP PUT request with the specified URL, HttpClient, JSON payload, and additional user headers.
	 *
	 * @param url               The URL for the PUT request.
	 * @param requestingClient  The HttpClient used to execute request.
	 * @param userHeaders       Additional headers provided by the user for customization.
	 */
	
	private static void buildPutRequest(String url, HttpClient requestingClient, Map<String, String> userHeaders) {
		
		// Selecting a JSON payload to be included in the PUT request
		@SuppressWarnings("resource")
		Scanner putScanner = new Scanner(System.in);
		System.out.println("Enter the JSON payload for the PUT request body: ");
		String putRequestBody = putScanner.nextLine();
		
		
		// Doing the request
		HttpRequest.Builder requestBuilder = (Builder) HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Content-Type", "application/json")
                .header("Accept", "application/json")
                .header("User-Agent", requestingClient.toString())
                .header("Accept-Language", "en-US")
                .header("Cache-Control", "no-cache")
                .header("Connection", "keep-alive")
                .header("Date", ZonedDateTime.now().format(DateTimeFormatter.RFC_1123_DATE_TIME))
                .header("Content-Length", String.valueOf(putRequestBody.length()))
                .PUT(BodyPublishers.ofString(putRequestBody));
		
		// Add the headers entered by user, and build the request
		userHeaders.forEach(requestBuilder::header);
		clientRequest = requestBuilder.build();
	}
	
	
	/**
	 * Constructs and builds an HTTP DELETE request with the specified URL, HttpClient, and additional user headers.
	 *
	 * @param url               The URL for the DELETE request.
	 * @param requestingClient  The HttpClient used to execute the request.
	 * @param userHeaders       Additional headers provided by the user for customization.
	 */
	
	private static void buildDeleteRequest(String url, HttpClient requestingClient, Map<String, String> userHeaders) {
		
		// Doing the request
		HttpRequest.Builder requestBuilder = (Builder) HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("User-Agent", requestingClient.toString())
                .header("Accept-Language", "en-US")
                .header("Cache-Control", "no-cache")
                .header("Connection", "keep-alive")
                .header("Date", ZonedDateTime.now().format(DateTimeFormatter.RFC_1123_DATE_TIME))
                .DELETE();
		
		// Add the headers entered by user, and build the request
		userHeaders.forEach(requestBuilder::header);
		clientRequest = requestBuilder.build();
	}
	
}
