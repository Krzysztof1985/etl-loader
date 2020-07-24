package com.superdevs.etlloader.wrappers;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;

import java.util.Collection;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_EMPTY;
import static lombok.AccessLevel.PRIVATE;
import static org.springframework.http.HttpStatus.UNPROCESSABLE_ENTITY;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(NON_EMPTY)
public class ResponseWrapper<T> {
    @ApiModelProperty(name = "Http status code", required = true, example = "200")
    @NonNull
    int httpStatusCode;

    String requestId;
    @ApiModelProperty(name = "Request id", required = true, example = "529269fb1459")

    @JsonUnwrapped
    @NonNull
    Message rootMessage;

    @ApiModelProperty(name = "List of errors", required = false)
    Collection<? extends Message> errors;

    @ApiModelProperty(name = "List of additional messages", required = false)
    Collection<Message> messages;

    public static ResponseWrapperBuilder<Void> ok() {
        return ResponseWrapper.<Void>builder()
                .httpStatusCode(HttpStatus.OK.value());
    }

    public static ResponseWrapperBuilder<Void> badRequest() {
        return ResponseWrapper.<Void>builder()
                .httpStatusCode(HttpStatus.BAD_REQUEST.value());
    }

    public static ResponseWrapperBuilder<Void> forbidden() {
        return ResponseWrapper.<Void>builder()
                .httpStatusCode(HttpStatus.FORBIDDEN.value());
    }

    public static ResponseWrapperBuilder<Void> unprocessableEntity() {
        return ResponseWrapper.<Void>builder()
                .httpStatusCode(UNPROCESSABLE_ENTITY.value());
    }

    public static ResponseWrapperBuilder<Void> notFound() {
        return ResponseWrapper.<Void>builder()
                .httpStatusCode(HttpStatus.NOT_FOUND.value());
    }

    public static ResponseWrapperBuilder<Void> internalServerError() {
        return ResponseWrapper.<Void>builder()
                .httpStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
    }

    @AllArgsConstructor
    @Getter
    @FieldDefaults(level = PRIVATE)
    public static class Message {
        @NonNull
        String code;
        @NonNull
        String reason;
        @NonNull
        String message;

        public static Message of(String code, String reason, String message) {
            return new Message(code, reason, message);
        }
    }

    @Getter
    @FieldDefaults(level = PRIVATE)
    @JsonInclude(NON_EMPTY)
    public static class ValidationError extends Message {
        String field;

        private ValidationError(String code, String reason, String message, String field) {
            super(code, reason, message);
            this.field = field;
        }

        public static ValidationError of(String code, String reason, String message, String field) {
            return new ValidationError(code, reason, message, field);
        }
    }
}
