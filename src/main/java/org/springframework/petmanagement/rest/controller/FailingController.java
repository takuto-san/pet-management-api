package org.springframework.petmanagement.rest.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.petmanagement.rest.api.OopsApi;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(exposedHeaders = "errors, content-type")
@RequestMapping("/api")
public class FailingController implements OopsApi {

    @Override
    public ResponseEntity<Void> failingRequest() {
        // This endpoint always fails with a 400 status
        // The ProblemDetail response is handled by the exception handler
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
}
