package ams.cms.util;

import org.springframework.stereotype.Component;

@Component
public class RestAPIUri
{
	//setting local contextPath
	//public static String contextPath = "http://localhost:8081/Card_Management";
	public static String contextPath = "http://localhost:8085/Card_Management";
	//public static String contextPath = "http://192.168.19.3:8085/Card_Management";
	
	//added by ankit local system context Path //setting local cardManagementService contextPath
	public static String contextPathCardManagementAPI = "http://localhost:8446/CardManagementAPI";
	
	public static String test() 
	{
		String uri = contextPath+"/api/test";
		return uri;
	}
	
	public static String getCMSCardNoAPIUrl() 
	{
		String uri = contextPath+"/api/getCardNo";
		return uri;
	} 
	
	public static String getCMSCardDetailsAPIUrl() 
	{
		String uri = contextPath+"/api/getCardDetails";
		return uri;
	} 
	
	public static String updateCardStatusBlock() 
	{
		String uri = contextPath+"/api/updateCardStatusBlock";
		return uri;
	} 
	
	public static String updateCardStatusActive() 
	{
		String uri = contextPath+"/api/updateCardStatusActive";
		return uri;
	} 
	
	//created by ankit on 26-04-2023
	public static String getCMSCardTypesAPIUrl()
	{
		String uri = contextPath+"/api/getCardTypes";
		return uri;
	}
	//created by ankit on 26-04-2023
	
	//created by ankit on 28-04-2023 for CardMangementServiceAPI
	public static String getCardIssuanceAPIUrl() 
	{
		String uri = contextPathCardManagementAPI + "/cardIssuance";
		return uri;
	}
	//created by ankit on 28-04-2023 for CardMangementServiceAPI
}
