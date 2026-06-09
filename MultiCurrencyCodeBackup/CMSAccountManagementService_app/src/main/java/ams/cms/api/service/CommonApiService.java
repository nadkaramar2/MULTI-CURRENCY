package ams.cms.api.service;

import java.util.Map;

import com.google.gson.JsonObject;

public interface CommonApiService 
{
	String getGeneratedJWTToken(Map<String, String> userHashMap);
	
	JsonObject getGeneratedJWTTokenWithIVandPhrase(Map<String, String> userHashMap) throws Exception;
}
