package ams.cms.api.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;

import ams.cms.api.model.CityModel;
import ams.cms.api.model.City_MasterDto;
import ams.cms.api.model.Country_MasterDto;
import ams.cms.api.model.PreAccountMaster;
import ams.cms.api.model.StateModel;
import ams.cms.api.model.State_MasterDto;
import ams.cms.api.service.CountryCodeMasterService;
import ams.cms.api.service.PreAccountMasterService;
import ams.cms.config.AppInfo;
import ams.cms.config.EncryptDecryptConfig;
import ams.cms.jwt.JwtUtil;
import ams.cms.logger.AMSLogger;
import ams.cms.util.ApiSecretKeyUtility;
import ams.cms.util.ProcessResponse;
import ams.cms.utility.PayloadReqRes;

@RestController
@RequestMapping("/address")
public class AddressApiController 
{
	@Autowired
	CountryCodeMasterService countryCodeMasterService;
	
	@Autowired 
	JwtUtil jwtUtil;
	
	@Autowired
	EncryptDecryptConfig encryptDecryptData;
	
	@Autowired
	PreAccountMasterService preAccountMasterService;
	
	@Autowired
	private ApiSecretKeyUtility apiSecretKeyUtility;
	
	@Autowired
	private	AppInfo appInfo;
	
	private AMSLogger amsLogger = AMSLogger.getInstance(SignUpController.class);
	
	@RequestMapping(path = "/getCountry", method = RequestMethod.POST)
	public ResponseEntity<?> getCountrylist(HttpServletRequest request,@RequestBody PayloadReqRes req) 
	{
		ProcessResponse processResponse = new ProcessResponse();
		//PayloadReqRes req = new PayloadReqRes();
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
			Country_MasterDto countryMaster = new ObjectMapper().readValue(decrypt, Country_MasterDto.class);
			
			List<Country_MasterDto> countryList = countryCodeMasterService.getCountrylist(countryMaster);

			if (countryList != null && countryList.size() > 0) 
			{
				processResponse.setCountryList(countryList);;
				processResponse.setCode("S0000");
				processResponse.setStatus("Success");
				processResponse.setMessage("Country Code fetched successfully");
			} 
			else 
			{
				processResponse.setCode("E0000");
				processResponse.setStatus("Failure");
				processResponse.setMessage("Exception while fetching the data");
			}

		} 
		catch (Exception e) 
		{
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
			processResponse.setCode("E0000");
			processResponse.setStatus("Failed");
			processResponse.setMessage("Internal Server Error - " + e.getMessage());
		}
		// return ResponseEntity.ok("{}");
		return ResponseEntity.ok(encryptDecryptData.encryptPayloadReqRes(req, processResponse));
	}
	
	@RequestMapping(path = "/getState", method = RequestMethod.POST)
	public ResponseEntity<?> getStatelist(HttpServletRequest request,@RequestBody PayloadReqRes req) 
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
			StateModel stateMaster = new ObjectMapper().readValue(decrypt, StateModel.class);
			List<State_MasterDto> stateList= countryCodeMasterService.getStatelist(stateMaster);

			if (stateList != null && stateList.size() > 0) 
			{
				processResponse.setStateList(stateList);
				processResponse.setCode("S0000");
				processResponse.setStatus("Success");
				processResponse.setMessage("Country Code fetched successfully");
			} 
			else 
			{
				processResponse.setCode("E0000");
				processResponse.setStatus("Failure");
				processResponse.setMessage("Exception while fetching the data");
			}

		} 
		catch (Exception e) 
		{
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
			processResponse.setCode("E0000");
			processResponse.setStatus("Failed");
			processResponse.setMessage("Internal Server Error - " + e.getMessage());
		}
		// return ResponseEntity.ok("{}");
		return ResponseEntity.ok(encryptDecryptData.encryptPayloadReqRes(req, processResponse));
	}
	
	@RequestMapping(path = "/getCity", method = RequestMethod.POST)
	public ResponseEntity<?> getCitylist(HttpServletRequest request,@RequestBody PayloadReqRes req) 
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
			CityModel cityMaster = new ObjectMapper().readValue(decrypt, CityModel.class);
			List<City_MasterDto> cityList = countryCodeMasterService.getCitylist(cityMaster);

			if (cityList != null && cityList.size() > 0) 
			{
				processResponse.setCityList(cityList);
				processResponse.setCode("S0000");
				processResponse.setStatus("Success");
				processResponse.setMessage("Country Code fetched successfully");
			} 
			else 
			{
				processResponse.setCode("E0000");
				processResponse.setStatus("Failure");
				processResponse.setMessage("Data");
			}

		} 
		catch (Exception e) 
		{
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
			processResponse.setCode("E0000");
			processResponse.setStatus("Failed");
			processResponse.setMessage("Internal Server Error - " + e.getMessage());
		}
		// return ResponseEntity.ok("{}");
		return ResponseEntity.ok(encryptDecryptData.encryptPayloadReqRes(req, processResponse));
	}
	
	//Adding address info api [Start]
	@RequestMapping(path = "/addAddress", method = RequestMethod.POST)
	public ResponseEntity<?> addAddress(HttpServletRequest request,@RequestBody PayloadReqRes req) 
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
			
			int count =	preAccountMasterService.addAddressOfCustomer(preAccountMaster);
			if (count > 0) 
			{
				processResponse.setCode("S0000");
				processResponse.setStatus("Success");
				processResponse.setMessage("Successfully added address info.");
			} 
			else 
			{
				processResponse.setCode("E0000");
				processResponse.setStatus("Failed");
				processResponse.setMessage("Issue while adding address.");
			}

		} 
		catch (Exception e) 
		{
			processResponse.setCode("E0000");
			processResponse.setStatus("Failed");
			processResponse.setMessage("Internal Server Error");
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		}
		// return ResponseEntity.ok("{}");
		return ResponseEntity.ok(encryptDecryptData.encryptPayloadReqRes(req, processResponse));
	}
	//Adding address info api [End]
}
