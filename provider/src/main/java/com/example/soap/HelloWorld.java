package com.example.soap;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;

/**
 * SOAP Web Service Interface
 * This defines the contract for the HelloWorld service
 */
@WebService
public interface HelloWorld {

    /**
     * Simple greeting method
     * @param name The name to greet
     * @return A greeting message
     */
    @WebMethod
    String sayHello(@WebParam(name = "name") String name);

    /**
     * Method to add two numbers
     * @param a First number
     * @param b Second number
     * @return Sum of a and b
     */
    @WebMethod
    int add(@WebParam(name = "a") int a, @WebParam(name = "b") int b);

    /**
     * Method to get user information
     * @param userId User ID
     * @return User information as string
     */
    @WebMethod
    String getUserInfo(@WebParam(name = "userId") String userId);
}
