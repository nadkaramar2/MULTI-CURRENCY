package ams.cms.dao;

import java.util.List;

import ams.cms.model.UserLogin;
import ams.cms.dao.generic.GenericDao;

public interface UserLoginDao extends GenericDao<UserLogin> {
	
	List<UserLogin> getUserLoginList(String userId);
	
	List<UserLogin> getUserLoginDetail(String sessionId);
	
	UserLogin getLastUserLogin(String userId);
	
	List<UserLogin> getUserLoginDetail(String userId, String sessionId);
	
	int updateUserLogin(UserLogin userLogin);
}
