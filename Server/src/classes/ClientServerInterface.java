package classes;

/**
 * @author Mattia Nangeroni
 * @author Massimiliano Calicchio
 * @author Simone Bogni
 *
 * Interface that defines the methods shared between client-side and server-side classes
 */
public interface ClientServerInterface {
	
	/** Checks if the provided username is already in use
	 * @param username username to be verified
	 * @return true if the username is already in use, false otherwise
	 */
	public boolean verifyUsernamePresence(String username);
	
	/**
	 * Updates the details of an user account
	 * @param name new name, or old if not changed
	 * @param surname new surname, or old one if not changed
	 * @param userId new username, or old one if not changed
	 * @param eMail new email address, or old one if not changed
	 * @param password new password, or old one if not changed
	 * @param city new city, or old one if not changed
	 * @param district new district, or old one if not changed
	 * @param latitude new latitude, or old one if not changed
	 * @param longitude new longitude, or old one if not changed
	 * @return true if the update was successful, false otherwise
	 */
	public boolean updatePerson(String name,String surname,String userId, String eMail,String password,String city,String district,Double latitude,Double longitude);
	
	/**
	 * Retrieves the names of all the available cities
	 * @return String array with all the cities
	 */
	public String[] getCityList();
	
	/**
	 * Retrieves the available district names for a given city
	 * @param city the city from which we need the districts
	 * @return String array with the district names
	 */
	public String[] getDistrictList(String city);
	
	/**
	 * Retrieves the reports still in progress for the given city and district
	 * @param city city
	 * @param district district
	 * @return Report array with the reports
	 */
	public Report[] getReportList(String city, String district);
	
	/**Retrieves an array of Double of the given city and district where:
	 * the first Double is the North-West point latitude,
	 * the second Double is the North-West point longitude,
	 * the third Double is the South-East point latitude,
	 * the fourth Double is the South-East point longitude
	 * @param city city
	 * @param district district
	 * @return Double array with the latitudes and the longitudes of the NW and SE points
	 */
	public Double[] getCoordinates(String city, String district);
	
	/**
	 * Stops the connection between client and server
	 */
	public void stop();
	
	/**
	 * Retrieves the reports on which a specific user is working on.
	 * Said user is defined through its username.
	 * @param username the username of the user
	 * @return Report array with the reports the user is still working on
	 */
	public Report[] getReportListUserWorkInProgress(String username);
	
	/** Deletes the account associated with the provided username
	 * @param username the username associated to the account
	 * @return true if the account has been successfully deleted, false otherwise
	 */
	public boolean deleteAccount(String username);
	
	/**
	 * Login in with the provided username and password
	 * @param username username
	 * @param password passsword
	 * @return an instance of Person representing the user that has logged in successfully, null if the log in hasn't worked
	 */
	public Person login(String username, String password);
	
	/**
	 * Requests an insertion of a given Person in the database
	 * @param p the person to be inserted
	 * @return true if the insertion was successful, false otherwise
	 */
	public boolean insertPerson(Person p);
	
	/**
	 * Updates a given report, associating it to a user, of which the username is provided.
	 * If the operation is successfull, the user will have that report among those he's working on.
	 * @param report the report to be taken
	 * @param username the username of the user
	 * @return true if the update was successful, false otherwise
	 */
	public boolean takeReport(Report report, String username);
	
	/**
	 * Closes permanently a report
	 * @param reportID the id of the report
	 * @param reportCause the initial cause for opening the report
	 * @param reportStatus the final status of the report
	 * @param username the username of that user that is closing the report
	 * @return true if operation was successful, false otherwise
	 */
	public boolean closeReport(String reportID, String reportCause, String reportStatus, String username);
	
	/**
	 * Insert the specified report in the database
	 * @param report report to be inserted
	 * @return true if operation was successful, false otherwise
	 */
	public boolean insertReport(Report report);
}
