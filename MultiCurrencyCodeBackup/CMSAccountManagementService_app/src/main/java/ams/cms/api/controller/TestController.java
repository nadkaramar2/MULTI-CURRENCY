package ams.cms.api.controller;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

//added by ankit for testing og image API
//@RequestMapping("/test")
@RestController
public class TestController 
{
	private static final String SEPARATOR = "|";
	
	@RequestMapping(path = "/test/addImage", method = RequestMethod.POST)
	public ResponseEntity<?> addImage(@RequestParam("image") MultipartFile image,
			@RequestParam("mobileNo") String mobileNo) {
		return ResponseEntity.ok("Nothing");
	}
	
	@RequestMapping(path = "/test/testStr", method = RequestMethod.GET)
	public ResponseEntity<?> testStr() {
		return ResponseEntity.ok("Hello World!!!");
	}
	
	@RequestMapping(path = "/getApiHash", method = RequestMethod.POST)
	public ResponseEntity<?> getApiHash(@RequestBody LinkedHashMap<String, String> linkedMapData, HttpServletRequest request) 
	{
		try 
		{
			String secretKeyVal = request.getHeader("secretkey");			
			if (secretKeyVal!=null)
			{
				TreeMap<String, String> treemap = new TreeMap<String, String> (linkedMapData);
				
				String finalStr = "";
				for (Map.Entry<String, String> entry : treemap.entrySet()) 
				{
					String value = (String) entry.getValue();
					if (finalStr.length() == 0)
					{
						finalStr = value + SEPARATOR;
					}
					else
					{
						finalStr = finalStr + value + SEPARATOR;
					}
				}
				finalStr = finalStr + secretKeyVal;
				System.out.println("finalStr::: "+finalStr);
				
				String hashValue = SHA_512(finalStr);
				System.out.println("Api hashValue::: \n"+hashValue);
				
				LinkedHashMap<String, String> apiHashResponse = new LinkedHashMap<String, String>();
				apiHashResponse.put("apihash", hashValue);

				return ResponseEntity.ok(apiHashResponse);
				//return ResponseEntity.ok(hashValue);
			}
		}
		catch (Exception e) 
		{
			e.printStackTrace();
		}
		return ResponseEntity.ok("Unauthorized Request!");
	}
	private String SHA_512(String str) throws NoSuchAlgorithmException, UnsupportedEncodingException 
	{
        MessageDigest md = MessageDigest.getInstance("SHA-512");
        md.update(str.getBytes("UTF-8"));
        byte byteData[] = md.digest(); 
        
        StringBuffer hashCodeBuffer = new StringBuffer();
        for (int i = 0; i < byteData.length; i++) {
            hashCodeBuffer.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));
        }
        return hashCodeBuffer.toString().toUpperCase();
	}
	
}
