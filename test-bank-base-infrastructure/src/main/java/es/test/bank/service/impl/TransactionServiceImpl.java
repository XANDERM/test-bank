package es.test.bank.service.impl;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import es.test.bank.entity.Transaction;
import es.test.bank.exception.TestBankBusinessException;
import es.test.bank.mapper.TransactionInfrastructureMapper;
import es.test.bank.model.TransactionModel;
import es.test.bank.repositories.TransactionCustomRepository;
import es.test.bank.repositories.TransactionsRepository;
import es.test.bank.service.AccountService;
import es.test.bank.service.TransactionService;
import lombok.AllArgsConstructor;

/**
 * Use case for creating a new product.
 */
@Service
@Transactional
@AllArgsConstructor
public class TransactionServiceImpl implements TransactionService {

    private TransactionCustomRepository transactionCustomRepository;
    private TransactionsRepository transactionsRepository;
    private TransactionInfrastructureMapper transactionMapper;
    private AccountService accountService;
    
    private final String ERROR_CODE = "406";
    private final String ERROR_DESCRIPTION = "Not Acceptable";
    
    @Override
    public List<TransactionModel> findAll(Direction direction) {
        List<Transaction> transactionList = this.transactionsRepository.findAll(Sort.by(direction,"amount"));
		return transactionMapper.toDomainModelList(transactionList);
    }

	@Override
	public List<TransactionModel> findAllByAccountIbanContains(String accountIban, Direction direction) {
		List<Transaction> transactionList;
		if (direction.isAscending()) {
			transactionList = this.transactionsRepository.findAllByAccountIbanIbanOrderByAmountAsc(accountIban);
		} else {
			transactionList = this.transactionsRepository.findAllByAccountIbanIbanOrderByAmountDesc(accountIban);
		}
		return transactionMapper.toDomainModelList(transactionList);
	}
	
    @Override
    public TransactionModel createTransaction(TransactionModel transactionModel) {
    	Double amountAccount = accountService.getAccountByIban(transactionModel.getAccountIban()).getAmount();
    	Double amountTransaction = Double.sum(transactionModel.getAmount(), transactionModel.getFee());
    	if (amountAccount > amountTransaction) {
	        Transaction transaction = this.transactionMapper.toEntity(transactionModel);
	        BigDecimal amountAccountBD = new BigDecimal(amountAccount);
	        BigDecimal amountTransactionBD = new BigDecimal(amountTransaction) ;
	        Double valueToUpdate = amountAccountBD.subtract(amountTransactionBD).doubleValue();
	        accountService.updateAccount(transactionModel.getAccountIban(), valueToUpdate);
	        transaction = this.transactionCustomRepository.create(transaction);
	        return  this.transactionMapper.toDomainModel(transaction);
    	} else {
    		throw new TestBankBusinessException(ERROR_CODE, ERROR_DESCRIPTION);
    	}
    }

	@Override
	public TransactionModel getTransactionByReference(String reference) {
		var transactionModel = this.transactionsRepository.findByReference(reference).orElse(new Transaction());
		if (transactionModel.isNew()) {
			return new TransactionModel();
		}
		return this.transactionMapper.toDomainModel(transactionModel);
	}

}