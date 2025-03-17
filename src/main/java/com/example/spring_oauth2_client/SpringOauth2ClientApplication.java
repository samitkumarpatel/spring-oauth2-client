package com.example.spring_oauth2_client;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.annotation.RegisteredOAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;
import java.util.Objects;

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
	final ClientRegistrationRepository clientRegistrationRepository;

	@GetMapping("/")
	public String index() {
		ClientRegistration registration =
				this.clientRegistrationRepository.findByRegistrationId("clientone");

		var token = registration.getClientSecret();
		log.info("Client secret: {}", token);
		return "redirect:/index.html";

	}

	@GetMapping("/token")
	@ResponseBody
	public Map<String,String> token(@RegisteredOAuth2AuthorizedClient("clientone") OAuth2AuthorizedClient authorizedClient) {

		return Map.of(
				"access_token", authorizedClient.getAccessToken().getTokenValue(),
				"refresh_token", Objects.nonNull(authorizedClient.getRefreshToken()) ? authorizedClient.getRefreshToken().getTokenValue() : ""
		);

	}
}