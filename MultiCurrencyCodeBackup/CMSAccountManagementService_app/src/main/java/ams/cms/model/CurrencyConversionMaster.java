package ams.cms.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonAutoDetect;

@Entity
@Table(name = "currency_conversion_master")
@JsonAutoDetect
public class CurrencyConversionMaster {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private String id;
	
	@Column(name = "from_currency")
	private String fromCurrency;
	
	@Column(name = "to_currency")
	private String toCurrency;
	
	@Column(name = "from_currency_value")
	private Double fromCurrencyValue;
	
	@Column(name = "to_currency_conversion")
	private Double toCurrencyConversion;
	
	@Column(name = "created_date")
	private Date createdDate;
	
	@Column(name = "created_by")
	private String createdBy;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getFromCurrency() {
		return fromCurrency;
	}

	public void setFromCurrency(String fromCurrency) {
		this.fromCurrency = fromCurrency;
	}

	public String getToCurrency() {
		return toCurrency;
	}

	public void setToCurrency(String toCurrency) {
		this.toCurrency = toCurrency;
	}

	

	public Double getFromCurrencyValue() {
		return fromCurrencyValue;
	}

	public void setFromCurrencyValue(Double fromCurrencyValue) {
		this.fromCurrencyValue = fromCurrencyValue;
	}

	public Double getToCurrencyConversion() {
		return toCurrencyConversion;
	}

	public void setToCurrencyConversion(Double toCurrencyConversion) {
		this.toCurrencyConversion = toCurrencyConversion;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	
	
	
	

}
