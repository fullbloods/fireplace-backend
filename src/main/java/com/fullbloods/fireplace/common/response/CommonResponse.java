package com.fullbloods.fireplace.common.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CommonResponse<T> {
    private int code;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private T data;

    private boolean success;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String message;
}