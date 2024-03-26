package clientPack;

import java.net.URI;
import java.net.http.HttpRequest;
import java.net.http.HttpRequest.BodyPublishers;
import java.util.Scanner;

public class ClientRequests {
	
	private static HttpRequest clientRequest;
	
	/**
	* Builds an HttpRequest based on the specified HTTP method.
	*
	* @param url the URL for the request
	* @param selectedMethod the HTTP method to be used (GET, HEAD, POST, PUT, DELETE)
	* @return the HttpRequest object based on the specified method and URL
	* @throws MethodNotAllowedException when the selected HTTP method is not permitted
	*/
	
	public static HttpRequest buildRequest(String url, String selectedMethod) throws MethodNotAllowedException {
		
		// Handle required HTTP methods
		switch (selectedMethod){
			case "GET":
				buildGetRequest(url);
				break;
				
			case "HEAD":
				buildHeadRequest(url);
				break;
				
			case "POST":
				buildPostRequest(url);
				break;
				
			case "PUT":
				buildPutRequest(url);
				break;
				
			case "DELETE":
				buildDeleteRequest(url);
				break;
				
			default:
				throw new MethodNotAllowedException("The method " + selectedMethod + " is not permitted in HTTP");
			
		}
		
		return clientRequest;
	}
	
	
	/**
	* Builds an HTTP GET request.
	* 
	* @param url the URL for the request
	*/
	
	private static void buildGetRequest(String url) {
		clientRequest = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .GET()
                .build();
	}
	
	
	/**
	* Builds an HTTP HEAD request.
	* 
	* @param url the URL for the request
	*/
	
	private static void buildHeadRequest(String url) {
		clientRequest = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .method("HEAD", HttpRequest.BodyPublishers.noBody())
                .build();
	}
	
	/**
	* Builds an HTTP POST request.
	* 
	* @param url the URL for the request
	*/
	
	private static void buildPostRequest(String url) {
		
		// Selecting a JSON payload to be included in the POST request
		@SuppressWarnings("resource")
		Scanner postScanner = new Scanner(System.in);
		System.out.println("Enter the JSON payload for the POST request body: ");
		String postRequestBody = postScanner.nextLine();
		
		// Doing the request
		clientRequest = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Content-Type", "application/json")
                .POST(BodyPublishers.ofString(postRequestBody))	
                .build();
	}
	
	
	/**
	* Builds an HTTP PUT request.
	* 
	* @param url the URL for the request
	*/
	
	private static void buildPutRequest(String url) {
		
		// Selecting a JSON payload to be included in the PUT request
		@SuppressWarnings("resource")
		Scanner putScanner = new Scanner(System.in);
		System.out.println("Enter the JSON payload for the PUT request body: ");
		String putRequestBody = putScanner.nextLine();
		
		
		// Doing the request
		clientRequest = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Content-Type", "application/json")
                .PUT(BodyPublishers.ofString(putRequestBody))
                .build();
	}
	
	
	/**
	* Builds an HTTP DELETE request.
	* 
	* @param url the URL for the request
	*/
	
	private static void buildDeleteRequest(String url) {
		clientRequest = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .DELETE()
                .build();
	}
	
}
