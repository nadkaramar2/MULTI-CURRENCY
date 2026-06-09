package ams.cms.api.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;

import ams.cms.api.model.AccountMaster;
import ams.cms.api.model.PreAccountMaster;
import ams.cms.api.service.AccountMasterApiService;
import ams.cms.api.service.CustomerIdService;
import ams.cms.api.service.PreAccountMasterService;
import ams.cms.config.AppInfo;
import ams.cms.config.CommonConstants;
import ams.cms.config.EncryptDecryptConfig;
import ams.cms.jwt.JwtAuthenticationFilter;
import ams.cms.logger.AMSLogger;
import ams.cms.model.AccountCreation;
import ams.cms.model.CustomerIdCreation;
import ams.cms.model.UserLogin;
import ams.cms.services.AccountMasterService;
import ams.cms.services.UserLoginService;
import ams.cms.util.ApiSecretKeyUtility;
import ams.cms.util.ProcessResponse;
import ams.cms.utility.ImageUtils;
import ams.cms.utility.PayloadReqRes;
import ams.cms.utility.SignInRequestParam;
import ams.cms.utility.SignInResponseParam;
import ams.cms.utility.UserSessionControl;
import ams.cms.utility.Utils;

@RestController
public class SignInController 
{
	private AMSLogger amsLogger = AMSLogger.getInstance(SignInController.class);
	
	@Autowired
	private AuthenticationManager authenticationManager;
	
	@Autowired
	private AccountMasterService accountMasterService;
	
	@Autowired
	private EncryptDecryptConfig encryptDecryptData;
	
	@Autowired
	private CustomerIdService customerIdService;

	@Autowired
	private AccountMasterApiService accountMasterApiService;
	
	@Autowired
	private PreAccountMasterService preAccountMasterService;
	
	@Autowired
	private UserSessionControl userSessionControl;
	
	@Autowired
	private UserLoginService serviceProvider;
	
	@Autowired
	private ApiSecretKeyUtility apiSecretKeyUtility;
	
	@Autowired
	private	AppInfo appInfo;
	
	//work add profile picture in response
	@RequestMapping(value = "/signIn", method = RequestMethod.POST)
	public ResponseEntity<?> signIn(HttpServletRequest request,@RequestBody PayloadReqRes req) throws Exception
	{
		String userId = "";
		boolean isErrorInResponse = false;		
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
			System.out.println(decrypt);
			SignInRequestParam signInRequestParam = new ObjectMapper().readValue(decrypt, SignInRequestParam.class);
			
			userId = signInRequestParam.getUserId();
			userId = (userId != null) ? userId.trim(): "" ;
			this.authenticationManager.authenticate(
			new UsernamePasswordAuthenticationToken(userId, signInRequestParam.getPassword()));
		}
		catch (UsernameNotFoundException unfe) 
		{
			processResponse.setMessage("Invalid Credentials - Please Check UserId/Mobile No");
			isErrorInResponse = true;
		}
		catch (BadCredentialsException e) 
		{
			processResponse.setMessage("Invalid Credentials - Please Check Password");
			isErrorInResponse = true;
		}
		catch (AuthenticationException e) 
		{
			processResponse.setMessage("Please Registered First. You are not a registered user.");
			isErrorInResponse = true;
		}
		catch (Exception e) 
		{
			processResponse.setMessage("Internal Server Error");			
			isErrorInResponse = true;
		}		
		try
		{
			if (!isErrorInResponse)
			{
				//Creating Session of login in user Start
				PreAccountMaster preAccountMaster = new PreAccountMaster();
				preAccountMaster.setStrMobileNo(userId);
				
				//Active session checking logic by Jyoti S
				UserLogin getLastLogin = serviceProvider.getLastUserLogin(userId);
				if (getLastLogin!=null && getLastLogin.getStatus().equalsIgnoreCase("A"))
				{
					Date currentDate = new Date();
					Date activeDate = getLastLogin.getActiveTime();
					long diff = currentDate.getTime() - activeDate.getTime();
					long diffMinutes = diff / (60 * 1000);
					long propMin = 2 * (60 * 1000);

					amsLogger.writeInfoLog("validateSession - propMin[" + propMin + "] diff[" + diff + "] currentDate[" + currentDate
							+ "] activeDate[" + activeDate + "] diffMinutes[" + diffMinutes + "] confActiveMin[15]");

					if (diff < propMin)
					{
						processResponse.setCode("E0000");
						processResponse.setMessage("USER_SESSION_IS_ACTIVE");
						processResponse.setStatus("Failure");
						return ResponseEntity.ok(encryptDecryptData.encryptPayloadReqRes(req, processResponse));
					} 
					else
					{
						// update session with status Inactive
						PreAccountMaster preAccountMasterTerminate = preAccountMasterService.getPreAccountMasterDataByMobileNumber(userId);
						userSessionControl.terminateActiveSession(preAccountMasterTerminate);
					}
				}
				
				String sessionId = userSessionControl.CreateSessionRecord(preAccountMaster);				
				UpdateLastLoginDateWithSessionIdAndJwtToken(userId, sessionId, JwtAuthenticationFilter.jwtTokenData);
				//Creating Session of login in user End
				
				//changes done by prashant Tayde Start
				CustomerIdCreation customerIdCreation = new CustomerIdCreation();
				customerIdCreation.setStrMobileNo(userId);				

				//String customerId = customerIdService.getCustIdExist(customerIdCreation);
				//if (customerId != null)
				
				//changes added by sagar 
				CustomerIdCreation customerIdCreationObj = customerIdService.getCustIdAgentsActiveTier(customerIdCreation);
				if (customerIdCreationObj !=null && customerIdCreationObj.getStrCustId() !=null)
				{
					String customerId = customerIdCreationObj.getStrCustId();
					processResponse.setCode("S0000");
					processResponse.setStatus("Success");
					processResponse.setCust_id(customerId);
					
					AccountCreation accountCreation = new AccountCreation();
					accountCreation.setStrMobileNo(userId);
					accountCreation.setStrCustId(customerId);
					processResponse.setUserId(userId);
					
		    		//changed the query -update-
					List<AccountCreation> accountCreations = accountMasterService.getSignInDataFromApp(accountCreation);					
					//changed the query -update-
					if (accountCreations != null && accountCreations.size() > 0)
					{
						//added code image url linked in database -add-
						String fileName = accountCreations.get(0).getStrTier1PassportPhotograph();       
						List<SignInResponseParam> signInDatalist = getMappedDataOfSignInApi(accountCreations);
						String generateImageUrl = ImageUtils.generateImageUrl(fileName);
						processResponse.setTier1ProfilePicutre(generateImageUrl);
						System.out.println(generateImageUrl);
						//added code -add-
						
						processResponse.setAccountNoList(signInDatalist);
						processResponse.setAccountHolderName(accountCreations.get(0).getStrAccountHolderName());
						processResponse.setIsAccountLinkedWithCustId("Y");
						processResponse.setMessage("Accounts Information fetched Successfully.");
					}
					else
					{
						processResponse.setIsAccountLinkedWithCustId("N");
						processResponse.setMessage("Please Register for Account.");
					}
					//changes added by sagar START
					if("NIGERIA".equalsIgnoreCase(CommonConstants.applicationName))
					{
						processResponse.setActiveTier(customerIdCreationObj.getStrActiveTier());
					}
					//changes added by sagar END
				}
				else
				{
					processResponse.setCode("E0000");
					processResponse.setStatus("Failed");
					processResponse.setMessage("Cust ID not assigned yet Please try after sometime.");
				}
				//changes done by prashant Tayde End
			}
			else
			{
				processResponse.setCode("E0000");
				processResponse.setStatus("Failed");
			}
		} 
		catch (Exception e)
		{
			processResponse.setCode("E0000");
			processResponse.setStatus("Failed");
		}
		return ResponseEntity.ok(encryptDecryptData.encryptPayloadReqRes(req, processResponse));
	}

	//created by prashant Tayde for fetching account info api Start
	@RequestMapping(value = "/getAccountInfo", method = RequestMethod.POST)
	public ResponseEntity<?> getAccountInfo(HttpServletRequest request,@RequestBody PayloadReqRes req) throws Exception 
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
			PreAccountMaster preAccountMaster = new ObjectMapper().readValue(decrypt, PreAccountMaster.class);
			System.out.println(preAccountMaster);

			CustomerIdCreation customerIdCreation = new CustomerIdCreation();
			customerIdCreation.setStrCustId(preAccountMaster.getCust_id());
			String getAccNoCreated = customerIdService.getIsAccNoCreated(customerIdCreation, preAccountMaster.getAccount_Type());

			if (getAccNoCreated.equalsIgnoreCase("N")) 
			{
				processResponse.setCode("S0000");
				processResponse.setStatus("Success");
				processResponse.setMessage("Please Wait, Your Account Creation is in Under Process!");
			} 
			else
			{
				processResponse.setCode("S0000");
				processResponse.setStatus("Success");
				
				AccountMaster accountMaster = new AccountMaster();
				accountMaster.setStrCustId(preAccountMaster.getCust_id());
				accountMaster.setStrAccountType(preAccountMaster.getAccount_Type());
				AccountMaster acmaster = accountMasterApiService.getCreditFlagOnCustId(accountMaster);

				processResponse.setCust_id(acmaster.getStrCustId());
				processResponse.setAccount_Type(acmaster.getStrAccountType());
				processResponse.setAccountNumber(acmaster.getStrAccountNumber());
				processResponse.setUserId(acmaster.getStrMobileNo());
				processResponse.setCreatedDate(acmaster.getStrDateOfCreation());
				processResponse.setIsCreditTypeAccount(acmaster.getStrIsCreditType());
				processResponse.setMessage("Account Information fetched Successfully.");
			}

		} 
		catch (Exception e) 
		{
			processResponse.setCode("E0000");
			processResponse.setStatus("Failed");
			processResponse.setMessage("Internal Server Error - " + e.getMessage());
		}
		return ResponseEntity.ok(encryptDecryptData.encryptPayloadReqRes(req, processResponse));
	}
	//created by prashant Tayde for fetching account info api 

	@RequestMapping(value = "/getUpdatedAccountsList", method = RequestMethod.POST)
	public ResponseEntity<?> getUpdatedAccountsList(HttpServletRequest request,@RequestBody PayloadReqRes req) throws Exception 
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
			PreAccountMaster preAccountMaster = new ObjectMapper().readValue(decrypt, PreAccountMaster.class);
			
			AccountCreation accountCreation = new AccountCreation();
			accountCreation.setStrCustId(preAccountMaster.getCust_id());
			
			
			List<AccountCreation> accountCreations = accountMasterService.getUpdatedAccountsListForApp(accountCreation);
			if (accountCreations != null && accountCreations.size() > 0)
			{
				List<SignInResponseParam> signInDatalist = getMappedDataOfSignInApi(accountCreations);
				
				processResponse.setCode("S0000");
				processResponse.setStatus("Success");
				processResponse.setAccountNoList(signInDatalist);
				processResponse.setMessage("Accounts Information fetched Successfully.");
				processResponse.setAccountHolderName(accountCreations.get(0).getStrAccountHolderName());
			}
			else
			{
				processResponse.setCode("E0000");
				processResponse.setStatus("Failed");
				processResponse.setMessage("Cust id not Correct for fetching updated account lists.");
			}
		} 
		catch (Exception e) 
		{
			processResponse.setCode("E0000");
			processResponse.setStatus("Failed");
			processResponse.setMessage("Internal Server Error - " + e.getMessage());
		}
		return ResponseEntity.ok(encryptDecryptData.encryptPayloadReqRes(req, processResponse));
	}
	
	
	
	private List<SignInResponseParam> getMappedDataOfSignInApi(List<AccountCreation> accountCreations) 
	{
		List<SignInResponseParam> signInResponseParams = new ArrayList<SignInResponseParam>();
		try 
		{
			for (AccountCreation accountCreation : accountCreations)
			{
				SignInResponseParam signInResponseParam = new SignInResponseParam();
				signInResponseParam.setStrAccountNumber(accountCreation.getStrAccountNumber());
				signInResponseParam.setStrAccountType(accountCreation.getStrAccountType());
				signInResponseParam.setAccountCategoryType(accountCreation.getAccountCategoryType());
				signInResponseParam.setQrCodeImageUrl(accountCreation.getQrCodeImageUrl());
				signInResponseParam.setAvailableBalance(accountCreation.getAvailableBalance());
				signInResponseParam.setIsMultiCurrencySupport(accountCreation.getIsMultiCurrencySupport());	
				signInResponseParam.setDescription(accountCreation.getDescription());
				signInResponseParams.add(signInResponseParam);
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return signInResponseParams;
	}
	
	//private void UpdateLastLoginDate(String loginId)
	private void UpdateLastLoginDateWithSessionIdAndJwtToken(String loginId, String sessionId, String jwtToken)
	{
		try 
		{
			PreAccountMaster preAccountMaster = new PreAccountMaster();
			preAccountMaster.setStrMobileNo(loginId);
			preAccountMaster.setStrLastloginDate(Utils.getCurrentDate());
			preAccountMaster.setStrSessionId(sessionId);
			preAccountMaster.setStrJwtToken(jwtToken);
			preAccountMasterService.updatePreAccountMasterBasedOnParameter(preAccountMaster);
		}
		catch (Exception e) {
		}
	}
}
