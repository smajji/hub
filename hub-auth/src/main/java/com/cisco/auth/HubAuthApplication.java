package com.cisco.auth;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.SecurityAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.ldap.core.support.LdapContextSource;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@SpringBootApplication(exclude = { SecurityAutoConfiguration.class })
@EnableDiscoveryClient
public class HubAuthApplication {

	public static void main(String[] args) {
		SpringApplication.run(HubAuthApplication.class, args);
	}

	@Bean
	public CorsFilter corsFilter() {
		final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		final CorsConfiguration config = new CorsConfiguration();
		config.setAllowCredentials(true);
		config.addAllowedOrigin("*");
		config.addAllowedHeader("*");
		config.addAllowedMethod("OPTIONS");
		config.addAllowedMethod("HEAD");
		config.addAllowedMethod("GET");
		config.addAllowedMethod("PUT");
		config.addAllowedMethod("POST");
		config.addAllowedMethod("DELETE");
		config.addAllowedMethod("PATCH");
		source.registerCorsConfiguration("/**", config);
		return new CorsFilter(source);
	}

	@Configuration
	@EnableWebSecurity
	protected static class WebSecurityConfig extends WebSecurityConfigurerAdapter {
		@Value("${ldap.url}")
		String ldapUrl;

		@Value("${ldap.base}")
		String base;

		@Value("${ldap.userDn}")
		String userDn;

		@Value("${ldap.userpassword}")
		String userpassword;

		@Override
		protected void configure(HttpSecurity http) throws Exception {
			// @formatter:off
			// http.authorizeRequests().anyRequest().authenticated().and().httpBasic().and().csrf().disable();
			// @formatter:on
			http.httpBasic().and().authorizeRequests().anyRequest().authenticated().and().csrf().disable();
//			http.authorizeRequests().anyRequest().authenticated().and().formLogin().usernameParameter("username")
//					.passwordParameter("password").permitAll().and().logout().deleteCookies("remove")
//					.invalidateHttpSession(true).logoutUrl("/logout").logoutSuccessUrl("/login?logout").and().csrf()
//					.disable();
		}

		@Override
		public void configure(AuthenticationManagerBuilder auth) throws Exception {
			auth.ldapAuthentication().userDnPatterns("cn={0}").contextSource(contextSource());
		}

		@Bean
		public LdapContextSource contextSource() {
			LdapContextSource contextSource = new LdapContextSource();
			contextSource.setUrl(ldapUrl);
			contextSource.setBase(base);
			contextSource.setReferral("follow");
			contextSource.setUserDn(userDn);
			contextSource.setPassword(userpassword);
			return contextSource;
		}
	}

}
