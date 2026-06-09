package ams.cms.scheduler.schedule;

import java.io.Serializable;
import java.util.Date;

public class SchedularModel implements Serializable 
{
	private static final long serialVersionUID = 1L;
	
	private String scheduleId;
	private String scheduleParam;
	private String scheduleName;
	private String scheduleDate;
	private String scheduleTime;
	private String schedularBeanName;
	private String schedularBeanMethod;
	
	private Date updatedScheduleDate;
	
	public String getScheduleId() {
		return scheduleId;
	}
	public void setScheduleId(String scheduleId) {
		this.scheduleId = scheduleId;
	}
	public String getScheduleParam() {
		return scheduleParam;
	}
	public void setScheduleParam(String scheduleParam) {
		this.scheduleParam = scheduleParam;
	}
	public String getScheduleName() {
		return scheduleName;
	}
	public void setScheduleName(String scheduleName) {
		this.scheduleName = scheduleName;
	}
	public String getScheduleTime() {
		return scheduleTime;
	}
	public void setScheduleTime(String scheduleTime) {
		this.scheduleTime = scheduleTime;
	}
	public String getScheduleDate() {
		return scheduleDate;
	}
	public void setScheduleDate(String scheduleDate) {
		this.scheduleDate = scheduleDate;
	}
	
	public Date getUpdatedScheduleDate() {
		return updatedScheduleDate;
	}
	public void setUpdatedScheduleDate(Date updatedScheduleDate) {
		this.updatedScheduleDate = updatedScheduleDate;
	}
	public String getSchedularBeanName() {
		return schedularBeanName;
	}
	public void setSchedularBeanName(String schedularBeanName) {
		this.schedularBeanName = schedularBeanName;
	}
	public String getSchedularBeanMethod() {
		return schedularBeanMethod;
	}
	public void setSchedularBeanMethod(String schedularBeanMethod) {
		this.schedularBeanMethod = schedularBeanMethod;
	}
	@Override
	public String toString() {
		return "SchedularModel [scheduleParam=" + scheduleParam + ", scheduleName=" + scheduleName + ", scheduleTime="
				+ scheduleTime + "]";
	}
}
