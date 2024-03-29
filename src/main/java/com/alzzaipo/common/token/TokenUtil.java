package com.alzzaipo.common.token;

import com.alzzaipo.common.LoginType;
import com.alzzaipo.common.Id;
import com.alzzaipo.common.token.domain.TokenProperties;
import com.alzzaipo.common.token.domain.TokenInfo;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import java.util.Date;
import javax.crypto.SecretKey;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TokenUtil {

	private static TokenProperties tokenProperties;

	@Autowired
	private TokenUtil(TokenProperties tokenProperties) {
		TokenUtil.tokenProperties = tokenProperties;
	}

	public static TokenInfo createToken(Id memberId, LoginType loginType) {
		String accessToken = generateAccessToken(memberId.get(), loginType);
		String refreshToken = generateRefreshToken(loginType);
		return new TokenInfo(accessToken, refreshToken);
	}

	public static TokenInfo refreshToken(Id memberId, String refreshToken) {
		Claims claims = getClaims(refreshToken);
		LoginType loginType = LoginType.valueOf(claims.get("loginType", String.class));
		Date refreshTokenExpirationDate = claims.getExpiration();

		String accessToken = generateAccessToken(memberId.get(), loginType);
		String newRefreshToken = generateRefreshToken(loginType, refreshTokenExpirationDate.getTime());
		return new TokenInfo(accessToken, newRefreshToken);
	}

	public static Id getMemberId(String token) {
		long memberId = Long.parseLong(getClaims(token).getSubject());
		return new Id(memberId);
	}

	public static LoginType getLoginType(String token) {
		return LoginType.valueOf((getClaims(token).get("loginType", String.class)));
	}

	public static boolean validate(String token) {
		try {
			Jwts.parserBuilder().setSigningKey(getSecretKey()).build().parseClaimsJws(token);
		} catch (Exception e) {
			return false;
		}
		return true;
	}

	private static String generateAccessToken(Long memberId, LoginType loginType) {
		return generateToken(memberId, loginType,
			System.currentTimeMillis() + tokenProperties.getAccessTokenExpirationTimeMillis());
	}

	private static String generateRefreshToken(LoginType loginType) {
		return generateToken(null, loginType,
			System.currentTimeMillis() + tokenProperties.getRefreshTokenExpirationTimeMillis());
	}

	private static String generateRefreshToken(LoginType loginType, long expirationTimeMillis) {
		return generateToken(null, loginType, expirationTimeMillis);
	}

	private static String generateToken(Long memberId, LoginType loginType, long expirationTimeMillis) {
		Claims claims = Jwts.claims();
		claims.put("loginType", loginType.name());

		if (memberId != null) {
			claims.setSubject(memberId.toString());
		}

		return Jwts.builder()
			.setClaims(claims)
			.setIssuedAt(new Date())
			.setExpiration(new Date(expirationTimeMillis))
			.signWith(getSecretKey(), SignatureAlgorithm.HS256)
			.compact();
	}

	private static Claims getClaims(String token) {
		return Jwts.parserBuilder()
			.setSigningKey(getSecretKey())
			.build()
			.parseClaimsJws(token)
			.getBody();
	}

	private static SecretKey getSecretKey() {
		byte[] keyBytes = Decoders.BASE64.decode(tokenProperties.getSecretKey());
		return Keys.hmacShaKeyFor(keyBytes);
	}
}
