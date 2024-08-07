package br.com.fiap.fiappay.controllers.exceptions;

public class SemLimiteException extends RuntimeException {

    public SemLimiteException(String message) {
        super(message);
    }

}
