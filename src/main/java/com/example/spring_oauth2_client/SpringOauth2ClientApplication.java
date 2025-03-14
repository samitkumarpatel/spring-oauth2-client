package com.example.spring_oauth2_client;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

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
	@ResponseBody
	public String index(Authentication authentication) {
		ClientRegistration registration =
				this.clientRegistrationRepository.findByRegistrationId("clientone");

		var token = registration.getClientSecret();
		log.info("Client secret: {}", token);
		return "index";

	}
}