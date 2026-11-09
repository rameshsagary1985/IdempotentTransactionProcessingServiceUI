package com.emphasis.IdempotentTransactionProcessingService.service;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.emphasis.IdempotentTransactionProcessingService.dto.TransactionRequest;
import com.emphasis.IdempotentTransactionProcessingService.model.Transaction;
import com.emphasis.IdempotentTransactionProcessingService.repository.TransactionRepository;

@Service
public class TransactionService {

	 private final TransactionRepository repository;
	 public TransactionService(
	            TransactionRepository repository) {

	        this.repository = repository;
	    }
	 
	 public String submitTransaction(
	            TransactionRequest request) {
		 Optional<Transaction> existingTransaction =
	                repository.findByIdempotencyKey(
	                        request.getIdempotencyKey());

	        if(existingTransaction.isPresent()) {

	            return "Duplicate Transaction Ignored";
	        }

	        Transaction transaction = new Transaction();

	        transaction.setTransactionId(
	                request.getTransactionId());

	        transaction.setAccountId(
	                request.getAccountId());

	        transaction.setTransactionType(
	                request.getTransactionType());

	        transaction.setAmount(
	                request.getAmount());

	        transaction.setCurrencyCode(
	                request.getCurrencyCode());

	        transaction.setSequenceNumber(
	                request.getSequenceNumber());

	        transaction.setIdempotencyKey(
	                request.getIdempotencyKey());

	        transaction.setRetryCount(0);

	        transaction.setStatus(
	                Transaction.Status.RECEIVED);

	        Transaction saved =
	                repository.save(transaction);

	        processTransactionAsync(saved.getId());

	        return "Transaction Accepted";
		 
	 }

	 public void processTransactionAsync(Long id) {
		 Transaction tx =
	                repository.findById(id)
	                          .orElseThrow();

	        try {

	            tx.setStatus(
	                    Transaction.Status.PROCESSING);
	            repository.save(tx);

	            processPayment(tx);

	            tx.setStatus(
	                    Transaction.Status.PROCESSED);

	            repository.save(tx);

	        } catch(Exception ex) {

	            tx.setStatus(
	                    Transaction.Status.FAILED);

	            tx.setRetryCount(
	                    tx.getRetryCount() + 1);

	            repository.save(tx);
	        }
	    
		
	 }

	 private void processPayment(Transaction tx) {
		  if(tx.getAmount().doubleValue() <= 0) {
	            throw new RuntimeException(
	                    "Invalid Amount");
	        }

	        System.out.println(
	                "Transaction Processed : "
	                + tx.getTransactionId());
		
	 }
}
