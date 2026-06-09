package ams.cms.utility;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.stereotype.Component;

@Component
public class ProjectFormats {

	public static String DateAndHoursFormat() {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = new Date();
		String formattedDate = dateFormat.format(date);
		return formattedDate;
	}

	public static Date getCurrentDateFormat() {
		Date date = new Date();
		return date;
	}
	
	public static String generateComplaintId(int num) {
		String complaintFormat = String.format("%05d", num);
		String complaintId = ("C" + complaintFormat);
		return complaintId;
	}
}
