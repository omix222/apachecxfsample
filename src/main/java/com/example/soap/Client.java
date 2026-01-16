package com.example.soap;

import org.apache.cxf.jaxws.JaxWsProxyFactoryBean;

/**
 * SOAP Web Service Requester (Client)
 * This class connects to the HelloWorld service and invokes its methods
 */
public class Client {

    private static final String SERVICE_ADDRESS = "http://localhost:9090/HelloWorld";

    public static void main(String[] args) {
        System.out.println("Apache CXF SOAP Client");
        System.out.println("=====================\n");

        // Create client proxy
        JaxWsProxyFactoryBean factory = new JaxWsProxyFactoryBean();
        factory.setServiceClass(HelloWorld.class);
        factory.setAddress(SERVICE_ADDRESS);

        HelloWorld client = (HelloWorld) factory.create();

        try {
            // Test 1: sayHello method
            System.out.println("Test 1: Calling sayHello()");
            System.out.println("---------------------------");
            String greeting = client.sayHello("Takahashi");
            System.out.println("Response: " + greeting);
            System.out.println();

            // Test 2: sayHello with null
            System.out.println("Test 2: Calling sayHello(null)");
            System.out.println("---------------------------");
            String guestGreeting = client.sayHello(null);
            System.out.println("Response: " + guestGreeting);
            System.out.println();

            // Test 3: add method
            System.out.println("Test 3: Calling add(10, 20)");
            System.out.println("---------------------------");
            int sum = client.add(10, 20);
            System.out.println("Response: 10 + 20 = " + sum);
            System.out.println();

            // Test 4: getUserInfo method
            System.out.println("Test 4: Calling getUserInfo(\"12345\")");
            System.out.println("---------------------------");
            String userInfo = client.getUserInfo("12345");
            System.out.println("Response:\n" + userInfo);
            System.out.println();

            // Test 5: Multiple operations
            System.out.println("Test 5: Multiple operations");
            System.out.println("---------------------------");
            for (int i = 1; i <= 3; i++) {
                String msg = client.sayHello("User" + i);
                System.out.println(i + ". " + msg);
            }
            System.out.println();

            System.out.println("All tests completed successfully!");

        } catch (Exception e) {
            System.err.println("Error calling web service:");
            System.err.println("  Message: " + e.getMessage());
            System.err.println("\nMake sure the server is running!");
            System.err.println("Start the server with: mvn exec:java@run-server");
            e.printStackTrace();
        }
    }
}
