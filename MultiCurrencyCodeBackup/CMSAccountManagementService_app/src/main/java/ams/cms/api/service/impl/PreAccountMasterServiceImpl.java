package ams.cms.api.service.impl;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ams.cms.api.dao.PreAccountMasterDao;
import ams.cms.api.dao.UpgradeTierReqResDao;
import ams.cms.api.model.PreAccountMaster;
import ams.cms.api.model.TierUpdateDto;
import ams.cms.api.model.UpgradeTierReqRes;
import ams.cms.api.service.PreAccountMasterService;
import ams.cms.util.ProcessResponse;

@Transactional
@Service
public class PreAccountMasterServiceImpl implements PreAccountMasterService
{
	@Autowired
	PreAccountMasterDao preAccountMasterDao;	
	
	@Autowired
	UpgradeTierReqResDao upgradeTierReqResDao;
	
	@Override
	public PreAccountMaster getPreAccountMasterData(String mobileNumber,String emailId) throws Exception 
	{
		return preAccountMasterDao.getPreAccountMasterData(mobileNumber, emailId);
	}
	
	@Override
	public PreAccountMaster saveSignUpData(PreAccountMaster preAccountMaster) throws Exception
	{
		 preAccountMasterDao.save(preAccountMaster);		 
		 return preAccountMaster;
	}
	
	@Override
	public int updateSignUpdata(PreAccountMaster preAccountMaster) throws Exception{
		
		return preAccountMasterDao.updateSignUpdata(preAccountMaster) ;
		
	}
	
	@Override
	public PreAccountMaster getPreAccountMasterBasedOnUserName(PreAccountMaster preAccountMaster) throws Exception 
	{
		return preAccountMasterDao.getPreAccountMasterBasedOnUserName(preAccountMaster);
	}

	@Override
	public int updatePreAccountMasterBasedOnParameter(PreAccountMaster preAccountMaster) throws Exception {
		return preAccountMasterDao.updatePreAccountMasterBasedOnParameter(preAccountMaster);
	}
	
	@Override
	public boolean isAccountExist(PreAccountMaster preAccountMaster) throws Exception 
	{
		return preAccountMasterDao.isAccountExist(preAccountMaster);
	}
	
	public int updateSignUpData(PreAccountMaster preAccountMaster, String otp) throws Exception {

		return preAccountMasterDao.updatePreAccountMaster(preAccountMaster, otp);
	}
	
	public int updateAccountType(PreAccountMaster preAccountMaster, String accountType) throws Exception {
		return preAccountMasterDao.updateAccountType(preAccountMaster , accountType);
	}
	@Override
	public PreAccountMaster getPreAccountMasterDataByMobileNumber(String mobileNumber) throws Exception 
	{
		return preAccountMasterDao.getPreAccountMasterDataByMobileNumber(mobileNumber);
	}
	
	@Override
	public PreAccountMaster getKycDataForverification(PreAccountMaster preAccountMaster) {
		return preAccountMasterDao.getKycDataForverification(preAccountMaster);
	}

	@Override
	public List<PreAccountMaster> getPreAccountData(PreAccountMaster preAccountMaster) throws Exception
	{
		return preAccountMasterDao.getPreAccountData(preAccountMaster);
	}
	
	@Override
	public PreAccountMaster getPreAccountMaster(PreAccountMaster preAccountMaster) throws Exception {
		return preAccountMasterDao.getPreAccountMaster(preAccountMaster);
	}
	
	@Override
	public int updatePreAccountMaster(PreAccountMaster preAccountMaster) throws Exception {
		return preAccountMasterDao.updatePreAccountMaster(preAccountMaster);
	}
	
	@Override
	public PreAccountMaster getPreAccountInfo(String mobileNumber) {
		return preAccountMasterDao.getPreAccountInfo(mobileNumber);
	}

	@Override
	public int updatePassword(PreAccountMaster preAccountMaster) {
		return preAccountMasterDao.updatePassword(preAccountMaster);
	}

	@Override
	public boolean isEmaiExist(PreAccountMaster preAccountMaster) throws Exception {
		return preAccountMasterDao.isEmaiExist(preAccountMaster);
	}
	
	@Override
	public int saveAccountKycDetails(PreAccountMaster accountKycDetailsMaster)throws Exception {
		int saveAccountKycDetails = preAccountMasterDao.saveAccountKycDetails(accountKycDetailsMaster);
		return saveAccountKycDetails;
	}

	public PreAccountMaster getPreAccountMasterToken(String token)
	{
		return preAccountMasterDao.getPreAccountMasterToken(token);
	}
	
	@Override
	public List<PreAccountMaster> getNonLinkedCustomerList(PreAccountMaster preAccountMaster) throws Exception {
		return preAccountMasterDao.getNonLinkedCustomerList(preAccountMaster);
	}

	@Override
	public List<PreAccountMaster> getNonLinkedAccountNoList(PreAccountMaster preAccountMaster) throws Exception {
		return preAccountMasterDao.getNonLinkedAccountNoList(preAccountMaster);
	}

	@Override
	public int updatePreAccountExistingInfoData(PreAccountMaster preAccountMaster) throws Exception {
		return preAccountMasterDao.updatePreAccountExistingInfoData(preAccountMaster);
 	}

	@Override
	public String getCountryCode(String mobileNo) throws Exception {
		return preAccountMasterDao.getCountryCode(mobileNo);
	}

	@Override
	public int getUpdateMobileCustlist(PreAccountMaster preAccountMaster) 
	{
		return preAccountMasterDao.getUpdateMobileCustlist(preAccountMaster);
	}

	@Override
	public int getUpdatePreAccountMasterKyclist(PreAccountMaster preAccountMaster) 
	{
		return preAccountMasterDao.getUpdatePreAccountMasterKyclist(preAccountMaster);
	}

	@Override
	public PreAccountMaster saveInsertPreAccountMasterlist(PreAccountMaster preAccountMaster) 
	{
		 preAccountMasterDao.save(preAccountMaster);		 
		 return preAccountMaster;
	}
	
	@Override
	public List<PreAccountMaster> getRegisterCustomers(PreAccountMaster preAccountMaster) {
		return preAccountMasterDao.getRegisterCustomers(preAccountMaster);
	}

	@Override
	public int addAddressOfCustomer(PreAccountMaster preAccountMaster) 
	{
		return preAccountMasterDao.addAddressOfCustomer(preAccountMaster);
	}
	//added by ankit on 12-05-2023
	@Override
	public ProcessResponse addBvnNumber(TierUpdateDto tierUpdateDto) 
	{
		ProcessResponse processResponse = new ProcessResponse();
		try
		{
			String strBvnNo = tierUpdateDto.getStrBvnNo();
			String requestCustId = tierUpdateDto.getStrCustId();
			PreAccountMaster preAccountMaster = new PreAccountMaster();
			preAccountMaster.setStrBvnNo(strBvnNo);
			preAccountMaster.setCust_id(requestCustId);
			UpgradeTierReqRes upgradeTierReqRes = new UpgradeTierReqRes();
			
			// checking bvn if exists then fetchng it 
			//'If BVN exitst then that means it is assigned to a customer'
			List<PreAccountMaster> checkBvnAndGetCustId = preAccountMasterDao.checkBvnAndGetCustId(preAccountMaster);
			if(checkBvnAndGetCustId.size() > 0) 
			{
				PreAccountMaster fetchedPreAccountMaster = checkBvnAndGetCustId.get(0);
				String cust_id = fetchedPreAccountMaster.getCust_id();
				if(requestCustId.equals(cust_id))
				{ 
					//if same cust id wiht bvn no making request checking if it is pending or not or approved 
					upgradeTierReqRes.setStrCustId(requestCustId);
					upgradeTierReqRes.setStrTierType("tier2");
					
					List<UpgradeTierReqRes> upgradeTierReqResFetchedDataList = upgradeTierReqResDao.getDetails(upgradeTierReqRes);
					if(upgradeTierReqResFetchedDataList.size()>0)
					{
						String status = upgradeTierReqResFetchedDataList.get(0).getStrReqStatus();
						if ("approved".equalsIgnoreCase(status)) 
						{
							processResponse.setCode("E000");
							processResponse.setStatus("Failed");
							processResponse.setMessage("Bvn is Already Configured.");
						}
						else
						{
							processResponse.setCode("S0000");
							processResponse.setStatus("Success");
							processResponse.setMessage("Your BVN no has been Saved Succsssfully.");
						}
					}
					else 
					{
						preAccountMasterDao.addBvnNo(preAccountMaster);
						processResponse.setCode("S0000");
						processResponse.setStatus("Success");
						processResponse.setMessage("Your BVN no has been Saved Succsssfully.");
					}
				}
				else
				{	
					processResponse.setCode("E000");
					processResponse.setStatus("Failed");
					processResponse.setMessage("Bvn already configured with other customer");
				}
			}
			else 
			{
				//'add to BVN cuz did not find bvn in kyc Datails'
				preAccountMasterDao.addBvnNo(preAccountMaster);
				processResponse.setCode("S0000");
				processResponse.setStatus("Success");
				processResponse.setMessage("Your BVN no has been Saved Succsssfully.");
			}
		}
		catch(Exception e) 
		{
			processResponse.setCode("E0000");
			processResponse.setStatus("Error");
			processResponse.setMessage("Internal Server Error "+e.getMessage());
			e.printStackTrace();
		}
		return processResponse;
	}
	//added by ankit on 12-05-2023
}
