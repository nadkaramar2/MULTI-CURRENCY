package ams.cms.api.service.impl;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import ams.cms.api.controller.SignUpController;
import ams.cms.api.dao.NubanCodeConfigDao;
import ams.cms.api.model.NUBANAccountDto;
import ams.cms.api.model.NubanCodeConfig;
import ams.cms.api.service.NubanCodeConfigService;
import ams.cms.config.AppInfo;
import ams.cms.dao.AccountTypeMasterDao;
import ams.cms.logger.AMSLogger;
import ams.cms.model.AccountTypeMaster;
import ams.cms.services.AccountTypeMasterService;
import ams.cms.util.ApiSecretKeyUtility;
import ams.cms.util.ProcessResponse;

//created by ankit on 26-05-2023
@Transactional
@Service
public class NubanCodeConfigServiceImpl implements NubanCodeConfigService
{
	private AMSLogger amsLogger = AMSLogger.getInstance(NubanCodeConfigServiceImpl.class);
	
	@Autowired
	private NubanCodeConfigDao nubanCodeConfigDao;
	
	@Autowired
	private AccountTypeMasterService  accountTypeMasterService;
	
	@Autowired
	private	AccountTypeMasterDao accountTypeMasterDao;
	
	@Autowired
	private AppInfo appInfo;
	
	@Autowired
	private ApiSecretKeyUtility apiSecretKeyUtility;

	@Override
	public int addNubanCodeConfig(NubanCodeConfig nubanCodeConfig) 
	{
		

		String participantId = appInfo.getStrParticipantId();
		
		/*I have not checked whether the same participant have made nuban request multiple times*/
		boolean nubanCodeExists = nubanCodeConfigDao.isNubanCodeExists(nubanCodeConfig);
		if(nubanCodeExists) {
			return 0;
		}else {
			String firstNubanSerialNumber = "0000001";
			nubanCodeConfig.setStrNubanSerialNo(firstNubanSerialNumber);
		}
		nubanCodeConfig.setStrCreatedDate(new Date());
		String strParticipantId = nubanCodeConfig.getStrParticipantId();
		if(strParticipantId == null) {
		nubanCodeConfig.setStrParticipantId(participantId);
		}
		Serializable save = nubanCodeConfigDao.save(nubanCodeConfig);
		amsLogger.writeInfoLog(save);
		return 1;
	}
	
	
	//this might throw exception
	private String nubanSerialNumberGenerator(NubanCodeConfig nubanCodeConfig) 
	{
		List<NubanCodeConfig> findSerialNoByUser = nubanCodeConfigDao.findSerialNoByUser(nubanCodeConfig);
		String strNubanSerialNo = findSerialNoByUser.get(0).getStrNubanSerialNo();
		int nubanSerialNoInt = Integer.parseInt(strNubanSerialNo);
		Integer newNubanSerialNo = nubanSerialNoInt + 1;
		return String.valueOf(newNubanSerialNo);
	}


	@Override
	public void addNewNubanCodeConfig(NubanCodeConfig nubanCodeConfig) {
		nubanCodeConfigDao.addNewNubanCodeConfig(nubanCodeConfig);
	}


	@Override
	public List<NubanCodeConfig> getNubanCodeConfig(NubanCodeConfig nubanCodeConfig) {
		return nubanCodeConfigDao.getNubanCodeConfig(nubanCodeConfig);
	}

	
	//added on 28-05-2023
	@Override
	public  int updateNubanSerialNo(NubanCodeConfig nubanCodeConfig) {
		NubanCodeConfig nubanCodeConfigObjectByNubanCode = nubanCodeConfigDao.getNubanCodeConfigObjectByNubanCode(nubanCodeConfig);
		String strNubanSerialNo = nubanCodeConfigObjectByNubanCode.getStrNubanSerialNo();
		try{
			int nubanSerialNo = Integer.parseInt(strNubanSerialNo);
			int newStrNubanSerialNo = nubanSerialNo + 1;
			strNubanSerialNo = String.valueOf(newStrNubanSerialNo);
			nubanCodeConfig.setStrNubanSerialNo(strNubanSerialNo);
			int updateNubanSerialNoByNubanCode = nubanCodeConfigDao.updateNubanSerialNoByNubanCode(nubanCodeConfig);
			if(updateNubanSerialNoByNubanCode> 0) {
				return updateNubanSerialNoByNubanCode;
			}
			else {
				return 0;
			}
		}catch(Exception e) {
			e.printStackTrace();
		}	
		return 0;
	}
	//added on 28-05-2023 -end
	public String generateAccountNo(AccountTypeMaster accountTypeMaster, NubanCodeConfig nubanCodeConfig) 
	{
		String strAccountTypeCode = accountTypeMaster.getStrAccountTypeCode();
		amsLogger.writeInfoLog("Inside generateAccountNo strAccountTypeCode::"+strAccountTypeCode);
		
		//update the serial No here -added these two values-
		String generatedNubanSerialNo = generateNewNubanSerialNoFromAccountType(accountTypeMaster);
		amsLogger.writeInfoLog("Inside generateAccountNo generatedNubanSerialNo::["+generatedNubanSerialNo+"]");
		
		accountTypeMaster.setStrNubanSerialNumber(generatedNubanSerialNo);
		accountTypeMasterService.updateNubanSerialNumberByAccountType(accountTypeMaster);
		//update the serial No here -added these two values-
		
		String strNubanCode = nubanCodeConfig.getStrNubanCode();
		amsLogger.writeInfoLog("Inside generateAccountNo strNubanCode::["+strNubanCode+"]");
		String uncheckedNuban = strNubanCode + strAccountTypeCode + generatedNubanSerialNo;
		
		amsLogger.writeInfoLog("Inside generateAccountNo uncheckedNuban::["+uncheckedNuban+"]");
		
		String generatedNUBANCheckedDigit = generateNUBANCheckedDigit(uncheckedNuban);
		String accountNo = uncheckedNuban + generatedNUBANCheckedDigit;
		return accountNo;
	}
	
	
	//added on 01-07-2023 generate nuban Serial number from the account type
	private String generateNewNubanSerialNoFromAccountType(AccountTypeMaster accountTypeMaster)
	{
		String strNubanSerialNo = accountTypeMaster.getStrNubanSerialNumber();
		
		int IntNubanSerianNo = Integer.parseInt(strNubanSerialNo);
		int newNubanSerialNo = IntNubanSerianNo + 1;
		
		String formattedNubanSerialNo = String.format("%07d", newNubanSerialNo);
		return String.valueOf(formattedNubanSerialNo);
	}
	//added on 01-07-2023 generate nuban Serial number from the account type

	//formatted the digits till 7 as it was coming out to be only single digit
	public String generateNewNubanSerialNo(NubanCodeConfig nubanCodeConfig) {
		String strNubanSerialNo = nubanCodeConfig.getStrNubanSerialNo();
		int IntNubanSerianNo = Integer.parseInt(strNubanSerialNo);
		int newNubanSerialNo = IntNubanSerianNo + 1;
		String formattedNubanSerialNo = String.format("%07d", newNubanSerialNo);
		return String.valueOf(formattedNubanSerialNo);
	}

	//to get the last digit
	private String generateNUBANCheckedDigit(String uncheckedNuban) 
	{
		amsLogger.writeInfoLog("Inside generateNUBANCheckedDigit uncheckedNuban::["+uncheckedNuban+"]");
		// first 15 digits multiplier for generating the remainder
	    int[] weights = {3, 7, 3, 3, 7, 3, 3, 7, 3, 3, 7 , 3 , 3, 7, 3};
	    int sum = 0;
	    int individualSum = 0;
	    for (int i = 0; i <= 14; i++) {
	        int digit = Character.getNumericValue(uncheckedNuban.charAt(i));
	        individualSum = digit * weights[i];
	        amsLogger.writeInfoLog("Individual Sum of Row " +i+ " is "+individualSum);
	        sum = sum + digit * weights[i];
	    }

	    amsLogger.writeInfoLog("Total Sum is "+sum);
	    //calculating modulo 10 of the sum
	    int remainder = sum % 10;
	    amsLogger.writeInfoLog("Remainder is "+remainder);
	    //check Digit is either zero or remainder
	    int checkDigit = (remainder == 0) ? 0 : (10 - remainder);
	    amsLogger.writeInfoLog("Generated Checked Digit "+checkDigit);
		return String.valueOf(checkDigit);
	}


	@Override
	public NUBANAccountDto getAccountNo(NUBANAccountDto nUBANAccountDto)
	{
		amsLogger.writeInfoLog("Inside getAccountNo----->>>");
		
		AccountTypeMaster accountTypeMaster = new AccountTypeMaster();
		accountTypeMaster.setStrAccountType(nUBANAccountDto.getStrAccountType());
		
		AccountTypeMaster accountTypeCode = accountTypeMasterDao.getAccountTypeObject(accountTypeMaster);
		amsLogger.writeInfoLog("Inside getAccountNo accountTypeCode::"+accountTypeCode);
		
		//added  by ankit
		NubanCodeConfig nubanCodeConfig = new NubanCodeConfig();		
		nubanCodeConfig.setStrNubanType(nUBANAccountDto.getStrNubanType());
		
		String strParticipantId = nUBANAccountDto.getStrParticipantId();
		
		if(strParticipantId == null) {
			nubanCodeConfig.setStrParticipantId(appInfo.getStrParticipantId());
		}
		NubanCodeConfig nubanCodeConfig2 = nubanCodeConfigDao.getNubanCodeConfigObject(nubanCodeConfig);
		amsLogger.writeInfoLog("Inside getAccountNo nubanCodeConfig2::"+nubanCodeConfig2);
		
		String generateAccountNo = generateAccountNo(accountTypeCode, nubanCodeConfig2);
		
		NUBANAccountDto newNUBANAccountDto = new NUBANAccountDto();
		newNUBANAccountDto.setStrAccountNo(generateAccountNo);
		return newNUBANAccountDto;
	}


	@Override
	public boolean isConfigCodeExists(NubanCodeConfig nubanCodeConfig) {
		return nubanCodeConfigDao.isNubanCodeExists(nubanCodeConfig);
	}


	@Override
	public NubanCodeConfig getNubanConfigObjectBasedOnParameter(NubanCodeConfig nubanCodeConfig) 
	{
		return nubanCodeConfigDao.getNubanConfigObjectBasedOnParameter(nubanCodeConfig);
	}
	
}
