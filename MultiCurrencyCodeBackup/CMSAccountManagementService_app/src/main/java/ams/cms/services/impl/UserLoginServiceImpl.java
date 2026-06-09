package ams.cms.services.impl;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ams.cms.model.UserLogin;
import ams.cms.dao.UserLoginDao;
import ams.cms.services.UserLoginService;

@Transactional
@Service
public class UserLoginServiceImpl implements UserLoginService {

	@Autowired
	private UserLoginDao userLoginDao;

	@Override
	public UserLogin saveUserLogin(UserLogin userLogin) throws Exception {

		userLoginDao.saveOrUpdate(userLogin);
		return userLogin;
	}

	@Override
	public List<UserLogin> getUserLoginList(String userId) {

		return userLoginDao.getUserLoginList(userId);
	}

	@Override
	public List<UserLogin> getUserLoginDetail(String sessionId) {
		
		return userLoginDao.getUserLoginDetail(sessionId);
	}

	@Override
	public UserLogin getLastUserLogin(String userId) 
	{
		return userLoginDao.getLastUserLogin(userId);
	}

	@Override
	public List<UserLogin> getUserLoginDetail(String userId, String sessionId) {
		
		return userLoginDao.getUserLoginDetail(userId, sessionId);
	}

	@Override
	public int updateUserLogin(UserLogin userLogin) {

		return userLoginDao.updateUserLogin(userLogin);
		
	}

}
