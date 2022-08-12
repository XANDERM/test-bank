package es.test.bank.controller;

import java.util.List;

import org.springframework.data.domain.Sort.Direction;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import es.test.bank.api.TransactionsApi;
import es.test.bank.dto.CreateTransactionRequestDTO;
import es.test.bank.dto.SortDTO;
import es.test.bank.dto.TransactionDTO;
import es.test.bank.dto.TransactionStatusRequestDTO;
import es.test.bank.dto.TransactionStatusResponseDTO;
import es.test.bank.model.TransactionModel;
import es.test.bank.model.TransactionStatusModel;
import es.test.bank.model.TransactionStatusResponseModel;
import es.test.bank.service.TransactionService;
import es.test.bank.service.TransactionStatusService;
import es.test.bank.mapper.CreateTransactionMapper;
import es.test.bank.mapper.TransactionMapper;
import es.test.bank.mapper.TransactionStatusRequestMapper;
import es.test.bank.mapper.TransactionStatusResponseMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("${openapi.testbank.base-path:/testbank}")
@AllArgsConstructor
@Slf4j
public class TransactionsRestController implements TransactionsApi {

	private TransactionMapper transactionMapper;
	private CreateTransactionMapper createTransactionMapper;
	private TransactionStatusRequestMapper transactionStatusRequestMapper;
	private TransactionStatusResponseMapper transactionStatusResponseMapper;

	private TransactionService transactionService;
	private TransactionStatusService transactionStatusService;

	private final String ASCENDING_ORDER = "ascending";

	@Override
	public ResponseEntity<List<TransactionDTO>> getTransactions(String authorization, String xB3TraceId, String xB3SpanId,
			String xB3ParentSpanId, String xB3Sampled, String accountIban, SortDTO sort) {

		Direction orderBy = (sort.equals(null) || sort.getValue().equals(ASCENDING_ORDER)) ? Direction.ASC : Direction.DESC;
		List<TransactionModel> transactionModelList;
		if (accountIban == null) {
			transactionModelList = transactionService.findAll(orderBy);
		} else {
			transactionModelList = transactionService.findAllByAccountIbanContains(accountIban, orderBy);
		}
		List<TransactionDTO> transactionDTO = this.transactionMapper.toDTOList(transactionModelList);
		return new ResponseEntity<List<TransactionDTO>>(transactionDTO, HttpStatus.OK);
	}

	@Override
	public ResponseEntity<Void> createTransaction(String authorization, CreateTransactionRequestDTO transactionDTO,
			String xB3TraceId, String xB3SpanId, String xB3ParentSpanId, String xB3Sampled) {

		TransactionModel transactionModel = this.createTransactionMapper.toModel(transactionDTO);

		LOGGER.info("Creating a product with the following content", transactionDTO);
		transactionService.createTransaction(transactionModel);
		LOGGER.info("Product created");
		return new ResponseEntity<>(HttpStatus.CREATED);
	}

	@Override
	public ResponseEntity<TransactionStatusResponseDTO> transactionStatus(String authorization,
			TransactionStatusRequestDTO transactionStatusRequestDTO, String xB3TraceId, String xB3SpanId,
			String xB3ParentSpanId, String xB3Sampled) {
		TransactionStatusModel transactionStatusModel = this.transactionStatusRequestMapper
				.toModel(transactionStatusRequestDTO);
		TransactionStatusResponseModel transactionStatusResponseModel = this.transactionStatusService
				.getTransactionStatus(transactionStatusModel);
		return new ResponseEntity<TransactionStatusResponseDTO>(
				this.transactionStatusResponseMapper.toDTO(transactionStatusResponseModel), HttpStatus.OK);

	}

}
