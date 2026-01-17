package com.example.soap;

import jakarta.ws.rs.ApplicationPath;
import jakarta.ws.rs.core.Application;

/**
 * JAX-RS Application Configuration
 * This class configures the REST application and sets the base path
 */
@ApplicationPath("/rest")
public class RestApplication extends Application {
    // JAX-RS will automatically discover and register all @Path annotated resources
}
