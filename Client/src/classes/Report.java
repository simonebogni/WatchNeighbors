/**
 * 
 */
package classes;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Calendar;

/**
 * @author Mattia Nangeroni
 * @author Massimiliano Calicchio
 * @author Simone Bogni
 *
 * An instance of the class Report represents a report of things went wrong in a district of a city
 */
public class Report implements Serializable {
	private static final long serialVersionUID = 6706796893663298332L;
	private String city;
	private String district;
	private int reportID;
	private String userID;
	private double userLatitude;
	private double userLongitude;
	private double reportLatitude;
	private double reportLongitude;
	private Timestamp timestamp;
	private String reportCause;
	private String reportStatus;
	private String userWorkInProgress;
	private Timestamp closingDate;

	/**
	 * Basic constructor of Report.
	 */
	public Report(){
		//returns current timestamp
		Calendar calendar = Calendar.getInstance();
		java.util.Date now = calendar.getTime();
		this.timestamp = new  Timestamp(now.getTime());

		this.reportID = 0;
		this.reportStatus = "";
		this.userWorkInProgress = "";
		this.closingDate = null;
	}

	/**
	 * Advanced constructor of Report
	 * @param city the report city
	 * @param district the report district
	 * @param userID the username of the person that has created the report
	 * @param userLatitude the latitude of the person that has created the report
	 * @param userLongitude the longitude  of the person that has created the report
	 * @param reportLatitude the latitude of the report
	 * @param reportLongitude the longitude of the report
	 * @param reportCause the cause for which the report has been created
	 */
	public Report(String city,
			String district,
			String userID,
			double userLatitude,
			double userLongitude,
			double reportLatitude,
			double reportLongitude,
			String reportCause
			){
		//returns current timestamp
		Calendar calendar = Calendar.getInstance();
		java.util.Date now = calendar.getTime();
		this.timestamp = new  Timestamp(now.getTime());

		this.reportID = 0;
		this.reportStatus = "Open";
		this.userWorkInProgress = null;
		this.closingDate = null;
		this.city = city;
		this.district = district;
		this.userID = userID;
		this.userLatitude = userLatitude;
		this.userLongitude = userLongitude;
		this.reportLatitude = reportLatitude;
		this.reportLongitude = reportLongitude;
		this.reportCause = reportCause; 
	}

	/** It creates the report given an array of String
	 * @param stringArray
	 * @return
	 */
	public static Report ReportFactory(String[] stringArray){
		if(stringArray.length==13){
			Report rpt = new Report();
			rpt.city=stringArray[0];
			rpt.district=stringArray[1];
			rpt.reportID=Integer.parseInt(stringArray[2]);
			rpt.userID=stringArray[3];
			rpt.userLatitude=Double.parseDouble(stringArray[4]);
			rpt.userLongitude=Double.parseDouble(stringArray[5]);
			rpt.reportLatitude=Double.parseDouble(stringArray[6]);
			rpt.reportLongitude=Double.parseDouble(stringArray[7]);
			rpt.timestamp = Timestamp.valueOf(stringArray[8]);
			rpt.reportCause=stringArray[9];
			rpt.reportStatus=stringArray[10];
			rpt.userWorkInProgress=stringArray[11];
			//se il campo nel db  null, la stringa stringArray[12] è "null"
			rpt.closingDate = (stringArray[12]==null || stringArray[12].equals("") || stringArray[12].equals("null")) ? null : Timestamp.valueOf(stringArray[12]);
			return rpt;
		} else {
			return new Report();
		}
	}



	/**
	 * @return the city of the report
	 */
	public String getCity() {
		return city;
	}

	/**
	 * @return the district
	 */
	public String getDistrict() {
		return district;
	}

	/**
	 * @return the id of the report
	 */
	public int getReportID() {
		return reportID;
	}

	/**
	 * @return the username of the person that has created the report
	 */
	public String getUserID() {
		return userID;
	}

	/**
	 * @return the latitude of the person that has created the report
	 */
	public double getUserLatitude() {
		return userLatitude;
	}

	/**
	 * @return the longitude of the person that has created the report
	 */
	public double getUserLongitude() {
		return userLongitude;
	}

	/**
	 * @return the longitude of the report
	 */
	public double getReportLatitude() {
		return reportLatitude;
	}

	/**
	 * @return the longitude of the report
	 */
	public double getReportLongitude() {
		return reportLongitude;
	}

	/**
	 * @return the Timestamp representing the creation date of the report
	 */
	public Timestamp getTimestamp() {
		return timestamp;
	}

	/**
	 * @return the cause for which the report has been created
	 */
	public String getReportCause() {
		return reportCause;
	}

	/**
	 * @return the actual status of the report
	 */
	public String getReportStatus() {
		return reportStatus;
	}

	/**
	 * @return the user who is currently checking the report
	 */
	public String getUserWorkInProgress() {
		return userWorkInProgress;
	}


	/**
	 * @return the Timestamp representing the closing date of the report
	 */
	protected Timestamp getClosingDate() {
		return closingDate;
	}

}
