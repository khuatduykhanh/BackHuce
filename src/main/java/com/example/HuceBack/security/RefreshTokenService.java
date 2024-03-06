package com.example.HuceBack.security;

import com.example.HuceBack.entity.RefreshToken;
import com.example.HuceBack.entity.User;
import com.example.HuceBack.exception.APIException;
import com.example.HuceBack.repository.RefreshTokenRepository;
import com.example.HuceBack.repository.UserRepository;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class RefreshTokenService {
    @Value("${app.jwt-secret}")
    private String jwtSecrect;
    @Value("${app-jwtRefreshExpirationMs}")
    private int refreshExpiration;
    @Autowired
    private RefreshTokenRepository refreshTokenRepository;
    @Autowired
    private UserRepository userRepository;
    private String generateRefreshToken(
            Authentication authentication
    ) {

        return buildToken(new HashMap<>(), authentication, refreshExpiration);
    }
    private String buildToken(
            Map<String, Object> extraClaims,
            Authentication authentication,
            long expiration
    ) {
        String username = authentication.getName();
        return Jwts
                .builder()
                .setClaims(extraClaims)
                .setSubject(username)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(key())
                .compact();
    }

    public String createRefreshToken(String email, Authentication authentication) {
        String token = generateRefreshToken(authentication);
        RefreshToken refreshToken = new RefreshToken();
        User user = userRepository.findByEmailAndIsEnabled(email,true).orElseThrow(() -> new APIException(HttpStatus.BAD_REQUEST, "not find"));
        RefreshToken refreshToken1 = refreshTokenRepository.findByUserInfo(user);
        if(refreshToken1 != null ){
            refreshTokenRepository.delete(refreshToken1);
        }
        refreshToken.setUserInfo(user);
        refreshToken.setToken(token);
        refreshToken.setExpiryDate(new Date(System.currentTimeMillis() + refreshExpiration));//10
        refreshTokenRepository.save(refreshToken);
        return token;
    }


    public Optional<RefreshToken> findByToken(String token) {
        return refreshTokenRepository.findByToken(token);
    }


    public RefreshToken verifyExpiration(RefreshToken token) {
        Date now = new Date(System.currentTimeMillis());
        int comparison = token.getExpiryDate().compareTo(now);
        if (comparison < 0 ) {
            refreshTokenRepository.delete(token);
            throw new RuntimeException(token.getToken() + " Refresh token was expired. Please make a new signin request");
        }
        return token;
    }
    private Key key(){
        return Keys.hmacShaKeyFor(
                Decoders.BASE64.decode(jwtSecrect)
        );
    }
}

