package es.test.bank.enumeration;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorLevel {

    /**
     * The error.
     */
    ERROR("ERROR", "Error level"),
    /**
     * The fatal.
     */
    FATAL("FATAL", "Fatal error"),
    /**
     * The info.
     */
    INFO("INFO", "Info error"),
    /**
     * The warning.
     */
    WARNING("WARNING", "Warning error");

    /**
     * Code of error level.
     */
    private String code;

    /**
     * Description of error level.
     */
    private String description;

}
