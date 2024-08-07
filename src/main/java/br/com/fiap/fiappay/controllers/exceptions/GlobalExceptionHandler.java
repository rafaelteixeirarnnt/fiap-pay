package br.com.fiap.fiappay.controllers.exceptions;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ValidationErrorResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex, HttpServletRequest request) {

        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;

        List<String> errors = ex.getBindingResult().getFieldErrors().stream()
                .map(FieldError::getDefaultMessage)
                .collect(Collectors.toList());

        String log = extractLogSnippet(ex);
        LocalDateTime timestamp = LocalDateTime.now();
        String errorCode = "VALIDATION_ERROR";
        String path = request.getRequestURI();

        ValidationErrorResponse errorResponse = new ValidationErrorResponse("Falha na validação", status.value(), log, errors, timestamp, errorCode, path);
        return new ResponseEntity<>(errorResponse, status);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleAllExceptions(Exception ex, HttpServletRequest request) {
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        String log = extractLogSnippet(ex);
        LocalDateTime timestamp = LocalDateTime.now();
        String errorCode = "INTERNAL_SERVER_ERROR";
        String path = request.getRequestURI();

        ErrorResponse errorResponse = new ErrorResponse(ex.getMessage(), status.value(), log, timestamp, errorCode, path);
        return new ResponseEntity<>(errorResponse, status);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ValidationErrorResponse> handleConstraintViolationException(ConstraintViolationException ex, HttpServletRequest request) {
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        var errors = ex.getConstraintViolations().stream()
                .map(ConstraintViolation::getMessage)
                .collect(Collectors.toList());

        String log = extractLogSnippet(ex);
        LocalDateTime timestamp = LocalDateTime.now();
        String errorCode = "VALIDATION_ERROR";
        String path = request.getRequestURI();

        var errorResponse = new ValidationErrorResponse("Validation failed", status.value(), log, errors, timestamp, errorCode, path);
        return new ResponseEntity<>(errorResponse, status);
    }

    @ExceptionHandler(NegocioException.class)
    public ResponseEntity<ErrorResponse> handleNegocioException(NegocioException ex, HttpServletRequest request) {
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        String log = extractLogSnippet(ex);
        LocalDateTime timestamp = LocalDateTime.now();
        String errorCode = "BUSINESS_ERROR";
        String path = request.getRequestURI();

        ErrorResponse errorResponse = new ErrorResponse(ex.getMessage(), status.value(), log, timestamp, errorCode, path);
        return new ResponseEntity<>(errorResponse, status);
    }

    @ExceptionHandler(SemLimiteException.class)
    public ResponseEntity<ErrorResponse> handleSemLimiteException(SemLimiteException ex, HttpServletRequest request) {
        HttpStatus status = HttpStatus.PAYMENT_REQUIRED;
        String log = extractLogSnippet(ex);
        LocalDateTime timestamp = LocalDateTime.now();
        String errorCode = "PAYMENT_REQUIRED";
        String path = request.getRequestURI();

        ErrorResponse errorResponse = new ErrorResponse(ex.getMessage(), status.value(), log, timestamp, errorCode, path);
        return new ResponseEntity<>(errorResponse, status);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorResponse> handleNegocioException(DataIntegrityViolationException ex, HttpServletRequest request) {
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        String log = Objects.requireNonNull(ex.getRootCause()).getMessage();
        LocalDateTime timestamp = LocalDateTime.now();
        String errorCode = "BUSINESS_ERROR";
        String path = request.getRequestURI();

        ErrorResponse errorResponse = new ErrorResponse("Falha ao salvar dados", status.value(), log, timestamp, errorCode, path);
        return new ResponseEntity<>(errorResponse, status);
    }

    private String extractLogSnippet(Exception ex) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        ex.printStackTrace(pw);
        return sw.toString().split("\n")[0];
    }
}
