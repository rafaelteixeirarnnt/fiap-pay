package br.com.fiap.fiappay.controllers.exceptions;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class ErrorResponse {

    private String message;
    private int status;
    private String log;
    private LocalDateTime timestamp;
    private String errorCode;
    private String path;

}
