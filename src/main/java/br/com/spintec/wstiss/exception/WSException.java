package br.com.spintec.wstiss.exception;

public class WSException extends RuntimeException {

    /**
     *
     */
    private static final long serialVersionUID = -7380301922193798357L;

    public WSException(String message, Throwable cause) {
        super(message, cause);
    }
}
