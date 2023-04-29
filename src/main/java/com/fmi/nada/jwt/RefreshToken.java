package com.fmi.nada.jwt;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

import javax.persistence.Id;

@AllArgsConstructor
@Getter
@RedisHash(value = "jwtToken", timeToLive = 60 * 60 * 24 * 7)
public class RefreshToken {

    @Id
    private Long id;

    private String refreshToken;

    @Indexed
    private String userName;

}
