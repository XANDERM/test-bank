package es.test.bank.service;

import es.test.bank.model.AccountModel;

public interface AccountService {
	
	AccountModel getAccountByIban(String iban);

	AccountModel createAccount(AccountModel accountModel);

	AccountModel updateAccount(String iban, Double amount);

}
