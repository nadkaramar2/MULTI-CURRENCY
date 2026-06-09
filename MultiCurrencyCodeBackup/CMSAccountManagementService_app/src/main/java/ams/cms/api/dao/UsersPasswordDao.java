package ams.cms.api.dao;

import java.util.List;

import ams.cms.api.model.UsersPassword;
import ams.cms.dao.GenericDao;




public interface UsersPasswordDao extends GenericDao<UsersPassword> {

	

	int deleteExistingPassword(UsersPassword usersPassword);
	
	List<UsersPassword> getUserPassword(UsersPassword userpassword);
}




