package com.example.soap;

import com.example.soap.generated.HelloWorld;
import com.example.soap.generated.HelloWorldService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.xml.ws.Binding;
import jakarta.xml.ws.BindingProvider;
import jakarta.xml.ws.handler.Handler;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * JAX-RS REST Endpoint that calls SOAP Web Service
 * This resource provides REST endpoints to interact with the SOAP service
 */
@Path("/api")
@ApplicationScoped
@Produces(MediaType.APPLICATION_JSON)
public class HelloWorldResource {

    /**
     * Test endpoint to verify the service is running
     */
    @GET
    @Path("/health")
    public Response health() {
        Map<String, String> response = new HashMap<>();
        response.put("status", "UP");
        response.put("service", "SOAP Client (WildFly Bootable JAR)");
        return Response.ok(response).build();
    }

    /**
     * Call SOAP sayHello method
     * GET /api/hello?name=YourName
     */
    @GET
    @Path("/hello")
    public Response sayHello(@QueryParam("name") @DefaultValue("Guest") String name) {
        try {
            HelloWorld client = createClientWithLogging();

            String greeting = client.sayHello(name);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("input", name);
            response.put("message", greeting);

            return Response.ok(response).build();
        } catch (Exception e) {
            return createErrorResponse("Failed to call SOAP service: " + e.getMessage());
        }
    }

    /**
     * Call SOAP add method
     * GET /api/add?a=10&b=20
     */
    @GET
    @Path("/add")
    public Response add(
            @QueryParam("a") @DefaultValue("0") int a,
            @QueryParam("b") @DefaultValue("0") int b) {
        try {
            HelloWorld client = createClientWithLogging();

            int result = client.add(a, b);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("a", a);
            response.put("b", b);
            response.put("result", result);
            response.put("operation", a + " + " + b + " = " + result);

            return Response.ok(response).build();
        } catch (Exception e) {
            return createErrorResponse("Failed to call SOAP service: " + e.getMessage());
        }
    }

    /**
     * Call SOAP getUserInfo method
     * GET /api/user/{userId}
     */
    @GET
    @Path("/user/{userId}")
    public Response getUserInfo(@PathParam("userId") String userId) {
        try {
            HelloWorld client = createClientWithLogging();

            com.example.soap.generated.User user = client.getUserInfo(userId);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("userId", user.getUserId());
            response.put("name", user.getName());
            response.put("status", user.getStatus());
            response.put("created", user.getCreated());

            return Response.ok(response).build();
        } catch (Exception e) {
            return createErrorResponse("Failed to call SOAP service: " + e.getMessage());
        }
    }

    /**
     * Run all tests
     * GET /api/test/all
     */
    @GET
    @Path("/test/all")
    public Response testAll() {
        Map<String, Object> results = new HashMap<>();

        try {
            HelloWorld client = createClientWithLogging();

            // Test 1: sayHello
            String greeting1 = client.sayHello("Takahashi");
            Map<String, String> test1 = new HashMap<>();
            test1.put("method", "sayHello");
            test1.put("input", "Takahashi");
            test1.put("result", greeting1);
            results.put("test1", test1);

            // Test 2: sayHello with null
            String greeting2 = client.sayHello(null);
            Map<String, String> test2 = new HashMap<>();
            test2.put("method", "sayHello");
            test2.put("input", "null");
            test2.put("result", greeting2);
            results.put("test2", test2);

            // Test 3: add
            int sum = client.add(10, 20);
            Map<String, Object> test3 = new HashMap<>();
            test3.put("method", "add");
            test3.put("input", "10, 20");
            test3.put("result", sum);
            results.put("test3", test3);

            // Test 4: getUserInfo
            com.example.soap.generated.User userInfo = client.getUserInfo("12345");
            Map<String, Object> test4 = new HashMap<>();
            test4.put("method", "getUserInfo");
            test4.put("input", "12345");
            Map<String, String> userDetails = new HashMap<>();
            userDetails.put("userId", userInfo.getUserId());
            userDetails.put("name", userInfo.getName());
            userDetails.put("status", userInfo.getStatus());
            userDetails.put("created", userInfo.getCreated());
            test4.put("result", userDetails);
            results.put("test4", test4);

            results.put("success", true);
            results.put("message", "All tests completed successfully!");

            return Response.ok(results).build();
        } catch (Exception e) {
            return createErrorResponse("Failed to run tests: " + e.getMessage());
        }
    }

    private Response createErrorResponse(String message) {
        Map<String, Object> error = new HashMap<>();
        error.put("success", false);
        error.put("error", message);
        error.put("hint", "Make sure the SOAP server is running at http://localhost:9090/HelloWorld");
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(error).build();
    }

    /**
     * Create SOAP client with logging handler attached.
     * This enables logging of raw SOAP request/response XML to standard output.
     */
    @SuppressWarnings("rawtypes")
    private HelloWorld createClientWithLogging() {
        HelloWorldService service = new HelloWorldService();
        HelloWorld client = service.getHelloWorldPort();

        // Add SOAP logging handler
        BindingProvider bindingProvider = (BindingProvider) client;
        Binding binding = bindingProvider.getBinding();
        List<Handler> handlerChain = binding.getHandlerChain();
        if (handlerChain == null) {
            handlerChain = new ArrayList<>();
        }
        handlerChain.add(new SOAPLoggingHandler());
        binding.setHandlerChain(handlerChain);

        return client;
    }
}
