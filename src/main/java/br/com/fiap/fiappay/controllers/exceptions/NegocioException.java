package br.com.fiap.fiappay.controllers.exceptions;

public class NegocioException extends RuntimeException {

    public NegocioException(String message) {
        super(message);
    }

}
