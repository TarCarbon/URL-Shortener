package ua.goit.config;


import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import ua.goit.jwt.AuthEntryPointJwt;
import ua.goit.jwt.AuthJwtTokenFilter;
import ua.goit.jwt.CookieAuthJwtTokenFilter;


@Configuration
@EnableMethodSecurity
@RequiredArgsConstructor
public class JwtSecurityConfiguration {
    private final UserDetailsService userDetailsService;
    private final AuthEntryPointJwt unauthorizedHandler;
    private final AuthJwtTokenFilter authJwtTokenFilter;
    private final CookieAuthJwtTokenFilter cookieAuthJwtTokenFilter;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();

        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());

        return authProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http.csrf(AbstractHttpConfigurer::disable)
                .exceptionHandling(exception -> exception.authenticationEntryPoint(unauthorizedHandler))
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth.requestMatchers("/V1/user/**",
                                "/V1/urls/list",
                                "V2/user/**",
                                "/V2/urls").permitAll()
                        .requestMatchers("/V1/urls/**"//,  "V2/urls/**"
                        ).authenticated()
                        //.requestMatchers("/V2/urls/**").permitAll()
                        //.anyRequest().authenticated())
                        .anyRequest().permitAll())
                .authenticationProvider(authenticationProvider())
                .addFilterBefore(authJwtTokenFilter, UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(cookieAuthJwtTokenFilter, UsernamePasswordAuthenticationFilter.class)
                .formLogin(form -> form.loginPage("/V2/urls")
                        .permitAll())
                        //.defaultSuccessUrl("/V2/all", true)
                        //.usernameParameter("username")
                        //.passwordParameter("password"))
                .logout(logout -> logout
                        .deleteCookies("jwtToken")
                        .logoutUrl("/logout"))
                .build();
    }
}
