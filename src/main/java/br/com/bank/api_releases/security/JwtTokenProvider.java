package br.com.bank.api_releases.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import java.util.Date;

@Component
public class JwtTokenProvider {
	@Value("${jwt.secret}")
	private String jwtSecret;

	@Value("${jwt.expiration}")
	private long jwtExpirationMs;

	public String generateToken(Authentication authentication) {
		UserDetails user = (UserDetails) authentication.getPrincipal();

		return Jwts.builder()
			.setSubject(user.getUsername())
			.setIssuedAt(new Date())
			.setExpiration(new Date(System.currentTimeMillis() + jwtExpirationMs))
			.signWith(SignatureAlgorithm.HS512, jwtSecret)
			.compact();
	}

	public String getUsernameFromToken(String token) {
		Claims claims = Jwts.parserBuilder()
			.setSigningKey(jwtSecret)
			.build()
			.parseClaimsJws(token)
			.getBody();
		return claims.getSubject();
	}

	public boolean validateToken(String token) {
		try {
			Jwts.parserBuilder()
				.setSigningKey(jwtSecret)
				.build()
				.parseClaimsJws(token);
			return true;
		} catch (Exception e) {
			return false;
		}
	}
}
