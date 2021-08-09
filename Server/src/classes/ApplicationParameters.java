package classes;

import java.awt.Color;

/**
 * @author Mattia Nangeroni
 * @author Massimiliano Calicchio
 * @author Simone Bogni
 *
 * A class that stores the parameters of the application
 */
public class ApplicationParameters {
	private static String serverAddress="127.0.0.1";
	private static final int PORT = 9999;
	private static final String DB_NAME = "jdbc:mysql://www.simonebogni.thecompletewebhosting.com:3306/simonebo_watchNeighbors";
	private static final String DB_USER = "simonebo_watNeig";
	private static final String DB_PSW = "12345Aa!";

	private static final String[] REPORT_CAUSES = {"Car alarm ringing", "Graffiti", "House alarm ringing", "Noise pollution", "Sales representative at the doorstep", "Suspicious person", "Suspicious sounds", "Theft", "VERY untuned person at karaoke"};
	private static final String[] REPORT_STATUS = {"Police intervention", "False alarm", "Situation cleared", "Sales representatives gone away", "Suspicious person gone away"};
	
	//Colors for markers
	private static final Color colorDefault = Color.GREEN;
	private static final Color colorTheft = Color.ORANGE;
	private static final Color colorCarAlarm = Color.YELLOW;
	private static final Color colorSuspiciousPerson = Color.PINK;
	private static final Color colorGraffiti = Color.CYAN;
	private static final Color colorHouseAlarm = new Color(150, 0, 24);
	private static final Color colorNoisePollution = new Color(144, 132, 53);
	private static final Color colorSalesRepresentative = new Color(153, 102, 204);
	private static final Color colorSuspiciousSounds = new Color(0, 47, 167);
	private static final Color colorUntunedPerson = new Color(150, 75, 0);



	/**
	 * Returns the color for the marker depending on the report cause
	 * @param cause the cause for which the report has been created
	 * @return the color for the marker
	 */
	public static Color getColor(String cause){
		switch(cause){
		case "Car alarm ringing":
			return colorCarAlarm;
			
		case "Graffiti":
			return colorGraffiti;
			
		case "House alarm ringing":
			return colorHouseAlarm;
			
		case "Noise pollution": 
			return colorNoisePollution; 
			 
		case "Sales representative at the doorstep": 
			return colorSalesRepresentative; 
			 
		case "Suspicious person": 
			return colorSuspiciousPerson; 
			 
		case "Suspicious sounds": 
			return colorSuspiciousSounds; 
			 
		case "Theft": 
			return colorTheft; 
			 
		case "VERY untuned person at karaoke":
			return colorUntunedPerson;
			
		default:
			return colorDefault;
		}
	}
	
	
	
	/**
	 * Get the server address
	 * @return address of the server
	 */
	public static String getServerAddress() {
		return serverAddress;
	}

	/**
	 * Get the port of the server
	 * @return port of the server
	 */
	public static int getPort() {
		return PORT;
	}

	
	/**
	 * Get the name of the database
	 * @return database name
	 */
	public static String getDbName() {
		return DB_NAME;
	}

	/**
	 * Get the username of the database user
	 * @return database user's username
	 */
	public static String getDbUser() {
		return DB_USER;
	}

	/**
	 * Get the password of the database user
	 * @return database user's password
	 */
	public static String getDbPsw() {
		return DB_PSW;
	}

	/**
	 * Get the causes to open a report in the form of a String array
	 * @return String array with the causes
	 */
	public static String[] getReportCauses() {
		return REPORT_CAUSES;
	}

	
	/**
	 * Get the statuses for the closure of a report in the form of a String array
	 * @return String array with the statuses
	 */
	public static String[] getReportStatus(){
		return REPORT_STATUS;
	}
}
