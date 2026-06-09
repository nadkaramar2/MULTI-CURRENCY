package ams.cms.utility;

import java.util.Collections;
import java.util.Map;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;

import ams.cms.model.BankDetailsInfo;

public class RequestResponseUtils 
{
	public static String getExternalServerResponseWithToken(Map<String, Object> requestData, String requestUrl, String requestToken) 
	{
		String respnseStr = "";
		try 
		{
			HttpHeaders headers = new HttpHeaders();
			
			headers = new HttpHeaders();
		    headers.setContentType(MediaType.APPLICATION_JSON);
		    headers.set("Authorization", "Bearer "+requestToken);
		    
		    HttpEntity<Map<String, Object>> request = new HttpEntity<Map<String, Object>>(requestData, headers);
		    
		    RestTemplate restTemplate = new RestTemplate();
		  
		    respnseStr = restTemplate.postForObject(requestUrl, request, String.class);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
		return respnseStr;
	}
	public static String getExternalServerResponseWithOutToken(String requestUrl, Map<String, Object> requestData) 
	{
		String respnseStr = "";
		try 
		{
			HttpHeaders headers = new HttpHeaders();
			
			headers = new HttpHeaders();
		    headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
		    
		    HttpEntity<Map<String, Object>> request = new HttpEntity<Map<String, Object>>(requestData, headers);
		    
		    RestTemplate restTemplate = new RestTemplate();
		  
		    respnseStr = restTemplate.postForObject(requestUrl, request, String.class);
		    
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return respnseStr;
	}
	
	public static String getExternalResponse(Map<String, Object> requestData, String requestUrl, String requestToken) 
	{
		try 
		{
			RestTemplate restTemplate = new RestTemplate();


	        HttpHeaders headers = new HttpHeaders();
	        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
	        //headers.add("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/54.0.2840.99 Safari/537.36");

	        HttpEntity<String> entity = new HttpEntity<>("parameters", headers);
	        
	        ResponseEntity<?> result = restTemplate.exchange(requestUrl, HttpMethod.GET, entity, String.class);
	        return (String) result.getBody();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}
	
	public static String getTokenFromExternalServer() 
	{
		try 
		{
			RestTemplate restTemplate = new RestTemplate();

			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
			//headers.add("PRIVATE-TOKEN", "xyz");

			MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
			map.add("client_id","0b54e219-9681-47cf-bcfb-bf72315157d9");
			map.add("client_secret","uzY8Q~l1l54gZWpK4ShaqoGc.3H4TE0.deKbacZF");
			map.add("grant_type","client_credentials");
			map.add("scope","0b54e219-9681-47cf-bcfb-bf72315157d9/.default");

			HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(map, headers);

			ResponseEntity<?> response = restTemplate.exchange("https://apitest.nibss-plc.com.ng/reset",
			                          HttpMethod.POST,
			                          entity,
			                          String.class);
			System.out.println("response get it...");
			
			String resp = (String) response.getBody();
			
			System.out.println("resp::"+resp);
			
			ExternalServerTokenModel externalServerTokenModel = new ObjectMapper().readValue(resp, ExternalServerTokenModel.class);
			System.out.println("externalServerTokenModel response::"+externalServerTokenModel);
			
			System.out.println("externalServerTokenModel access_token::"+externalServerTokenModel.getAccess_token());
			
			return resp;
		}
		catch (Exception e) 
		{
			e.printStackTrace();
		}
		return "";
	}
	
	public static String getTokenFromExternalServerForBankDetailsApi() 
	{
		try 
		{
			RestTemplate restTemplate = new RestTemplate();

			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

			MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
			map.add("client_id","2aee5ee6-c8d7-4da2-ad17-001d6d5902ff");
			map.add("client_secret","Je.8Q~RPHNZ8xhFgEnzg42ip_y2O..fcqxlcFaVu");
			map.add("grant_type","client_credentials");
			map.add("scope","2aee5ee6-c8d7-4da2-ad17-001d6d5902ff/.default");

			HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(map, headers);

			ResponseEntity<?> response = restTemplate.exchange("https://apitest.nibss-plc.com.ng/reset",
			                          HttpMethod.POST,
			                          entity,
			                          String.class);
			System.out.println("response get it...");
			
			String resp = (String) response.getBody();
			
			System.out.println("resp::"+resp);
			
			ExternalServerTokenModel externalServerTokenModel = new ObjectMapper().readValue(resp, ExternalServerTokenModel.class);
			System.out.println("externalServerTokenModel response::"+externalServerTokenModel);
			
			System.out.println("externalServerTokenModel access_token::"+externalServerTokenModel.getAccess_token());
			
			return externalServerTokenModel.getAccess_token();
		}
		catch (Exception e) 
		{
			e.printStackTrace();
		}
		return "";
	}
	
	public static void getBankDetailsFromNIBSS() 
	{
		try 
		{

			String token = getTokenFromExternalServerForBankDetailsApi();
			//String token = "eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiIsImtpZCI6Ii1LSTNROW5OUjdiUm9meG1lWm9YcWJIWkdldyJ9.eyJhdWQiOiIyYWVlNWVlNi1jOGQ3LTRkYTItYWQxNy0wMDFkNmQ1OTAyZmYiLCJpc3MiOiJodHRwczovL2xvZ2luLm1pY3Jvc29mdG9ubGluZS5jb20vMjc5YzdiMWItYmEwNi00MjdiLWE2ODEtYzhhNTQ5MmQyOTNkL3YyLjAiLCJpYXQiOjE2ODM2OTczMDIsIm5iZiI6MTY4MzY5NzMwMiwiZXhwIjoxNjgzNzAxMjAyLCJhaW8iOiJFMlpnWUhDZmRhZGF2WHpYZzF3TjdZamREUFYzVG1STWFONFU5bmNOMTNOemlXRDcya2tBIiwiYXpwIjoiMmFlZTVlZTYtYzhkNy00ZGEyLWFkMTctMDAxZDZkNTkwMmZmIiwiYXpwYWNyIjoiMSIsInJoIjoiMC5BWUlBRzN1Y0p3YTZlMEttZ2NpbFNTMHBQZVplN2lyWHlLSk5yUmNBSFcxWkF2LUNBQUEuIiwidGlkIjoiMjc5YzdiMWItYmEwNi00MjdiLWE2ODEtYzhhNTQ5MmQyOTNkIiwidXRpIjoiRnhlaWxLMERlRW1yc2NBX2pHQUVBQSIsInZlciI6IjIuMCJ9.MyAGRg4i3j5ZFYnOKEqd6O8NFBatV89Q5vULPiD6bVkSxFw_IYPDo31GZGhJEO8Iv86DpwkIEBj0tfvTj1FaDK3QfaFeKT6IUldYUg5xC8YcxD5-u9TqcQOMRLjenLKBWbMh6R33Refj5nRlTZRATC_ijvDa75ms42F7OkD8FbYUpQIsGV2fc3pIFhCQmdXV3wtlEWdY0kQiwrGqgJ5E0cr1BBSh0Um6mF9As0Jw843j3Uzk-qHD29BJ2EPSBlUPXOBlfyvCR4mco0nepfUdz4hY9_vt9XbJKKBvfiKj3yEe7SWwh3I1MFKdiBziLwE8Ue3LY5We8mqd0-h3vpWYlg";
			System.out.println("Token Response::"+token);
			
			RestTemplate restTemplate = new RestTemplate();

			HttpHeaders headers = new HttpHeaders();
			headers.set("Authorization", "Bearer "+token);

			HttpEntity<String> entity = new HttpEntity<>(headers);

			ResponseEntity<?> response = restTemplate.exchange("https://apitest.nibss-plc.com.ng/nibsspayplus/v2/Banks",
			                          HttpMethod.GET,
			                          entity,
			                          String.class);
			System.out.println("response get it...");
			
			String resp = (String) response.getBody();
			
			System.out.println("resp::"+resp);
			
			BankInfoResponseModel bankInfoResponseModel = new ObjectMapper().readValue(resp, BankInfoResponseModel.class);
			System.out.println("bankInfoResponseModel response::"+bankInfoResponseModel);
			
			if (bankInfoResponseModel!=null && bankInfoResponseModel.getData()!=null 
					&& bankInfoResponseModel.getData().getBanks()!=null &&bankInfoResponseModel.getData().getBanks().size() > 0)
			{
				/*
				int[] resultArr = 
						
				if (resultArr.length > 0) 
				{
					System.out.println(String.valueOf(resultArr));
				}
				*/
			}			
		}
		catch (Exception e) 
		{
			e.printStackTrace();
		}
	}
	
}
