package ru.practicum.ewm.exception;

public class EwmException extends RuntimeException {
    public EwmException() {
        super();
    }

    public EwmException(String message) {
        super(message);
    }

    public EwmException(String message, Throwable cause) {
        super(message, cause);
    }

    public EwmException(Throwable cause) {
        super(cause);
    }

    protected EwmException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
