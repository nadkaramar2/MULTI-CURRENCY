package ams.cms.dao;

import java.util.List;

import ams.cms.model.ParticipantMaster;

public interface ParticipantMasterDao extends GenericDao<ParticipantMaster>
{
	List<ParticipantMaster> getAllParticipantList(ParticipantMaster ParticipantMaster);
	
	String getApiKeyBasedOnParticipant(ParticipantMaster participantMaster);
}
