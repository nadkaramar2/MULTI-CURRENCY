package ams.cms.api.service;

import java.util.List;

import ams.cms.api.model.PreAccountMaster;
import ams.cms.api.model.TierUpdateDto;
import ams.cms.util.ProcessResponse;

public interface PreAccountMasterService 
{
	 PreAccountMaster saveSignUpData(PreAccountMaster preAccountMaster)throws Exception;
	 
	 int updateSignUpdata(PreAccountMaster preAccountMaster)throws Exception;
	 
	 PreAccountMaster getPreAccountMasterData(String mobileNumber,String emailId)throws Exception;
	 
	//Added by Sunny SOni for login Start
	 PreAccountMaster getPreAccountMasterBasedOnUserName(PreAccountMaster preAccountMaster) throws Exception;
	 
	 int updatePreAccountMasterBasedOnParameter(PreAccountMaster preAccountMaster) throws Exception;
	//Added by Sunny SOni for login End
	 
	 boolean isAccountExist(PreAccountMaster preAccountMaster) throws Exception;

	 int updateSignUpData(PreAccountMaster preAccountMaster,String otp) throws Exception;
	 
	 int updateAccountType(PreAccountMaster preAccountMaster,String accountType) throws Exception;
	 
	 PreAccountMaster getPreAccountMasterDataByMobileNumber(String mobileNo) throws Exception;
	 
	 PreAccountMaster getKycDataForverification(PreAccountMaster preAccountMaster) throws Exception;
	 
	 List<PreAccountMaster> getPreAccountData(PreAccountMaster preAccountMaster) throws Exception;
	 
	 PreAccountMaster getPreAccountMaster(PreAccountMaster preAccountMaster) throws Exception;
	 
	 int updatePreAccountMaster(PreAccountMaster preAccountMaster) throws Exception;

	 int updatePassword(PreAccountMaster preAccountMaster);

	 PreAccountMaster getPreAccountInfo(String mobileNumber);
	 
	 int updatePreAccountExistingInfoData(PreAccountMaster preAccountMaster) throws Exception;

	 boolean isEmaiExist(PreAccountMaster preAccountMaster) throws Exception;
	 
	 int saveAccountKycDetails(PreAccountMaster accountKycDetailsMaster)throws Exception;
	 
	 PreAccountMaster getPreAccountMasterToken(String token);
	 
	 List<PreAccountMaster> getNonLinkedCustomerList(PreAccountMaster preAccountMaster) throws Exception;
		
	 List<PreAccountMaster> getNonLinkedAccountNoList(PreAccountMaster preAccountMaster) throws Exception;
	 
	 String getCountryCode(String mobileNo) throws Exception;

	int getUpdateMobileCustlist(PreAccountMaster preAccountMaster);

	int getUpdatePreAccountMasterKyclist(PreAccountMaster preAccountMaster);

	PreAccountMaster saveInsertPreAccountMasterlist(PreAccountMaster preAccountMaster);
	
	List<PreAccountMaster> getRegisterCustomers(PreAccountMaster preAccountMaster);
	
	int addAddressOfCustomer(PreAccountMaster preAccountMaster);
	
	//added by ankit on 12-05-2023
	ProcessResponse addBvnNumber(TierUpdateDto tierUpdateDto);
	//added by ankit on 12-05-2023
}
