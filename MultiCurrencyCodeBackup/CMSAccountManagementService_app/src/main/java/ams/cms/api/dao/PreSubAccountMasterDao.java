package ams.cms.api.dao;

import java.util.List;

import ams.cms.api.model.PreSubAccountMaster;
import ams.cms.dao.GenericDao;

public interface PreSubAccountMasterDao extends GenericDao<PreSubAccountMaster>
{
	String getAccountTypeExist(PreSubAccountMaster preSubAccMaster);
	
	int updateIsAccountNoCreatedField(PreSubAccountMaster preSubAccountMaster);
	
	List<PreSubAccountMaster> getPendingRegCustWithLinkAccount(PreSubAccountMaster preSubAccountMaster);
}
