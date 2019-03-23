package com.d.util;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ServletUtil {
    public static HttpServletRequest getRequest() {
        return ((ServletRequestAttributes) (RequestContextHolder.currentRequestAttributes())).getRequest();
    }

    public static HttpServletResponse getResponse() {
        return ((ServletRequestAttributes) (RequestContextHolder.currentRequestAttributes())).getResponse();
    }

    public static long currentUserId() {
        String header = getRequest().getHeader("user-token");
        UserToken token = UserToken.fromTokenString(header);
        if (token == null || !token.valid()) {
            throw new RuntimeException("未授权");
        }
        return token.getId();
    }
}
