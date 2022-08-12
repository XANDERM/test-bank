package es.test.bank.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import es.test.bank.api.AccountApi;
import es.test.bank.dto.AccountDTO;
import es.test.bank.dto.CreateAccountRequestDTO;
import es.test.bank.model.AccountModel;
import es.test.bank.service.AccountService;
import es.test.bank.mapper.AccountMapper;
import es.test.bank.mapper.CreateAccountMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("${openapi.testbank.base-path:/testbank}")
@AllArgsConstructor
@Slf4j
public class AccountRestController implements AccountApi {

	private AccountService accountService;
	private AccountMapper accountMapper;
	private CreateAccountMapper createAccountMapper;

	@Override
	public ResponseEntity<AccountDTO> getAccount(String authorization, String iban, String xB3TraceId, String xB3SpanId,
			String xB3ParentSpanId, String xB3Sampled) {

		AccountModel accountModel = accountService.getAccountByIban(iban);
		AccountDTO accountDTO = accountMapper.toDTO(accountModel);
		return new ResponseEntity<AccountDTO>(accountDTO, HttpStatus.OK);
	}

	@Override
	public ResponseEntity<Void> createAccount(String authorization, CreateAccountRequestDTO createAccountRequestDTO, String xB3TraceId,
			String xB3SpanId, String xB3ParentSpanId, String xB3Sampled) {

		AccountModel accountModel = this.createAccountMapper.toModel(createAccountRequestDTO);

		LOGGER.info("Creating a product with the following content", createAccountRequestDTO);
		accountService.createAccount(accountModel);
		LOGGER.info("Product created");
		return new ResponseEntity<>(HttpStatus.CREATED);
	}

	@Override
	public ResponseEntity<Void> updateAmount(String authorization, String iban, Double amount,
			String xB3TraceId, String xB3SpanId, String xB3ParentSpanId, String xB3Sampled) {

		LOGGER.info("Updating a account " + iban + " with the next amount: " + amount);
		accountService.updateAccount(iban, amount);
		LOGGER.info("Product updated");
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}

}
