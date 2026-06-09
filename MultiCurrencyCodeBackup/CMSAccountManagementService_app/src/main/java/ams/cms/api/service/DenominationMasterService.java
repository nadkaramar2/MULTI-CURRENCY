package ams.cms.api.service;

import java.util.List;

import ams.cms.api.model.DenominationMaster;

public interface DenominationMasterService 
{
	DenominationMaster saveDenominationMasterData(DenominationMaster denominationMaster) throws Exception;

	List<DenominationMaster> getTxnDenominationDetails(DenominationMaster denominationMaster);

	List<DenominationMaster> getTxnDenominationDetailsbyAgent(DenominationMaster denominationMaster);
}
