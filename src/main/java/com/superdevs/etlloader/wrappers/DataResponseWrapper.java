package com.superdevs.etlloader.wrappers;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;

import java.util.Collection;
import java.util.List;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_EMPTY;
import static java.lang.Math.toIntExact;
import static lombok.AccessLevel.PRIVATE;

@NoArgsConstructor
@Data
@FieldDefaults(level = PRIVATE)
@JsonInclude(NON_EMPTY)
public class DataResponseWrapper<T> extends ResponseWrapper<T> {

    @ApiModelProperty(name = "Number of entities returned", required = true, example = "1")
    Integer count;

    @ApiModelProperty(name = "Data wrapper", required = true)
    Collection<T> data;

    @Builder(builderMethodName = "dataResponseWrapperBuilder")
    private DataResponseWrapper(int httpStatusCode, String requestId, Message rootMessage, Collection<? extends Message> errors, Collection<Message> messages, Long count, Collection<T> data) {
        super(httpStatusCode, requestId, rootMessage, errors, messages);
        this.count = toIntExact(count);
        this.data = data;
    }

    public static <T> DataResponseWrapperBuilder<T> ok(T t) {
        return DataResponseWrapper.<T>dataResponseWrapperBuilder()
                .httpStatusCode(HttpStatus.OK.value())
                .data(List.of(t))
                .count(1L);
    }

    public static <T> DataResponseWrapperBuilder<T> ok(Collection<T> results) {
        return DataResponseWrapper.<T>dataResponseWrapperBuilder()
                .httpStatusCode(HttpStatus.OK.value())
                .data(results)
                .count((long) results.size());
    }
}
