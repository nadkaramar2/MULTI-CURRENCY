package ams.cms.services.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ams.cms.dao.AccountTypeWiseWalletDao;
import ams.cms.model.AccountTypeWiseWalletMaster;
import ams.cms.services.AccountTypeWiseWalletService;

@Transactional
@Service
public class AccountTypeWiseWalletServiceImpl implements AccountTypeWiseWalletService
{
	@Autowired
	AccountTypeWiseWalletDao accountTypeWiseWalletDao;
	
	@Override
	public List<AccountTypeWiseWalletMaster> getAccountTypeWiseWalletMasterList(AccountTypeWiseWalletMaster accountTypeWiseWalletMaster) throws Exception 
	{
		return accountTypeWiseWalletDao.getAccountTypeWiseWalletMasterList(accountTypeWiseWalletMaster);
	}
	
	@Override
	public AccountTypeWiseWalletMaster addAccountTypeWiseWallet(AccountTypeWiseWalletMaster accountTypeWiseWalletMaster) throws Exception
	{
		HashMap<String, String> percentageMccWiseMapData = new HashMap<>();
		try 
		{
			String mccCodecStr = accountTypeWiseWalletMaster.getStrMccCode();
			String participantId = accountTypeWiseWalletMaster.getStrParticipantID();
			String loginUser = accountTypeWiseWalletMaster.getStrCreatedBy();
			String accountType = accountTypeWiseWalletMaster.getStrAccounType();
			
			String percentageStr = accountTypeWiseWalletMaster.getStrPercentage();
			
			String[] mccCodeArr = {};
			if (mccCodecStr!=null && mccCodecStr.indexOf(",")!=-1)
			{
				mccCodeArr = mccCodecStr.split(",");
			}
			System.out.println("addAccountTypeWiseWallet mccCodeArr::"+mccCodeArr);
			
			if (percentageStr!=null && percentageStr.indexOf(",")!=-1)
			{
				String[] percentageStrArr = percentageStr.split(",");
				System.out.println("percentageStrArr::"+percentageStrArr);
				for (String percentageDataStr : percentageStrArr) 
				{
					System.out.println("percentageDataStr::"+percentageDataStr);
					if (percentageDataStr!=null && percentageDataStr.trim().length() == 0)
					{
						continue;
					}
					
					String mccCode = percentageDataStr.substring(0, percentageDataStr.indexOf("~"));
					String percentageValue = percentageDataStr.substring(percentageDataStr.indexOf("~")+1);
					if (mccCode != null && mccCode.trim().length() > 0 && percentageValue != null && percentageValue.trim().length() > 0) 
					{
						percentageMccWiseMapData.put(mccCode.trim(), percentageValue);					
					}
				}
			}
			else 
			{
				if (percentageStr.indexOf("~")!= -1) 
				{
					String mccCode = percentageStr.substring(0, percentageStr.indexOf("~"));
					String percentageValue = percentageStr.substring(percentageStr.indexOf("~")+1);
					if (mccCode != null && mccCode.trim().length() > 0 && percentageValue != null && percentageValue.trim().length() > 0) 
					{
						percentageMccWiseMapData.put(mccCode.trim(), percentageValue);					
					}
				}
			}
			
			if (mccCodeArr.length > 0)
			{
				List<AccountTypeWiseWalletMaster> listOfAccountTypeWiseWalletMasters = new ArrayList<AccountTypeWiseWalletMaster>();
				for (int i = 0; i < mccCodeArr.length; i++)
				{
					String mccCode = mccCodeArr[i];
					if (mccCode!=null && mccCode.trim().length() > 0)
					{
						AccountTypeWiseWalletMaster accountTypeWiseWallet = new AccountTypeWiseWalletMaster();
						accountTypeWiseWallet.setStrMccCode(mccCode);
						accountTypeWiseWallet.setStrParticipantID(participantId);
						accountTypeWiseWallet.setStrCreatedBy(loginUser);
						accountTypeWiseWallet.setStrDateOfCreation(new Date());
						accountTypeWiseWallet.setStrAccounType(accountType);
						
						//Added for set percentage to this mcc Start
						if (percentageMccWiseMapData.size() > 0 && percentageMccWiseMapData.containsKey(mccCode))
						{
							accountTypeWiseWallet.setStrPercentage(percentageMccWiseMapData.get(mccCode));
							percentageMccWiseMapData.remove(mccCode);
						}
						else 
						{
							accountTypeWiseWallet.setStrPercentage(""+0);
						}
						//Added for set percentage to this mcc End
						
						listOfAccountTypeWiseWalletMasters.add(accountTypeWiseWallet);
					}
				}
				if (listOfAccountTypeWiseWalletMasters.size() > 0) 
				{
					int[] resultArr = accountTypeWiseWalletDao.batchEntryforAccountTypeWiseWallet(listOfAccountTypeWiseWalletMasters);
					if (resultArr.length > 0)
					{
						accountTypeWiseWalletMaster.setStrID(String.valueOf(resultArr));
					}
				}
			}
			else
			{
				//Added for set percentage to this mcc Start
				if (percentageMccWiseMapData.size() > 0 && percentageMccWiseMapData.containsKey(mccCodecStr))
				{
					accountTypeWiseWalletMaster.setStrPercentage(percentageMccWiseMapData.get(mccCodecStr));
					percentageMccWiseMapData.remove(mccCodecStr);
				}
				else 
				{
					accountTypeWiseWalletMaster.setStrPercentage(""+0);
				}
				//Added for set percentage to this mcc End
				accountTypeWiseWalletMaster.setStrDateOfCreation(new Date());
				accountTypeWiseWalletDao.save(accountTypeWiseWalletMaster);
			}
		}
		catch (Exception e) 
		{
			e.printStackTrace();
		}
		return accountTypeWiseWalletMaster;
	}
}
