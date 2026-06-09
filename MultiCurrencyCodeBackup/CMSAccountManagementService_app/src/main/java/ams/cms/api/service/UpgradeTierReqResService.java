package ams.cms.api.service;

import ams.cms.api.model.TierUpdateDto;
import ams.cms.api.model.UpgradeTierReqRes;
import ams.cms.util.ProcessResponse;

//created by ankit
public interface UpgradeTierReqResService 
{
	ProcessResponse addEntryInUpgradeTierReqRes(TierUpdateDto tierUpdateDto);
	
	int updateUpgradeRequest(UpgradeTierReqRes upgradeTierReqRes);
}
