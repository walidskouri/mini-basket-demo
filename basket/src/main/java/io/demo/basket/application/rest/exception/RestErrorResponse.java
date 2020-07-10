package io.demo.basket.application.rest.exception;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.demo.basket.domain.exception.SubError;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.OffsetDateTime;
import java.util.List;

@Data
@Builder(toBuilder = true)
@AllArgsConstructor
public class RestErrorResponse {

    // HTTP Response Status Code
    @JsonIgnore
    private int httpStatus;

    // General Error message
    private String message;

    @JsonProperty(value = "detailed_message")
    private String detailedMessage;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy hh:mm:ss")
    private OffsetDateTime timestamp;

    @JsonIgnore
    private Throwable throwable;

    // Internal code
    @JsonProperty(value = "error_code")
    private int errorCode;

    @JsonProperty(value = "sub_errors")
    private List<SubError> subErrors = null;
}
