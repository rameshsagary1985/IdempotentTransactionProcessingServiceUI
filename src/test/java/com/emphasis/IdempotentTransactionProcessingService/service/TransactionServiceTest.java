package com.emphasis.IdempotentTransactionProcessingService.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.emphasis.IdempotentTransactionProcessingService.dto.TransactionRequest;
import com.emphasis.IdempotentTransactionProcessingService.model.Transaction;
import com.emphasis.IdempotentTransactionProcessingService.repository.TransactionRepository;

@ExtendWith(MockitoExtension.class)
class TransactionServiceTest {

    @Mock
    private TransactionRepository repository;

    @InjectMocks
    private TransactionService service;

    @Test
    void shouldProcessNewTransaction() {

        TransactionRequest request =
                new TransactionRequest();

        request.setTransactionId("TXN100");
        request.setAccountId("ACC100");
        request.setTransactionType("DEBIT");
        request.setAmount(
                BigDecimal.valueOf(100));

        request.setCurrencyCode("INR");
        request.setSequenceNumber(1L);
        request.setIdempotencyKey("KEY100");

        when(repository.findByIdempotencyKey(
                "KEY100"))
                .thenReturn(Optional.empty());

        Transaction tx = new Transaction();
        tx.setId(1L);

        when(repository.save(any()))
                .thenReturn(tx);

        String result =
                service.submitTransaction(request);

        assertEquals(
                "Transaction Accepted",
                result);
    }

    @Test
    void shouldRejectDuplicateTransaction() {

        Transaction existing =
                new Transaction();

        existing.setIdempotencyKey(
                "KEY100");

        when(repository.findByIdempotencyKey(
                "KEY100"))
                .thenReturn(
                        Optional.of(existing));

        TransactionRequest request =
                new TransactionRequest();

        request.setIdempotencyKey(
                "KEY100");

        assertEquals(
                "Duplicate Transaction Ignored",
                service.submitTransaction(
                        request));
    }

    @Test
    void shouldMarkTransactionSuccess() {

        Transaction tx =
                new Transaction();

        tx.setId(1L);
        tx.setRetryCount(0);

        tx.setAmount(
             BigDecimal.valueOf(1000));

        tx.setStatus(
             Transaction.Status.PROCESSING);

        when(repository.findById(1L))
                .thenReturn(Optional.of(tx));

        service.processTransactionAsync(1L);

        assertEquals(
                Transaction.Status.PROCESSED,
                tx.getStatus());
    }

    @Test
    void shouldMarkTransactionFailed() {

        Transaction tx =
                new Transaction();

        tx.setId(1L);

        tx.setAmount(
              BigDecimal.valueOf(-1));

        tx.setRetryCount(0);

        when(repository.findById(1L))
                .thenReturn(Optional.of(tx));

        service.processTransactionAsync(1L);

        assertEquals(
                Transaction.Status.FAILED,
                tx.getStatus());

        assertEquals(
                1,
                tx.getRetryCount());
    }
}
