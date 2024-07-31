package br.com.fiap.fiappay.controllers.exceptions;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class ValidationErrorResponse extends ErrorResponse {

    private List<String> errors;

    public ValidationErrorResponse(String message, int status, String log, List<String> errors, LocalDateTime timestamp,
                                   String errorCode, String path) {
        super(message, status, log, timestamp, errorCode, path);
        this.errors = errors;
    }

}
