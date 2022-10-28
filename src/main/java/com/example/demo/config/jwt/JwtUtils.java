package com.example.demo.config.jwt;

import com.example.demo.utils.DateUtils;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.SecurityException;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import javax.crypto.spec.SecretKeySpec;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.security.Key;
import java.sql.Date;
import java.time.LocalDate;
import java.util.Base64;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class JwtUtils {

    private static final Logger logger = LoggerFactory.getLogger(JwtUtils.class);

    public static final long EXPIRATION_TIME = 1 * TimeUnit.DAYS.toMillis(1); // 1 day

    private static final String JWT_SECRET = "notihub@123";

    public static final String TOKEN_PREFIX = "Bearer";

    public static final String AUTHORIZATION_HEADER = "Authorization";

    public void addAuthentication(HttpServletResponse res, String username) {
        String jwt = generateToken(username, EXPIRATION_TIME);
        res.addHeader(AUTHORIZATION_HEADER, jwt);
    }

    public String generateToken(String username, long expiration) {
        byte[] decodedKey = Base64.getDecoder().decode(JWT_SECRET);
        Key key = new SecretKeySpec(decodedKey, 0, decodedKey.length, "AES");
        return Jwts.builder().setSubject(username).setIssuedAt(DateUtils.asDate(LocalDate.now()))
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(key, SignatureAlgorithm.HS512).compact();
    }

    public Authentication getAuthentication(HttpServletRequest request) {
        String token = request.getHeader(AUTHORIZATION_HEADER);
        // parse the token.
        Optional<Jws<Claims>> jwsToken = getJwtToken(token);
        if (jwsToken.isPresent()) {
            return new UsernamePasswordAuthenticationToken("notihub", "notihub@123");
        }
        return null;
    }

    public boolean removeToken(String token) {
        Optional<Jws<Claims>> jwsOpt = getJwtToken(token);
        if (!jwsOpt.isPresent()) {
            return true;
        }
        jwsOpt.get().getBody().clear();
        return true;
    }

    public Optional<Jws<Claims>> getJwtToken(String authToken) {
        if (StringUtils.isEmpty(authToken)) {
            return Optional.empty();
        }
        byte[] decodedKey = Base64.getDecoder().decode(JWT_SECRET);
        Key key = new SecretKeySpec(decodedKey, 0, decodedKey.length, "AES");
        try {
            Jws<Claims> jws = Jwts.parserBuilder().setSigningKey(key).build()
                    .parseClaimsJws(authToken.replace(TOKEN_PREFIX, StringUtils.EMPTY));
            return Optional.ofNullable(jws);
        } catch (SecurityException e) {
            logger.error("Invalid JWT signature: {}", e.getMessage());
        } catch (MalformedJwtException e) {
            logger.error("Invalid JWT token: {}", e.getMessage());
        } catch (ExpiredJwtException e) {
            logger.error("JWT token is expired: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            logger.error("JWT token is unsupported: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            logger.error("JWT claims string is empty: {}", e.getMessage());
        }

        return Optional.empty();
    }

    private String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader(AUTHORIZATION_HEADER);
        if (org.springframework.util.StringUtils.hasText(bearerToken) && bearerToken.startsWith(TOKEN_PREFIX)) {
            return bearerToken.substring(7);
        }
        return null;
    }

    public boolean validateJwtToken(String authToken) {
        Optional<Jws<Claims>> jwsOpt = getJwtToken(authToken);
        return jwsOpt.isPresent();
    }

}