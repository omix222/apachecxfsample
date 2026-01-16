package com.example.soap;

import org.apache.cxf.jaxws.JaxWsServerFactoryBean;

/**
 * SOAP Web Service Provider (Server)
 * This class publishes the HelloWorld service and makes it available for clients
 */
public class Server {

    private static final String SERVICE_ADDRESS = "http://localhost:9090/HelloWorld";

    public static void main(String[] args) {
        System.out.println("Starting Apache CXF SOAP Server...");

        // Create service implementation
        HelloWorldImpl implementor = new HelloWorldImpl();

        // Create server factory
        JaxWsServerFactoryBean factory = new JaxWsServerFactoryBean();
        factory.setServiceClass(HelloWorld.class);
        factory.setAddress(SERVICE_ADDRESS);
        factory.setServiceBean(implementor);

        // Start the server
        factory.create();

        System.out.println("Server started successfully!");
        System.out.println("Service available at: " + SERVICE_ADDRESS);
        System.out.println("WSDL available at: " + SERVICE_ADDRESS + "?wsdl");
        System.out.println("\nPress Ctrl+C to stop the server...");

        // Keep the server running
        try {
            Thread.sleep(Long.MAX_VALUE);
        } catch (InterruptedException e) {
            System.err.println("Server interrupted: " + e.getMessage());
        }
    }
}
