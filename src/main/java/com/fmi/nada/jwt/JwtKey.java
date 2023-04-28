package com.fmi.nada.jwt;

import io.jsonwebtoken.security.Keys;
import org.springframework.data.util.Pair;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Map;
import java.util.Random;

/**
 * JWT Key 제공 및 조회
 */
public class JwtKey {

    private static final Map<String, String> SECRET_KEY_SET = Map.of(
            "key1", "prcU5TO3JUFPIrVpgaZoz5VV4EcCUdNh",
            "key2", "2BMwMxZaTNlpC5qtTBCSIjkII96thKC4",
            "key3", "bffJVtA68nyOQvZeFn4AFvu8iLG1XpdH"
    );

    private static final String[] KID_SET = SECRET_KEY_SET.keySet().toArray(new String[0]);
    private static Random randomIndex = new Random();

    /**
     * SECRET_KEY_SET에서 랜덤한 KEY 가져오기
     *
     * @return kid와 key 쌍
     */
    public static Pair<String, Key> getRandomKey() {
        String kid = KID_SET[randomIndex.nextInt(KID_SET.length)];
        String secretKey = SECRET_KEY_SET.get(kid);

        return Pair.of(kid, Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8)));
    }

    /**
     * kid로 Key찾기
     *
     * @param kid kid
     * @return key
     */
    public static Key getKey(String kid) {
        String key = SECRET_KEY_SET.getOrDefault(kid, null);

        if (key == null)
            return null;
        return Keys.hmacShaKeyFor(key.getBytes(StandardCharsets.UTF_8));
    }

}
