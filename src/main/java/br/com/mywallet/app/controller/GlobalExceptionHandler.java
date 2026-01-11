package br.com.mywallet.app.controller;

import br.com.mywallet.app.domain.exceptions.RegraDeNegocioException;
import br.com.mywallet.app.domain.exceptions.ResourceNotFoundException;
import br.com.mywallet.app.domain.model.ApiError.ApiErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiErrorResponse> handleRecursoNaoEncontrado(
            ResourceNotFoundException e,
            HttpServletRequest request
    ) {
        ApiErrorResponse errorResponse = new ApiErrorResponse(
                HttpStatus.NOT_FOUND.value(),
                "Dados não encontrados.",
                e.getMessage(),
                request.getRequestURI()
        );

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }

    @ExceptionHandler(RegraDeNegocioException.class)
    public ResponseEntity<ApiErrorResponse> handleRegraDeNegocio(
            RegraDeNegocioException e,
            HttpServletRequest request
    ) {
        ApiErrorResponse errorResponse = new ApiErrorResponse(
                HttpStatus.NOT_FOUND.value(),
                "Violação de regra de negócio.",
                e.getMessage(),
                request.getRequestURI()
        );

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }
}
