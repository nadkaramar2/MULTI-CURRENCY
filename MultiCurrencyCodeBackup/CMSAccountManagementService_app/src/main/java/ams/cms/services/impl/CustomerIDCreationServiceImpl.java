package ams.cms.services.impl;


import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ams.cms.api.dao.CustomerIdDao;
import ams.cms.config.CommonConstants;
import ams.cms.dao.CustomerIDCreationDao;
import ams.cms.dao.CustomerIdTableDao;
import ams.cms.model.CustomerIdCreation;
import ams.cms.model.CustomerIdTable;
import ams.cms.model.CustomerInfo;
import ams.cms.services.CustomerIDCreationService;
import ams.cms.utility.Utils;

@Transactional
@Service
public class CustomerIDCreationServiceImpl implements CustomerIDCreationService
{
	@Autowired
	CustomerIDCreationDao customerIdCreationDao;
	
	@Autowired
	CustomerIdTableDao customerIdTableDao;
	
	@Autowired
	CustomerIdDao customerIdDao;

	@Override
	public String getCustomerId() throws Exception {
		
		return customerIdCreationDao.getCustomerId();
	}

	@Override
	public CustomerIdCreation saveAccountInformation(CustomerIdCreation customerIdCreation) throws Exception {
		// TODO Auto-generated method stub
		return customerIdCreationDao.getSingleAccountKycDetail(customerIdCreation);
	}

	@Override
	public String getCustId(String year, String julianDate) {
		// TODO Auto-generated method stub
		return customerIdCreationDao.getCustid(year, julianDate) ;
	}

	@Override
	public int updateCustid(CustomerIdCreation customerIdCreation) {
		
		return customerIdCreationDao.updateCustid(customerIdCreation);
	}

	@Override
	public int updateCustIdTable(CustomerIdTable customerIdTable) {
		customerIdTableDao.save(customerIdTable);
		return 0;
	}
	
	@Override
	public int insertCustId(CustomerIdCreation customerIdCreation) {
		customerIdCreationDao.save(customerIdCreation);
		return Integer.parseInt(customerIdCreation.getStrID());
	}

	@Override
	public  List<CustomerIdCreation>  getcustomerdetailsbyId(CustomerIdCreation customerIdCreation) {
	
		return customerIdCreationDao.getcustomerdetailsbyId(customerIdCreation);
	}

	@Override
	public CustomerIdCreation getCustomerIdInfo(CustomerIdCreation customerIdCreation) throws Exception {
		return customerIdCreationDao.getCustomerIdInfo(customerIdCreation);
	}

	@Override
	public CustomerIdCreation getCustomerIdInfoByMobile(String mobileNumber) throws Exception {
		return customerIdCreationDao.getCustomerIdInfoByMobile(mobileNumber);
	}
	
	@Override
	public List<CustomerIdCreation> getCustomerAccountInfo(CustomerIdCreation customerIdCreation) {
		return customerIdCreationDao.getCustomerAccountInfo(customerIdCreation);
	}

	@Override
	public List<CustomerIdCreation> getCustomerAccountDetailsBasedOnCustId(CustomerIdCreation customerIdCreation) {
		return customerIdCreationDao.getCustomerAccountDetailsBasedOnCustId(customerIdCreation);
	}

	@Override
	public int updateCustomerAccountDetails(CustomerIdCreation customerIdCreation) {
		return customerIdCreationDao.updateCustomerAccountDetails(customerIdCreation);
	}

	@Override
	public String getActiveTier(String strMobileNo) {
		return customerIdCreationDao.getActiveTier(strMobileNo);
	}

	@Override
	public CustomerIdCreation getCustomerInformationByCustId(CustomerIdCreation customerIdCreation) {
		return customerIdDao.getCustomerInformationByCustId(customerIdCreation);
	}

	@Override
	public int updateCustomerAccountDetailsBasedOnParameter(CustomerIdCreation customerIdCreation)
	{
		return customerIdCreationDao.updateCustomerAccountDetailsBasedOnParameter(customerIdCreation);
	}
	
	@Override
	public CustomerIdCreation saveCustomerMaster(CustomerIdCreation customerIdCreation) 
	{
		try 
		{
			customerIdCreationDao.save(customerIdCreation);
			return customerIdCreation;
		}
		catch (Exception e) 
		{
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public int updateCustomerMailId(CustomerIdCreation customerIdCreation) {
		return customerIdCreationDao.updateCustomerAccountDetails(customerIdCreation);
	
	}
	@SuppressWarnings("unused")
	@Override
	public CustomerIdCreation mapDataToCustomerIdCreation(CustomerInfo customerInfo, String strCustID) throws Exception 
	{
		CustomerIdCreation customerIdCreation = new CustomerIdCreation();		
		customerIdCreation.setStrCustId(strCustID);

		customerIdCreation.setStrTitle(customerInfo.getTitle());
		customerIdCreation.setStrFirstName(customerInfo.getFirstName());
		customerIdCreation.setStrMiddleName(customerInfo.getMiddleName());
		customerIdCreation.setStrLastName(customerInfo.getLastName());
		customerIdCreation.setStrGender(customerInfo.getGender());
		
		customerIdCreation.setStrCountry(customerInfo.getCountry());		
		customerIdCreation.setStrState(customerInfo.getState());
		customerIdCreation.setStrCity(customerInfo.getCity());
		customerIdCreation.setStrPinCode(customerInfo.getPinCode());
		
		customerIdCreation.setStrAddress1(customerInfo.getAddress1());
		customerIdCreation.setAddress2(customerInfo.getAddress2());
		customerIdCreation.setAddress3(customerInfo.getAddress3());	
		
		customerIdCreation.setStrPhoneCode(customerInfo.getCountry());
		customerIdCreation.setStrMobileNo(customerInfo.getMobileNo().trim());
		
		customerIdCreation.setStrParticipantID(customerInfo.getParticipantId());
		
		if (customerInfo.getBirthDate() != null) 
		{
			SimpleDateFormat dt = new SimpleDateFormat("yyyy-MM-dd");
			Date birthDate = dt.parse(customerInfo.getBirthDate());
			customerIdCreation.setBirthDate(birthDate);
		}
		if (customerInfo.getEmailID() != null && customerInfo.getEmailID().trim().length() > 0) 
		{
			customerIdCreation.setStrEmailID(customerInfo.getEmailID());
		}	
		
		// tier 1 will be set always during registration Start
		if("NIGERIA".equalsIgnoreCase(CommonConstants.applicationName)) 
		{
			customerIdCreation.setStrActiveTier("tier1");
			customerIdCreation.setStrActiveTierDate(Utils.getCurrentDate());
			customerIdCreation.setStrActiveTierTime(Utils.getFormattedCurrentTime());
			customerIdCreation.setStrTier1Date(Utils.getCurrentDate());
			customerIdCreation.setStrTier1Time(Utils.getFormattedCurrentTime());
			customerIdCreation.setStrTier1Status("A");		
		}
		// tier 1 will be set always during registration End
		
		return customerIdCreation;
	}

	@Override
	public int updateCustomerAccountTier(CustomerIdCreation customerIdCreation) 
	{
		try 
		{
			customerIdCreation.setStrActiveTierDate(Utils.getCurrentDate());
			customerIdCreation.setStrActiveTierTime(Utils.getFormattedCurrentTime());			
			
			if ("tier2".equalsIgnoreCase(customerIdCreation.getStrActiveTier())) 
			{
				customerIdCreation.setStrTier2Date(Utils.getCurrentDate());
				customerIdCreation.setStrTier2Time(Utils.getFormattedCurrentTime());
				
				customerIdCreation.setStrTier2Status("A");
			}
			else if ("tier3".equalsIgnoreCase(customerIdCreation.getStrActiveTier())) 
			{
				customerIdCreation.setStrTier3Date(Utils.getCurrentDate());
				customerIdCreation.setStrTier3Time(Utils.getFormattedCurrentTime());
				
				customerIdCreation.setStrTier3Status("A");
			}
			return customerIdCreationDao.updateCustomerAccountDetailsBasedOnParameter(customerIdCreation);
		}
		catch (Exception e) 
		{
			e.printStackTrace();
		}
		return 0;
	}
}
