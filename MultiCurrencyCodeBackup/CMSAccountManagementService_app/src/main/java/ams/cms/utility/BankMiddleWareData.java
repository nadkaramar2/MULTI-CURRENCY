package ams.cms.utility;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(value = JsonInclude.Include.NON_NULL)
public class BankMiddleWareData implements Serializable 
{
	private static final long serialVersionUID = 1L;
	
	private String category;
	private String code;
	private String bankCode;
	private String name;
	
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getBankCode() {
		return bankCode;
	}
	public void setBankCode(String bankCode) {
		this.bankCode = bankCode;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	@Override
	public String toString() {
		return "BankMiddleWareData [category=" + category + ", code=" + code + ", bankCode=" + bankCode + ", name="
				+ name + "]";
	}
}
