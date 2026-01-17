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
    public User getUserInfo(String userId) {
        if (userId == null || userId.trim().isEmpty()) {
            // Return a default user for invalid input
            return new User("unknown", "Unknown User", "Inactive", "N/A");
        }

        // Simulate user lookup and return User object
        return new User(
            userId,
            "User_" + userId,
            "Active",
            "2024-01-01"
        );
    }
}
