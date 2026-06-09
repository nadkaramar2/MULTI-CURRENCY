package ams.cms.security;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import ams.cms.api.model.PreAccountMaster;
import ams.cms.api.service.PreAccountMasterService;
import ams.cms.util.EncryptDecryptUtil;

@Service
public class CustomUserDetailsService implements CustomUserDetails
{
	@Autowired
	private PreAccountMasterService preAccountMasterService;
	
	@Autowired
	private EncryptDecryptUtil encryptDecryptUtil;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	 
	
	//SecurityUser securityUser;//Added getting for exiting details
	
	public static HashMap<String, SecurityUser> securityUserMap = new HashMap<>();
	public static HashMap<String, String> usernameMap = new HashMap<>();
	
	public static boolean isByPassRequestApi = false;	
	
	@Override
	public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException 
	{
		PreAccountMaster preAccountMaster = new PreAccountMaster();
		try
		{
			preAccountMaster.setStrMobileNo(userName);
			preAccountMaster = preAccountMasterService.getPreAccountMasterBasedOnUserName(preAccountMaster);
			if (preAccountMaster.getStrID() == null && preAccountMaster.getStrPassword() == null)
			{
				throw new UsernameNotFoundException("UserName Not Found!!!");
			}
		}
		catch (NullPointerException e) 
		{
			e.printStackTrace();
			throw new NullPointerException(""+e);
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
		
		String decryptPassword = encryptDecryptUtil.decrypt(preAccountMaster.getStrPassword());
		System.out.println("decryptPassword::"+decryptPassword);
		
		String encyptpassword = passwordEncoder.encode(decryptPassword);
		
		preAccountMaster.setStrPassword(encyptpassword);
		
		return UserFactory.create(preAccountMaster, true);
	}

	@Override
	public SecurityUser getByPassUserDetails(Map<String, String> bypassMapData) 
	{
		PreAccountMaster preAccountMaster = new PreAccountMaster();
		String userName = bypassMapData.get("user_name");
		isByPassRequestApi = true;
		preAccountMaster.setUserName(userName);
		//this.securityUser = UserFactory.create(preAccountMaster, false);
		SecurityUser securityUser = UserFactory.create(preAccountMaster, false);
		securityUserMap.put(userName, securityUser);
		//return this.securityUser;
		return securityUser;
	}
	
	@Override
	public SecurityUser getUserDetailsByUserName(String userName) 
	{
		//return this.securityUser;
		if (securityUserMap.containsKey(userName))
		{
			return securityUserMap.get(userName);
		}
		return null;
	}
	
	/*
	public void updatejwtToken(String jwtToken)
	{
		this.securityUser.setJwtToken(jwtToken);
	}
	*/
	
	public void updatejwtToken(String jwtToken, String userName) 
	{
		if (securityUserMap.containsKey(userName))
		{
			securityUserMap.get(userName).setJwtToken(jwtToken);
			usernameMap.put(jwtToken, userName);
		}
	}
}
