package ams.cms.utility;

import java.security.SecureRandom;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.UUID;

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import ams.cms.api.model.PreAccountMaster;

@Component
public class TokenUtils {

	private final String PASSPHRASE = "phrase";
	private final String IV = "iv";
	private static final String SECRET = "AMS-account-management-secret-key";
	private static ObjectMapper mapper = new ObjectMapper();

	// IV,PASSPHRASE and TOKEN
	public Claims generateClaims() throws Exception {
		Claims claims = Jwts.claims();
		claims = this.setPassPhrase(claims);
		claims = this.setIVPhrase(claims);
		return claims;
	}

	public Claims setPassPhrase(Claims claim) {

		claim.put(PASSPHRASE, RandomStringUtils.randomAlphanumeric(8));

		return claim;
	}

	public String getPhraseFromClaim(Claims claim) {

		return (String) claim.get(PASSPHRASE);
	}

	public Claims setIVPhrase(Claims claim) {

		byte[] salt = new byte[16];
		new SecureRandom().nextBytes(salt);
		claim.put(IV, Hex.encodeHexString(salt));
		return claim;
	}

	public String getIVPhraseFromClaim(Claims claim) {

		return (String) claim.get(IV);
	}

	public Claims getClaimsFromToken(String token) {
		Claims claims;
		try {
			claims = Jwts.parser().setSigningKey(SECRET).parseClaimsJws(token).getBody();
		} catch (Exception e) {
			claims = null;
		}
		return claims;
	}

	public String generateToken(Claims claims) throws Exception {
		return Jwts.builder().setClaims(claims).setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60))
				.signWith(SignatureAlgorithm.HS512, SECRET).compact();
	}
	
	public static String generateSessionId(PreAccountMaster preAccountMaster) 
	{
		return UUID.randomUUID().toString();
	}
	
	public static Timestamp getDate() {
		return getCurrentTimeStamp();
	}
	
	public static Timestamp getCurrentTimeStamp() {

		java.util.Date today = new java.util.Date();
		return new java.sql.Timestamp(today.getTime());
	}
	
	public static String convertJsonToString(Object obj) {
		String reqres = "";
		try {
			reqres = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(obj);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}

		return reqres;
	}
}
