package com.example.HuceBack.security;

import com.example.HuceBack.entity.User;
import com.example.HuceBack.exception.APIException;
import com.example.HuceBack.exception.ResourceNotFoundException;
import com.example.HuceBack.repository.UserRepository;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.security.Key;
import java.util.Date;
@Component
public class JwtProvider {
    @Value("${app.jwt-secret}")
    private String jwtSecrect;
    @Value("${app-jwt-expiration-milliseconds}")
    private int jwtExpirationMs;
    @Value("${app-jwtRefreshExpirationMs}")
    private int refreshExpiration;
    @Autowired
    private UserRepository userRepository;
    private static final Logger logger = LoggerFactory.getLogger(JwtProvider.class);
    public String generateToken(String email){

        Date currentDate = new Date();

        Date expireDate = new Date(currentDate.getTime() + jwtExpirationMs);

        String token = Jwts.builder()
                .setSubject(email)
                .setIssuedAt(new Date())
                .setExpiration(expireDate)
                .signWith(key())
                .compact();
        return token;
    }
    // get username from Jwt token
    public String getEmail(String token){
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(key())
                .build()
                .parseClaimsJws(token)
                .getBody();
        return claims.getSubject();
    }
    private Key key(){
        return Keys.hmacShaKeyFor(
                Decoders.BASE64.decode(jwtSecrect)
        );
    }


    public boolean validateToken(String token){
        try{
            Jwts.parserBuilder()
                    .setSigningKey(key())
                    .build()
                    .parse(token);
            return true;
        } catch (MalformedJwtException ex) {
            logger.error("Invalid JWT token", ex.getMessage());
        } catch (ExpiredJwtException ex) {
            logger.error("Expired JWT token", ex.getMessage());
        } catch (UnsupportedJwtException ex) {
            logger.error("Unsupported JWT token", ex.getMessage());
        } catch (IllegalArgumentException ex) {
            logger.error("JWT claims string is empty.", ex.getMessage());
        }
        return false;
    }

    public User getUserFromRequest(HttpServletRequest request){
        String bearerToken = request.getHeader("Authorization");
        String jwt = null;
        if(StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")){
            jwt = bearerToken.substring(7, bearerToken.length());
        }
        if(jwt != null && validateToken(jwt)){
            String mail = getEmail(jwt);
            return userRepository.findByEmail(mail).orElseThrow(() -> new ResourceNotFoundException("mail", "mail",mail));
        }
        return null;
    }
    public String getTokenFromRequest(HttpServletRequest request){
        String bearerToken = request.getHeader("Authorization");
        if(StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")){
            return bearerToken.substring(7, bearerToken.length());
        }
        return null;
    }
}
