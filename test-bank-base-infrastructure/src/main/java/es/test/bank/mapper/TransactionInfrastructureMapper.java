package es.test.bank.mapper;

import java.util.List;

import javax.persistence.EntityNotFoundException;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;
import org.springframework.beans.factory.annotation.Autowired;

import es.test.bank.entity.Account;
import es.test.bank.entity.Transaction;
import es.test.bank.model.TransactionModel;
import es.test.bank.repositories.AccountRepository;

@Mapper(componentModel = "spring", unmappedSourcePolicy = ReportingPolicy.IGNORE, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public abstract class TransactionInfrastructureMapper implements GenericMapper<Transaction, TransactionModel> {

	@Autowired
	private AccountRepository accountRepository;
	
	@Override
	@Mapping(source = "accountIban", target = "accountIban", qualifiedByName = "fromStringToAccount")
	public abstract Transaction toEntity(TransactionModel transactionModel);
	
	@Override
	@Mapping(source = "accountIban", target = "accountIban", qualifiedByName = "fromAccountToString")
	public abstract TransactionModel toDomainModel(Transaction transaction);
	
	@Named("fromStringToAccount")
    public Account fromStringToAccount(String accountIban) throws EntityNotFoundException {
        return accountRepository.getOne(accountIban);
    }
	
	@Named("fromAccountToString")
    public String fromAccountToString(Account account) throws EntityNotFoundException {
        return account.getIban();
    }

	@Override
	public abstract List<Transaction> toEntities(List<TransactionModel> transactionModelList);
	
	@Override
	public abstract List<TransactionModel> toDomainModelList(List<Transaction> transactionList);
	
}
