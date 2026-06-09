package ams.cms.security;

import ams.cms.api.model.PreAccountMaster;

public class UserFactory 
{
	public static SecurityUser create(PreAccountMaster preAccountMaster, boolean isAlreadyLogin)
	{
		String userName = null;
		String password = null;
		
		if (preAccountMaster.getUserName() != null)
		{
			userName = preAccountMaster.getUserName();
		}
		else if (preAccountMaster.getStrMobileNo() != null)
		{
			userName = preAccountMaster.getStrMobileNo();
		}
		 
		if (preAccountMaster.getStrPassword() != null)
		{
			password = preAccountMaster.getStrPassword();
		}
		
		//password = preAccountMaster.getEncryptedPassword();
		
		return new SecurityUser(userName, password, null, isAlreadyLogin);
	}
	
}
