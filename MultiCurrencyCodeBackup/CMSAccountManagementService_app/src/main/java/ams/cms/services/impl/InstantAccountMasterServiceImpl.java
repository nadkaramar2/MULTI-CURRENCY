package ams.cms.services.impl;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ams.cms.dao.AccountCreditLimitCategoryDao;
import ams.cms.dao.AccountMasterDao;
import ams.cms.dao.AccountTypeMasterDao;
import ams.cms.dao.InstantAccountMasterDao;
import ams.cms.model.AccountCreation;
import ams.cms.model.AccountCreditLimitCategory;
import ams.cms.model.AccountTypeMaster;
import ams.cms.model.InstantAccountCreation;
import ams.cms.services.InstantAccountMasterService;
import ams.cms.utility.Utils;

@Transactional
@Service
public class InstantAccountMasterServiceImpl implements InstantAccountMasterService
{
	@Autowired
	AccountMasterDao accountMasterDao;
	
	@Autowired
	InstantAccountMasterDao instantAccountMasterDao;
	
	@Autowired
	AccountTypeMasterDao accountTypeMasterDao;
	
	@Autowired
	AccountCreditLimitCategoryDao accountCreditLimitCategoryDao;
	
	@Override
	public void saveInstantAccountInformation(InstantAccountCreation instantAccountCreation) throws Exception 
	{
		instantAccountMasterDao.save(instantAccountCreation);
	}

	@Override
	public int createInstantAccount(InstantAccountCreation instantAccountCreation) throws Exception
	{
		int[] responseArr = {};
		try 
		{
			String accountType = instantAccountCreation.getStrAccountType();
			String participantId = instantAccountCreation.getStrParticipantID();
			
			AccountTypeMaster accountTypeMasterdata = new AccountTypeMaster();
			accountTypeMasterdata.setStrAccountType(accountType);
			accountTypeMasterdata.setStrParticipantId(participantId);
			
			AccountTypeMaster accountTypeMaster = accountTypeMasterDao.getAccountTypeObject(accountTypeMasterdata);
			System.out.println("accountTypeMaster::"+accountTypeMaster);
			
			if (accountTypeMaster==null)
			{
				return responseArr.length;
			}
			
			String lastAccountNumber = accountTypeMaster.getStrLastAccNumber();
			boolean isCreditType = (accountTypeMaster.getStrIsCreditType().equalsIgnoreCase("YES")) ? true : false;
			System.out.println("isCreditType::"+isCreditType);
			
			AccountCreditLimitCategory accountCreditLimitCategory = new AccountCreditLimitCategory();
			String taxType = "";
			if (isCreditType)
			{
				accountCreditLimitCategory.setStrParticipantId(participantId);
				accountCreditLimitCategory.setStrCreditType("GENERAL");
				accountCreditLimitCategory = accountCreditLimitCategoryDao.getAccountCreditLimitCategoryObj(accountCreditLimitCategory);
				taxType = "IGST";
			}
			
			List<AccountCreation> accountCreationlist = new ArrayList<AccountCreation>();
			int totalQuantity = Integer.parseInt(instantAccountCreation.getStrQuantity());
			for (int i = 0; i < totalQuantity; i++)
			{
				String newAccountNo = Utils.getUpdatedAccNumber(lastAccountNumber);
				
				AccountCreation accountCreation = new AccountCreation();
				
				accountCreation.setStrParticipantID(participantId);
				accountCreation.setStrAccountType(instantAccountCreation.getStrAccountType());
				accountCreation.setStrFirstName(instantAccountCreation.getStrAccountName());
				accountCreation.setStrAccountNumber(newAccountNo);
				
				accountCreation.setStrOpeningBalance(accountCreditLimitCategory.getStrCreditLimit());
				//accountCreation.setStrCreditLimitAmount(accountCreditLimitCategory.getStrCreditLimit());
				accountCreation.setStrAvailableCreditLimit(accountCreditLimitCategory.getStrCreditLimit());
				accountCreation.setStrCreditLimitCategory(accountCreditLimitCategory.getStrCreditType());
				
				//accountCreation.setStrTaxType(taxType);
				accountCreation.setStrClosingBalance(accountCreditLimitCategory.getStrCreditLimit());

				accountCreationlist.add(accountCreation);
				
				lastAccountNumber = newAccountNo;
			}
			
			responseArr = accountMasterDao.batchEntryOfInstanAccount(accountCreationlist);
			
			if (responseArr!=null && responseArr.length > 0) 
			{
				accountTypeMaster.setStrLastAccNumber(lastAccountNumber);
				accountTypeMasterDao.updateLastAccountNumber(accountTypeMaster);
				
				saveInstantAccountInformation(instantAccountCreation);
				
				System.out.println("responseArr::"+responseArr);
				
				return responseArr.length;
			}			
		}
		catch (Exception e) 
		{
			e.printStackTrace();
		}
		return responseArr.length;	
	}
}
