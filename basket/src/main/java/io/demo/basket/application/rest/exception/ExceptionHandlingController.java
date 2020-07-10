package io.demo.basket.application.rest.exception;

import io.demo.basket.domain.exception.BasketException;
import io.demo.basket.domain.exception.ErrorMessageType;
import io.demo.basket.domain.exception.SubError;
import io.demo.basket.infrastructure.util.logging.TracingConstant;
import org.springframework.beans.TypeMismatchException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static io.demo.basket.domain.exception.ErrorMessageType.GENERAL_ERROR_MESSAGE;
import static io.demo.basket.infrastructure.util.Utility.buildStacktraceForLog;
import static io.demo.basket.infrastructure.util.logging.TracingConstant.STACKTRACE;
import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;
import static org.apache.commons.collections4.CollectionUtils.isNotEmpty;
import static org.springframework.http.HttpStatus.*;


@RestControllerAdvice
public class ExceptionHandlingController extends ResponseEntityExceptionHandler {

    @ExceptionHandler(BasketException.class)
    protected ResponseEntity<Object> handleBasketException(final BasketException basketException) {
        RestErrorResponse errorMessage = createErrorMessage(basketException.getError(),
                singletonList(createSubError(basketException.getRejectedObjectName(), basketException.getMessage())));
        return new ResponseEntity<>(errorMessage, new HttpHeaders(), basketException.getError().getHttpStatus());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleAll(final Exception ex) {
        RestErrorResponse errorMessage = createErrorMessage(GENERAL_ERROR_MESSAGE,
                asList(createSubError(TracingConstant.MESSAGE, ex.toString()), createSubError(STACKTRACE, String.join("", buildStacktraceForLog(ex)))));
        return new ResponseEntity<>(errorMessage, new HttpHeaders(), INTERNAL_SERVER_ERROR);
    }

    @Override
    protected ResponseEntity<Object> handleServletRequestBindingException(ServletRequestBindingException ex,
                                                                          HttpHeaders headers,
                                                                          HttpStatus status,
                                                                          WebRequest request) {
        RestErrorResponse restErrorResponse = RestErrorResponse.builder()
                .httpStatus(status.value())
                .message(ex.getMessage())
                .build();
        return handleExceptionInternal(ex, restErrorResponse, headers, status, request);
    }

    @Override
    protected ResponseEntity<Object> handleMissingServletRequestParameter(MissingServletRequestParameterException ex,
                                                                          HttpHeaders headers,
                                                                          HttpStatus status,
                                                                          WebRequest request) {
        String message = ex.getParameterName() + ": parameter is missing";
        RestErrorResponse restErrorResponse = RestErrorResponse.builder()
                .httpStatus(BAD_REQUEST.value())
                .message("Type mismatch error")
                .detailedMessage(message)
                .build();

        return buildResponseEntity(restErrorResponse, headers);
    }

    @Override
    protected ResponseEntity<Object> handleHttpMediaTypeNotSupported(HttpMediaTypeNotSupportedException ex,
                                                                     HttpHeaders headers,
                                                                     HttpStatus status,
                                                                     WebRequest request) {
        StringBuilder builder = new StringBuilder();
        builder.append(ex.getContentType());
        builder.append(" media type is not supported. Supported media types are ");
        ex.getSupportedMediaTypes().forEach(t -> builder.append(t).append(", "));
        String error = builder.substring(0, builder.length() - 2);
        RestErrorResponse restErrorResponse = RestErrorResponse.builder()
                .httpStatus(UNSUPPORTED_MEDIA_TYPE.value())
                .message(error)
                .throwable(ex)
                .build();
        return buildResponseEntity(restErrorResponse, headers);
    }

    @Override
    protected ResponseEntity<Object> handleTypeMismatch(final TypeMismatchException ex,
                                                        final HttpHeaders headers,
                                                        final HttpStatus status,
                                                        final WebRequest request) {
        String moreInformation;
        if (ex instanceof MethodArgumentTypeMismatchException) {
            moreInformation = String.format("The parameter '%s' with value '%s' could not be converted to type '%s'",
                    ((MethodArgumentTypeMismatchException) ex).getName(), ex.getValue(), ex.getRequiredType().getSimpleName());
        } else {
            moreInformation = String.format("%s value for %s should be of type %s",
                    ex.getValue(), ex.getPropertyName(), ex.getRequiredType().getSimpleName());
        }

        RestErrorResponse restErrorResponse = RestErrorResponse.builder()
                .httpStatus(BAD_REQUEST.value())
                .message("Type mismatch error")
                .detailedMessage(moreInformation)
                .build();

        return buildResponseEntity(restErrorResponse, headers);
    }

    @Override
    protected ResponseEntity<Object> handleNoHandlerFoundException(final NoHandlerFoundException ex,
                                                                   final HttpHeaders headers,
                                                                   final HttpStatus status,
                                                                   final WebRequest request) {
        RestErrorResponse restErrorResponse = RestErrorResponse.builder()
                .httpStatus(NOT_FOUND.value())
                .message("Not found")
                .detailedMessage("No handler found for " + ex.getHttpMethod() + " " + ex.getRequestURL())
                .build();

        return buildResponseEntity(restErrorResponse, headers);
    }

    @Override
    protected ResponseEntity<Object> handleHttpRequestMethodNotSupported(final HttpRequestMethodNotSupportedException ex,
                                                                         final HttpHeaders headers,
                                                                         final HttpStatus status,
                                                                         final WebRequest request) {
        final StringBuilder error = new StringBuilder();
        error.append(ex.getMethod());
        error.append(" method is not supported for this request. Supported methods are ");
        ex.getSupportedHttpMethods().forEach(t -> error.append(t + " "));
        RestErrorResponse restErrorResponse = RestErrorResponse.builder()
                .httpStatus(METHOD_NOT_ALLOWED.value())
                .message(error.toString())
                .throwable(ex)
                .build();
        return buildResponseEntity(restErrorResponse, headers);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                  HttpHeaders headers,
                                                                  HttpStatus status,
                                                                  WebRequest request) {
        RestErrorResponse restErrorResponse = RestErrorResponse.builder()
                .httpStatus(BAD_REQUEST.value())
                .message("Validation error")
                .build();
        if (isNotEmpty(ex.getBindingResult().getFieldErrors())) {
            restErrorResponse.getSubErrors().addAll(fieldErrorsToSubErrors(ex.getBindingResult().getFieldErrors()));
        }
        if (isNotEmpty(ex.getBindingResult().getGlobalErrors())) {
            restErrorResponse.getSubErrors().addAll(objectErrorsToSubErrors(ex.getBindingResult().getGlobalErrors()));
        }

        return buildResponseEntity(restErrorResponse, headers);
    }

    /**
     * Handle HttpMessageNotReadableException. Happens when request JSON is malformed.
     */
    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex,
                                                                  HttpHeaders headers,
                                                                  HttpStatus status,
                                                                  WebRequest request) {
        RestErrorResponse restErrorResponse = RestErrorResponse.builder()
                .httpStatus(BAD_REQUEST.value())
                .message("Malformed JSON request")
                .detailedMessage("Invalid JSON payload received")
                .build();

        return buildResponseEntity(restErrorResponse, headers);
    }

    /**
     * Handle HttpMessageNotWritableException.
     */
    @Override
    protected ResponseEntity<Object> handleHttpMessageNotWritable(HttpMessageNotWritableException ex,
                                                                  HttpHeaders headers,
                                                                  HttpStatus status,
                                                                  WebRequest request) {
        RestErrorResponse restErrorResponse = RestErrorResponse.builder()
                .httpStatus(INTERNAL_SERVER_ERROR.value())
                .message("Error writing JSON output")
                .detailedMessage("Failed to write HTTP message. No converter found for return value")
                .build();

        return buildResponseEntity(restErrorResponse, headers);
    }

    /**
     * Handle DataIntegrityViolationException, inspects the cause for different DB causes.
     */
    @ExceptionHandler(DataIntegrityViolationException.class)
    protected ResponseEntity<Object> handleDataIntegrityViolation(DataIntegrityViolationException ex) {
        RestErrorResponse restErrorResponse = RestErrorResponse.builder()
                .httpStatus(INTERNAL_SERVER_ERROR.value())
                .message("Database error")
                .detailedMessage("Requested operation resulted in a violation of a defined integrity constraint")
                .build();

        return buildResponseEntity(restErrorResponse);
    }

    private ResponseEntity<Object> buildResponseEntity(RestErrorResponse restErrorResponse, HttpHeaders headers) {
        return new ResponseEntity<>(restErrorResponse, headers, valueOf(restErrorResponse.getHttpStatus()));
    }

    private ResponseEntity<Object> buildResponseEntity(RestErrorResponse restErrorResponse) {
        return buildResponseEntity(restErrorResponse, new HttpHeaders());
    }

    private List<SubError> fieldErrorsToSubErrors(List<FieldError> fieldErrors) {
        return fieldErrors.stream()
                .map(fieldError -> createSubError(fieldError.getObjectName(), fieldError.getDefaultMessage(), fieldError.getField()))
                .collect(Collectors.toList());
    }

    private List<SubError> objectErrorsToSubErrors(List<ObjectError> objectErrors) {
        return objectErrors.stream()
                .map(fieldError -> createSubError(fieldError.getObjectName(), fieldError.getDefaultMessage()))
                .collect(Collectors.toList());
    }

    private RestErrorResponse createErrorMessage(ErrorMessageType error, List<SubError> subErrors) {
        RestErrorResponse.RestErrorResponseBuilder errorResponseBuilder = RestErrorResponse.builder();
        if (error != null) {
            errorResponseBuilder.errorCode(error.getCode())
                    .timestamp(OffsetDateTime.now())
                    .detailedMessage(error.getMessage())
                    .message(error.getHttpStatus() != null ? error.getHttpStatus().getReasonPhrase() : null);

        }
        errorResponseBuilder.subErrors(subErrors);
        return errorResponseBuilder.build();
    }

    private SubError createSubError(String object, String message) {
        return createSubError(object, message, null);
    }

    private SubError createSubError(String object, String message, String field) {
        SubError subError = new SubError();
        subError.setObject(object);
        subError.setMessage(message);
        subError.setField(field);
        return subError;
    }
}
