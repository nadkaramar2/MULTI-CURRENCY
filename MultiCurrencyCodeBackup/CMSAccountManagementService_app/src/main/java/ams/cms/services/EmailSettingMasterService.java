package ams.cms.services;

import ams.cms.model.EmailSettingMaster;

public interface EmailSettingMasterService 
{
	EmailSettingMaster getEmailSettingInfoBasedOnParticipantId(EmailSettingMaster emailSettingMaster);
	
	EmailSettingMaster getEmailSettingInfoBasedOnParticipant(EmailSettingMaster emailSettingMaster);
}
