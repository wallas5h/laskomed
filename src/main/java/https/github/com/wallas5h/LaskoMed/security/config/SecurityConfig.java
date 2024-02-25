package https.github.com.wallas5h.LaskoMed.security.config;

import https.github.com.wallas5h.LaskoMed.security.JwtAuthenticationEntryPoint;
import https.github.com.wallas5h.LaskoMed.security.JwtAuthenticationFilter;
import jakarta.servlet.DispatcherType;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity(debug = false)
@AllArgsConstructor
public class SecurityConfig {

  private JwtAuthenticationEntryPoint authenticationEntryPoint;

  private JwtAuthenticationFilter authenticationFilter;

  private AuthenticationProvider authenticationProvider;

  @Bean
  SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

    http
        .csrf((csrf) -> csrf.disable())
        .authorizeHttpRequests((authorize) -> {
          authorize.dispatcherTypeMatchers(DispatcherType.FORWARD, DispatcherType.ERROR).permitAll();
          authorize.requestMatchers("/css/**", "/favicon.ico", "/error").permitAll();
          authorize.requestMatchers("/auth/register", "/auth/login").permitAll();
          authorize.requestMatchers( "/swagger-ui/**", "/v3/api-docs/**").permitAll();
          authorize.anyRequest().authenticated();
        })
        .httpBasic(Customizer.withDefaults());
    http.formLogin(formLogin -> formLogin.loginPage("/login"));
    http.csrf(AbstractHttpConfigurer::disable);

    http.authenticationProvider(authenticationProvider);
    http.sessionManagement((session) -> session
        .sessionCreationPolicy(SessionCreationPolicy.STATELESS));

    http.exceptionHandling( exception -> exception
        .authenticationEntryPoint(authenticationEntryPoint));

    http.addFilterBefore(authenticationFilter, UsernamePasswordAuthenticationFilter.class);

    return http.build();
  }
}