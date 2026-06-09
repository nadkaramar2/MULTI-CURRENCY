package ams.cms.model;

import java.io.Serializable;

public class CustomerInfo implements Serializable 
{	
	private static final long serialVersionUID = 1L;
	
	private String title;
	private String firstName;
	private String middleName;
	private String lastName;
	private String gender;	
	private String birthDate;
	private String emailID;
	private String mobileNo;
	private String address1;
	private String address2;
	private String address3;
	private String pinCode;
	private String city;
	private String state;
	private String country;	
	private String tier1PassportPhotograph;
	private String tier2PassportPhotograph;
	private String bvnNumber;
	private String poiId;
	private String poiPhoto;
	private String poaId;
	private String poaPhoto;
	private String profilePhotoImage;
	private String participantId;
	
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getProfilePhotoImage() {
		return profilePhotoImage;
	}
	public void setProfilePhotoImage(String profilePhotoImage) {
		this.profilePhotoImage = profilePhotoImage;
	}
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getMiddleName() {
		return middleName;
	}
	public void setMiddleName(String middleName) {
		this.middleName = middleName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}
	public String getBirthDate() {
		return birthDate;
	}
	public void setBirthDate(String birthDate) {
		this.birthDate = birthDate;
	}
	public String getEmailID() {
		return emailID;
	}
	public void setEmailID(String emailID) {
		this.emailID = emailID;
	}
	public String getMobileNo() {
		return mobileNo;
	}
	public void setMobileNo(String mobileNo) {
		this.mobileNo = mobileNo;
	}
	public String getAddress1() {
		return address1;
	}
	public void setAddress1(String address1) {
		this.address1 = address1;
	}
	public String getAddress2() {
		return address2;
	}
	public void setAddress2(String address2) {
		this.address2 = address2;
	}
	public String getAddress3() {
		return address3;
	}
	public void setAddress3(String address3) {
		this.address3 = address3;
	}
	public String getPinCode() {
		return pinCode;
	}
	public void setPinCode(String pinCode) {
		this.pinCode = pinCode;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getCountry() {
		return country;
	}
	public void setCountry(String country) {
		this.country = country;
	}
	public String getTier1PassportPhotograph() {
		return tier1PassportPhotograph;
	}
	public void setTier1PassportPhotograph(String tier1PassportPhotograph) {
		this.tier1PassportPhotograph = tier1PassportPhotograph;
	}
	public String getTier2PassportPhotograph() {
		return tier2PassportPhotograph;
	}
	public void setTier2PassportPhotograph(String tier2PassportPhotograph) {
		this.tier2PassportPhotograph = tier2PassportPhotograph;
	}
	public String getBvnNumber() {
		return bvnNumber;
	}
	public void setBvnNumber(String bvnNumber) {
		this.bvnNumber = bvnNumber;
	}
	public String getPoiId() {
		return poiId;
	}
	public void setPoiId(String poiId) {
		this.poiId = poiId;
	}
	public String getPoiPhoto() {
		return poiPhoto;
	}
	public void setPoiPhoto(String poiPhoto) {
		this.poiPhoto = poiPhoto;
	}
	public String getPoaId() {
		return poaId;
	}
	public void setPoaId(String poaId) {
		this.poaId = poaId;
	}
	public String getPoaPhoto() {
		return poaPhoto;
	}
	public void setPoaPhoto(String poaPhoto) {
		this.poaPhoto = poaPhoto;
	}
	public String getParticipantId() {
		return participantId;
	}
	public void setParticipantId(String participantId) {
		this.participantId = participantId;
	}
	
}
