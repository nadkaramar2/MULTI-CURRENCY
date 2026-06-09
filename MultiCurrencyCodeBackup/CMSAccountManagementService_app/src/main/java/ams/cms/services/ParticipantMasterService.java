package ams.cms.services;

import java.util.List;

import ams.cms.model.ParticipantMaster;

public interface ParticipantMasterService 
{
	ParticipantMaster addParticipant(ParticipantMaster participantMaster) throws Exception;
	
	List<ParticipantMaster> getAllParticipantList(ParticipantMaster ParticipantMaster);
	
	String generateAPIkEYParticipantWise(ParticipantMaster participantMaster);
	
	String getApiKeyBasedOnParticipant(ParticipantMaster participantMaster);
}
