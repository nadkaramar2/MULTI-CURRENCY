package ams.cms.services.impl;

import java.sql.Connection;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import ams.cms.api.utitlity.Utils;
import ams.cms.dao.EmailSettingMasterDao;
import ams.cms.model.EmailSettingMaster;
import ams.cms.services.EmailSettingMasterService;

@Transactional
@Service
public class EmailSettingMasterServiceImpl implements EmailSettingMasterService
{
	@Autowired
	private EmailSettingMasterDao emailSettingMasterDao;
	
	@Autowired
	private Environment environment;

	@Override
	public EmailSettingMaster getEmailSettingInfoBasedOnParticipant(EmailSettingMaster emailSettingMaster) 
	{
		try 
		{
			String databaseUrl = environment.getProperty("database_url");
			System.out.println("databaseUrl=["+databaseUrl+"]");
			
			String databaseUsername = environment.getProperty("database_username");
			System.out.println("databaseUsername=["+databaseUsername+"]");
			
			String databasePassword = environment.getProperty("database_password");
			System.out.println("databasePassword=["+databasePassword+"]");
			
			Connection conn = Utils.getConnectDB(databaseUrl, databaseUsername, databasePassword);
			System.out.println("conn=["+conn+"]");
			
			return emailSettingMasterDao.getEmailSettingInfoBasedOnParticipant(conn, emailSettingMaster);
		}
		catch (Exception e) 
		{
			e.printStackTrace();
		}
		
		return null;
	}

	@Override
	public EmailSettingMaster getEmailSettingInfoBasedOnParticipantId(EmailSettingMaster emailSettingMaster) 
	{
		return emailSettingMasterDao.getEmailSettingInfoBasedOnParticipant(emailSettingMaster);
	}
	
}
