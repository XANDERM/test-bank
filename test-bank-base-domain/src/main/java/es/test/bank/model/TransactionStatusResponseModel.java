package es.test.bank.model;

import es.test.bank.enumeration.Status;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class TransactionStatusResponseModel {
	
	  private String reference;

	  private Status status;

	  private Double amount;

	  private Double fee;

}
