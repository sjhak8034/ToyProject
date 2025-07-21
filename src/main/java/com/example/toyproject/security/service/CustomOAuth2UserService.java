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
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;

@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final UserRepository userRepository; // 사용자 정보 저장용 레포지토리 (구현 필요)

    public CustomOAuth2UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // OAuth2 로그인 시 유저 정보를 가져오는 메소드
    @Override
    @Transactional
    public OAuth2User loadUser(OAuth2UserRequest userRequest) {
        OAuth2User oAuth2User = super.loadUser(userRequest);

        // 제공자(provider) 정보 추출
        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        String userNameAttributeName = userRequest.getClientRegistration()
                .getProviderDetails().getUserInfoEndpoint().getUserNameAttributeName();

        // OAuth2UserInfo 객체 생성
        OAuth2Attributes attributes = OAuth2Attributes.of(
                registrationId, userNameAttributeName, oAuth2User.getAttributes());

        // 사용자 정보 저장
        User user = save(attributes);

        // 사용자 정보와 권한을 포함한 객체 반환
        return new DefaultOAuth2User(
                Collections.singleton(new SimpleGrantedAuthority(user.getRole().getValue())),
                attributes.getAttributes(),
                attributes.getNameAttributeKey());
    }

    // 첫 로그인시에만 저장 이후로 데이터가 존재하면 저장하지 않음
    protected User save(OAuth2Attributes attributes) {
        User user = userRepository.findByEmail(attributes.getEmail())
                .orElse(attributes.toEntity());

        return userRepository.save(user);
    }
}