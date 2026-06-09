package ams.cms.services;

import java.util.List;

import ams.cms.model.UserLogin;

public interface UserLoginService {
	
	UserLogin saveUserLogin(UserLogin userlogin) throws Exception;

	List<UserLogin> getUserLoginList(String userId);
	
	List<UserLogin> getUserLoginDetail(String sessionId);
	
	UserLogin getLastUserLogin(String userId);
	
	List<UserLogin> getUserLoginDetail(String userId, String sessionId);
	
	int updateUserLogin(UserLogin userLogin);

}
