package ams.cms.notification;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class EmailTemplate implements Serializable 
{
	private static final long serialVersionUID = 1L;
	
	private String strFrom;
	private String strTo;
	private String strCc;
	private String strBcc;
	private String strSubject;
	private String strText;
	private String strPathToAttachment;
	private ArrayList<String> strToList = new ArrayList<String>();
	private List<String> strCcList = new ArrayList<String>();
	private List<String> strBccList = new ArrayList<String>();
	
	private String strParticipantid;
	
	public String getStrFrom() {
		return strFrom;
	}
	public void setStrFrom(String strFrom) {
		this.strFrom = strFrom;
	}
	public String getStrTo() {
		return strTo;
	}
	public void setStrTo(String strTo) {
		this.strTo = strTo;
	}
	public String getStrSubject() {
		return strSubject;
	}
	public void setStrSubject(String strSubject) {
		this.strSubject = strSubject;
	}
	public String getStrText() {
		return strText;
	}
	public void setStrText(String strText) {
		this.strText = strText;
	}	
	public String getStrPathToAttachment() {
		return strPathToAttachment;
	}
	public void setStrPathToAttachment(String strPathToAttachment) {
		this.strPathToAttachment = strPathToAttachment;
	}
	public ArrayList<String> getStrToList() {
		return strToList;
	}
	public void setStrToList(ArrayList<String> strToList) {
		this.strToList = strToList;
	}
	public List<String> getStrCcList() {
		return strCcList;
	}
	public void setStrCcList(List<String> strCcList) {
		this.strCcList = strCcList;
	}
	public List<String> getStrBccList() {
		return strBccList;
	}
	public void setStrBccList(List<String> strBccList) {
		this.strBccList = strBccList;
	}
	public String getStrCc() {
		return strCc;
	}
	public void setStrCc(String strCc) {
		this.strCc = strCc;
	}
	public String getStrBcc() {
		return strBcc;
	}
	public void setStrBcc(String strBcc) {
		this.strBcc = strBcc;
	}
	public String getStrParticipantid() {
		return strParticipantid;
	}
	public void setStrParticipantid(String strParticipantid) {
		this.strParticipantid = strParticipantid;
	}
}
