package ams.cms.dao;

import java.util.List;

import ams.cms.model.ChargeRelatedMaster;



public interface ChargeRelatedMasterDao extends GenericDao<ChargeRelatedMaster>{

	List<ChargeRelatedMaster> getChargeRelatedList(ChargeRelatedMaster chargeRelatedMaster);
	
	String getChargeRelatedDescription(ChargeRelatedMaster chargeRelatedMaster);

	Boolean validateChargeRelated(ChargeRelatedMaster chargeRelatedMaster);

}
