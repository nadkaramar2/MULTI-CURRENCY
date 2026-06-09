package ams.cms.api.service;

import java.util.List;

import ams.cms.api.model.PreSubAccountMaster;

public interface PreSubAccountMasterService {

	PreSubAccountMaster savePreSubAccountMaster(PreSubAccountMaster preSubAccMaster) throws Exception;

	String getAccountTypeExist(PreSubAccountMaster preSubAccMaster) throws Exception;
	
	int updateIsAccountNoCreatedField(PreSubAccountMaster preSubAccountMaster) throws Exception;
	
	List<PreSubAccountMaster> getPendingRegCustWithLinkAccount(PreSubAccountMaster preSubAccountMaster);

}
