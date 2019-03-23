package com.d.pay.core;

import lombok.Getter;

/**
 * 支付渠道
 */
@Getter
public enum PayChannel {
    NONE(0), ALIPAY(1), WXPAY(2);
    private int code;
    private Terminal[] terminal;

    PayChannel(int code) {
        this.code = code;
    }

    PayChannel(int code, Terminal... terminal) {
        this.code = code;
        this.terminal = terminal;
    }

    public boolean support(Terminal terminalEnum) {
        for (Terminal t : getTerminal()) {
            if (t.ordinal() == terminalEnum.ordinal()) return true;
        }
        return false;
    }

}
