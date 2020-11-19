package br.com.spintec.wstiss.exception;

public class BuilderException extends RuntimeException {

    /**
     *
     */
    private static final long serialVersionUID = 4692990520420522395L;

    public BuilderException(String message, Throwable cause) {
        super(message, cause);
    }

    public BuilderException(String message) {
        super(message);
    }

}
