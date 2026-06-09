package ams.cms.services;

import java.util.List;

import ams.cms.model.ParticipantWiseWalletMaster;

public interface ParticipantWiseWalletService 
{
	ParticipantWiseWalletMaster addParticipantWiseWallet(ParticipantWiseWalletMaster participantWiseWalletMaster) throws Exception;
	
	List<ParticipantWiseWalletMaster> getParticipantBasedMcc(ParticipantWiseWalletMaster participantWiseWalletMaster) throws Exception;
	
	List<ParticipantWiseWalletMaster> getParticipantBasedMccAndDescr(ParticipantWiseWalletMaster participantWiseWalletMaster) throws Exception;
}
