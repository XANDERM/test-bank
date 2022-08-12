package es.test.bank.mapper;

import org.mapstruct.Mapper;

import es.test.bank.dto.CreateAccountRequestDTO;
import es.test.bank.mapper.GenericMapper;
import es.test.bank.model.AccountModel;

/**
 * The RestCreateProductRequestMapper interface.
 */
@Mapper(componentModel = "spring")
public interface CreateAccountMapper extends GenericMapper<AccountModel, CreateAccountRequestDTO>{
	
}
