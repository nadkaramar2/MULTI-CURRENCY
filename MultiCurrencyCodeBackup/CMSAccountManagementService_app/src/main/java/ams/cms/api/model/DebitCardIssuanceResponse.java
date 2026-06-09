package ams.cms.api.model;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class DebitCardIssuanceResponse implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String outResponseCode;
	private String message;
	private String outCard;
	private String outTokenCard;
	
	//added by ankit
	private CardDetails cardDetails;
	//created by ankit
	
	public String getOutResponseCode() {
		return outResponseCode;
	}
	public void setOutResponseCode(String outResponseCode) {
		this.outResponseCode = outResponseCode;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String getOutCard() {
		return outCard;
	}
	public void setOutCard(String outCard) {
		this.outCard = outCard;
	}
	public String getOutTokenCard() {
		return outTokenCard;
	}
	public void setOutTokenCard(String outTokenCard) {
		this.outTokenCard = outTokenCard;
	}
	//created by ankit
	public CardDetails getCardDetails()
	{
	    return cardDetails;
	}
	public void setCardDetails(CardDetails cardDetails)
	{
	    this.cardDetails = cardDetails;
	}
	
	
}
