package ams.cms.dao;

import java.sql.Connection;

import ams.cms.model.EmailSettingMaster;

public interface EmailSettingMasterDao extends GenericDao<EmailSettingMaster> 
{
	EmailSettingMaster getEmailSettingInfoBasedOnParticipant(EmailSettingMaster emailSettingMaster);
	
	EmailSettingMaster getEmailSettingInfoBasedOnParticipant(Connection conn, EmailSettingMaster emailSettingMaster);
}
