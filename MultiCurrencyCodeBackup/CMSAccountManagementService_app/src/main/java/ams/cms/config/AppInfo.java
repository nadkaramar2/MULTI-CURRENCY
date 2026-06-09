package ams.cms.config;

import java.io.Serializable;

import org.springframework.stereotype.Component;

@Component
public class AppInfo implements Serializable 
{
	private static final long serialVersionUID = 1L;
	
	private String iv;
	private String salt;
	private String phrase;
	private String secretKey;
	private String apiKey;
	
	private String decryptedPayload;
	private String strParticipantId;
	private String apiHash;
	
	public String getIv() {
		return iv;
	}
	public void setIv(String iv) {
		this.iv = iv;
	}
	public String getPhrase() {
		return phrase;
	}
	public void setPhrase(String phrase) {
		this.phrase = phrase;
	}
	public String getSecretKey() {
		return secretKey;
	}
	public void setSecretKey(String secretKey) {
		this.secretKey = secretKey;
	}
	public String getApiKey() {
		return apiKey;
	}
	public void setApiKey(String apiKey) {
		this.apiKey = apiKey;
	}
	public String getSalt() {
		return salt;
	}
	public void setSalt(String salt) {
		this.salt = salt;
	}
	public String getDecryptedPayload() {
		return decryptedPayload;
	}
	public void setDecryptedPayload(String decryptedPayload) {
		this.decryptedPayload = decryptedPayload;
	}
	public String getStrParticipantId() {
		return strParticipantId;
	}
	public void setStrParticipantId(String strParticipantId) {
		this.strParticipantId = strParticipantId;
	}
	public String getApiHash() {
		return apiHash;
	}
	public void setApiHash(String apiHash) {
		this.apiHash = apiHash;
	}
}
