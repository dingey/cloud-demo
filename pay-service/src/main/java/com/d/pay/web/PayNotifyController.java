package com.d.pay.web;

import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

public class PayNotifyController {
    @RequestMapping(path = "/pay/notify")
    public void notify(HttpServletRequest request) {

    }
}
