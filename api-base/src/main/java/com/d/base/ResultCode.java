package com.d.base;

import lombok.Getter;

@Getter
public enum ResultCode {
    SUCCESS(0, "成功"), //
    FAIL(1, "失败"),
    SERVER_ERROR(2, "系统错误，请稍后再试"),
    SERVER_BUSY(3, "服务器繁忙，请稍后再试"),
    LOGIN_TIMEOUT(4, "登陆超时"),
    INVENTORY_SHORTAGE(5, "库存不足"),
    UNSUPPORTED_PAY(6, "不支持的支付方式");

    private int code;
    private String message;

    ResultCode(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public static ResultCode fromCode(int code) {
        for (ResultCode rc : values()) {
            if (rc.getCode() == code) {
                return rc;
            }
        }
        return null;
    }
}
