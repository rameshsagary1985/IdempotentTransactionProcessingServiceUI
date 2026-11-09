package com.emphasis.IdempotentTransactionProcessingService.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.emphasis.IdempotentTransactionProcessingService.model.User;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {}