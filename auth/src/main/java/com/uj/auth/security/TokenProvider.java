package com.uj.auth.security;


import java.util.Date;
import java.util.Hashtable;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;

import com.uj.auth.AppProperties;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;


@Service
public class TokenProvider {

    private static final Logger logger = LoggerFactory.getLogger(TokenProvider.class);

    private AppProperties appProperties;

    public TokenProvider(AppProperties appProperties) {
        this.appProperties = appProperties;
    }

    public String createToken(Authentication authentication) {
    	User userPrincipal = (User) authentication.getPrincipal();
        logger.info("inside cereate Token --> " + userPrincipal);
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + appProperties.getAuth().getTokenExpirationMsec());

        Map<String, Object> map = new Hashtable<String, Object>();
        
        map.put("username", userPrincipal.getUsername());

        String token = Jwts.builder()
                .setSubject(userPrincipal.getUsername())
                .addClaims(map)
                .setIssuedAt(new Date())
                .setExpiration(expiryDate)
                .signWith(io.jsonwebtoken.SignatureAlgorithm.HS512, appProperties.getAuth().getTokenSecret())
                .compact();
        logger.info("token for {} is {}", userPrincipal.getUsername(), token);
        return token;
    }

    public String getUserIdFromToken(String token) {
    	 logger.debug("inside getUserIdFromTokenn --> ");
        Claims claims = Jwts.parser()
                .setSigningKey(appProperties.getAuth().getTokenSecret())
                .parseClaimsJws(token)
                .getBody();
   	 logger.debug("claims --> " +claims);
        return claims.getSubject();
    }

    public boolean validateToken(String authToken) {
    	 logger.debug("inside validateToken --> ");
        try {
            Jwts.parser().setSigningKey(appProperties.getAuth().getTokenSecret()).parseClaimsJws(authToken);
            return true;
        } catch (MalformedJwtException ex) {
            logger.error("Invalid JWT token");
        } catch (ExpiredJwtException ex) {
            logger.error("Expired JWT token");
        } catch (UnsupportedJwtException ex) {
            logger.error("Unsupported JWT token");
        } catch (IllegalArgumentException ex) {
            logger.error("JWT claims string is empty.");
        }
        return false;
    }

}
