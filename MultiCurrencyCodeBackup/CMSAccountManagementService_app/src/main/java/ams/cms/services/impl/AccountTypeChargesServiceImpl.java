package ams.cms.services.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;

import ams.cms.dao.AccountTypeChargesDao;
import ams.cms.dao.ChargeMasterDao;
import ams.cms.model.AccountTypeCharges;
import ams.cms.services.AccountTypeChargesService;

@Transactional
@Service
public class AccountTypeChargesServiceImpl implements AccountTypeChargesService{

	@Autowired
	AccountTypeChargesDao accountTypeChargesDao;
	
	@Autowired
	ChargeMasterDao chargeMasterDao;
	
	@Override
	public List<AccountTypeCharges> getSelectedChargesAccountTypeWise(AccountTypeCharges accountTypeCharges) {
		return accountTypeChargesDao.getSelectedChargesAccountTypeWise(accountTypeCharges);
	}

	@Override
	public AccountTypeCharges addChargingConfigData(AccountTypeCharges accountTypeCharges) 
	{
		HashMap<String, String> amountWiseMapData = new HashMap<>();
		HashMap<String, String> percentageWiseMapData = new HashMap<>();
		try 
		{
			String selectedChargeTypeWithAmount = accountTypeCharges.getSelectedChargeTypeWithAmount();
			String selectedChargeTypeWithPercentage = accountTypeCharges.getSelectedChargeTypeWithPercentage();
			String participantId = accountTypeCharges.getStrParticipantID();
			String loginUser = accountTypeCharges.getStrCreatedBy();
			String accountType = accountTypeCharges.getStrAccountType();
			
			if (selectedChargeTypeWithAmount!=null && selectedChargeTypeWithAmount.trim().length() > 0)
			{
				if (selectedChargeTypeWithAmount.indexOf(",")!=-1) 
				{
					String[] selectedChargeTypeWithAmountArr = selectedChargeTypeWithAmount.split(",");
					for(String selectedChargeTypeWithAmt : selectedChargeTypeWithAmountArr)
					{
						String[] chargeTypeWithAmtArr =  selectedChargeTypeWithAmt.split("~");
						String chargeAmount = chargeTypeWithAmtArr[0];
						String chargeType = chargeTypeWithAmtArr[1];
						amountWiseMapData.put(chargeType, chargeAmount);
					}
				}
				else
				{
					String[] chargeTypeWithAmtArr =  selectedChargeTypeWithAmount.split("~");
					String chargeAmount = chargeTypeWithAmtArr[0];
					String chargeType = chargeTypeWithAmtArr[1];
					amountWiseMapData.put(chargeType, chargeAmount);
				}
			}
			
			if (selectedChargeTypeWithPercentage!=null && selectedChargeTypeWithPercentage.trim().length() > 0)
			{
				if (selectedChargeTypeWithPercentage.indexOf(",")!=-1) 
				{
					String[] selectedChargeTypeWithAmountArr = selectedChargeTypeWithPercentage.split(",");
					for(String selectedChargeTypeWithAmt : selectedChargeTypeWithAmountArr)
					{
						String[] chargeTypeWithPerctgArr =  selectedChargeTypeWithAmt.split("~");
						String chargePercentage = chargeTypeWithPerctgArr[0];
						String chargeType = chargeTypeWithPerctgArr[1];
						percentageWiseMapData.put(chargeType, chargePercentage);
					}
				}
				else
				{
					String[] chargeTypeWithPerctgArr =  selectedChargeTypeWithPercentage.split("~");
					String chargePercentage = chargeTypeWithPerctgArr[0];
					String chargeType = chargeTypeWithPerctgArr[1];
					percentageWiseMapData.put(chargeType, chargePercentage);
				}
			}
			
			List<AccountTypeCharges> accountTypeChargesList = new ArrayList<AccountTypeCharges>();			
			if (amountWiseMapData!=null && amountWiseMapData.size() > 0) 
			{
				for (Map.Entry<String,String> entry : amountWiseMapData.entrySet()) 
				{
					String chargeType = entry.getKey();
					String chargeAmount = entry.getValue();
							
					AccountTypeCharges accountTypeCharge = new AccountTypeCharges();
					accountTypeCharge.setStrAccountType(accountType);
					accountTypeCharge.setStrChargeType(chargeType);
					
					String chargeDescription = chargeMasterDao.getChargeDescriptionBasedOnChargeType(chargeType);
					accountTypeCharge.setStrChargeDescription(chargeDescription);
					
					accountTypeCharge.setStrCreatedBy(loginUser);
					accountTypeCharge.setStrParticipantID(participantId);
					accountTypeCharge.setStrAmount(chargeAmount);
					accountTypeCharge.setStrPercentage("0");
					
					accountTypeChargesList.add(accountTypeCharge);
				}
			}
			if (percentageWiseMapData!=null && percentageWiseMapData.size() > 0)
			{
				for (Map.Entry<String,String> entry : percentageWiseMapData.entrySet()) 
				{
					String chargeType = entry.getKey();
					String chargePercentage = entry.getValue();
							
					AccountTypeCharges accountTypeCharge = new AccountTypeCharges();
					accountTypeCharge.setStrAccountType(accountType);
					accountTypeCharge.setStrChargeType(chargeType);
					
					String chargeDescription = chargeMasterDao.getChargeDescriptionBasedOnChargeType(chargeType);
					accountTypeCharge.setStrChargeDescription(chargeDescription);
					
					accountTypeCharge.setStrCreatedBy(loginUser);
					accountTypeCharge.setStrParticipantID(participantId);
					accountTypeCharge.setStrAmount("0");
					accountTypeCharge.setStrPercentage(chargePercentage);
					
					accountTypeChargesList.add(accountTypeCharge);
				}
			}
			
			if (accountTypeChargesList!=null && accountTypeChargesList.size() > 0) 
			{
				int[] resultArr = accountTypeChargesDao.batchEntryforAccountTypeChargesData(accountTypeChargesList);
				if (resultArr.length > 0) 
				{
					accountTypeCharges.setStrId(String.valueOf(resultArr));
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return accountTypeCharges;
	}
	//end of charging module configuration:::added by prashant Tayde
}
