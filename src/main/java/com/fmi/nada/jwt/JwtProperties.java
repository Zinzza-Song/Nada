package com.fmi.nada.jwt;

/**
 * JWT 기본 설정값
 */
public class JwtProperties {

    public static final int EXPIRATION_TIME = 3600000; // 1시간
    public static final String COOKIE_NAME = "JWT-AUTHENTICATION";

}
