package ams.cms.api.service;

import java.util.List;

import ams.cms.api.model.UsersPassword;

public interface UsersPasswordService {

	int addUsersPassword(UsersPassword usersPassword);

	int deleteExistingPassword(UsersPassword usersPassword);

	List<UsersPassword> getUserPassword(UsersPassword userpassword);
}
