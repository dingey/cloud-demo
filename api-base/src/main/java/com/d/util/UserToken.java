package com.d.util;

import com.di.kit.AESUtil;

public class UserToken {
    // uid
    private Long id;
    // 过期时间秒
    private Long expired;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getExpired() {
        return expired;
    }

    public void setExpired(Long expired) {
        this.expired = expired;
    }

    public boolean expired() {
        return expired != null && expired > 0 && expired < System.currentTimeMillis() / 1000;
    }

    public boolean valid() {
        return id != null && !expired();
    }

    public String tokenString() {
        return AESUtil.encrypt(JsonUtil.single().toJson(this));
    }

    public static UserToken fromTokenString(String tokenStr) {
        String decrypt = AESUtil.decrypt(tokenStr);
        return JsonUtil.single().fromJson(decrypt, UserToken.class);
    }

    @Override
    public String toString() {
        return "{id:" + id + ",expired:" + expired + "}";
    }
}
