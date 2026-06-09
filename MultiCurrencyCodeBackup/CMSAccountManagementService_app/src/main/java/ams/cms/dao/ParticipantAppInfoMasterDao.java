package ams.cms.dao;

import ams.cms.model.ParticipantAppInfoMaster;

public interface ParticipantAppInfoMasterDao extends GenericDao<ParticipantAppInfoMaster> 
{
	ParticipantAppInfoMaster getParticipantAppInfoObject(ParticipantAppInfoMaster participantAppInfoMaster);
	
	int updateParticipantAppInfo(ParticipantAppInfoMaster participantAppInfoMaster) throws Exception;
}
