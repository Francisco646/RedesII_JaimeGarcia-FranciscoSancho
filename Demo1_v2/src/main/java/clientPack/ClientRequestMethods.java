package clientPack;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpRequest.Builder;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;


/**
 * This class provides methods to construct and build various types of HTTP requests, such as GET, HEAD, POST, PUT, and DELETE.
 */

public class ClientRequestMethods {

	private static HttpRequest clientRequest;
	
	
	/**
	 * Constructs and builds an HTTP GET request with the specified URL, HttpClient, and additional user headers.
	 *
	 * @param url               The URL for the GET request.
	 * @param requestingClient  The HttpClient to be used for the request.
	 * @param userHeaders       Additional headers provided by the user for customization.
	 */
	
	protected static HttpRequest buildGetRequest(String url, HttpClient requestingClient, Map<String, String> userHeaders) {
		
		// Doing the request
		HttpRequest.Builder requestBuilder = (Builder) HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("User-Agent", requestingClient.toString())
                .header("Accept-Language", "en-US")
                .header("Cache-Control", "no-cache")
                .header("Date", ZonedDateTime.now().format(DateTimeFormatter.RFC_1123_DATE_TIME))
                .GET();
		
		// Add the headers entered by user, and build the request
		userHeaders.forEach(requestBuilder::header);
		clientRequest = requestBuilder.build();
		return clientRequest;
	}
	
	
	/**
	 * Constructs and builds an HTTP HEAD request with the specified URL, HttpClient, and additional user headers.
	 *
	 * @param url               The URL for the HEAD request.
	 * @param requestingClient  The HttpClient used to execute the request.
	 * @param userHeaders       Additional headers provided by the user for customization.
	 */
	
	protected static HttpRequest buildHeadRequest(String url, HttpClient requestingClient, Map<String, String> userHeaders) {
		
		// Doing the request
		HttpRequest.Builder requestBuilder = (Builder) HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("User-Agent", requestingClient.toString())
                .header("Accept-Language", "en-US")
                .header("Cache-Control", "no-cache")
                .header("Date", ZonedDateTime.now().format(DateTimeFormatter.RFC_1123_DATE_TIME))
                .method("HEAD", HttpRequest.BodyPublishers.noBody());
		
		// Add the headers entered by user, and build the request
		userHeaders.forEach(requestBuilder::header);
		clientRequest = requestBuilder.build();
		return clientRequest;
	}
	
	
	/**
	 * Constructs and builds an HTTP POST request with the specified URL, HttpClient, JSON payload, and additional user headers.
	 *
	 * @param url               The URL for the POST request.
	 * @param requestingClient  The HttpClient used to execute the request.
	 * @param userHeaders       Additional headers provided by the user for customization.
	 */
	
	protected static HttpRequest buildPostRequest(String url, HttpClient requestingClient, Map<String, String> userHeaders, String postRequestBody) {
		
		// Doing the request
		HttpRequest.Builder requestBuilder = (Builder) HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Content-Type", "application/json")
                .header("Accept", "application/json")
                .header("User-Agent", requestingClient.toString())
                .header("Accept-Language", "en-US")
                .header("Cache-Control", "no-cache")
                .header("Date", ZonedDateTime.now().format(DateTimeFormatter.RFC_1123_DATE_TIME))
                .POST(BodyPublishers.ofString(postRequestBody));
		
		// Add the headers entered by user, and build the request
		userHeaders.forEach(requestBuilder::header);
		clientRequest = requestBuilder.build();
		return clientRequest;
	}
	
	
	/**
	 * Constructs and builds an HTTP PUT request with the specified URL, HttpClient, JSON payload, and additional user headers.
	 *
	 * @param url               The URL for the PUT request.
	 * @param requestingClient  The HttpClient used to execute request.
	 * @param userHeaders       Additional headers provided by the user for customization.
	 */
	
	protected static HttpRequest buildPutRequest(String url, HttpClient requestingClient, Map<String, String> userHeaders, String putRequestBody) {
		
		// Doing the request
		HttpRequest.Builder requestBuilder = (Builder) HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Content-Type", "application/json")
                .header("Accept", "application/json")
                .header("User-Agent", requestingClient.toString())
                .header("Accept-Language", "en-US")
                .header("Cache-Control", "no-cache")
                .header("Date", ZonedDateTime.now().format(DateTimeFormatter.RFC_1123_DATE_TIME))
                .PUT(BodyPublishers.ofString(putRequestBody));
		
		// Add the headers entered by user, and build the request
		userHeaders.forEach(requestBuilder::header);
		clientRequest = requestBuilder.build();
		return clientRequest;
	}
	
	
	/**
	 * Constructs and builds an HTTP DELETE request with the specified URL, HttpClient, and additional user headers.
	 *
	 * @param url               The URL for the DELETE request.
	 * @param requestingClient  The HttpClient used to execute the request.
	 * @param userHeaders       Additional headers provided by the user for customization.
	 */
	
	protected static HttpRequest buildDeleteRequest(String url, HttpClient requestingClient, Map<String, String> userHeaders) {
		
		// Doing the request
		HttpRequest.Builder requestBuilder = (Builder) HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("User-Agent", requestingClient.toString())
                .header("Accept-Language", "en-US")
                .header("Cache-Control", "no-cache")
                .header("Date", ZonedDateTime.now().format(DateTimeFormatter.RFC_1123_DATE_TIME))
                .DELETE();
		
		// Add the headers entered by user, and build the request
		userHeaders.forEach(requestBuilder::header);
		clientRequest = requestBuilder.build();
		return clientRequest;
	}
	
}
