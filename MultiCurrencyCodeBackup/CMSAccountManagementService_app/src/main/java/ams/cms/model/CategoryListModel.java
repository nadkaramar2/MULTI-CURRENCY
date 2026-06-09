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
@Table(name = "account_type_category_master")
public class CategoryListModel implements Serializable
{
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private int id;
	
	@Column(name = "participant_id")
	private String strParticipantID;
	
	@Column(name = "type")
	private String strType;
	
	@Column(name = "description")
	private String strDescription;
	
	@Column(name = "category_type")
	private String strCategoryType;

	public int getId() {
		return id;
	}

	public String getStrParticipantID() {
		return strParticipantID;
	}

	public void setStrParticipantID(String strParticipantID) {
		this.strParticipantID = strParticipantID;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getStrType() {
		return strType;
	}

	public void setStrType(String strType) {
		this.strType = strType;
	}

	public String getStrDescription() {
		return strDescription;
	}

	public void setStrDescription(String strDescription) {
		this.strDescription = strDescription;
	}

	public String getStrCategoryType() {
		return strCategoryType;
	}

	public void setStrCategoryType(String strCategoryType) {
		this.strCategoryType = strCategoryType;
	}
}
