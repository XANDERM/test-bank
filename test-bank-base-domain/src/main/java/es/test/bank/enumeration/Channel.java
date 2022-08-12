package es.test.bank.enumeration;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Channel {

	CLIENT("CLIENT"),
	  
	ATM("ATM"),
  
	INTERNAL("INTERNAL");

	private String value;
}
