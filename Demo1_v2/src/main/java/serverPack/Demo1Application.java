package serverPack;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.google.gson.Gson;

import static spark.Spark.*;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


/**
 * The Demo1Application class serves as the entry point for the application.
 * It initializes and starts the server to handle RESTful API requests.
 */

@SpringBootApplication
public class Demo1Application {

	
	 /**
     * The main method is the entry point for the application. It initializes the server and starts handling requests.
     *
     * @param args The command-line arguments.
     */
	
	public static void main(String[] args) {
		
		Server serveer = new Server();
		
       serveer.run();
        
	}

}
