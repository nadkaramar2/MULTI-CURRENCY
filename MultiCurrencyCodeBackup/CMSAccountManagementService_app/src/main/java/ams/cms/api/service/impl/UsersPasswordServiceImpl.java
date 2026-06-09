package ams.cms.api.service.impl;

import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ams.cms.api.dao.UsersPasswordDao;
import ams.cms.api.model.UsersPassword;
import ams.cms.api.service.UsersPasswordService;


@Transactional
@Service
public class UsersPasswordServiceImpl implements UsersPasswordService{

	@Autowired
	UsersPasswordDao usersPasswordDao;
	
	

	@Override
	public int addUsersPassword(UsersPassword usersPassword)
	{
		usersPassword.setStrPasswordCreatedDate(new Date());
		usersPasswordDao.save(usersPassword);
		return Integer.parseInt(usersPassword.getStrID());
	}

	@Override
	public int deleteExistingPassword(UsersPassword usersPassword) {
		
		return usersPasswordDao.deleteExistingPassword(usersPassword);
	}
	
	
	@Override
	public List<UsersPassword> getUserPassword(UsersPassword userpassword) {
		return usersPasswordDao.getUserPassword(userpassword);
	}

}
