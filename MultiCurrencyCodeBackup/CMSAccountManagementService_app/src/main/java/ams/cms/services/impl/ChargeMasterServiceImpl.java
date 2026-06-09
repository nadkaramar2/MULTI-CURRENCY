package ams.cms.services.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ams.cms.dao.ChargeMasterDao;
import ams.cms.model.AccountTypeCharges;
import ams.cms.model.ChargeMaster;
import ams.cms.services.ChargeMasterService;

@Transactional
@Service
public class ChargeMasterServiceImpl implements ChargeMasterService {

	
	//charging module configuration...Added by prashant
	
	
	@Autowired
	ChargeMasterDao chargeMasterDao;

	@Override
	public List<ChargeMaster> getChargeMasterList(ChargeMaster chargeMaster) throws Exception {

		return chargeMasterDao.getChargeMasterList(chargeMaster);
	}

	@Override
	public List<ChargeMaster> getTransactionChargeList(ChargeMaster chargeMaster) throws Exception {

		return chargeMasterDao.getTransactionChargeList(chargeMaster);
	}

	@Override
	public List<ChargeMaster> getFuelChargeList(ChargeMaster chargeMaster) throws Exception {

		return chargeMasterDao.getFuelChargeList(chargeMaster);
	}

	@Override
	public List<AccountTypeCharges> getSelectedChargesAccountTypeWise(AccountTypeCharges accountTypeCharges) {
		return chargeMasterDao.getSelectedChargesAccountTypeWise(accountTypeCharges);
	}

	@Override
	public AccountTypeCharges addChargingConfigData(AccountTypeCharges accountTypeCharges) {
		
		HashMap<String, String> amountWiseMapData = new HashMap<>();
		try {

			String chargeTypeStr = accountTypeCharges.getStrChargeType();
			String participantId = accountTypeCharges.getStrParticipantID();
			String loginUser = accountTypeCharges.getStrCreatedBy();
			String accountType = accountTypeCharges.getStrAccountType();
			String chargeDesc = accountTypeCharges.getStrChargeDescription();
			String amtStr = accountTypeCharges.getStrAmount();

			String[] chargeTypeArr = {};

			/*
			 * //String[] amountArr = {}; if (amtStr != null && amtStr.indexOf(",") != -1) {
			 * //amountArr = amtStr.split(","); }
			 * System.out.println("ChargeMasterServiceImpl.addChargingConfigData()" +
			 * amtStr);
			 */
			

			
			if(amtStr!= null && amtStr.indexOf(",")!=-1)
			{
				String[] amountArr = amtStr.split(",");
				System.out.println("ChargeMasterServiceImpl.addChargingConfigData()" + amountArr);
				for(String amountDataStr : amountArr)
				{
					System.out.println("ChargeMasterServiceImpl.addChargingConfigData()"+amountDataStr);
					if(amountDataStr!= null && amountDataStr.trim().length() ==0) {
						continue;
					}
					String[] amount =  amountDataStr.split("~");
					amountWiseMapData.put(amount[1], amount[0]);
				}
			}
			else {
				if(amtStr.indexOf(",")!=-1)
				{
					String amount = amtStr.substring(0, amtStr.indexOf("~"));
					if(amount!=null && amount.trim().length()>0)
					{
						amountWiseMapData.put(amount.trim(), amount);
					}

				}
			}
			
			if (chargeTypeStr != null && chargeTypeStr.indexOf(",") != -1) {
				chargeTypeArr = chargeTypeStr.split(",");
			}

			System.out.println("ChargeMasterServiceImpl.addChargingConfigData()" + chargeTypeArr);

			if (chargeTypeArr.length > 0) {
				List<AccountTypeCharges> accountTypeChargess = new ArrayList<AccountTypeCharges>();
				for (int i = 0; i < chargeTypeArr.length; i++) {
					String chargeType = chargeTypeArr[i];//CIC

					if (chargeType != null && chargeType.trim().length() > 0) {
						AccountTypeCharges accountTypeChargestt = new AccountTypeCharges();
						accountTypeChargestt.setStrAccountType(accountType);
						accountTypeChargestt.setStrChargeType(chargeType);
						String chargeDescription = chargeMasterDao.getChargeDescriptionBasedOnChargeType(chargeType);
						accountTypeChargestt.setStrChargeDescription(chargeDescription);
						accountTypeChargestt.setStrCreatedBy(loginUser);
						accountTypeChargestt.setStrParticipantID(participantId);
						//amountWiseMapData.get(amtStr);
						if (amountWiseMapData.containsKey(chargeType)) {
							accountTypeChargestt.setStrAmount(amountWiseMapData.get(chargeType));
							amountWiseMapData.remove(chargeType);
						}
						else {
							accountTypeChargestt.setStrAmount("0");
						}
						if(amountWiseMapData.containsKey(chargeType)) {
							accountTypeChargestt.setStrPercentage(amountWiseMapData.get(chargeType));
							amountWiseMapData.remove(chargeType);
						}
						else {
							accountTypeChargestt.setStrPercentage("0");
						}
						
						accountTypeChargess.add(accountTypeChargestt);

					}
				}

				if (accountTypeChargess.size() > 0) {
					int[] resultArr = chargeMasterDao.batchEntryforChargingConfigData(accountTypeChargess);
					if (resultArr.length > 0) {
						accountTypeCharges.setStrId(String.valueOf(resultArr));
					}
				}
			}

			else {
				accountTypeCharges.setStrCreatedDate(new Date());
				// chargeMasterDao.save(accountTypeCharges);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return accountTypeCharges;
	}	
	//end of charging module configuration:::added by prashant Tayde
	
	@Override
	public ChargeMaster addChargeType(ChargeMaster chargeMaster) {
		chargeMasterDao.save(chargeMaster);
		return chargeMaster;
	}

	@Override
	public Boolean validateChargeType(ChargeMaster chargeMaster) throws Exception {
		
		return chargeMasterDao.validateChargeType(chargeMaster);
	}
}
