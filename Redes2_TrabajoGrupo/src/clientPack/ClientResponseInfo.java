package clientPack;


/**
 * This class represents information about an HTTP response.
 * It includes fields for the response code, message, body, and any error message.
 */

public class ClientResponseInfo {

	private int responseCode;
    private String responseMessage;
    private String responseBody;
    private String errorMessage;
    
    // Getters and Setters for response fields

    public int getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(int responseCode) {
        this.responseCode = responseCode;
    }

    public String getResponseMessage() {
        return responseMessage;
    }

    public void setResponseMessage(String responseMessage) {
        this.responseMessage = responseMessage;
    }

    public String getResponseBody() {
        return responseBody;
    }

    public void setResponseBody(String responseBody) {
        this.responseBody = responseBody;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
	
}
