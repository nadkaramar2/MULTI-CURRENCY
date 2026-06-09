package ams.cms.jwt;

import java.security.SecureRandom;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import ams.cms.config.AppInfo;
import ams.cms.config.CommonConstants;
import ams.cms.security.SecurityUser;
import ams.cms.util.AesUtil;

@Component
public class JwtUtil 
{
	private static final String SECRET = "AMS-account-management-secret-key";
	private final String PASSPHRASE = "phrase";
	private final String IV = "iv";
	private final String USERNAME = "username";
	
	private Claims claim;//Added by Sunny Soni
	
	@Autowired
	private	AppInfo appInfo;
	
	public String getUsernameFromToken(String token) 
	{
		return extractClaim(token, Claims::getSubject);
	}
	
	public Date extractExpiration(String token) {
		return extractClaim(token, Claims::getExpiration);
		
	}
	
	public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
		final Claims claims = getClaimsFromToken(token);
		return claimsResolver.apply(claims);
	}
	
	public Claims getClaimsFromToken(String token) 
	{
		Claims claims = null;
		try 
		{
			//if(!"NIGERIA".equalsIgnoreCase(CommonConstants.applicationName)) 
			if (token!=null)
			{
				claims = Jwts.parser().setSigningKey(SECRET).parseClaimsJws(token).getBody();
			}
		}
		catch (Exception e) 
		{
			e.printStackTrace();
		}
		return claims;
	}
	
	private Boolean isTokenExpired(String token)
	{
		return extractExpiration(token).before(new Date());
	}
	
	public String generateToken(UserDetails userDetails)
	{
		Map<String, Object> claims = new HashMap<>();
		return createToken(claims, userDetails.getUsername());
	}
	
	private String createToken(Map<String, Object> claims, String subject)
	{
		return Jwts.builder().setClaims(claims)
				.setSubject(subject)
				.setIssuedAt(new Date(System.currentTimeMillis()))
				.setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 30))
				.signWith(SignatureAlgorithm.HS256, SECRET)
				.compact();
	}
	
	public Boolean validateToken(String token, UserDetails userDetails) {
		final String username = getUsernameFromToken(token);
		return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
	}
	
	public Boolean validateTokenByJwtToken(String token, SecurityUser securityUser)
	{
		final String existingJwtToken = securityUser.getJwtToken();
		
		return (token.equals(existingJwtToken) && !isTokenExpired(token));
	}

	public String generateToken(Claims claims) throws Exception
	{
		return Jwts.builder().setClaims(claims)
				.setSubject(getUserName(claims))
				.setIssuedAt(new Date(System.currentTimeMillis()))
				.setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 30))
				.signWith(SignatureAlgorithm.HS256, SECRET)
				.compact();
		
		/*return Jwts.builder().setClaims(claims).setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 30))
				.signWith(SignatureAlgorithm.HS256, SECRET).compact();*/
	}
	
	//Added by Sunny Soni for Creating and getting IV and PHRASE Start
	
	//public Claims generateClaims() throws Exception {
	public Claims generateClaims(UserDetails userDetails) throws Exception 
	{
		Claims claims = Jwts.claims();
		
		//Added by Sunny soni for set user name Start
		claims = this.setUserName(claims, userDetails);
		//Added by Sunny soni for set user name End
		
		claims = this.setPassPhrase(claims);
		claims = this.setIVPhrase(claims);
		return claims;
	}
	
	public Claims setUserName(Claims claim, UserDetails userDetails) 
	{
		claim.put(USERNAME, userDetails.getUsername());
		return claim;
	}
	
	public String getUserName(Claims claim) 
	{
		String userName = (String) claim.get(USERNAME);
		return userName;
	}
	
	public Claims setPassPhrase(Claims claim)
	{
		claim.put(PASSPHRASE, RandomStringUtils.randomAlphanumeric(8));
		return claim;
	}

	public String getPhraseFromClaim(Claims claim)
	{
		String phraseRes = "";
		if(claim == null && "NIGERIA".equalsIgnoreCase(CommonConstants.applicationName)) 
		{
			//phraseRes = "PKnjqiuH";
			phraseRes = appInfo.getPhrase();
			System.out.println("Inside getPhraseFromClaim phraseRes::["+phraseRes+"]");
		}
		else 
		{
			phraseRes = (String) claim.get(PASSPHRASE);
		}		
		//return (String) claim.get(PASSPHRASE);
		return phraseRes;
	}

	public Claims setIVPhrase(Claims claim)
	{
		byte[] salt = new byte[16];
		new SecureRandom().nextBytes(salt);
		claim.put(IV, Hex.encodeHexString(salt));
		return claim;
	}

	public String getIVPhraseFromClaim(Claims claim) 
	{
		String ivRes = "";
		if(claim == null && "NIGERIA".equalsIgnoreCase(CommonConstants.applicationName)) 
		{
			//ivRes = "a38c86959d094a1be70311493c781f90";
			ivRes = appInfo.getIv();
			System.out.println("Inside getIVPhraseFromClaim ivRes::["+ivRes+"]");
		}
		else 
		{
			ivRes = (String) claim.get(IV);
		}
		//return (String) claim.get(IV);
		return ivRes;
	}
	//Added by Sunny Soni for Creating and getting IV and PHRASE End
	
	//Added by Sunny Soni for current validate claim Start
	public void setClaims(Claims claim) {
		this.claim = claim;
	}
	
	public Claims getRequestClaim() {
		return this.claim;
	}	
	//Added by Sunny Soni for current validate claim End
	
	public String getRandomSalt() 
	{
		String salt = AesUtil.random(16);
		return salt;
	}
}
