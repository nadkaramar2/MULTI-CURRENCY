package ams.cms.services;

import java.util.List;

import ams.cms.model.MccWiseInterestModel;

public interface MccWiseInterestService 
{
	MccWiseInterestModel saveMccWiseInterest(MccWiseInterestModel mccWiseInterestModel) throws Exception;
	
	List<MccWiseInterestModel> getMccWiseInterest(MccWiseInterestModel mccWiseInterestModel) throws Exception;
	
	Boolean validateMccWiseInterst(MccWiseInterestModel mccWiseInterestModel);

	List<MccWiseInterestModel> getMccWiseInterestView(MccWiseInterestModel mccWiseInterestModel);
	
	String getGracePeriod(String mcc);
}
