package ams.cms.dao;

import java.util.List;

import ams.cms.model.MerchantCategoryCodeMaster;

public interface MerchantCategoryCodeMasterDao extends GenericDao<MerchantCategoryCodeMaster>
{
	List<MerchantCategoryCodeMaster> getUnSelectedMccAndDescrList(MerchantCategoryCodeMaster merchantCategoryCodeMaster);
	List<MerchantCategoryCodeMaster> getSelectedMccAndDescrList(MerchantCategoryCodeMaster merchantCategoryCodeMaster);
}
