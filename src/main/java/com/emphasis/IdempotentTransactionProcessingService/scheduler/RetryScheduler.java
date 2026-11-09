package com.emphasis.IdempotentTransactionProcessingService.scheduler;

import java.util.List;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.emphasis.IdempotentTransactionProcessingService.model.Transaction;
import com.emphasis.IdempotentTransactionProcessingService.repository.TransactionRepository;
import com.emphasis.IdempotentTransactionProcessingService.service.TransactionService;
@Component
public class RetryScheduler {
	
	private static final int MAX_RETRY = 3;
	
	 private final TransactionRepository repository;
	    private final TransactionService service;
	    
	    public RetryScheduler(
	            TransactionRepository repository,
	            TransactionService service) {

	        this.repository = repository;
	        this.service = service;
	    }
	    
	    @Scheduled(fixedDelay = 60000)
	    public void retryFailedTransactions() {

	        List<Transaction> failedTransactions =
	                repository.findByStatus(
	                        Transaction.Status.FAILED);

	        failedTransactions.stream()
	                .filter(tx ->
	                        tx.getRetryCount() < MAX_RETRY)
	                .forEach(tx -> {

	                    System.out.println(
	                            "Retrying : "
	                            + tx.getTransactionId());

	                    service.processTransactionAsync(
	                            tx.getId());
	                });
	    }

}
