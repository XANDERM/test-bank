package es.test.bank.model;

import java.time.OffsetDateTime;

import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * The type product.
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TransactionModel {
	
	  private String reference;

	  @NotNull
	  private String accountIban;

	  private OffsetDateTime date;

	  @NotNull
	  private Double amount;

	  private Double fee;

	  private String description;


}
