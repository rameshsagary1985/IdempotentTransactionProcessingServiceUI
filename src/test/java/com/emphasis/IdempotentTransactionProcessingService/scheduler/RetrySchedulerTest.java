package com.emphasis.IdempotentTransactionProcessingService.scheduler;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.emphasis.IdempotentTransactionProcessingService.model.Transaction;
import com.emphasis.IdempotentTransactionProcessingService.repository.TransactionRepository;
import com.emphasis.IdempotentTransactionProcessingService.service.TransactionService;

@ExtendWith(MockitoExtension.class)
class RetrySchedulerTest {

    @Mock
    TransactionRepository repository;

    @Mock
    TransactionService service;

    @InjectMocks
    RetryScheduler scheduler;

    @Test
    void shouldRetryFailedTransactions() {

        Transaction tx =
                new Transaction();

        tx.setId(1L);
        tx.setRetryCount(1);

        tx.setStatus(
                Transaction.Status.FAILED);

        when(repository.findByStatus(
                Transaction.Status.FAILED))
                .thenReturn(List.of(tx));

        scheduler.retryFailedTransactions();

        verify(service)
                .processTransactionAsync(1L);
    }
}