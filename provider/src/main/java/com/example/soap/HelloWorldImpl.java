package com.example.soap;

import javax.jws.WebService;

/**
 * SOAP Web Service Implementation
 * This class provides the actual implementation of the HelloWorld service
 */
@WebService(endpointInterface = "com.example.soap.HelloWorld")
public class HelloWorldImpl implements HelloWorld {

    @Override
    public String sayHello(String name) {
        if (name == null || name.trim().isEmpty()) {
            return "Hello, Guest!";
        }
        return "Hello, " + name + "! Welcome to Apache CXF SOAP Web Service.";
    }

    @Override
    public int add(int a, int b) {
        System.out.println("Adding " + a + " + " + b);
        return a + b;
    }

    @Override
    public String getUserInfo(String userId) {
        if (userId == null || userId.trim().isEmpty()) {
            return "Error: User ID is required";
        }

        // Simulate user lookup
        return String.format(
            "User Information:\n" +
            "  User ID: %s\n" +
            "  Name: User_%s\n" +
            "  Status: Active\n" +
            "  Created: 2024-01-01",
            userId, userId
        );
    }
}
