package ams.cms.model;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(value = JsonInclude.Include.NON_EMPTY)
public class TransactionInformation implements Serializable
{
	private static final long serialVersionUID = 1L;
	
	private int totalDebitCount;
	private double totalDebitAmount;
	
	private int totalCreditCount;
	private double totalCreditAmount;
	
	public int getTotalDebitCount() {
		return totalDebitCount;
	}
	public void setTotalDebitCount(int totalDebitCount) {
		this.totalDebitCount = totalDebitCount;
	}
	public int getTotalCreditCount() {
		return totalCreditCount;
	}
	public void setTotalCreditCount(int totalCreditCount) {
		this.totalCreditCount = totalCreditCount;
	}
	public double getTotalDebitAmount() {
		return totalDebitAmount;
	}
	public void setTotalDebitAmount(double totalDebitAmount) {
		this.totalDebitAmount = totalDebitAmount;
	}
	public double getTotalCreditAmount() {
		return totalCreditAmount;
	}
	public void setTotalCreditAmount(double totalCreditAmount) {
		this.totalCreditAmount = totalCreditAmount;
	}
	
	@Override
	public String toString() {
		return "TransactionInformation [totalDebitCount=" + totalDebitCount + ", totalDebitAmount=" + totalDebitAmount
				+ ", totalCreditCount=" + totalCreditCount + ", totalCreditAmount=" + totalCreditAmount + "]";
	}
}
