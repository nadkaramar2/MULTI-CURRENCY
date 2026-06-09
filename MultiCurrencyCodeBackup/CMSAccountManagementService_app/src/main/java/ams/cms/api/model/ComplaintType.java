package ams.cms.api.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
//created by ankit
@Entity
@Table(name = "complaint_type")
public class ComplaintType implements Serializable {
	private static final long serialVersionUID = 5459581698798549309L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(columnDefinition = "TEXT", name = "id", unique = true)
	private long strId;

	@Column(columnDefinition = "TEXT", name = "complaint_type")
	private String strComplaintType;

	public long getStrId() {
		return strId;
	}

	public void setStrId(long strId) {
		this.strId = strId;
	}

	public String getStrComplaintType() {
		return strComplaintType;
	}

	public void setStrComplaintType(String strComplaintType) {
		this.strComplaintType = strComplaintType;
	}
}
