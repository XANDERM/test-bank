package es.test.bank.service.impl;

import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import es.test.bank.model.TransactionModel;
import es.test.bank.model.TransactionStatusModel;
import es.test.bank.model.TransactionStatusResponseModel;
import es.test.bank.service.TransactionService;
import es.test.bank.service.TransactionStatusService;
import lombok.AllArgsConstructor;

/**
 * Use case for creating a new product.
 */
@Service
@Transactional
@AllArgsConstructor
public class TransactionStatusServiceImpl implements TransactionStatusService {

	private final KieContainer kieContainer;
	private final TransactionService transactionService;

	@Override
	public TransactionStatusResponseModel getTransactionStatus(TransactionStatusModel transactionStatusModel) {
		TransactionStatusResponseModel transactionStatusResponseModel = new TransactionStatusResponseModel();
		TransactionModel transactionModel = this.transactionService
				.getTransactionByReference(transactionStatusModel.getReference());
		KieSession kieSession = kieContainer.newKieSession();
		kieSession.insert(transactionStatusModel);
		kieSession.insert(transactionModel);
		kieSession.setGlobal("transactionStatusResponseModel", transactionStatusResponseModel);
		kieSession.fireAllRules();
		kieSession.dispose();
		return transactionStatusResponseModel;
	}

}