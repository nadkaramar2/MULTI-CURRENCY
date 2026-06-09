package ams.cms.api.model;


import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;


public class CardDetails {
	@JsonProperty("strParticipantID")
	@JsonAlias({ "strParticipantID", "strPartID" })
	private String strPartID;
	private String strCardType;
	@JsonProperty("strCardNo")
	@JsonAlias({ "strCardNo", "strCardNumber" })
	private String strCardNumber;
	@JsonProperty("strMemberNo")
	@JsonAlias({ "strCardSeqNumber", "strMemberNo" })
	private String strCardSeqNumber;
	private String strTokenCard;
	private String strServiceCode;
	private String strEmbossLine1;
	private String strEmbossLine2;
	private String strEncodeFirstName;
	private String strEncodeMiddleName;
	private String strEncodeLastName;
	private String strCardIssueDate;
	private String strCardIssueCode;
	private String strCardHolderSince;
	private String strExpiryDate;
	private String strNewExpiryDate;
	private String strCardStatus;
	private String strDailyPinRetryLimit;
	private String strDailyPinRetryCount;
	private String strConsecutivePinRetryLimit;
	private String strConsecutivePinRetryCount;
	private String strLastUpdatedDate;
	private String strCardIssuedUser;
	private String strLastUpdatedUser;
	private String strCardMailerIssueDate;
	private String strPinMailerIssueDate;
	private String strPinMailerIssueFlag;
	private String strCardMailerIssueFlag;
	//added by ankit 
	private String strEncryptedCard;
	//added by ankit 
	public String getStrPartID()
	{
	    return strPartID;
	}
	public void setStrPartID(String strPartID)
	{
	    this.strPartID = strPartID;
	}
	public String getStrCardType()
	{
	    return strCardType;
	}
	public void setStrCardType(String strCardType)
	{
	    this.strCardType = strCardType;
	}
	public String getStrCardNumber()
	{
	    return strCardNumber;
	}
	public void setStrCardNumber(String strCardNumber)
	{
	    this.strCardNumber = strCardNumber;
	}
	public String getStrCardSeqNumber()
	{
	    return strCardSeqNumber;
	}
	public void setStrCardSeqNumber(String strCardSeqNumber)
	{
	    this.strCardSeqNumber = strCardSeqNumber;
	}
	public String getStrTokenCard()
	{
	    return strTokenCard;
	}
	public void setStrTokenCard(String strTokenCard)
	{
	    this.strTokenCard = strTokenCard;
	}
	public String getStrServiceCode()
	{
	    return strServiceCode;
	}
	public void setStrServiceCode(String strServiceCode)
	{
	    this.strServiceCode = strServiceCode;
	}
	public String getStrEmbossLine1()
	{
	    return strEmbossLine1;
	}
	public void setStrEmbossLine1(String strEmbossLine1)
	{
	    this.strEmbossLine1 = strEmbossLine1;
	}
	public String getStrEmbossLine2()
	{
	    return strEmbossLine2;
	}
	public void setStrEmbossLine2(String strEmbossLine2)
	{
	    this.strEmbossLine2 = strEmbossLine2;
	}
	public String getStrEncodeFirstName()
	{
	    return strEncodeFirstName;
	}
	public void setStrEncodeFirstName(String strEncodeFirstName)
	{
	    this.strEncodeFirstName = strEncodeFirstName;
	}
	public String getStrEncodeMiddleName()
	{
	    return strEncodeMiddleName;
	}
	public void setStrEncodeMiddleName(String strEncodeMiddleName)
	{
	    this.strEncodeMiddleName = strEncodeMiddleName;
	}
	public String getStrEncodeLastName()
	{
	    return strEncodeLastName;
	}
	public void setStrEncodeLastName(String strEncodeLastName)
	{
	    this.strEncodeLastName = strEncodeLastName;
	}
	public String getStrCardIssueDate()
	{
	    return strCardIssueDate;
	}
	public void setStrCardIssueDate(String strCardIssueDate)
	{
	    this.strCardIssueDate = strCardIssueDate;
	}
	public String getStrCardIssueCode()
	{
	    return strCardIssueCode;
	}
	public void setStrCardIssueCode(String strCardIssueCode)
	{
	    this.strCardIssueCode = strCardIssueCode;
	}
	public String getStrCardHolderSince()
	{
	    return strCardHolderSince;
	}
	public void setStrCardHolderSince(String strCardHolderSince)
	{
	    this.strCardHolderSince = strCardHolderSince;
	}
	public String getStrExpiryDate()
	{
	    return strExpiryDate;
	}
	public void setStrExpiryDate(String strExpiryDate)
	{
	    this.strExpiryDate = strExpiryDate;
	}
	public String getStrNewExpiryDate()
	{
	    return strNewExpiryDate;
	}
	public void setStrNewExpiryDate(String strNewExpiryDate)
	{
	    this.strNewExpiryDate = strNewExpiryDate;
	}
	public String getStrCardStatus()
	{
	    return strCardStatus;
	}
	public void setStrCardStatus(String strCardStatus)
	{
	    this.strCardStatus = strCardStatus;
	}
	public String getStrDailyPinRetryLimit()
	{
	    return strDailyPinRetryLimit;
	}
	public void setStrDailyPinRetryLimit(String strDailyPinRetryLimit)
	{
	    this.strDailyPinRetryLimit = strDailyPinRetryLimit;
	}
	public String getStrDailyPinRetryCount()
	{
	    return strDailyPinRetryCount;
	}
	public void setStrDailyPinRetryCount(String strDailyPinRetryCount)
	{
	    this.strDailyPinRetryCount = strDailyPinRetryCount;
	}
	public String getStrConsecutivePinRetryLimit()
	{
	    return strConsecutivePinRetryLimit;
	}
	public void setStrConsecutivePinRetryLimit(String strConsecutivePinRetryLimit)
	{
	    this.strConsecutivePinRetryLimit = strConsecutivePinRetryLimit;
	}
	public String getStrConsecutivePinRetryCount()
	{
	    return strConsecutivePinRetryCount;
	}
	public void setStrConsecutivePinRetryCount(String strConsecutivePinRetryCount)
	{
	    this.strConsecutivePinRetryCount = strConsecutivePinRetryCount;
	}
	public String getStrLastUpdatedDate()
	{
	    return strLastUpdatedDate;
	}
	public void setStrLastUpdatedDate(String strLastUpdatedDate)
	{
	    this.strLastUpdatedDate = strLastUpdatedDate;
	}
	public String getStrCardIssuedUser()
	{
	    return strCardIssuedUser;
	}
	public void setStrCardIssuedUser(String strCardIssuedUser)
	{
	    this.strCardIssuedUser = strCardIssuedUser;
	}
	public String getStrLastUpdatedUser()
	{
	    return strLastUpdatedUser;
	}
	public void setStrLastUpdatedUser(String strLastUpdatedUser)
	{
	    this.strLastUpdatedUser = strLastUpdatedUser;
	}
	public String getStrCardMailerIssueDate()
	{
	    return strCardMailerIssueDate;
	}
	public void setStrCardMailerIssueDate(String strCardMailerIssueDate)
	{
	    this.strCardMailerIssueDate = strCardMailerIssueDate;
	}
	public String getStrPinMailerIssueDate()
	{
	    return strPinMailerIssueDate;
	}
	public void setStrPinMailerIssueDate(String strPinMailerIssueDate)
	{
	    this.strPinMailerIssueDate = strPinMailerIssueDate;
	}
	public String getStrPinMailerIssueFlag()
	{
	    return strPinMailerIssueFlag;
	}
	public void setStrPinMailerIssueFlag(String strPinMailerIssueFlag)
	{
	    this.strPinMailerIssueFlag = strPinMailerIssueFlag;
	}
	public String getStrCardMailerIssueFlag()
	{
	    return strCardMailerIssueFlag;
	}
	public void setStrCardMailerIssueFlag(String strCardMailerIssueFlag)
	{
	    this.strCardMailerIssueFlag = strCardMailerIssueFlag;
	}
	public String getStrEncryptedCard()
	{
	    return strEncryptedCard;
	}
	public void setStrEncryptedCard(String strEncryptedCard)
	{
	    this.strEncryptedCard = strEncryptedCard;
	}
	
	
}
