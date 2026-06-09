package ams.cms.model;

public class CurrencyConversionRateRequest {
	
	private String fromCurrency;
	private String toCurrency;
	private String loadedAmt;
	private String amtToBeConverted;

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

	public String getAmtToBeConverted() {
		return amtToBeConverted;
	}

	public void setAmtToBeConverted(String amtToBeConverted) {
		this.amtToBeConverted = amtToBeConverted;
	}

	public String getLoadedAmt() {
		return loadedAmt;
	}

	public void setLoadedAmt(String loadedAmt) {
		this.loadedAmt = loadedAmt;
	}
	
	
	
	

}
