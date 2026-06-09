package ams.cms.api.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "nuban_type_config")
public class NubanTypeConfig implements Serializable{
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private long strId;
	
	@Column(name = "nuban_type")
	private String strNubanType;
	
	@Column(name = "nuban_type_description")
	private String strNubanTypeDescription;
	
	public long getStrId() {
		return strId;
	}
	public void setStrId(long strId) {
		this.strId = strId;
	}
	public String getStrNubanType() {
		return strNubanType;
	}
	public void setStrNubanType(String strNubanType) {
		this.strNubanType = strNubanType;
	}
	public String getStrNubanTypeDescription() {
		return strNubanTypeDescription;
	}
	public void setStrNubanTypeDescription(String strNubanTypeDescription) {
		this.strNubanTypeDescription = strNubanTypeDescription;
	}
	
}
