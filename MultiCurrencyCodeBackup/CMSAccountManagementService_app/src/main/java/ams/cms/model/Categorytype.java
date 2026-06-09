package ams.cms.model;

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
@Table(name = "category_type")
public class Categorytype implements Serializable
{
	
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private int strID;
  
	@Column(name="type")
	private String strType;	
	
	@Column(name="description")
	private String strDescription;

	public String getStrType() {
		return strType;
	}


	public void setStrType(String strType)
	{
		this.strType = strType;
	}


	public String getStrDescription() {
		return strDescription;
	}


	public void setStrDescription(String strDescription) {
		this.strDescription = strDescription;
	}
	
	
	
	
	
}
