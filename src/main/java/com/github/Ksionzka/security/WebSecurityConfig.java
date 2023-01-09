package com.github.Ksionzka.security;

import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@AllArgsConstructor
@EnableWebSecurity
public class WebSecurityConfig {

//	@Bean
//	public InMemoryUserDetailsManager userDetailsService(PasswordEncoder passwordEncoder) {
//		UserDetails user = User.withUsername("user")
//				.password(passwordEncoder.encode("password"))
//				.roles("USER")
//				.build();
//
//		UserDetails admin = User.withUsername("admin")
//				.password(passwordEncoder.encode("admin"))
//				.roles("USER", "ADMIN")
//				.build();
//
//		return new InMemoryUserDetailsManager(user, admin);
//	}

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http.authorizeRequests()
				.antMatchers("/api/register/**").permitAll()
//				.antMatchers("/h2/**").hasRole("ADMIN")
				.antMatchers("/h2/**").permitAll()
				.anyRequest().authenticated()
				.and()
				.csrf().disable()
				.headers().frameOptions().disable()
				.and()
				.httpBasic();
		return http.build();
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		PasswordEncoder encoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
		return encoder;
	}
}