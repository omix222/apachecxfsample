package com.example.soap;

import com.example.soap.generated.HelloWorld;
import com.example.soap.generated.HelloWorldService;

/**
 * SOAP Web Service Requester (Client)
 * This class connects to the HelloWorld service using WSDL-generated code
 */
public class Client {

    public static void main(String[] args) {
        System.out.println("Apache CXF SOAP Client (WSDL-Generated)");
        System.out.println("========================================\n");

        try {
            // Create service from WSDL-generated code
            HelloWorldService service = new HelloWorldService();
            HelloWorld client = service.getHelloWorldPort();

            // Test 1: sayHello method
            System.out.println("Test 1: Calling sayHello(\"Takahashi\")");
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
            com.example.soap.generated.User userInfo = client.getUserInfo("12345");
            System.out.println("Response:");
            System.out.println("  User ID: " + userInfo.getUserId());
            System.out.println("  Name: " + userInfo.getName());
            System.out.println("  Status: " + userInfo.getStatus());
            System.out.println("  Created: " + userInfo.getCreated());
            System.out.println();

            // Test 5: Multiple operations
            System.out.println("Test 5: Multiple operations");
            System.out.println("---------------------------");
            for (int i = 1; i <= 3; i++) {
                String msg = client.sayHello("User" + i);
                System.out.println(i + ". " + msg);
            }
            System.out.println();

            System.out.println("===========================================");
            System.out.println("All tests completed successfully!");
            System.out.println("This client was generated from the WSDL file");
            System.out.println("===========================================");

        } catch (Exception e) {
            System.err.println("Error calling web service:");
            System.err.println("  Message: " + e.getMessage());
            System.err.println("\nMake sure the server is running!");
            System.err.println("Start the server with: mvn -pl provider exec:java");
            e.printStackTrace();
        }
    }
}
