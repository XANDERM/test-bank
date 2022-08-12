package es.test.bank.mapper;

import org.mapstruct.Mapper;

import es.test.bank.dto.TransactionStatusResponseDTO;
import es.test.bank.mapper.GenericMapper;
import es.test.bank.model.TransactionStatusResponseModel;

/**
 * The RestCreateProductRequestMapper interface.
 */
@Mapper(componentModel = "spring")
public interface TransactionStatusResponseMapper extends GenericMapper<TransactionStatusResponseModel, TransactionStatusResponseDTO>{
	
}
