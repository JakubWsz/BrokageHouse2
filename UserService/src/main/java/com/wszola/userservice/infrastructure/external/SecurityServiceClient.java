package com.wszola.userservice.infrastructure.external;

import com.wszola.userservice.domain.model.UserCredentials;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Component
@FeignClient(name = "security-service", url = "${security.service.url}")
public interface SecurityServiceClient {

	@PostMapping("/api/v1/auth/register")
	void registerUser(@RequestBody UserCredentials userCredentials);
}
