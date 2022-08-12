package es.test.bank.enumeration;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Status {
		  
	PENDING("PENDING"),
  
	SETTLED("SETTLED"),
  
	FUTURE("FUTURE"),
  
	INVALID("INVALID");

	private String value;

}
