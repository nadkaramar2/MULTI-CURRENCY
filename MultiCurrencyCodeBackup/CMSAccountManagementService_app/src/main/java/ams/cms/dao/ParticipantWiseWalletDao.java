package ams.cms.dao;

import java.util.List;

import ams.cms.model.ParticipantWiseWalletMaster;

public interface ParticipantWiseWalletDao extends GenericDao<ParticipantWiseWalletMaster>
{
	int[] batchEntryforParticipantWiseWallet(List<ParticipantWiseWalletMaster> listOfParticipantWiseWalletMasters);
	
	List<ParticipantWiseWalletMaster> getParticipantBasedMcc(ParticipantWiseWalletMaster participantWiseWalletMaster);
	
	List<ParticipantWiseWalletMaster> getParticipantBasedMccAndDescr(ParticipantWiseWalletMaster participantWiseWalletMaster);
}
