package ams.cms.api.dao;

import java.util.List;

import ams.cms.api.model.PreAccountMaster;
import ams.cms.api.model.PreSubAccountMaster;
import ams.cms.dao.GenericDao;
import ams.cms.model.AccountKycDetails;

public interface PreAccountMasterDao extends GenericDao<PreAccountMaster>
{
	PreAccountMaster getPreAccountMasterData(String mobileNumber,String emailId);
	
	//Added by Sunny SOni for login Start
	//Here mobile no. used as userName 
	PreAccountMaster getPreAccountMasterBasedOnUserName(PreAccountMaster preAccountMaster);
	
	int updatePreAccountMasterBasedOnParameter(PreAccountMaster preAccountMaster);
	//Added by Sunny SOni for login End
	
	int updateSignUpdata(PreAccountMaster preAccountMaster);
	
	boolean isAccountExist(PreAccountMaster preAccountMaster);
	
	int updatePreAccountMaster(PreAccountMaster preAccountMaster,String otp);
	
	int updateAccountType(PreAccountMaster preAccountMaster,String accountType);
	
	PreAccountMaster getKycDataForverification(PreAccountMaster preAccountMaster);

	//List<AccountCreation> getIntantAccNumberBasedOnKyc(AccountCreation accountCreation);
	
	List<PreAccountMaster> getPreAccountData(PreAccountMaster preAccountMaster);
	
	PreAccountMaster getPreAccountMaster(PreAccountMaster preAccountMaster);
	
	PreAccountMaster getPreAccountMasterDataByMobileNumber(String mobileNumber);
	
	int updatePreAccountMaster(PreAccountMaster preAccountMaster);	
	
	PreAccountMaster getPreAccountInfo(String mobileNumber);
	
	int updatePreAccountExistingInfoData(PreAccountMaster preAccountMaster);

	int updatePassword(PreAccountMaster preAccountMaster);

	boolean isEmaiExist(PreAccountMaster preAccountMaster);
	
	int addressProofUpload(String imageName,String mobileNo);
	
	int IdentityProofUpload(String imageName,String mobileNo);
	
	AccountKycDetails getKycIdentityDocumentType(String mobileNo);
	
	AccountKycDetails getKycAddressDocumentType(String mobileNo);
	
	int saveAccountKycDetails(PreAccountMaster accountKycDetails);
	
	PreAccountMaster getPreAccountMasterToken(String token);
	
	List<PreAccountMaster> getNonLinkedCustomerList(PreAccountMaster preAccountMaster);
	
	List<PreAccountMaster> getNonLinkedAccountNoList(PreAccountMaster preAccountMaster);
	
	String getCountryCode(String mobileNo);

	int getUpdateMobileCustlist(PreAccountMaster preAccountMaster);

	int getUpdatePreAccountMasterKyclist(PreAccountMaster preAccountMaster);

	List<PreSubAccountMaster> getPendingRegCustWithLinkAccount(PreSubAccountMaster preSubAccountMaster);
	
	List<PreAccountMaster> getRegisterCustomers(PreAccountMaster preAccountMaster);
	
	int addAddressOfCustomer(PreAccountMaster preAccountMaster);
	
	// created by ankit on 09-05-2023
	int saveTier1PassportPhoto(String saveImageName, String mobileNo);
	
	int saveTier2PassportPhoto(String saveImageName, String mobileNo);
	// created by ankit on 09-05-2023

	// created by ankit on 12-05-2023
	int addBvnNo(PreAccountMaster preAccountMaster);
	// created by ankit on 12-05-2023

	// created by ankit on 12-05-2023
	List<PreAccountMaster> checkBvnAndGetCustId(PreAccountMaster preAccountMaster);
	// created by ankit on 12-05-2023
}
