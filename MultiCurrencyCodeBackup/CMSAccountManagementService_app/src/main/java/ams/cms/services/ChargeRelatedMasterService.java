package ams.cms.services;

import java.util.List;

import ams.cms.model.ChargeRelatedMaster;

public interface ChargeRelatedMasterService {

	List<ChargeRelatedMaster> getChargeRelatedList(ChargeRelatedMaster chargeRelatedMaster) throws Exception;

	String getChargeRelatedDescription(ChargeRelatedMaster chargeRelatedMaster);

	ChargeRelatedMaster addChargeRelated(ChargeRelatedMaster chargeRelatedMaster);

	Boolean validateChargeRelated(ChargeRelatedMaster chargeRelatedMaster);
	
}
