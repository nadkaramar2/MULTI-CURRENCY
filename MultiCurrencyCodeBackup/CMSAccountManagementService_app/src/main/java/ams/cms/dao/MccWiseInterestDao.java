package ams.cms.dao;

import java.util.List;

import ams.cms.model.MccWiseInterestModel;

public interface MccWiseInterestDao extends GenericDao<MccWiseInterestModel> 
{
	List<MccWiseInterestModel> getMccWiseInterest(MccWiseInterestModel mccWiseInterestModel);
	
	Boolean validateMccWiseInterst(MccWiseInterestModel mccWiseInterestModel);

	List<MccWiseInterestModel> getMccWiseInterestView(MccWiseInterestModel mccWiseInterestModel);
	
	String getGracePeriod(String accountType);
}
