package serverPack;

import static spark.Spark.delete;

import static spark.Spark.get;
import static spark.Spark.port;
import static spark.Spark.post;
import static spark.Spark.put;
import static spark.Spark.staticFileLocation;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;


/**
 * The Server class represents a server that handles RESTful API requests for managing resources.
 * It extends the Thread class to run as a separate thread.
 */

public class Server extends Thread {

	private static final ServerActivityLogger logger = ServerActivityLogger.getInstance();
	private List<Resource> resources = new ArrayList<Resource>();
	
	
	/**
     * Constructs a new instance of the Server class.
     * Initializes the resources list and loads data from a JSON file if it exists.
     */

	public Server() {
		resources = new ArrayList<Resource>();
		// Load the resources list from JSON file if it exists
	    if (Files.exists(Paths.get("resources.json"))) {
	        Gson gson = new Gson();
	        try {
	            String json = Files.readString(Paths.get("resources.json"));
	            Type listType = new TypeToken<List<Resource>>(){}.getType();
	            resources = gson.fromJson(json, listType);
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	    }
	}
	
	
	/**
     * Starts the server on the specified port and defines various endpoints.
     */

	@Override
	public void run() {
		int port = 9092; // Puerto configurable

		List<Resource> resources = this.resources;

		// Configurar el puerto del servidor
		port(port);

		// Endpoint GET para servir un archivo HTML estático
		get("/hello", (req, res) -> {

			// Get the content so that user is able to see it on client side
			String htmlContent = "";
			htmlContent = new String(Files.readAllBytes(Paths.get("index.html")));

			logger.logRequest(req.requestMethod() + " " + req.pathInfo());
			logger.log("Response: " + htmlContent);
			return htmlContent;
		});

		// Endpoint GET para obtener todos los recursos
		get("/resources", (req, res) -> {

			Gson gson = new Gson();
			String json = gson.toJson(resources);
			logger.logRequest(req.requestMethod() + " " + req.pathInfo()); 
			logger.log("Response: " + json); 
			return json;
		});

		// Endpoint POST para agregar un nuevo recurso
		post("/resource", (req, res) -> {
			String requestBody = req.body(); 
			if (requestBody == null || requestBody.isEmpty()) {
				res.status(400); // Bad Request

				logger.logRequest(req.requestMethod() + " " + req.pathInfo()); 
				logger.log("Response: " + "400 Bad Request (empty body)"); 

				return "Cuerpo de la solicitud vacío";
			}

			Gson gson = new Gson();
			Resource newResource;
			try {
				newResource = gson.fromJson(requestBody, Resource.class); 
			} catch (Exception e) {
				res.status(400); // Bad Request
				logger.logRequest(req.requestMethod() + " " + req.pathInfo()); 
				logger.log("Response: " + "400 Bad Request (invalid json format)"); 
				return "Formato de JSON inválido";
			}

			// Agregar el nuevo recurso a la lista de recursos
			resources.add(newResource);
			saveResourcesToJson(resources);
			res.status(201); 

			logger.logRequest(req.requestMethod() + " " + req.pathInfo()); 
			logger.log("Response: " + "201 Recurso agregado correctamente: " + newResource.toString());

			return "Recurso agregado correctamente: " + newResource.toString();
		});

		// Endpoint PUT para modificar un recurso existente
		put("/resource/:id", (req, res) -> {
			String resourceId = req.params(":id"); 
			String requestBody = req.body(); 

			// Verificar si el ID del recurso es valido
			if (resourceId == null || resourceId.isEmpty()) {
				res.status(400); // Bad Request
				logger.logRequest(req.requestMethod() + " " + req.pathInfo()); 
				logger.log("Response: " + "400 Bad Request (Unvalid ID)"); 
				return "ID del recurso no válido";
			}

			// Verificar si el cuerpo de la solicitud es valido
			if (requestBody == null || requestBody.isEmpty()) {
				res.status(400); // Bad Request
				logger.logRequest(req.requestMethod() + " " + req.pathInfo()); 
				logger.log("Response: " + "400 Bad Request (empty body)"); 
				return "Cuerpo de la solicitud vac�o";
			}

			Gson gson = new Gson();
			Resource updatedResource;
			try {
				updatedResource = gson.fromJson(requestBody, Resource.class); 
			} catch (Exception e) {
				res.status(400); // Bad Request
				logger.logRequest(req.requestMethod() + " " + req.pathInfo()); 
				logger.log("Response: " + "400 Bad Request (invalid json format)"); 
				return "Formato de JSON invalido";
			}

			// Buscar el recurso correspondiente en la lista por su ID y actualizarlo
			for (Resource resource : resources) {
				if (resource.getId().equals(resourceId)) {

					resource.setName(updatedResource.getName());
					resource.setBreed(updatedResource.getBreed());
					resource.setAge(updatedResource.getAge());
					
					saveResourcesToJson(resources);
					res.status(200);

					logger.logRequest(req.requestMethod() + " " + req.pathInfo()); 
					logger.log("Response: " + "201 Recurso modificado correctamente: " + resourceId); 

					return "Recurso con ID " + resourceId + " modificado correctamente";
				}
			}

			// Si no se encuentra el recurso, devolver un mensaje de error
			res.status(404);

			logger.logRequest(req.requestMethod() + " " + req.pathInfo()); 
			logger.log("Response: " + "404 Error: Resource with ID " + resourceId + " not found"); 

			return "Recurso con ID " + resourceId + " no encontrado";
		});

		// Endpoint DELETE para eliminar un recurso existente
		delete("/resource/:id", (req, res) -> {
			String resourceId = req.params(":id"); 
			
			if (resourceId == null || resourceId.isEmpty()) {
				res.status(400); // Bad Request
				logger.logRequest(req.requestMethod() + " " + req.pathInfo()); 
				logger.log("Response: " + "400 Bad Request (invalid ID)"); 
				return "ID del recurso no valido";
			}

			boolean resourceRemoved = resources.removeIf(resource -> resource.getId().equals(resourceId));

			// Verificar si se elimina correctamente el recurso
			if (resourceRemoved) {
				res.status(200);
				saveResourcesToJson(resources);
				logger.logRequest(req.requestMethod() + " " + req.pathInfo()); 
				logger.log("Response: " + "200 Recurso eliminado correctamente: " + resourceId); 
				return "Recurso con ID " + resourceId + " eliminado correctamente";
			} else {
				// Si no se encuentra el recurso, devolver un mensaje de error
				res.status(404); 
				logger.logRequest(req.requestMethod() + " " + req.pathInfo()); 
				logger.log("Response: " + "404 Error: Resource with ID " + resourceId + " not found"); 
				return "Recurso con ID " + resourceId + " no encontrado";
			}
		});
	}
	
	/**
     * Saves the list of resources to a JSON file.
     *
     * @param resources The list of resources to be saved to the JSON file.
     */
	
	private void saveResourcesToJson(List<Resource> resources) {
		Gson gson = new Gson();
	    String json = gson.toJson(resources);
	    try {
	        Files.write(Paths.get("resources.json"), json.getBytes());
	    } catch (IOException e) {
	        e.printStackTrace();
	    }
	}
}
