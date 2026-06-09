package ams.cms.dao.impl;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

//import AMS.speta.usermgtsdk.constant.UserConstant;
import ams.cms.model.UserLogin;
import ams.cms.dao.UserLoginDao;
import ams.cms.dao.generic.AbstractGenericDao;

@Repository
public class UserLoginDaoImpl extends AbstractGenericDao<UserLogin> implements UserLoginDao {
	
	@Autowired
	JdbcTemplate jdbcTemplate; 
	
	@SuppressWarnings("unchecked")
	@Override
	public List<UserLogin> getUserLoginList(String userId){
		
		Criteria crit = createEntityCriteria();
		
		crit.add(Restrictions.eq("userId",userId));
		return crit.list();	
		
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<UserLogin> getUserLoginDetail(String sessionId) {
		
		Criteria crit = createEntityCriteria();
		
		crit.add(Restrictions.eq("sessionId",sessionId));
		
		return crit.list();	
	}

	@Override
	public UserLogin getLastUserLogin(String userId) {
		
		UserLogin userLogin = null;
		
		try{
			Criteria crit = createEntityCriteria();
			
			crit.add(Restrictions.eq("userId", userId));
			
			crit.addOrder(Order.desc("id"));
			
			crit.setMaxResults(1);
			
			userLogin = (UserLogin) crit.uniqueResult();
			
		}catch(Exception e){
			e.printStackTrace();
		}

		return userLogin;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<UserLogin> getUserLoginDetail(String userId, String sessionId) {
		
		Criteria crit = createEntityCriteria();
		crit.add(Restrictions.eq("userId",userId));
		crit.add(Restrictions.eq("sessionId",sessionId));
		crit.add(Restrictions.eq("status","A"));
		
		return crit.list();	
	}
	
	@Override
	public int updateUserLogin(UserLogin userLogin) {
		String sql = "UPDATE user_login SET logout_time = ?,status = ? WHERE session_id = ?";
		int update = this.jdbcTemplate.update(sql,
				new Object[] { userLogin.getLogoutTime(),userLogin.getStatus(),userLogin.getSessionId() });

		return update;
	}

}
