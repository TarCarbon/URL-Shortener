package ua.goit.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import io.jsonwebtoken.Jwts;
import org.springframework.stereotype.Component;


import java.util.Date;
import java.time.Duration;
import java.util.List;


@Component
@RequiredArgsConstructor
@Slf4j
public class JwtTokenUtils {

  @Value("${jwt.secret}")
  private String secret;

  @Value("${jwt.lifetime}")
  private Duration lifetime;


  public String generateToken(UserDetails userDetails) {

    Date issuedDate = new Date();
    Date expiredDate = new Date(issuedDate.getTime() + lifetime.toMillis());


    return Jwts.builder()
            .setSubject(userDetails.getUsername())
            .setIssuedAt(issuedDate)
            .setExpiration(expiredDate)
            .signWith(SignatureAlgorithm.ES384, secret)
            .compact();
  }


  public String getUsername(String token) {
    return getAllClaimsFromToken(token).getSubject();
  }


  private Claims getAllClaimsFromToken(String token) {
    return Jwts.parser()
            .setSigningKey(secret)
            .parseClaimsJws(token)
            .getBody();
  }
}
