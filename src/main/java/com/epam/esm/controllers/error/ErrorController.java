package com.epam.esm.controllers.error;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ErrorController{
    @RequestMapping("/error")
    public ResponseEntity<String> handleError(HttpServletResponse response){
        // Get the error code and message from the request
        int statusCode = response.getStatus();
        // Create the error response
        String errorResponse = "{\"statusCode\": " + statusCode + ", \"message\": \"" + "Page not found!" + "\"}";

        // Return the error response with the appropriate HTTP status code
        return new ResponseEntity<>(errorResponse, HttpStatus.valueOf(statusCode));
    }
}