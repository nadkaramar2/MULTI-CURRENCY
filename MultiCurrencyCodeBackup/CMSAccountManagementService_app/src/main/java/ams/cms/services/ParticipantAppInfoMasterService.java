package ams.cms.services;

import ams.cms.model.ParticipantAppInfoMaster;

public interface ParticipantAppInfoMasterService 
{
	ParticipantAppInfoMaster addParticipantAppInfo(ParticipantAppInfoMaster participantAppInfoMaster) throws Exception;
	
	int updateParticipantAppInfo(ParticipantAppInfoMaster participantAppInfoMaster) throws Exception;
	
	ParticipantAppInfoMaster getParticipantAppInfoObject(ParticipantAppInfoMaster participantAppInfoMaster) throws Exception;
}
