package es.test.bank.service;

import es.test.bank.model.TransactionStatusModel;
import es.test.bank.model.TransactionStatusResponseModel;

/**
 * Use case for the creation of a new product.
 */
public interface TransactionStatusService {
	
	TransactionStatusResponseModel getTransactionStatus(TransactionStatusModel transactionStatusModel);
    
}
