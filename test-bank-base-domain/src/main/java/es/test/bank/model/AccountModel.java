package es.test.bank.model;

import javax.validation.constraints.NotNull;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
@NoArgsConstructor
public class AccountModel {
	
	@NonNull
	@NotNull
	private String iban;
	
	@NonNull
	@NotNull
	private Double amount;

}
