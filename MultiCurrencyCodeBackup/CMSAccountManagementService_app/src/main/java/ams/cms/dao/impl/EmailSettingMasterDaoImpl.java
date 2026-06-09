package ams.cms.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import ams.cms.dao.EmailSettingMasterDao;
import ams.cms.dao.generic.AbstractGenericDao;
import ams.cms.logger.AMSLogger;
import ams.cms.model.EmailSettingMaster;

@Repository
public class EmailSettingMasterDaoImpl extends AbstractGenericDao<EmailSettingMaster> implements EmailSettingMasterDao 
{
	private AMSLogger amsLogger = AMSLogger.getInstance(EmailSettingMasterDaoImpl.class);
	
	@SuppressWarnings("unchecked")
	@Override
	public EmailSettingMaster getEmailSettingInfoBasedOnParticipant(EmailSettingMaster emailSettingMaster) 
	{
		try 
		{
			System.out.println("emailSettingMaster::"+emailSettingMaster); 
			
			boolean isCriteriaExecute = false;
			Criteria criteria = createEntityCriteria();
			if (emailSettingMaster.getStrParticipantId()!=null && emailSettingMaster.getStrParticipantId().trim().length() > 0)
			{
				criteria.add(Restrictions.eq("strParticipantId", emailSettingMaster.getStrParticipantId().trim()));
				isCriteriaExecute = true;
			}
			
			if (isCriteriaExecute)
			{
				List<EmailSettingMaster> listData = (List<EmailSettingMaster>) criteria.list();
				if (listData !=null && listData.size() > 0) 
				{
					return listData.get(0);
				}
			}
		}
		catch (Exception e) 
		{
			//amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public EmailSettingMaster getEmailSettingInfoBasedOnParticipant(Connection conn, EmailSettingMaster emailSettingMaster) 
	{
		PreparedStatement pstmt = null;
        ResultSet rs = null;
        EmailSettingMaster emailSettingMasterResp = null;
		try 
		{
			String sql = "select * from mail_setting where participant_id = '"+emailSettingMaster.getStrParticipantId()+"'";
            pstmt = conn.prepareStatement(sql);
            rs = pstmt.executeQuery();
            
            while (rs.next()) 
            {
            	emailSettingMasterResp = new EmailSettingMaster();            	
            	emailSettingMasterResp.setHost(rs.getString("host"));
            	emailSettingMasterResp.setPort(rs.getInt("port"));
            	emailSettingMasterResp.setUsername(rs.getString("username"));
            	emailSettingMasterResp.setPassword(rs.getString("password"));
            	emailSettingMasterResp.setFromEmailAddress(rs.getString("from_email_address"));
            }
            System.out.println("emailSettingMasterResp::"+emailSettingMasterResp);
            if (pstmt != null)
            {
            	pstmt.close();
            	pstmt = null;
            }
            if (rs != null)
            {
            	rs.close();
            	rs = null;
            }
            if (conn != null) 
            {
            	conn.close();
            	conn = null;
            }
            
            System.out.println("pstmt::["+pstmt+"] resultSet=["+rs+"] conn=["+conn+"]");
            return emailSettingMasterResp;
		}
		catch (Exception e)
		{
			e.printStackTrace();
			if (conn!=null) 
			{
				try 
				{
					conn.close();
				}
				catch (SQLException e1) 
				{
					e1.printStackTrace();
				}
			}
		}
		return null;
	}

}
