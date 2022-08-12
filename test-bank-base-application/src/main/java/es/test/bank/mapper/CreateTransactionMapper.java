package es.test.bank.mapper;

import org.mapstruct.Mapper;

import es.test.bank.dto.CreateTransactionRequestDTO;
import es.test.bank.mapper.GenericMapper;
import es.test.bank.model.TransactionModel;

/**
 * The RestCreateProductRequestMapper interface.
 */
@Mapper(componentModel = "spring")
public interface CreateTransactionMapper extends GenericMapper<TransactionModel, CreateTransactionRequestDTO>{
	
}
