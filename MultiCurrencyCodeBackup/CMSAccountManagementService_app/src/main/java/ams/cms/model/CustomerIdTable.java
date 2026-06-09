package ams.cms.model;


import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonAutoDetect;

@JsonAutoDetect
@Entity
@Table(name = "customer_id_table")
public class CustomerIdTable implements Serializable {
	
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
	
	@Column(name = "create_date")
	private Date strCreatedDate;
	
	@Column(name = "created_by")
	private String strCreatedBy;
	
	@Transient
	private String strAction;
	
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
	
	public String getstrJulianDate() {
		return strJulianDate;
	}

	public void setstrJulianDate(String strJulianDate) {
		this.strJulianDate = strJulianDate;
	}
	
	public String getstrLastTxnSerialNo() {
		return strLastTxnSerialNo;
	}

	public void setstrLastTxnSerialNo(String strLastTxnSerialNo) {
		this.strLastTxnSerialNo = strLastTxnSerialNo;
	}

	
	public Date getStrCreatedDate() {
		return strCreatedDate;
	}

	public void setStrCreatedDate(Date strCreatedDate) {
		this.strCreatedDate = strCreatedDate;
	}

	public String getstrCreatedby() {
		return strCreatedBy;
	}

	public void setstrCreatedby(String strCreatedBy) {
		this.strCreatedBy = strCreatedBy;
	}

	public String getStrAction() {
		return strAction;
	}

	public void setStrAction(String strAction) {
		this.strAction = strAction;
	}
	
	
	
}
