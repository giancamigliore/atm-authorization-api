package com.itti.digital.atm.atm_authorization_api.errors;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.slf4j.Logger;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


@Order(Ordered.HIGHEST_PRECEDENCE)
@ControllerAdvice
public class MsBaseExceptionHandler extends ResponseEntityExceptionHandler {

    final static Logger log = org.slf4j.LoggerFactory.getLogger(MsBaseExceptionHandler.class);

    /**
     * Maneja todas las excepciones del tipo AlfaMsException qye fueron lanzadas desde los micro-servicios
     *
     * @param aex Excepción lanzada por un método
     * @return ResponseEntity con el objeto ErrorBody en su body
     */
    @ExceptionHandler(AlfaMsException.class)
    public ResponseEntity<Object> handleAlfaMsException(AlfaMsException aex) {

        HttpStatus httpStatus;
        switch (aex.getType()) {
            case NOT_FOUND:
                httpStatus = HttpStatus.NOT_FOUND;
                break;
            case APPLICATION:
                httpStatus = HttpStatus.UNPROCESSABLE_ENTITY;
                break;
            case SECURITY:
                httpStatus = HttpStatus.UNAUTHORIZED;
                break;
            case COMMUNICATION_UNAVAILABLE:
                httpStatus = HttpStatus.SERVICE_UNAVAILABLE;
                break;
            case COMMUNICATION:
            case DATABASE:
            case INTERNAL:
            default:
                httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
                break;
        }

        return getResponseEntity(aex, httpStatus, aex.getType(), aex.getErrorCode(), aex.getErrorMessage());
    }

    @ExceptionHandler({ConstraintViolationException.class})
    public ResponseEntity<Object> handleConstraintViolation(ConstraintViolationException ex, WebRequest request) {
        List<String> errors = new ArrayList<>();
        for (ConstraintViolation<?> violation : ex.getConstraintViolations()) {
            errors.add(violation.getRootBeanClass().getName() + " " +
                    violation.getPropertyPath() + ": " + violation.getMessage());
        }
        String errorMessage = String.join("; ", errors);
        logger.error(errorMessage);
        return getResponseEntity(ex, HttpStatus.BAD_REQUEST, AlfaMsTypeException.APPLICATION, null, errorMessage);
    }

    @ExceptionHandler({MethodArgumentTypeMismatchException.class})
    public ResponseEntity<Object> handleMethodArgumentTypeMismatch(MethodArgumentTypeMismatchException ex, WebRequest request) {
        String error = "Parámetro " + ex.getName() + " debe ser del tipo " + Objects.requireNonNull(ex.getRequiredType()).getSimpleName();
        return getResponseEntity(ex, HttpStatus.BAD_REQUEST, AlfaMsTypeException.APPLICATION, null, error);
    }



    private ResponseEntity<Object> getResponseEntity(Throwable e, HttpStatus status, AlfaMsTypeException alfaMsTypeException, String errorCode, String errorMessage) {
        log.error("ERROR AL OBTENER RESPONSE ENTITY: ",e);
        ErrorBody errorBody = new ErrorBody();
        errorBody.setCode(errorCode);
        errorBody.setType(alfaMsTypeException);
        errorBody.setMessage(errorMessage);
        return new ResponseEntity<>(errorBody, status);
    }
	
}
