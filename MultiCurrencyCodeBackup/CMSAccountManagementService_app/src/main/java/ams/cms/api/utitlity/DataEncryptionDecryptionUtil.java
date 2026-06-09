package ams.cms.api.utitlity;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class DataEncryptionDecryptionUtil implements Serializable 
{
	private static final long serialVersionUID = 1L;
	private String iv;
	private String phrase;
	
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
}
