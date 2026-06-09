package ams.cms.api.model;

import java.io.Serializable;

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
@Table(name = "state_master")
public class StateModel implements Serializable 
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
    
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private String strID;
		
	@Column(name = "state_name")
	private String strStateName;
	
	@Column(name = "country_id")
	private String strCountryID;
	
	@Transient
	private String strStatus;
	
	@Transient
	private String strSelectID;
	
	public String getStrID() {
		return strID;
	}
	public void setStrID(String strID) {
		this.strID = strID;
	}
	public String getStrCountryID() {
		return strCountryID;
	}
	public void setStrCountryID(String strCountryID) {
		this.strCountryID = strCountryID;
	}
	public String getStrStateName() {
		return strStateName;
	}
	public void setStrStateName(String strStateName) {
		this.strStateName = strStateName;
	}
	public String getStrStatus() {
		return strStatus;
	}
	public void setStrStatus(String strStatus) {
		this.strStatus = strStatus;
	}
	public String getStrSelectID() {
		return strSelectID;
	}
	public void setStrSelectID(String strSelectID) {
		this.strSelectID = strSelectID;
	}
	
	
}
