package es.test.bank.mapper;

import org.mapstruct.Mapper;

import es.test.bank.dto.TransactionStatusRequestDTO;
import es.test.bank.mapper.GenericMapper;
import es.test.bank.model.TransactionStatusModel;

/**
 * The RestCreateProductRequestMapper interface.
 */
@Mapper(componentModel = "spring")
public interface TransactionStatusRequestMapper extends GenericMapper<TransactionStatusModel, TransactionStatusRequestDTO>{
	
}
