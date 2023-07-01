package com.brokagehouse.authservice.config;

import com.brokagehouse.authservice.context.AuthenticationManager;
import com.brokagehouse.authservice.context.SecurityContextRepository;
import io.netty.handler.codec.http.HttpMethod;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import reactor.core.publisher.Mono;

@Configuration
@EnableWebFluxSecurity
@EnableReactiveMethodSecurity
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class WebSecurityConfig {

	AuthenticationManager authenticationManager;
	SecurityContextRepository securityContextRepository;

	@Bean
	public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
		http
				.authorizeExchange(authorizeExchangeSpec -> authorizeExchangeSpec
						.pathMatchers(String.valueOf(HttpMethod.OPTIONS), "/**").permitAll()
						.pathMatchers("/api/v1/auth/login", "/api/v1/auth/register").permitAll()
						.pathMatchers("/api/v1/auth/logout", "/api/v1/auth/update-credentials").authenticated()
						.anyExchange().authenticated()
				)
				.csrf(ServerHttpSecurity.CsrfSpec::disable)
				.httpBasic(ServerHttpSecurity.HttpBasicSpec::disable)
				.formLogin(ServerHttpSecurity.FormLoginSpec::disable)
				.authenticationManager(authenticationManager)
				.securityContextRepository(securityContextRepository)
				.exceptionHandling(exceptionHandlingSpec -> exceptionHandlingSpec
						.authenticationEntryPoint((swe, e) ->
								Mono.fromRunnable(() -> swe.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED))
						)
						.accessDeniedHandler((swe, e) ->
								Mono.fromRunnable(() -> swe.getResponse().setStatusCode(HttpStatus.FORBIDDEN))
						)
				);

		return http.build();
	}
}

