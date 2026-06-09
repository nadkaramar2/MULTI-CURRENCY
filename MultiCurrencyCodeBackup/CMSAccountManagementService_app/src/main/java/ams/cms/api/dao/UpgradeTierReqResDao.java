package ams.cms.api.dao;

import java.util.List;

import ams.cms.api.model.UpgradeTierReqRes;
import ams.cms.dao.GenericDao;

//added by ankit -12-05-2023
public interface UpgradeTierReqResDao extends GenericDao<UpgradeTierReqRes>
{
	List<UpgradeTierReqRes> getDetails(UpgradeTierReqRes upgradeTierReqRes);

	int addEntryInUpgradeTierReqRes(UpgradeTierReqRes upgradeTierReqRes);
	
	int updateUpgradeRequest(UpgradeTierReqRes upgradeTierReqRes);
}
