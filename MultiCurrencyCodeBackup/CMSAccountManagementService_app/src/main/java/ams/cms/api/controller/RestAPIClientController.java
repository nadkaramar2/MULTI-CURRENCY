package ams.cms.api.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.fasterxml.jackson.databind.ObjectMapper;

import ams.cms.api.model.CardDetailsRequest;
import ams.cms.api.service.CustomerIdService;
import ams.cms.api.service.RestAPIClientService;
import ams.cms.config.AppInfo;
import ams.cms.config.EncryptDecryptConfig;
import ams.cms.jwt.JwtUtil;
import ams.cms.logger.AMSLogger;
import ams.cms.model.AccountCreation;
import ams.cms.model.CustomerIdCreation;
import ams.cms.notification.EmailTemplate;
import ams.cms.notification.email.EmailService;
import ams.cms.services.AccountMasterService;
import ams.cms.services.CardAccountLinkageService;
import ams.cms.util.ApiSecretKeyUtility;
import ams.cms.util.ProcessResponse;
import ams.cms.utility.PayloadReqRes;

@Controller
@RequestMapping(path = "/rest",   consumes = MediaType.APPLICATION_JSON_VALUE,  produces = MediaType.APPLICATION_JSON_VALUE, 	method = {RequestMethod.GET, RequestMethod.POST})
public class RestAPIClientController 
{
	private AMSLogger amsLogger = AMSLogger.getInstance(RestAPIClientController.class);
	
	@Autowired
	private RestAPIClientService restAPIClientService;
	
	@Autowired
	EncryptDecryptConfig encryptDecryptData;
	
	@Autowired
	private CustomerIdService customerIdService;
	
	@Autowired
	private ApiSecretKeyUtility apiSecretKeyUtility;
	
	@Autowired
	private	AppInfo appInfo;
	
	//Simple Get Method to Test the API Call -THis cAN bE REMOVED
	/*
	@RequestMapping(value = "/test", method = RequestMethod.GET)
	public ResponseEntity<?> getCardNo(){
		String test = restAPIClientService.test();
		return new ResponseEntity<>(test,HttpStatus.OK);
	}
	*/
	
	@SuppressWarnings("null")
	@RequestMapping(value = "/getClearCardNo", method = RequestMethod.POST)
	public ResponseEntity<?> getCardNo(HttpServletRequest request,@RequestBody PayloadReqRes req)
	{
		ProcessResponse processResponse = new ProcessResponse();
		try 
		{ 
			ProcessResponse processResps = apiSecretKeyUtility.validateApiKey(request,processResponse); 
			amsLogger.writeInfoLog("Inside processToGetBasicInformation processResp2::"+processResps); 

			if ("E0000".equalsIgnoreCase(processResps.getCode())) 
			{
				return ResponseEntity.ok(encryptDecryptData.encryptPayloadReqRes(req,processResps)); 
			}
			
			String participantId = appInfo.getStrParticipantId();
			
			String decrypt = encryptDecryptData.decryptPayloadReqRes(req);
			CardDetailsRequest cardDetailsRequest = new ObjectMapper().readValue(decrypt, CardDetailsRequest.class);
			
			String custId =	cardDetailsRequest.getStrCustId();			
			CustomerIdCreation customerIdCreation = new CustomerIdCreation();
			customerIdCreation.setStrCustId(custId);
			String existingPIN = customerIdService.getCustomerPIN(customerIdCreation);			
			if (existingPIN != null)
			{
				if (existingPIN.equals(ams.cms.utility.Utils.generateHash(cardDetailsRequest.getStrPIN()))) 
				{
					ResponseEntity<?> cardNoFromClient = restAPIClientService.getCardNoFromClient(cardDetailsRequest);		
					CardDetailsRequest body = (CardDetailsRequest) cardNoFromClient.getBody();
					String strClearCardNo = body.getStrCardNo();			
					if(strClearCardNo == null) 
					{
						processResponse.setCode("E0000");
						processResponse.setStatus("Failed");
						processResponse.setMessage("Data Not Found.");
					}
					else
					{
						processResponse.setCode("S0000");
						processResponse.setStatus("Success");
						processResponse.setStrClearCardNo(strClearCardNo);
						processResponse.setMessage("Data Fetched Successfully.");
					}
				}
				else 
				{
					processResponse.setCode("E0000");
					processResponse.setStatus("Failed");
					processResponse.setMessage("Entered PIN is Incorrect.");
				}
			}
			else 
			{
				processResponse.setCode("E0000");
				processResponse.setStatus("Failed");
				processResponse.setMessage("PIN Not Found for this account.");
			}
		}
		catch(Exception e) 
		{
			processResponse.setCode("E0000");
			processResponse.setStatus("Failed");
			processResponse.setMessage("Internal Server Error ");
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		}
		return ResponseEntity.ok(encryptDecryptData.encryptPayloadReqRes(req, processResponse));
	}
	
	/*Commented Ankit New Request Card API Changes Start
	//added by ankit on 29-4-2023
		@RequestMapping(value = "/getCardTypes", method = RequestMethod.GET)
		public ResponseEntity<?> getCardTypes()
		//public ResponseEntity<?> getCardTypes()
		{
		    	PayloadReqRes req = new PayloadReqRes();
			ProcessResponse processResponse = new ProcessResponse();
			
			try 
			{    
			    		String salt = jwtUtil.getRandomSalt();
			    		req.setSalt(salt);
					ResponseEntity<?> cardTypes2 = restAPIClientService.getCardTypes();
					
					List<CardTypeResponse> cardTypes = (List<CardTypeResponse>) cardTypes2.getBody();
					if(cardTypes.size() > 0) {
						processResponse.setCode("S0000");
						processResponse.setStatus("Success");
						processResponse.setMessage("All Card Types");
						processResponse.setCardTypeResponse(cardTypes);
					}else {
					processResponse.setCode("E0000");
					processResponse.setStatus("Failed");
					processResponse.setMessage("Card Types Not Found");
					}
			}
			catch(Exception e) 
			{
				processResponse.setCode("E0000");
				processResponse.setStatus("Failed");
				processResponse.setMessage("Internal Server Error"+ e.getMessage());
				e.printStackTrace();
			}
			
			//return new ResponseEntity<>(processResponse,HttpStatus.OK);
			return ResponseEntity.ok(encryptDecryptData.encryptPayloadReqRes(req, processResponse));
		}
		//added by ankit on 29-4-2023
		
		//added by ankit on 29-4-2023
		@RequestMapping(value = "/issueCard", method = RequestMethod.POST)
		 public ResponseEntity<?> cardIssueReqestByCustomer(@RequestBody PayloadReqRes req)
		//public ResponseEntity<?> cardIssueReqestByCustomer(@RequestBody CardIssueRequest cardIssueRequest)
		{
		    ProcessResponse processResponse = new ProcessResponse();
		    try
		    {

			String decrypt = encryptDecryptData.decryptPayloadReqRes(req);
			CardIssueRequest cardIssueRequest = new ObjectMapper().readValue(decrypt, CardIssueRequest.class);

			String custId = cardIssueRequest.getStrCustId();
			CustomerIdCreation customerIdCreation = new CustomerIdCreation();
			customerIdCreation.setStrCustId(custId);
			String existingPIN = customerIdService.getCustomerPIN(customerIdCreation);

			if (existingPIN != null)
			{
			    if (existingPIN.equals(ams.cms.utility.Utils.generateHash(cardIssueRequest.getStrPIN())))
			    {
				String strAccountType = cardIssueRequest.getStrAccountType();
				String strAccountNumber = cardIssueRequest.getStrAccountNumber();
				AccountCreation toAccountCreation = new AccountCreation();
				toAccountCreation.setStrAccountNumber(strAccountNumber);
				toAccountCreation.setStrAccountType(strAccountType);
				
				List<AccountCreation> accountInfoAndStatus = accountMasterService.getAccountInfoForCardCreation(toAccountCreation);
				if (accountInfoAndStatus != null && accountInfoAndStatus.size() > 0)
				{
				    if (accountInfoAndStatus.size() > 0 && accountInfoAndStatus.get(0).getStrStatus() != null
					    && accountInfoAndStatus.get(0).getStrStatus().equals("Active"))
				    {

					CardAccountLinkage cardAccountLinkage = new CardAccountLinkage();
					cardAccountLinkage.setStrAccountNumber(strAccountNumber);
					cardAccountLinkage.setStrAccountType(strAccountType);
					List<CardAccountLinkage> cardAccountLinkageBasedOnAccount = cardAccountLinkageService
						.getCardAccountLinkageBasedOnAccount(cardAccountLinkage);
					if (cardAccountLinkageBasedOnAccount.size() > 0)
					{
					    boolean checkIfCardExists = checkIfCardExists(
						    cardIssueRequest, cardAccountLinkageBasedOnAccount
					    );
					    if (checkIfCardExists == false)
					    {
						
						// Response From the CardManagementAPI will get Received first
						ResponseEntity<?> requestToGetTheCardNumber = restAPIClientService
							.requestToGetTheCardNumber(cardIssueRequest, accountInfoAndStatus.get(0));
						DebitCardIssuanceResponse body = (DebitCardIssuanceResponse) requestToGetTheCardNumber.getBody();
						CardDetails cardDetails = null;
						
						if(body.getOutResponseCode().equals("00")) {
						    cardDetails = body.getCardDetails();
						    CardAccountLinkage mapCardAccountLinkageData = mapCardAccountLinkageData(
								cardIssueRequest, cardDetails, accountInfoAndStatus.get(0)
							);
							cardAccountLinkageService.addCardAccountLinkageData(mapCardAccountLinkageData);
							sendCardRequestEmail(accountInfoAndStatus.get(0));
							processResponse.setCode("S000");
							processResponse.setStatus("Sucess");
							processResponse.setMessage("Your Request For Card is Sucessfully Submitted");
						}else {
						    	processResponse.setCode("E000");
							processResponse.setStatus("Error");
							processResponse.setMessage(body.getMessage());
						}
					    }
					    else
					    {
						processResponse.setCode("E0000");
						processResponse.setStatus("Failed");
						processResponse.setMessage("CardType Alredy Exists");
					    }
					}
					else
					{
					    ResponseEntity<?> requestToGetTheCardNumber = restAPIClientService
							.requestToGetTheCardNumber(cardIssueRequest, accountInfoAndStatus.get(0));
						DebitCardIssuanceResponse body = (DebitCardIssuanceResponse) requestToGetTheCardNumber.getBody();
						CardDetails cardDetails = null;
						
						if(body.getOutResponseCode().equals("00")) {
						    cardDetails = body.getCardDetails();
						    CardAccountLinkage mapCardAccountLinkageData = mapCardAccountLinkageData(
								cardIssueRequest, cardDetails, accountInfoAndStatus.get(0)
							);
							cardAccountLinkageService.addCardAccountLinkageData(mapCardAccountLinkageData);
							sendCardRequestEmail(accountInfoAndStatus.get(0));
							processResponse.setCode("S000");
							processResponse.setStatus("Sucess");
							processResponse.setMessage("Your Request For Card is Sucessfully Submitted");
						}else {
						    	processResponse.setCode("E000");
							processResponse.setStatus("Error");
							processResponse.setMessage(body.getMessage());
						}
						}
				    }
				    else
				    {
					processResponse.setCode("E0000");
					processResponse.setStatus("Failed");
					processResponse.setMessage("Account is Inactive");
				    }
				}
				else
				{
				    processResponse.setCode("E0000");
				    processResponse.setStatus("Failed");
				    processResponse.setMessage("Account Number Not Found");
				}

			    }
			    else
			    {
				processResponse.setCode("E0000");
				processResponse.setStatus("Failed");
				processResponse.setMessage("Entered PIN is Incorrect.");
			    }
			}
			else
			{
			    processResponse.setCode("E0000");
			    processResponse.setStatus("Failed");
			    processResponse.setMessage("Please Provide Pin");
			}
		    } catch (Exception e)
		    {
			processResponse.setCode("E0000");
			processResponse.setStatus("Failed");
			processResponse.setMessage("Internal Server Error ");
			e.printStackTrace();
		    }

		     return ResponseEntity.ok(encryptDecryptData.encryptPayloadReqRes(req,
		     processResponse));
		}
		//added by ankit on 29-4-2023
		
		//add this -start- 02-05-2023
		private CardAccountLinkage mapCardAccountLinkageData(CardIssueRequest cardIssueRequest,CardDetails cardDetails, AccountCreation accountCreation) 
		{
		    
		    String strFirstName = accountCreation.getStrFirstName();
		    String strMiddleName = accountCreation.getStrMiddleName();
		    String strLastName = accountCreation.getStrLastName();
		    String strAccountStatus = accountCreation.getStrStatus();
		    
		    
		    String strCardHolderName =  strFirstName+" "+ strMiddleName +" "+ strLastName; 
		    String strAccountNumber = cardIssueRequest.getStrAccountNumber();
		    String strAccountType = cardIssueRequest.getStrAccountType();
		    String strCardType = cardIssueRequest.getStrCardType();
		    String strCardDescription = cardIssueRequest.getStrCardDescription();
		    
		    String strCardStatus = cardDetails.getStrCardStatus();
		    String strCardNumber = cardDetails.getStrCardNumber();
		    String strEncryptedCard = cardDetails.getStrEncryptedCard();
		    String strTokenCard = cardDetails.getStrTokenCard();
		    
		    
		    CardAccountLinkage cardAccountLinkage = new CardAccountLinkage();
		    
		    cardAccountLinkage.setStrCardNumber(strCardNumber);
		    cardAccountLinkage.setStrCardEncCard(strEncryptedCard);
		    cardAccountLinkage.setStrTokenCard(strTokenCard);
		    cardAccountLinkage.setStrCardStatus(strCardStatus);
		    cardAccountLinkage.setCreationDate(new Date());
		    cardAccountLinkage.setStrAccountNumber(strAccountNumber);
		    cardAccountLinkage.setStrAccountType(strAccountType);
		    cardAccountLinkage.setStrCardType(strCardType);
		    cardAccountLinkage.setStrCardHolderName(strCardHolderName);
		    cardAccountLinkage.setStrAccountStatus(strAccountStatus);
		    cardAccountLinkage.setStrCardDescription(strCardDescription);
		    return cardAccountLinkage;
		}
		//add this -end- 02-05-2023
		
		//add this -start- 02-05-2023
		private boolean checkIfCardExists(CardIssueRequest cardIssueRequest, List<CardAccountLinkage> cardAccountLinkage) {
			try {
				int counter = 0;
				String strRequestCardType = cardIssueRequest.getStrCardType();
				
				for(int i = 0; i < cardAccountLinkage.size(); i++) {
					String strCardType = cardAccountLinkage.get(i).getStrCardType();
					if(strCardType.equals(strRequestCardType)) {
						counter++;
					}
				}
				if(counter > 0) {
					return true;
				}else
				{
					return false;
				}

			}catch(Exception e) {
				e.printStackTrace();
			}
				return false;
			}
			//add this -end- 02-05-2023
		
			//add this -start- 02-05-2023
			private void sendCardRequestEmail(AccountCreation accountCreation)
			{

			    try
			    {
				String strCustId = accountCreation.getStrCustId();
				CustomerIdCreation customerIdCreation = new CustomerIdCreation();
				customerIdCreation.setStrCustId(strCustId);

				List<CustomerIdCreation> emailOfCustomer = customerIdService
					.getEmailOfCustomer(customerIdCreation);

				if (emailOfCustomer.size() > 0)
				{
				    String strEmailID = emailOfCustomer.get(0).getStrEmailID();
				    // added not in strEmailId or condition
				    if (strEmailID != null || !strEmailID.isEmpty())
				    {
					EmailTemplate emailTemplate = new EmailTemplate();
					String stringSenderEmail = "contactus@AMStechnologies.com";
					emailTemplate.setStrFrom(stringSenderEmail);
					emailTemplate.setStrTo(strEmailID);
					emailTemplate.setStrSubject("Request For Card");
					emailTemplate.setStrText(
						"Dear " + accountCreation.getStrAccountHolderName() + "\n"
							+ "Thankyou For Your Request" + "\n"
							+ "The Card Will be Assigned To Your Account Number "
							+ accountCreation.getStrAccountNumber()
							+ " And You Will Be Notified Accourdingly " + "."
					);
					emailService.sendSimpleMessage(emailTemplate);
				    }
				}
			    } catch (Exception e)
			    {
				e.printStackTrace();
			    }
			}
			//add this -end- 02-05-2023 

			//do not add this just for testing ' required' remove this 
			@RequestMapping(value = "/issueCardDummyRequest", method = RequestMethod.POST)
			// public ResponseEntity<?> internalAPICallToGetTheCardNumber() {
			public ResponseEntity<?> internalAPICallToGetTheCardNumber(CardIssueRequest cardIssueRequest, AccountCreation accountCreation
			)
			{
			    ResponseEntity<?> requestToGetTheCardNumber = restAPIClientService
				    .requestToGetTheCardNumber(cardIssueRequest, accountCreation);
			    return requestToGetTheCardNumber;
			}
			//do not add this just for testing ' required' remove this
			 * Commented Ankit New Request Card API Changes Start
			 */
}
