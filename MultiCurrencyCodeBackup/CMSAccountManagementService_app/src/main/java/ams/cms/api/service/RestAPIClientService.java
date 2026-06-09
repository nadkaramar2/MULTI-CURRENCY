package ams.cms.api.service;

import org.springframework.http.ResponseEntity;

import ams.cms.api.model.CardDetailsRequest;
import ams.cms.model.CardAccountLinkage;

//created by ankit
public interface RestAPIClientService 
{
	public ResponseEntity<?> getCardNoFromClient(CardDetailsRequest cardDetails);
	//public String test();
	
	public ResponseEntity<?> getCardDetails(CardAccountLinkage cardAccountLinkage);
	
	public ResponseEntity<?> updateCardStatusBlock(CardAccountLinkage cardAccountLinkage);
	
	public ResponseEntity<?> updateCardStatusActive(CardAccountLinkage cardAccountLinkage);
	
	/*Commented Ankit New Request Card API Changes Start
	//added by ankit 02-05-2023
	public ResponseEntity<?> getCardTypes();

	public ResponseEntity<?> requestToGetTheCardNumber(CardIssueRequest cardIssueRequest, AccountCreation accountCreation);
	//added by ankit 02-05-2023
	 * 
	 Commented Ankit New Request Card API Changes End*/
}
