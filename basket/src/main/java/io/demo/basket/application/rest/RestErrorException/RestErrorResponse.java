package io.demo.basket.application.rest.RestErrorException;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

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
}
