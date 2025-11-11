package br.com.senacsp.tads.stads4ma.library.infrascrture.config;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
public class JwtHelper {

    private String secret = "Algo aleatorio";
    private final int EXPIRATION = 1000*60*60*15;

    public String generateToken(UserDatails userDetails){
        return Jwts.builder()
        .setSubject(userDetails.getUsername())
        .setIssuedAt(new Date())
        .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION))
                .signWith(getSingningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public String extractUsername(String token){
        return Jwts.parserBuilder().setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }
    public boolean isTokenValid(String token, UserDatails userDatails){
        final String username= this.extractUsername(token);
        return (username.equals(userDatails.getUsername()) && !isTokenExpired);
    }

    public boolean isTokenExpired(String token){
        return Jwts.parserBuilder().setSigningKey(this.getSingningKey())
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getExpiration()
                .before(new Date());
    }
    private SignatureAlgorithm getSigningKey(){
        return Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }

}
