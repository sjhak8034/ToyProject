package com.example.toyproject.security.service;

import com.example.toyproject.security.oauth_mapper.OAuth2Attributes;
import com.example.toyproject.user.entity.User;
import com.example.toyproject.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final UserRepository userRepository; // 사용자 정보 저장용 레포지토리 (구현 필요)

    public CustomOAuth2UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) {
        OAuth2User oAuth2User = super.loadUser(userRequest);

        // 제공자(provider) 정보 추출
        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        String userNameAttributeName = userRequest.getClientRegistration()
                .getProviderDetails().getUserInfoEndpoint().getUserNameAttributeName();

        // OAuth2UserInfo 객체 생성
        OAuth2Attributes attributes = OAuth2Attributes.of(
                registrationId, userNameAttributeName, oAuth2User.getAttributes());

        // 사용자 정보 저장 또는 업데이트
        User user = saveOrUpdate(attributes);

        // 사용자 정보와 권한을 포함한 객체 반환
        return new DefaultOAuth2User(
                Collections.singleton(new SimpleGrantedAuthority(user.getRole().getValue())),
                attributes.getAttributes(),
                attributes.getNameAttributeKey());
    }

    private User saveOrUpdate(OAuth2Attributes attributes) {
        User user = userRepository.findByEmail(attributes.getEmail())
                .map(entityUser -> {
                    // 명시적으로 update 호출 후 객체 반환
                    entityUser.update(attributes.getName(), attributes.getPicture());
                    return entityUser;
                })
                .orElse(attributes.toEntity());

        return userRepository.save(user);
    }
}