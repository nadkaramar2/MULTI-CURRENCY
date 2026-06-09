package ams.cms.services.impl;

import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ams.cms.dao.MccWiseInterestDao;
import ams.cms.model.MccWiseInterestModel;
import ams.cms.services.MccWiseInterestService;

@Transactional
@Service
public class MccWiseInterestServiceImpl implements MccWiseInterestService
{
	@Autowired
	MccWiseInterestDao mccWiseInterestDao;
	
	@Override
	public MccWiseInterestModel saveMccWiseInterest(MccWiseInterestModel mccWiseInterestModel) throws Exception 
	{
		mccWiseInterestModel.setStrCreatedDate(new Date());
		mccWiseInterestDao.save(mccWiseInterestModel);
		return mccWiseInterestModel;
	}
	
	@Override
	public List<MccWiseInterestModel> getMccWiseInterest(MccWiseInterestModel mccWiseInterestModel) throws Exception 
	{
		return mccWiseInterestDao.getMccWiseInterest(mccWiseInterestModel);
	}
	//Added by Pankaj P for validate values - Start
	@Override
	public Boolean validateMccWiseInterst(MccWiseInterestModel mccWiseInterestModel) {
		// TODO Auto-generated method stub
		return mccWiseInterestDao.validateMccWiseInterst(mccWiseInterestModel);
	}
	//Added by Pankaj P for validate values - End

	//Added by Abhishek T for view Mcc_wise_interest table-start
	@Override
	public List<MccWiseInterestModel> getMccWiseInterestView(MccWiseInterestModel mccWiseInterestModel) {
		
		return mccWiseInterestDao.getMccWiseInterestView(mccWiseInterestModel);
	}
	//Added by Abhishek T for view Mcc_wise_interest table-End
	
	@Override
	public String getGracePeriod(String mcc) {
		return mccWiseInterestDao.getGracePeriod(mcc);
	}
}
