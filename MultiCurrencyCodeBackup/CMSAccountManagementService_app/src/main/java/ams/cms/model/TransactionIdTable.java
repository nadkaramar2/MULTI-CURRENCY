package ams.cms.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonAutoDetect;

@JsonAutoDetect
@Entity
@Table(name = "transaction_id_table")
public class TransactionIdTable {

	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private String strID;
	
	@Column(name = "year")
	private String strYear;
	
	@Column(name = "julian_date")
	private String strJulianDate;
	
	@Column(name = "last_txn_serial_no")
	private String strLastTxnSerialNo;
	
	@Column(name = "created_date")
	private String strCreatedDate;
	
	@Column(name = "created_by")
	private String strCreatedBy;

	public String getStrID() {
		return strID;
	}

	public void setStrID(String strID) {
		this.strID = strID;
	}

	public String getStrYear() {
		return strYear;
	}

	public void setStrYear(String strYear) {
		this.strYear = strYear;
	}

	public String getStrJulianDate() {
		return strJulianDate;
	}

	public void setStrJulianDate(String strJulianDate) {
		this.strJulianDate = strJulianDate;
	}

	public String getStrLastTxnSerialNo() {
		return strLastTxnSerialNo;
	}

	public void setStrLastTxnSerialNo(String strLastTxnSerialNo) {
		this.strLastTxnSerialNo = strLastTxnSerialNo;
	}

	public String getStrCreatedDate() {
		return strCreatedDate;
	}

	public void setStrCreatedDate(String strCreatedDate) {
		this.strCreatedDate = strCreatedDate;
	}

	public String getStrCreatedBy() {
		return strCreatedBy;
	}

	public void setStrCreatedBy(String strCreatedBy) {
		this.strCreatedBy = strCreatedBy;
	}
	
	
}
