package com.example.spring_oauth2_client;

import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.ResponseEntity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.annotation.RegisteredOAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoders;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.ResponseBody;

import java.security.Principal;
import java.text.ParseException;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

@SpringBootApplication
@EnableWebSecurity
public class SpringOauth2ClientApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringOauth2ClientApplication.class, args);
	}

}

@Controller
@RequiredArgsConstructor
@Slf4j
class WebController {
	final OAuth2AuthorizedClientService authorizedClientService;

	@GetMapping(value = "/")
	public String index(HttpServletResponse response, Authentication authentication) {

		var token = authorizedClientService.loadAuthorizedClient("mvcclient", authentication.getName())
				.getAccessToken().getTokenValue();
		log.info("Client secret: {}", token);
		Cookie cookie = new Cookie("access_token", token);
		response.addCookie(cookie);
		return "redirect:/index.html";
	}

	@GetMapping("/access_token")
	@ResponseBody
	public Map<String,String> token(@RegisteredOAuth2AuthorizedClient("mvcclient") OAuth2AuthorizedClient authorizedClient) {

		return Map.of(
				"access_token", authorizedClient.getAccessToken().getTokenValue(),
				"refresh_token", Objects.nonNull(authorizedClient.getRefreshToken()) ? authorizedClient.getRefreshToken().getTokenValue() : ""
		);

	}

	@SneakyThrows
    @GetMapping("/me")
	@ResponseBody
	public Map<String, Object> user(@CookieValue("access_token") String accessToken) {
		var decodeJWT = SignedJWT.parse(accessToken);
		log.info("Decode JWT getHeader: {}", decodeJWT.getHeader());
		log.info("Decode JWT getPayload: {}", decodeJWT.getPayload());
		log.info("Decode JWT: getJWTClaimsSet {}", decodeJWT.getJWTClaimsSet());
		return decodeJWT.getJWTClaimsSet().getClaims();
	}

}