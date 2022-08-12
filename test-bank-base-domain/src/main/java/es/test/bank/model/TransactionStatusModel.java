package es.test.bank.model;

import es.test.bank.enumeration.Channel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@AllArgsConstructor
@EqualsAndHashCode
public class TransactionStatusModel {
	
	private String reference;

	private Channel channel;

}
