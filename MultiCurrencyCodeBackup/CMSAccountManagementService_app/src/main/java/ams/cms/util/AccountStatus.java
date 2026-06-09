package ams.cms.util;

import java.util.LinkedHashMap;

public class AccountStatus 
{
	private static LinkedHashMap<String, String> statusMap = new LinkedHashMap<>();
	public static final String[] STATUS_ARR = new String[] 
	{
		"Active",
		"Dormant",
		"Inactive"
	};
	
	static 
	{
		for (String status: STATUS_ARR)
		{
			statusMap.put(status.toLowerCase(), status);
		}
	}
	
	public static String getAccountStatus(String key) 
	{
		if (key != null && key.trim().length() > 0 ) 
		{
			key = key.toLowerCase();
			return statusMap.get(key);
		}
		return null;
	}
}
