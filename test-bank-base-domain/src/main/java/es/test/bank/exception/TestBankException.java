package es.test.bank.exception;

public class TestBankException extends RuntimeException {

    /**
     * The Constant serialVersionUID.
     */
    private static final long serialVersionUID = -6788661443546955762L;

    /**
     * The error code.
     */
    private final String errorCode;

    /**
     * <p>
     * Constructor for TestBankException.
     * </p>
     *
     * @param errorCode a {@link String} object.
     */
    public TestBankException(String errorCode) {
        this.errorCode = errorCode;
    }

    /**
     * <p>
     * Constructor for TestBankException (errorCode, message)
     * </p>.
     *
     * @param errorCode a {@link String} string.
     * @param message   a {@link String} message.
     */
    public TestBankException(String errorCode, String message) {
        super(message, null, false, false);
        this.errorCode = errorCode;
    }

    /**
     * <p>
     * Getter for the field <code>id</code>.
     * </p>
     *
     * @return a {@link Long} object.
     */
    public String getErrorCode() {
        return errorCode;
    }

}
