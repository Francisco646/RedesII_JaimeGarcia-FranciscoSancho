# RedesII_JaimeGarcia-FranciscoSancho

### Client functionalities
	Class to start GUI program: As the client is initialized from a external class, it was needed to create a new class. The class is intended to perform two main purposes: the first one was to create a new http client, which will be used during the whole process, while the second was to call a static method located in other class. This mentioned method carries out the rest of the client configuration.

	GUI configuration: At first, the client section was created with the intention of being managed from the java console. However, we realized that it was a good idea to create a basic graphical user interface, because we wanted to introduce the request parameters in an easier way, and receiving the response in a more clear and understandable format. We have already touched this topic on a subject a year ago, so we had some notions about how to proceed.
This part consist on two graphical user interfaces. The main GUI manages the general configuration and output the response from the server, while the secondary is in charge of sending headers and the payload (this second only with post and put). 

	GUI management: The backbone of the client configuration can be found in a client layout configuration class. This class manages several tasks, as configuring both main and secondary GUI, process the request (and get the response), and update the correct request labels. All these tasks are performed from methods in different classes. We have decided to do so because the code would have a better organization, gaining in modularity and giving a clearer understanding of our code.

	Class for managing layout items: This class has several methods which organize the items of both layouts. There is a method which configure the main layout, and another two methods that configure the secondary layout: the header and the payload. The last method only gets executed when post or put methods are selected. We decided to split these configuration in two parts, because it was better to manage it in the GUI management class. It is another example of how modularization can help to have a more organized and readable code.

	Manage request: The program has a class with a method. This method receives several parameters from the GUI management class, and calls a different method depending on the received user input. All the methods are located in another class, as we will see later.

	Class for request methods: A class which host one function per method (get, head, post, put, delete). Each function builds the request, with the URL in URI format, some headers, and the payload when needed. Despite the functions are quite similar, we decided to use one function per method, in order to have a clearer and more understandable program.

	Response management: There is another class which is in charge of managing the client-side response section. There is a function which builds the received response, and another one opening a connection to the url and processing the received response. This second function generates an object of another class, which contains some general data about the received response, and gets updated each time the client sends a request and receive a response.

	Cookies management: The response management class contains all the cookies logic. To manage cookies, a static block is initiated. A static block basically initiates the static data members of a class, ensuring it is initiated before any other constructor. We made the decision of setting it up this way in order to avoid possible errors and exceptions during the execution process. Then, there are another two methods, adding cookies to request and updating the cookies from response, respectively. These two methods are invoked when the function in charge of processing the response is called from the main client class. Again, due to modularity and organizational purposes, the methods have been separated.

### Server functionalities
	Server application: There is a class which creates an object of a class managing the server configuration. Then, when the server program is started, this method is running until the program is closed. We have decided to configure the server following concurrency patters, in order to support multiple request made at the same time.

	Server management class: This class manages most of the server configuration. As we are working in a concurrent way, the class extends from thread. From this point, the class has a constructor which creates a list of resources and stores there resources from a json file (in case there are resources stored there). The json file stores the resources that have been inserted into the server via payloads, in put and post configuration. 
On the other hand, there is another function inheriting from thread class. This function gets the existing resources, configures the port and manages the server endpoints. The endpoints respectively serves a html file, outputs the list of resources, add, modify and delete resources. In the post, put and delete methods there are personalized error codes, depending on the receiving response; mainly 400 or 404 errors (both client side errors). 

	This main class also has a function in charge of saving the resources to the json file, each time a resource is added, modified or deleted. The function gets the whole list of resources and overwrites the file in order to introduce there the complete list. We decided to set up this function because there were repeating code across all endpoints and we opted for setting up the function and call it each time needed. With this decision, the code gained in readability and maintainability.

	Resources: We decided to create a separate class to manage the different resources across the server. In this case, we decided to configure the resources to host dogs information such as id, name, breed and age. Each time an endpoint to manage resources is invoked, a resource object with these parameters is created, with the purpose of output and store the resources at later stages.

	Logger: Another server class, in this case managing logging purposes. There is a final attribute in the server class managing the logger class. Each time an endpoint is used, the logger class logs the request and the response, and writes the outputted logs into a separate file, for storage purposes. The class was created to manage logging functionality. We found out that configuring logger was easy, so we decided to proceed with it.

### Additional notes
	There is an html file in the project folder. This file gets served when the get hello endpoint gets invoked.

	The resources are served in json format, and stored in a file like that.

	The logger file stores both the request and the response from the server, the second as a log.

	Maven dependencies have been installed and imported into the system, in order to make the server side program to work properly.

	The client program is terminated when the user closes the main GUI.

	The server program is terminated from the eclipse console.

	Although client and server are together within one project, it is recommended to import the project to two different workspaces and executing client in one and server in the other, so that they can be checked in a better way.

