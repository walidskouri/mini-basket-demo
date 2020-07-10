package io.demo.basket.domain.exception;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.validation.annotation.Validated;

import java.io.Serializable;
import java.util.Objects;


@Validated
public class SubError implements Serializable {
    @JsonProperty("object")
    private String object = null;

    @JsonProperty("field")
    private String field = null;

    @JsonProperty("rejected_value")
    private String rejectedValue = null;

    @JsonProperty("message")
    private String message = null;

    public SubError object(String object) {
        this.object = object;
        return this;
    }

    /**
     * The object containing the erroneous field.
     *
     * @return object
     **/
    @ApiModelProperty(example = "oder", value = "The object containing the erroneous field.")


    public String getObject() {
        return object;
    }

    public void setObject(String object) {
        this.object = object;
    }

    public SubError field(String field) {
        this.field = field;
        return this;
    }

    /**
     * The erroneous field.
     *
     * @return field
     **/
    @ApiModelProperty(example = "order_id", value = "The erroneous field.")


    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public SubError rejectedValue(String rejectedValue) {
        this.rejectedValue = rejectedValue;
        return this;
    }

    /**
     * The erroneous value.
     *
     * @return rejectedValue
     **/
    @ApiModelProperty(value = "The erroneous value.")


    public String getRejectedValue() {
        return rejectedValue;
    }

    public void setRejectedValue(String rejectedValue) {
        this.rejectedValue = rejectedValue;
    }

    public SubError message(String message) {
        this.message = message;
        return this;
    }

    /**
     * Description message.
     *
     * @return message
     **/
    @ApiModelProperty(example = "order_id can't be null", value = "Description message.")


    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        SubError subError = (SubError) o;
        return Objects.equals(this.object, subError.object) &&
                Objects.equals(this.field, subError.field) &&
                Objects.equals(this.rejectedValue, subError.rejectedValue) &&
                Objects.equals(this.message, subError.message);
    }

    @Override
    public int hashCode() {
        return Objects.hash(object, field, rejectedValue, message);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class SubError {\n");

        sb.append("    object: ").append(toIndentedString(object)).append("\n");
        sb.append("    field: ").append(toIndentedString(field)).append("\n");
        sb.append("    rejectedValue: ").append(toIndentedString(rejectedValue)).append("\n");
        sb.append("    message: ").append(toIndentedString(message)).append("\n");
        sb.append("}");
        return sb.toString();
    }

    /**
     * Convert the given object to string with each line indented by 4 spaces
     * (except the first line).
     */
    private String toIndentedString(Object o) {
        if (o == null) {
            return "null";
        }
        return o.toString().replace("\n", "\n    ");
    }
}

