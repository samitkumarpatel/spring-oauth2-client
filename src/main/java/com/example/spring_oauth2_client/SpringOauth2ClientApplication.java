package com.example.spring_oauth2_client;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.annotation.RegisteredOAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@SpringBootApplication
public class SpringOauth2ClientApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringOauth2ClientApplication.class, args);
	}

}

@Controller
@RequiredArgsConstructor
class WebController {

	@GetMapping("/token")
	@ResponseBody
	public String token(@RegisteredOAuth2AuthorizedClient("spring") OAuth2AuthorizedClient authorizedClient) {
		return authorizedClient.getAccessToken().getTokenValue();
	}
}