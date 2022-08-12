package es.test.bank.service.impl;

import java.util.Optional;

import javax.persistence.EntityNotFoundException;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import es.test.bank.entity.Account;
import es.test.bank.mapper.AccountInfrastructureMapper;
import es.test.bank.model.AccountModel;
import es.test.bank.repositories.AccountRepository;
import es.test.bank.service.AccountService;
import lombok.AllArgsConstructor;

/**
 * Use case for creating a new product.
 */
@Service
@Transactional
@AllArgsConstructor
public class AccountServiceImpl implements AccountService {

    private AccountRepository accountRepository;
    private AccountInfrastructureMapper accountMapper;
    
    @Override
    public AccountModel getAccountByIban(String iban) {
        Optional<Account> optionalAccount = this.accountRepository.findById(iban);
        if (optionalAccount.isPresent()) {
        	return accountMapper.toDomainModel(optionalAccount.get());
        }
        throw new EntityNotFoundException("Account not found");
		
    }

	@Override
	public AccountModel createAccount(AccountModel accountModel) {
		Account account = this.accountMapper.toEntity(accountModel);
		account = this.accountRepository.save(account);
        return  this.accountMapper.toDomainModel(account);
	}

	@Override
	public AccountModel updateAccount(String iban, Double amount) {
		Optional<Account> optionalAccount = this.accountRepository.findById(iban);
        if (optionalAccount.isPresent()) {
        	optionalAccount.get().setAmount(amount);
    		return this.accountMapper.toDomainModel(this.accountRepository.save(optionalAccount.get()));
        }
        throw new EntityNotFoundException("Account not found");
	}

}