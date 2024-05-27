package clientPack;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.net.HttpCookie;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.util.List;
import java.util.Map;


/**
 * This class provides methods for handling HTTP responses in the client application.
 * It includes methods for sending HTTP requests using various methods and retrieving the corresponding responses.
 */

public class ClientResponses {

	// Attributes
	private static HttpResponse<String> clientResponse;
	private static HttpURLConnection connection;
	private static URL url;
	private static String usedMethod;
	private static ClientResponseInfo responseInfo;
	
	private static Map<String, List<String>> urlCookies;
	private static List<String> urlCookieList;
	private static CookieManager cookieManager;
	
	static {
        // Set up a CookieManager to handle cookies
        cookieManager = new CookieManager();
        cookieManager.setCookiePolicy(CookiePolicy.ACCEPT_ALL);
        CookieHandler.setDefault(cookieManager);
    }
	
	
	/**
	 * Send an HTTP request using the specified method and return the corresponding HTTP response.
	 *
	 * @param inputRequest    The HTTP request to be sent.
	 * @param requestingClient   The HttpClient that will send the request.
	 * @param selectedMethod  The selected HTTP method (GET, HEAD, POST, PUT, DELETE).
	 * @return The HTTP response received from the server.
	 */
	
	protected static HttpResponse<String> buildResponse(HttpRequest inputRequest, HttpClient requestingClient, String selectedMethod) {
		
		usedMethod = selectedMethod;
		
		try {
			clientResponse = requestingClient.send(inputRequest, BodyHandlers.ofString());
		} catch (IOException | InterruptedException e) {
			e.getMessage();
		}
		
		return clientResponse;
	}
	
	
	/**
	 * Open a connection to the specified URL, send a request, and retrieve the HTTP response.
	 *
	 * @param url The URL to which the HTTP request will be sent.
	 * @return The HttpURLConnection representing the connection to the specified URL.
	 */
	
	protected static ClientResponseInfo getResponse(URL requestURL) {
		responseInfo = new ClientResponseInfo();
		url = requestURL;

	    try {
	    	
	    	// Open the connection using the requested method
	    	connection = (HttpURLConnection) url.openConnection();
	        connection.setRequestMethod(usedMethod);
	        
	        // Adding cookies to request
	        addCookiesToRequest();

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
	            
	            // Update cookies from the response
                updateCookiesFromResponse(connection);
	            
	        } catch (IOException ex) {
	            responseInfo.setErrorMessage("Error reading response body: " + ex.getMessage());
	        }

	    } catch (IOException e) {
	        responseInfo.setErrorMessage("Error connecting to URL: " + e.getMessage());
	    } finally {
	        if (connection != null) {
	            connection.disconnect();
	        }
	    }

	    return responseInfo;
	}
	
	
	/**
	 * Adds the cookies received in the previous response to the current request.
	 *
	 * This method uses the default CookieManager to retrieve the stored cookies and
	 * sets them in the request headers of the provided {@link HttpURLConnection} object.
	 *
	 * This ensures that any cookies received in the previous response are included in the
	 * subsequent requests, implementing the persistent cookie functionality.
	 *
	 * @param connection The {@link HttpURLConnection} object for which the cookies need to be added.
	 */
	
	static void addCookiesToRequest() {
	    try {
	        // Get the stored cookies from the CookieManager
	        List<HttpCookie> cookies = cookieManager.getCookieStore().getCookies();
	        StringBuilder cookieHeader = new StringBuilder();
	        for (HttpCookie cookie : cookies) {
	            if (cookieHeader.length() > 0) {
	                cookieHeader.append("; ");
	            }
	            cookieHeader.append(cookie.getName()).append("=").append(cookie.getValue());
	        }

	        // Set the "Cookie" header in the request
	        connection.setRequestProperty("Cookie", cookieHeader.toString());
	    } catch (IllegalStateException e) {
	        System.err.println("Error setting cookies in the request: " + e.getMessage());
	    }
	}

	
	/**
	 * Updates the CookieManager cookies received in the response.
	 *
	 * This method extracts the "Set-Cookie" headers from the header fields of the
	 * provided {@link HttpURLConnection} object and adds them to the default CookieManager.
	 *
	 * This ensures that the cookies are stored and can be used in subsequent requests.
	 *
	 * @param connection The {@link HttpURLConnection} object from which the cookies need to be extracted.
	 */
	
	static void updateCookiesFromResponse(HttpURLConnection connection) {
	    try {
	        // Get the header fields from the HttpURLConnection
	        Map<String, List<String>> headers = connection.getHeaderFields();

	        // Extract the "Set-Cookie" headers from the header fields
	        List<String> setCookieHeaders = headers.get("Set-Cookie");

	        // Add the cookies to the CookieManager
	        if (setCookieHeaders != null && !setCookieHeaders.isEmpty()) {
	            for (String setCookieHeader : setCookieHeaders) {
	                if (!setCookieHeader.trim().isEmpty()) {
	                    List<HttpCookie> parsedCookies = HttpCookie.parse(setCookieHeader);
	                    if (!parsedCookies.isEmpty()) {
	                        for (HttpCookie cookie : parsedCookies) {
	                            cookieManager.getCookieStore().add(null, cookie);
	                            System.out.println(cookie);
	                        }
	                    }
	                }
	            }
	        }
	    } catch (IllegalStateException e) {
	        // Handle the IllegalStateException when adding cookies to the CookieManager
	        System.err.println("Error updating cookies in the CookieManager: " + e.getMessage());
	    }
	}
	
}
