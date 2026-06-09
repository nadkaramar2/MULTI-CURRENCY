package ams.cms.api.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonAutoDetect;

@JsonAutoDetect
@Entity
@Table(name = "denomination_values_master")
public class DenominationValuesMaster implements Serializable 
{
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private String strID;
	
	@Column(name = "country")
	private String strCountry;
	
	@Column(name = "currency")
	private String strCurrency;
	
	@Column(name = "currency_code")
	private String strCurrencyCode;
	
	@Column(name = "currenyc_number")
	private String strCurrencyNumber;
	
	@Column(name = "d10")
	private Integer d10;
	
	@Column(name = "d20")
	private Integer d20;
	
	@Column(name = "d50")
	private Integer d50;
	
	@Column(name = "d100")
	private Integer d100;
	
	@Column(name = "d200")
	private Integer d200;
	
	@Column(name = "d500")
	private Integer d500;
	
	@Column(name = "d1000")
	private Integer d1000;
	
	@Column(name = "d2000")
	private Integer d2000;

	public String getStrID() {
		return strID;
	}

	public void setStrID(String strID) {
		this.strID = strID;
	}

	public String getStrCountry() {
		return strCountry;
	}

	public void setStrCountry(String strCountry) {
		this.strCountry = strCountry;
	}

	public String getStrCurrency() {
		return strCurrency;
	}

	public void setStrCurrency(String strCurrency) {
		this.strCurrency = strCurrency;
	}

	public String getStrCurrencyCode() {
		return strCurrencyCode;
	}

	public void setStrCurrencyCode(String strCurrencyCode) {
		this.strCurrencyCode = strCurrencyCode;
	}

	public String getStrCurrencyNumber() {
		return strCurrencyNumber;
	}

	public void setStrCurrencyNumber(String strCurrencyNumber) {
		this.strCurrencyNumber = strCurrencyNumber;
	}

	public Integer getD10() {
		return d10;
	}

	public void setD10(Integer d10) {
		this.d10 = d10;
	}

	public Integer getD20() {
		return d20;
	}

	public void setD20(Integer d20) {
		this.d20 = d20;
	}

	public Integer getD50() {
		return d50;
	}

	public void setD50(Integer d50) {
		this.d50 = d50;
	}

	public Integer getD100() {
		return d100;
	}

	public void setD100(Integer d100) {
		this.d100 = d100;
	}

	public Integer getD200() {
		return d200;
	}

	public void setD200(Integer d200) {
		this.d200 = d200;
	}

	public Integer getD500() {
		return d500;
	}

	public void setD500(Integer d500) {
		this.d500 = d500;
	}

	public Integer getD1000() {
		return d1000;
	}

	public void setD1000(Integer d1000) {
		this.d1000 = d1000;
	}

	public Integer getD2000() {
		return d2000;
	}

	public void setD2000(Integer d2000) {
		this.d2000 = d2000;
	}
}
