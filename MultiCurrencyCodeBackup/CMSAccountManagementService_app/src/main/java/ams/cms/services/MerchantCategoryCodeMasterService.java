package ams.cms.services;

import java.util.List;

import ams.cms.model.MerchantCategoryCodeMaster;

public interface MerchantCategoryCodeMasterService 
{
	List<MerchantCategoryCodeMaster> getAllListOfMerchantCategoryCode(MerchantCategoryCodeMaster merchantCategoryCodeMaster) throws Exception;
	List<MerchantCategoryCodeMaster> getUnSelectedMccAndDescrList(MerchantCategoryCodeMaster merchantCategoryCodeMaster) throws Exception;
	List<MerchantCategoryCodeMaster> getSelectedMccAndDescrList(MerchantCategoryCodeMaster merchantCategoryCodeMaster) throws Exception;
}
