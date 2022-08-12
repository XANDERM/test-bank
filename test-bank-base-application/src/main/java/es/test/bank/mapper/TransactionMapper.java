package es.test.bank.mapper;

import org.mapstruct.Mapper;

import es.test.bank.dto.TransactionDTO;
import es.test.bank.mapper.GenericMapper;
import es.test.bank.model.TransactionModel;

/**
 * The RestCreateProductRequestMapper interface.
 */
@Mapper(componentModel = "spring")
public interface TransactionMapper extends GenericMapper<TransactionModel, TransactionDTO>{
	
}
