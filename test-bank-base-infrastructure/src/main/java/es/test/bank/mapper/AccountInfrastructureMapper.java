package es.test.bank.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import es.test.bank.entity.Account;
import es.test.bank.model.AccountModel;

@Mapper(componentModel = "spring", unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface AccountInfrastructureMapper extends GenericMapper<Account, AccountModel> {
	
	@Override
	@Mapping(target = "transactions", ignore = true)
	Account toEntity (AccountModel accountModel);
	
	@Override
	@Mapping(source = "iban", target ="iban")
	@Mapping(source = "amount", target ="amount")
	AccountModel toDomainModel (Account account);

}