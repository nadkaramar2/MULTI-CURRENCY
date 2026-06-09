package ams.cms.api.dao.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import ams.cms.api.dao.UsersPasswordDao;
import ams.cms.api.model.UsersPassword;
import ams.cms.dao.generic.AbstractGenericDao;

@Repository
public class UserPasswordDaoImpl extends AbstractGenericDao<UsersPassword> implements UsersPasswordDao {

	@Autowired
	JdbcTemplate jdbcTemplate;

	
	@Override
	public int deleteExistingPassword(UsersPassword usersPassword) {
		// DELETE FROM users_passwords ORDER BY mobile_no = '8291305922'
		return 0;
	}

	@Override
	public List<UsersPassword> getUserPassword(UsersPassword userpassword) {
		try {
			String query = "SELECT password AS strNewPassword, password_created_date AS strPasswordCreatedDate FROM users_passwords where mobile_no = ? ORDER BY password_created_date ASC LIMIT 10";
			System.out.println("UserPasswordDaoImpl.getUserPassword()" + query.toString());
			List<UsersPassword> usrPassword = jdbcTemplate.query(query,
					new BeanPropertyRowMapper<UsersPassword>(UsersPassword.class),

					new Object[] {userpassword.getStrMobileNo()});
			return usrPassword;
		}

		catch (Exception e) {
			System.out.println("PreAccountMasterDaoImpl.getUserPassword()" + e);
			e.printStackTrace();
		}
		return null;
	}

}
