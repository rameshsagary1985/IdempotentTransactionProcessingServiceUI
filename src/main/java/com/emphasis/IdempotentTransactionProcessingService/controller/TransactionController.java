package com.emphasis.IdempotentTransactionProcessingService.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.emphasis.IdempotentTransactionProcessingService.dto.TransactionRequest;
import com.emphasis.IdempotentTransactionProcessingService.service.TransactionService;

@RestController
@RequestMapping("/transactions")
public class TransactionController {
	
	  private final TransactionService service;
	  
	  public TransactionController(
	            TransactionService service) {
	        this.service = service;
	    }
	  
	  @PostMapping
	    public ResponseEntity<String> process(

	            @RequestBody
	            TransactionRequest request) {

	        return ResponseEntity.ok(
	                service.submitTransaction(request));
	    }
	  
		/*
		 * @GetMapping("/add") public String showSignUpForm(TransactionRequest
		 * transactionRequest) { return "add-transaction"; }
		 */

}
