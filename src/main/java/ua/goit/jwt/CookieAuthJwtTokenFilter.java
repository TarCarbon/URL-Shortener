package ua.goit.jwt;

import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public final class CookieAuthJwtTokenFilter extends OncePerRequestFilter {
    private final JwtUtils jwtUtils;
    private final UserDetailsService userDetailsService;
    private static final int COOKIE_LIFE_IN_SEC = 600;

    @Lazy
    @Autowired
    private AuthenticationManager authenticationManager;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        Map<String, String[]> parameterMap = request.getParameterMap();

        String username = request.getParameter("username");
        String password = request.getParameter("password");

        if ((parameterMap.containsKey("username") && parameterMap.containsKey("password"))
                && !parameterMap.containsKey("register")) {
            try {
                log.info("Generate jwt token");
                Authentication authentication = authenticationManager.authenticate(
                        new UsernamePasswordAuthenticationToken(username, password));

                UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                String token = jwtUtils.generateJwtToken(authentication);

                Cookie jwtCookie = new Cookie("jwtToken", token);
                jwtCookie.setPath("/");
                jwtCookie.setMaxAge(COOKIE_LIFE_IN_SEC);
                response.addCookie(jwtCookie);
                filterChain.doFilter(request, response);
                return;
            } catch (Exception e) {
                filterChain.doFilter(request, response);
                return;
            }
        }

        String jwtFromCookie;
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            Optional<Cookie> jwtTokenCookie = Arrays.stream(cookies)
                    .filter(cookie -> cookie.getName().equals("jwtToken"))
                    .findFirst();

            if (jwtTokenCookie.isPresent()) {
                log.info("JWT TOKEN IS PRESENT");
                jwtFromCookie = jwtTokenCookie.get().getValue();
                log.info("TOKEN FROM COOKIE " + jwtFromCookie);
                String extractUsername;
                try {
                    extractUsername = jwtUtils.getUsernameFromJwtToken(jwtFromCookie);
                } catch (ExpiredJwtException e) {
                    log.info("JWT TOKEN IS DEPRECATED");
                    filterChain.doFilter(request, response);
                    return;
                }

                UserDetails userByName = userDetailsService.loadUserByUsername(extractUsername);
                if (jwtUtils.validateJwtToken(jwtFromCookie)) {
                    log.info("TOKEN IS VALID, USER WILL BE ADDED TO CONTEXT");
                    String name = jwtUtils.getUsernameFromJwtToken(jwtFromCookie);

                    SecurityContext context = SecurityContextHolder.createEmptyContext();
                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                            name, null, userByName.getAuthorities());
                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    context.setAuthentication(authToken);
                    SecurityContextHolder.setContext(context);
                }
            }
        }
        filterChain.doFilter(request, response);
    }
}
