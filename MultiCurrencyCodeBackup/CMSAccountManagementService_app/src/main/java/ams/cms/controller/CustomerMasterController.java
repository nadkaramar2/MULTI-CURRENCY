package ams.cms.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import ams.cms.config.CustomerIdCreationConfigIF;
import ams.cms.model.AccountCreation;
import ams.cms.model.AccountKycDetails;
import ams.cms.model.CustomerIdCreation;
import ams.cms.model.CustomerIdMap;
import ams.cms.services.AccountKycDetailsService;
import ams.cms.services.AccountMasterService;
import ams.cms.services.AddressProofDocumentTypeMasterService;
import ams.cms.services.CustomerIDCreationService;
import ams.cms.services.IdentityProofDocumentTypeMasterServices;
import ams.cms.utility.Utils;

@RestController
@RequestMapping("/customerId")
public class CustomerMasterController
{
	@Autowired
	private CustomerIDCreationService customerMasterService;
	
	@Autowired
	private CustomerIdCreationConfigIF customerIdCreationConfigIF;
	
	@Autowired
	private AccountKycDetailsService accountKycDetailsService;
	
	@Autowired
	private AccountMasterService accountMasterService;
	
	@RequestMapping(value = "/getCustomerId", method = RequestMethod.GET)
	public ResponseEntity<?> getCustomerId()
	{
		String result = null;
		try 
		{
			CustomerIdMap custIdMapData = customerIdCreationConfigIF.getCustId();
			
			if(custIdMapData != null)
			{
							
				return ResponseEntity.ok(custIdMapData);
			}
			
		}
		catch (Exception e) 
		{
			e.printStackTrace();
		}
		return ResponseEntity.ok(result);
	}
	
	@RequestMapping(value = "/getCustomerInfo", method = RequestMethod.POST)
	public ResponseEntity<?> getCustomerInfo(@RequestBody CustomerIdCreation customerIdCreation)
	{
		String result = null;
		try 
		{
			CustomerIdCreation customerIdCreationObj = customerMasterService.getCustomerIdInfo(customerIdCreation);
			
			if(customerIdCreationObj != null)
			{
				Date birthDate = customerIdCreationObj.getBirthDate();
				if (birthDate!=null) 
				{
					String strDate = Utils.simpleDateFormat.format(birthDate);
					customerIdCreationObj.setStrDOB(strDate);
				}
				if (customerIdCreationObj.getStrTitle()==null) 
				{
					customerIdCreationObj.setStrTitle(" ");
				}
				
				AccountKycDetails accountKycDetails = new AccountKycDetails();
				accountKycDetails.setStrCustId(customerIdCreationObj.getStrCustId());
				accountKycDetails.setStrMobileNo(customerIdCreationObj.getStrMobileNo());
				
				accountKycDetails = accountKycDetailsService.getSingleAccountKycDetail(accountKycDetails);
				if (accountKycDetails!=null && accountKycDetails.getStrID()!=null)
				{
					customerIdCreationObj.setStrAddressProofDocumentId(accountKycDetails.getStrAddressProofDocumentType());
					customerIdCreationObj.setStrAddressProofDocumentValue(accountKycDetails.getStrAddressDocumentValue());
					
					customerIdCreationObj.setStrIdentityProofDocumentId(accountKycDetails.getStrIdentityProofDocumentType());
					customerIdCreationObj.setStrIdentityProofDocumentValue(accountKycDetails.getStrIdentityProofDocumentValue());
					
					customerIdCreationObj.setStrIdeProofImage(accountKycDetails.getStrIdentityProofDocumentName());
					customerIdCreationObj.setStrAddProofImage(accountKycDetails.getStrAddressDocumentName());

					customerIdCreationObj.setStrBvnNumber(accountKycDetails.getStrBvnNumber());
					customerIdCreationObj.setStrTier1PassportPhotograph(accountKycDetails.getStrTier1PassportPhotograph());
					customerIdCreationObj.setStrTier2PassportPhotograph(accountKycDetails.getStrTier2PassportPhotograph());
				}
				
				AccountCreation accountCreation = new AccountCreation();
				accountCreation.setStrCustId(customerIdCreationObj.getStrCustId());
				
				List<AccountCreation> accountCreations = accountMasterService.getAccountInformationList(accountCreation);
				
				if (accountCreations!=null && accountCreations.size() > 0)
				{
					StringBuilder accountTypeBuilder = new StringBuilder();					
					for(AccountCreation accCreation: accountCreations)
					{
						String accountType = accCreation.getStrAccountType();
						if (accountTypeBuilder.length() > 0) 
						{
							accountTypeBuilder.append(",").append(accountType);
						}
						else
						{
							accountTypeBuilder.append(accountType);
						}
					}
					customerIdCreationObj.setStrAccountType(accountTypeBuilder.toString());
				}
				return ResponseEntity.ok(customerIdCreationObj);
			}			
		}
		catch (Exception e) 
		{
			e.printStackTrace();
		}
		return ResponseEntity.ok(result);
	}
	
	@RequestMapping(value = "/insertCustId", method = RequestMethod.POST)
	public ResponseEntity<?> insertCustId(@RequestBody CustomerIdCreation customerIdCreation)
	{
		try 
		{
			 int count = customerIdCreationConfigIF.updateCustId(customerIdCreation);
			 if (count > 0)
			 {
				//Added KYC Details Start
				AccountKycDetails accountKycDetails = new AccountKycDetails();
				accountKycDetails = new AccountKycDetails();
				accountKycDetails.setDateOfSubmission(new Date());
				
				accountKycDetails.setStrParticipantID(customerIdCreation.getStrParticipantID());
				accountKycDetails.setStrCustId(customerIdCreation.getStrCustId());
				
				accountKycDetails.setStrMobileNo(customerIdCreation.getStrMobileNo());
				
				accountKycDetails.setStrAddressProofDocumentType(customerIdCreation.getStrAddressProofDocumentId());
				accountKycDetails.setStrAddressDocumentValue(customerIdCreation.getStrAddressProofDocumentValue());
				accountKycDetails.setStrIdentityProofDocumentType(customerIdCreation.getStrIdentityProofDocumentId());
				accountKycDetails.setStrIdentityProofDocumentValue(customerIdCreation.getStrIdentityProofDocumentValue());
				
				if("YES".equalsIgnoreCase(customerIdCreation.getStrKycUpdateRequired()))
				{
					accountKycDetailsService.updateKycDetail(accountKycDetails);
				}
				else
				{
					accountKycDetailsService.saveAccountKycDetails(accountKycDetails);
				}
				//Added KYC Details End	
			 }
		}
		catch (Exception e) 
		{
			e.printStackTrace();
		}
		return ResponseEntity.ok(customerIdCreation);
	}
	
	@RequestMapping(value = "/getcustomerdetailsbyId", method = RequestMethod.POST)
	public ResponseEntity<?> getcustomerdetailsbyId(@RequestBody CustomerIdCreation customerIdCreation)
	{
		List<CustomerIdCreation> listData = new ArrayList<CustomerIdCreation>();
		try 
		{
			listData = customerIdCreationConfigIF.getcustomerdetailsbyId(customerIdCreation);		
		}
		catch (Exception e) 
		{
			e.printStackTrace();
		}
		return ResponseEntity.ok(listData);
	}
	
	@RequestMapping(value = "/saveCustomerInfo", method = RequestMethod.POST)
	public ResponseEntity<?> processToSaveCustomerInfo(@RequestBody CustomerIdCreation customerIdCreation)
	{
		try {
			System.out.println("Inside saveCustomerInfo..........>>>>>");
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return ResponseEntity.ok("");
	}

	@RequestMapping(value = "/getCustByMobileNumber",method = RequestMethod.POST)
	public CustomerIdCreation processToGetCustDetailsByMobileNo(@RequestBody String mobileNumber)
	{
		try {
			CustomerIdCreation customerIdCreation = customerMasterService.getCustomerIdInfoByMobile(mobileNumber);
			if(!(customerIdCreation.equals(null))) {
				return customerIdCreation;
			}
		} catch(Exception e) {
			
		}
		return null;
		
	}
	
	@RequestMapping(value = "/getCustomerAccountInfo", method = RequestMethod.POST)
	public ResponseEntity<?> getCustomerAccountInfo(@RequestBody CustomerIdCreation customerIdCreation)
	{
		String result = null;
		try 
		{
			List<CustomerIdCreation> accountInfoList = customerMasterService.getCustomerAccountInfo(customerIdCreation);
			return ResponseEntity.ok(accountInfoList);
		}
		catch (Exception e) {
			System.out.println("Exception in getAccountInfoListByParticipantId::"+e);
			e.printStackTrace();
		}
		return ResponseEntity.ok(result);
	}
	
	
	@RequestMapping(value = "/getCustomerAccountDetailsBasedOnCustId", method = RequestMethod.POST)
	public ResponseEntity<?> getCustomerAccountDetailsBasedOnCustId(@RequestBody CustomerIdCreation customerIdCreation)
	{
		String result = null;
		try 
		{
			List<CustomerIdCreation> customerIdCreation2 = customerMasterService.getCustomerAccountDetailsBasedOnCustId(customerIdCreation);
			
			if (customerIdCreation2!=null && customerIdCreation2.size() > 0)
			{
				customerIdCreation = customerIdCreation2.get(0);
				
				Date birthDate = customerIdCreation.getBirthDate();
				if (birthDate!=null) 
				{
					String strDate = Utils.simpleDateFormat.format(birthDate);
					customerIdCreation.setStrDOB(strDate);
				}			
					AccountKycDetails accountKycDetails = new AccountKycDetails();
					accountKycDetails.setStrCustId(customerIdCreation.getStrCustId());
					accountKycDetails = accountKycDetailsService.getSingleAccountKycDetail(accountKycDetails);
			
			if (accountKycDetails!=null)
			{
				customerIdCreation.setStrAddressProofDocumentId(accountKycDetails.getStrAddressProofDocumentType());
				customerIdCreation.setStrAddressProofDocumentValue(accountKycDetails.getStrAddressDocumentValue());
				customerIdCreation.setStrIdentityProofDocumentId(accountKycDetails.getStrIdentityProofDocumentType());
				customerIdCreation.setStrIdentityProofDocumentValue(accountKycDetails.getStrIdentityProofDocumentValue());
			}	
			
			}
			return ResponseEntity.ok(customerIdCreation2);		
		}
		catch (Exception e) 
		{
			System.out.println("CustomerMasterController.getCustomerAccountDetailsBasedOnCustId()"+e);
			e.printStackTrace();
		}
		return ResponseEntity.ok(result);
	}	
	
	
	
	

	@RequestMapping(value = "/updateCustomerAccountDetails", method = RequestMethod.POST)
	public ResponseEntity<?> updateCustomerAccountDetails(@RequestBody CustomerIdCreation customerIdCreation)
	{
		Integer result = null;
		try 
		{
			int count = customerMasterService.updateCustomerAccountDetails(customerIdCreation);
			if(count > 0)
			{
				//Added KYC Details for update
				AccountKycDetails accountKycDetails = new AccountKycDetails();
				accountKycDetails = new AccountKycDetails();
				accountKycDetails.setStrParticipantID(customerIdCreation.getStrParticipantID());
				accountKycDetails.setStrMobileNo(customerIdCreation.getStrMobileNo());
				accountKycDetails.setStrCustId(customerIdCreation.getStrCustId());
								
				accountKycDetails.setStrAddressProofDocumentType(customerIdCreation.getStrAddressProofDocumentId());
				accountKycDetails.setStrAddressDocumentValue(customerIdCreation.getStrAddressProofDocumentValue());
				accountKycDetails.setStrIdentityProofDocumentType(customerIdCreation.getStrIdentityProofDocumentId());
				accountKycDetails.setStrIdentityProofDocumentValue(customerIdCreation.getStrIdentityProofDocumentValue());
			
				int cnt = accountKycDetailsService.updateKycDetailsInfoCustomerDetails(accountKycDetails);
				if (cnt > 0) 
				{
					return ResponseEntity.ok(cnt +"");
				}
				//Added KYC Details for update
			
			}
			
		}
		catch (Exception e) {
			System.out.println("CustomerMasterController.updateCustomerAccountDetails()"+e);
			e.printStackTrace();
		}
		return ResponseEntity.ok(result);
	}
	
	@RequestMapping(value = "/getActiveTier", method = RequestMethod.POST)
	public ResponseEntity<?> getActiveTier(@RequestBody String strMobileNo)
	{
		String result = null;
		try 
		{
			result = customerMasterService.getActiveTier(strMobileNo);
			if (result != null) 
			{
				return ResponseEntity.ok(result);
			}
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
			result = "Exception while save AccountTypes:"+e.getMessage();
		}
		return ResponseEntity.ok(result);
	}
}
