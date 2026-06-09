package ams.cms.api.model;


public class DebitCardIssuanceRequest {
	private String inPartID; 
	private String inFunction;  //request Type NewCard or Renewal
	//changed data type to int
	private String inCustomerID;
	private String inCard;
	private String inNewCard;
	private String inMbr;
	private String inCardType;
	private String inCardIssueCode; //Physical /Virtual
	private String inEmbossLine1;
	private String inEmbossLine2;
	private String inEncodeFirstName;
	private String inEncodeMiddleName;
	private String inEncodeLastName;
	private String inCardMailerFlag;
	private String inPinMailerFlag;
	private String inUserID;
	private String inHotListFlag;
	private String inInstantFlag;
	private String inCode;
	private String inBranchCode;
	public String getInPartID() {
		return inPartID;
	}
	public void setInPartID(String inPartID) {
		this.inPartID = inPartID;
	}
	public String getInFunction() {
		return inFunction;
	}
	public void setInFunction(String inFunction) {
		this.inFunction = inFunction;
	}
	public String getInCustomerID() {
		return inCustomerID;
	}
	public void setInCustomerID(String inCustomerID) {
		this.inCustomerID = inCustomerID;
	}
	public String getInCard() {
		return inCard;
	}
	public void setInCard(String inCard) {
		this.inCard = inCard;
	}
	public String getInNewCard() {
		return inNewCard;
	}
	public void setInNewCard(String inNewCard) {
		this.inNewCard = inNewCard;
	}
	public String getInMbr() {
		return inMbr;
	}
	public void setInMbr(String inMbr) {
		this.inMbr = inMbr;
	}
	public String getInCardType() {
		return inCardType;
	}
	public void setInCardType(String inCardType) {
		this.inCardType = inCardType;
	}
	public String getInCardIssueCode() {
		return inCardIssueCode;
	}
	public void setInCardIssueCode(String inCardIssueCode) {
		this.inCardIssueCode = inCardIssueCode;
	}
	public String getInEmbossLine1() {
		return inEmbossLine1;
	}
	public void setInEmbossLine1(String inEmbossLine1) {
		this.inEmbossLine1 = inEmbossLine1;
	}
	public String getInEmbossLine2() {
		return inEmbossLine2;
	}
	public void setInEmbossLine2(String inEmbossLine2) {
		this.inEmbossLine2 = inEmbossLine2;
	}
	public String getInEncodeFirstName() {
		return inEncodeFirstName;
	}
	public void setInEncodeFirstName(String inEncodeFirstName) {
		this.inEncodeFirstName = inEncodeFirstName;
	}
	public String getInEncodeMiddleName() {
		return inEncodeMiddleName;
	}
	public void setInEncodeMiddleName(String inEncodeMiddleName) {
		this.inEncodeMiddleName = inEncodeMiddleName;
	}
	public String getInEncodeLastName() {
		return inEncodeLastName;
	}
	public void setInEncodeLastName(String inEncodeLastName) {
		this.inEncodeLastName = inEncodeLastName;
	}
	public String getInCardMailerFlag() {
		return inCardMailerFlag;
	}
	public void setInCardMailerFlag(String inCardMailerFlag) {
		this.inCardMailerFlag = inCardMailerFlag;
	}
	public String getInPinMailerFlag() {
		return inPinMailerFlag;
	}
	public void setInPinMailerFlag(String inPinMailerFlag) {
		this.inPinMailerFlag = inPinMailerFlag;
	}
	public String getInUserID() {
		return inUserID;
	}
	public void setInUserID(String inUserID) {
		this.inUserID = inUserID;
	}
	public String getInHotListFlag() {
		return inHotListFlag;
	}
	public void setInHotListFlag(String inHotListFlag) {
		this.inHotListFlag = inHotListFlag;
	}
	public String getInInstantFlag() {
		return inInstantFlag;
	}
	public void setInInstantFlag(String inInstantFlag) {
		this.inInstantFlag = inInstantFlag;
	}
	public String getInCode() {
		return inCode;
	}
	public void setInCode(String inCode) {
		this.inCode = inCode;
	}
	public String getInBranchCode() {
		return inBranchCode;
	}
	public void setInBranchCode(String inBranchCode) {
		this.inBranchCode = inBranchCode;
	}
	
	
}
