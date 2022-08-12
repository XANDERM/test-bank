package es.test.bank.service;

import java.util.List;

import org.springframework.data.domain.Sort.Direction;

import es.test.bank.model.TransactionModel;

/**
 * Use case for the creation of a new product.
 */
public interface TransactionService {
    
    TransactionModel createTransaction(TransactionModel transactionModel);

	List<TransactionModel> findAll(Direction direction);

	List<TransactionModel> findAllByAccountIbanContains(String accountIban, Direction direction);
	
	TransactionModel getTransactionByReference(String reference);
    
}
