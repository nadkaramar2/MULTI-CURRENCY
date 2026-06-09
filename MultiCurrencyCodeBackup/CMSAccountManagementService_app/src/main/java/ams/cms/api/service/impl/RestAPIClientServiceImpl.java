package ams.cms.api.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import ams.cms.api.model.CardDetailsRequest;
import ams.cms.api.service.RestAPIClientService;
import ams.cms.model.CardAccountLinkage;
import ams.cms.util.RestAPIUri;

@Service
public class RestAPIClientServiceImpl implements RestAPIClientService
{
	@Autowired
	RestTemplate restTemplate;
	
	@Override
	public ResponseEntity<?> getCardNoFromClient(CardDetailsRequest cardDetails) 
	{
		String cmsCardNoApiUrl = RestAPIUri.getCMSCardNoAPIUrl();
		ResponseEntity<CardDetailsRequest> postForEntity = restTemplate.postForEntity(cmsCardNoApiUrl, 	cardDetails,	CardDetailsRequest.class);
		return postForEntity;
	}

	@Override
	public ResponseEntity<?> getCardDetails(CardAccountLinkage cardAccountLinkage) 
	{
		String cmsCardNoApiUrl = RestAPIUri.getCMSCardDetailsAPIUrl();
		ResponseEntity<CardAccountLinkage> postForEntity = restTemplate.postForEntity(cmsCardNoApiUrl, 	cardAccountLinkage,	CardAccountLinkage.class);
		return postForEntity;
	}
	
	@Override
	public ResponseEntity<?> updateCardStatusBlock(CardAccountLinkage cardAccountLinkage) {
		String cmsCardNoApiUrl = RestAPIUri.updateCardStatusBlock();
		ResponseEntity<CardAccountLinkage> postForEntity = restTemplate.postForEntity(cmsCardNoApiUrl, 	cardAccountLinkage,	CardAccountLinkage.class);
		return postForEntity;
	}
	
	@Override
	public ResponseEntity<?> updateCardStatusActive(CardAccountLinkage cardAccountLinkage) {
		String cmsCardNoApiUrl = RestAPIUri.updateCardStatusActive();
		ResponseEntity<CardAccountLinkage> postForEntity = restTemplate.postForEntity(cmsCardNoApiUrl, 	cardAccountLinkage,	CardAccountLinkage.class);
		return postForEntity;
	}
	
	/*Commented Ankit New Request Card API Changes Start
	@Override
	public ResponseEntity<?> getCardTypes() {
		String cmsCardNoApiUrl = RestAPIUri.getCMSCardTypesAPIUrl();
		ResponseEntity<List<CardTypeResponse>> response = restTemplate.exchange(cmsCardNoApiUrl, HttpMethod.GET, null, new ParameterizedTypeReference<List<CardTypeResponse>>() {});
		return response;
		
	}
	
	@Override
	public ResponseEntity<?> requestToGetTheCardNumber(CardIssueRequest cardIssueRequest,AccountCreation accountCreation) {
		String cardIssuanceAPIUrl = RestAPIUri.getCardIssuanceAPIUrl();
		DebitCardIssuanceRequest debitCardIssuanceRequest = mapDataToDebitCardIssuanceRequest(cardIssueRequest,accountCreation);
		ResponseEntity<DebitCardIssuanceResponse> postForEntity = restTemplate.postForEntity(cardIssuanceAPIUrl, debitCardIssuanceRequest, DebitCardIssuanceResponse.class);
		return postForEntity;
	}
	//created by ankit on 28-04--2023


	//support method to rquestGetTheCardNumber
	private DebitCardIssuanceRequest mapDataToDebitCardIssuanceRequest(CardIssueRequest cardIssueRequest, AccountCreation accountCreation) 
	{
    	String strFirstName = accountCreation.getStrFirstName();
    	String strLastName = accountCreation.getStrLastName();
    	String strMiddleName = accountCreation.getStrMiddleName();
		//Required Field which i am setting Static for now !
		String inFunctionNewCard = "NEWCARD";
		String cardType = cardIssueRequest.getStrCardType(); //card Types Primary Key but here passing card Type
		//String inCardType = "47";
		String inCard = null;  //should be null for "NEWCARD"
		String inBranchCode = "5022";
		String inCardIssueCode = "0";
		String inCustomerID = "1234";
		String inPartIdOfBank = "6";   
		String isInstantFlag = "Y";
		String system = "System";
		String embossLineOne = "Instant Card Holder";
		String inCardMailer = "N";
		String isMbr = "1";
		String inCode = "123";
		DebitCardIssuanceRequest debitCardIssucanceRequest = new DebitCardIssuanceRequest(); 
		debitCardIssucanceRequest.setInBranchCode(inBranchCode);
		debitCardIssucanceRequest.setInCard(inCard);
		debitCardIssucanceRequest.setInCardIssueCode(inCardIssueCode);
		debitCardIssucanceRequest.setInCustomerID(inCustomerID);
		debitCardIssucanceRequest.setInInstantFlag(isInstantFlag);
		debitCardIssucanceRequest.setInPartID(inPartIdOfBank);
		debitCardIssucanceRequest.setInFunction(inFunctionNewCard);
		debitCardIssucanceRequest.setInCardType(cardType);
		debitCardIssucanceRequest.setInEncodeFirstName(strFirstName);
		debitCardIssucanceRequest.setInEncodeMiddleName(strMiddleName);
		debitCardIssucanceRequest.setInEncodeLastName(strLastName);
		debitCardIssucanceRequest.setInUserID(system);
		debitCardIssucanceRequest.setInEmbossLine1(embossLineOne);
		debitCardIssucanceRequest.setInCardMailerFlag(inCardMailer);
		debitCardIssucanceRequest.setInMbr(isMbr);
		return debitCardIssucanceRequest;
	}
	Commented Ankit New Request Card API Changes End*/
	/*
	@Override
	public String test() 
	{
	        String text = RestAPIUri.test();
	        String forObject = restTemplate.getForObject(text,String.class);
	        System.out.print(forObject);
	        return forObject;
	}
	 */
}
