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
@Table(name = "city_master")
public class CityModel implements Serializable 
{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private String strID;
	
	@Column(name = "city_name")
	private String strCityName;
	
	@Column(name = "state_id")
	private String strStateID;
	
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
	public String getStrStateID() {
		return strStateID;
	}
	public void setStrStateID(String strStateID) {
		this.strStateID = strStateID;
	}
	public String getStrCityName() {
		return strCityName;
	}
	public void setStrCityName(String strCityName) {
		this.strCityName = strCityName;
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
