package com.self.service;

import java.time.Instant;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.crypto.SecretKey;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtService {
	
	private static final String SECRET="80DE7A47F498DE816DBE9E2497FA8BF3518353718CEA91748346EFFC867E817DB9F23CF6A1A52B5E505C9C82080DB241AC395B60830B2CC2138427F3B6482007";
	private static final long VALIDITY=TimeUnit.MINUTES.toMillis(30);
	
	public String generateToken(UserDetails userDetails)
	{
		Map<String,String> claims=new HashMap<>();
		claims.put("iss", "https://secure.genuinecoder.com");		
		
		return Jwts.builder()
				.claims(claims)
				.subject(userDetails.getUsername())
				.issuedAt(Date.from(Instant.now()))
				.expiration(Date.from(Instant.now().plusMillis(VALIDITY)))
				.signWith(generateKey())
				.compact();
	}
	
	private SecretKey generateKey()
	{
		byte[] decodeKey= Base64.getDecoder().decode(SECRET);
		return Keys.hmacShaKeyFor(decodeKey);
	}
	
	public String extractUsername(String jwt)
	{
		Claims claims=	getClaims(jwt);		
		return claims.getSubject();
	}
	
	private Claims getClaims(String jwt)
	{
		return Jwts.parser()
					.verifyWith(generateKey())
					.build()
					.parseSignedClaims(jwt)
					.getPayload();
	}

	public boolean isTokenValid(String jwt) {
		
		Claims claims=getClaims(jwt);
		return claims.getExpiration().after(Date.from(Instant.now()));
	}

}
