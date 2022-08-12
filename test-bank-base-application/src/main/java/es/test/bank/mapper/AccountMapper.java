package es.test.bank.mapper;

import org.mapstruct.Mapper;

import es.test.bank.dto.AccountDTO;
import es.test.bank.mapper.GenericMapper;
import es.test.bank.model.AccountModel;

/**
 * The RestCreateProductRequestMapper interface.
 */
@Mapper(componentModel = "spring")
public interface AccountMapper extends GenericMapper<AccountModel, AccountDTO>{
	
}
