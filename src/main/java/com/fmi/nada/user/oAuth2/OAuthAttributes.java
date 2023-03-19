package com.fmi.nada.user.oAuth2;

import com.fmi.nada.user.Member;
import lombok.Builder;
import lombok.Getter;

import java.util.Map;

@Getter
@Builder
public class OAuthAttributes {

    private Map<String, Object> attributes;
    private String nameAttributeKey;
    private String name;
    private String email;
    private String provider;

    public static OAuthAttributes of(
            String registrationId,
            String userNameAttributeName,
            Map<String, Object> attributes) {
//        if ("naver".equals(registrationId))
//            return ofNaver("id", attributes);
//        else if ("kakao".equals(registrationId))
//            return ofKakao("id", attributes);

        return ofGoogle(userNameAttributeName, attributes);
    }

    private static OAuthAttributes ofGoogle(String userNameAttributeName, Map<String, Object> attributes) {
        return OAuthAttributes.builder()
                .name((String) attributes.get("name"))
                .email((String) attributes.get("email"))
                .provider("Google")
                .attributes(attributes)
                .nameAttributeKey(userNameAttributeName)
                .build();
    }

//    private static OAuthAttributes ofNaver(String userNameAttributeName, Map<String, Object> attributes) {
//        Map<String, Object> response = (Map<String, Object>) attributes.get("response");
//
//        return OAuthAttributes.builder()
//                .name((String) attributes.get("name"))
//                .email((String) attributes.get(("email")))
//                .provider("Naver")
//                .attributes(response)
//                .nameAttributeKey(userNameAttributeName)
//                .build();
//    }
//
//    private static OAuthAttributes ofKakao(String userNameAttributeName, Map<String, Object> attributes) {
//        Map<String, Object> response = (Map<String, Object>) attributes.get("kakao_account");
//        Map<String, Object> account = (Map<String, Object>) attributes.get("profile");
//
//        return OAuthAttributes.builder()
//                .name((String) account.get("nickname"))
//                .email((String) response.get("email"))
//                .provider("Kakao")
//                .attributes(response)
//                .nameAttributeKey(userNameAttributeName)
//                .build();
//    }

    public Member toEntity() {
        return Member.builder()
                .memberName(name)
                .username(email)
                .authority("ROLE_USER")
                .provider(provider)
                .build();
    }

}
