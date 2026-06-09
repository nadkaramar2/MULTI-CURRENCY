package ams.cms;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.client.RestTemplate;

import ams.cms.logger.AMSLogger;
import ams.cms.util.EncryptDecryptUtil;
import ams.cms.utility.Utils;

@SpringBootApplication
@EnableConfigurationProperties({ FileUploadProperties.class })
public class AMSCmsAccountManagementServiceApplication 
{
	public static void main(String[] args) 
	{
		SpringApplication.run(AMSCmsAccountManagementServiceApplication.class, args);
	}
	
	static 
	{
		AMSLogger.deleteExistingLogFile();
	}
	
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	//added by ankit
	@Bean
	public RestTemplate restTemplate() {
		return new RestTemplate();
	}
	//added by ankit
	
	
	private static void testSome() 
	{
		try
		{
			System.out.println(ams.cms.utility.Utils.generateHash("1234"));
			System.out.println("Hello Abhishek..........");
			
			String str = Utils.generateHash("Ab@123");
			EncryptDecryptUtil encryptDecryptUtil = new EncryptDecryptUtil();
			 
			String encryptPwd = encryptDecryptUtil.encrypt("Abcd123*");
			System.out.println("Encryption Password:: "+encryptPwd);
			
			String decryptPwd = encryptDecryptUtil.decrypt("qQEQY36xBW66eiX6xsK3Bw==");
			System.out.println("Decryption Password:: "+decryptPwd);
		}
		catch (Exception e) {
			System.out.println("Exception occured::"+ExceptionUtils.getStackTrace(e));
		}
	}	
}
