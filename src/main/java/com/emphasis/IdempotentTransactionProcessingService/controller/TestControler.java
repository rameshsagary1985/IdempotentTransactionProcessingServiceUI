package com.emphasis.IdempotentTransactionProcessingService.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
@RestController
public class TestControler {
	
	
	//@RequestMapping("/api/products")
	  // 2. GET a single product by ID (Endpoint: GET http://localhost:8080/api/products/1)
    @GetMapping("/message")
    public String getWishmessage() {
        return "this is wishmessage";
    }

}
