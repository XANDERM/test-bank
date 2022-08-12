package es.test.bank.exception;

import lombok.Getter;

/**
 * <p>
 * TestBankBusinessException class.
 * </p>
 *
 * @author Grupo TestBank
 * @version 0.0.1
 */
@Getter
public class TestBankBusinessException extends TestBankException {

    private static final long serialVersionUID = -2634207060501411224L;

    private String errorCode;
    private String description;

    /**
     * Constructor for TestBankBusinessException.
     *
     * @param errorCode a {@link String} object.
     */
    public TestBankBusinessException(String errorCode, String description) {
        super(errorCode);
        this.errorCode = errorCode;
        this.description = description;
    }

}
