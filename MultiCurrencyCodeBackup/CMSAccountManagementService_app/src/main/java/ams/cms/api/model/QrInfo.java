/*Added by pankaj Pawar for QR CODE Generation*/

package ams.cms.api.model;

public class QrInfo {
	
	private String base64Image;	
	private String filePath;
	
	private String account_no;
	private String mobile_no;
	private String account_type;
	private String account_name;
	private String qrCodeImageUrl;
	
	private String actualBase64Image;

	public String getAccount_no() {
		return account_no;
	}

	public void setAccount_no(String account_no) {
		this.account_no = account_no;
	}

	public String getMobile_no() {
		return mobile_no;
	}

	public void setMobile_no(String mobile_no) {
		this.mobile_no = mobile_no;
	}

	public String getAccount_type() {
		return account_type;
	}

	public void setAccount_type(String account_type) {
		this.account_type = account_type;
	}

	public String getAccount_name() {
		return account_name;
	}

	public void setAccount_name(String account_name) {
		this.account_name = account_name;
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public String getBase64Image() {
		return base64Image;
	}

	public void setBase64Image(String base64Image) {
		this.base64Image = base64Image;
	}

	public String getQrCodeImageUrl() {
		return qrCodeImageUrl;
	}

	public void setQrCodeImageUrl(String qrCodeImageUrl) {
		this.qrCodeImageUrl = qrCodeImageUrl;
	}

	public String getActualBase64Image() {
		return actualBase64Image;
	}

	public void setActualBase64Image(String actualBase64Image) {
		this.actualBase64Image = actualBase64Image;
	}
	
}
