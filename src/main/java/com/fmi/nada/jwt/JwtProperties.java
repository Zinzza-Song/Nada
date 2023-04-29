package com.fmi.nada.jwt;

/**
 * JWT 기본 설정값
 */
public class JwtProperties {

    public static final int EXPIRATION_TIME = 1800000; // 30분
    public static final int EXPIRATION_REFRESH_TIME = 604800000; // 7일
    public static final String COOKIE_NAME = "JWT-AUTHENTICATION";

}
