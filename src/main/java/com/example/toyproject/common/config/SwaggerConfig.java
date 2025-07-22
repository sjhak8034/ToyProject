package com.example.toyproject.common.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.PathItem;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.responses.ApiResponse;
import io.swagger.v3.oas.models.responses.ApiResponses;
import io.swagger.v3.oas.models.security.OAuthFlow;
import io.swagger.v3.oas.models.security.OAuthFlows;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Value("${app.frontend.url:http://localhost:5173}")
    private String frontendUrl;

    @Bean
    public OpenAPI openAPI() {
        // JWT 인증 설정
        String jwtSchemeName = "JWT";
        SecurityRequirement jwtRequirement = new SecurityRequirement().addList(jwtSchemeName);

        // OAuth2 인증 설정
        String oauth2SchemeName = "OAuth2";
        SecurityRequirement oauth2Requirement = new SecurityRequirement().addList(oauth2SchemeName);

        Components components = new Components()
                // OAuth2 스키마
                .addSecuritySchemes(oauth2SchemeName, new SecurityScheme()
                        .name(oauth2SchemeName)
                        .description(
                                "OAuth2 인증을 사용하여 Google 계정으로 로그인합니다. " +
                                        "인증 후, JWT 토큰이 발급되어 API 요청 시 사용됩니다."
                        )
                        .type(SecurityScheme.Type.OAUTH2)
                        .flows(new OAuthFlows()
                                .implicit(new OAuthFlow()
                                        .authorizationUrl("/oauth2/authorization/google"))));

        OpenAPI openAPI = new OpenAPI()
                .addSecurityItem(jwtRequirement)
                .addSecurityItem(oauth2Requirement)
                .components(components)
                .info(new Info()
                        .title("채팅 프로젝트 API")
                        .version("1.0")
                        .description("채팅 프로젝트 API 문서"));


        // 로그아웃 스키마
        PathItem logoutPath = new PathItem()
                .post(new Operation()
                        .addTagsItem("인증")
                        .summary("로그아웃")
                        .description("사용자 로그아웃 및 쿠키에 저장된 JWT 토큰 삭제")
                        .responses(new ApiResponses()
                                .addApiResponse("200", new ApiResponse().description("로그아웃 성공"))));

        openAPI.path("/logout", logoutPath);

        return openAPI;
    }
}