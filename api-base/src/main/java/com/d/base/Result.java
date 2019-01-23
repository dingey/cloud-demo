package com.d.base;

import com.d.util.JsonUtil;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.beans.Transient;

@Slf4j
@Getter
@Setter
@SuppressWarnings({"unused","unchecked"})
public class Result<T> {
    private int code;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String message;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private T data;

    public Result(int code, T data, String message) {
        this.code = code;
        this.data = data;
        this.message = message;
    }

    public static <T> Result success(T data) {
        return new Result(0, data, null);
    }

    public static <T> Result fail(int code) {
        if (code < 1) {
            throw new RuntimeException("code must large than 0.");
        }
        ResultCode resultCode = ResultCode.fromCode(code);
        if (resultCode != null) {
            return fail(resultCode);
        }
        return new Result(code, null, null);
    }

    public static <T> Result fail(String message) {
        return new Result(0, null, message);
    }

    public static <T> Result fail(ResultCode code) {
        return new Result(code.getCode(), null, code.getMessage());
    }

    @Transient
    public boolean success() {
        return code == 0;
    }

    @Override
    public String toString() {
        return JsonUtil.single().toJson(this);
    }
}
