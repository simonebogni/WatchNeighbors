package classes;

import org.openstreetmap.gui.jmapviewer.Coordinate;


/**
 * @author Mattia Nangeroni
 * @author Massimiliano Calicchio
 * @author Simone Bogni
 * Class that represents the client
 */
public class Client {
	private Person user=null;
	private ClientServerInterface server = null;

	private void initStub() {
		try{
			server = new ClientStub();
			System.out.println("Checking username beppe: "+server.verifyUsernamePresence("beppe"));
			System.out.println("Checking username paperino: "+server.verifyUsernamePresence("paperino"));
			System.out.println("Checking username trova: "+server.verifyUsernamePresence("trova"));
		} catch(Exception e){
			e.printStackTrace();
			System.out.println("Unable to instantiate Stub.");
		}
	}

	/**
	 * Constructor for the class Client
	 */
	public Client(){
		initStub();
	}

	/** Checks if the provided username is already in use
	 * @param username username to be verified
	 * @return true if the username is already in use, false otherwise
	 */
	public boolean verifyUsernamePresence(String username){
		return server.verifyUsernamePresence(username);
	}

	/**
	 * Retrieves the names of all the available cities
	 * @return String array with all the cities
	 */
	public String[] getCityList(){
		return server.getCityList();
	}
	
	/**
	 *  Updates the details of an user account
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
	public boolean updatePerson(String name,String surname,String userId, String eMail,String password,String city,String district,Double latitude,Double longitude){
		return server.updatePerson(name, surname, userId, eMail, password, city, district, latitude, longitude);
	}

	/**
	 * Retrieves the available district names for a given city
	 * @param city the city from which we need the districts
	 * @return String array with the district names
	 */
	public String[] getDistrictList(String city) {
		return server.getDistrictList(city);
	}


	/**
	 * Retrieves the reports still in progress for the given city and district
	 * @param city city
	 * @param district district
	 * @return Report array with the reports
	 */
	public Report[] getReportList(String city, String district) {
		return server.getReportList(city, district);
	}

	/**
	 * Retrieves the reports on which a specific user is working on.
	 * Said user is defined through its username.
	 * @param username the username of the user
	 * @return Report array with the reports the user is still working on
	 */
	public Report[] getReportListUserWorkInProgress(String username) {
		return server.getReportListUserWorkInProgress(username);
	}


	/**
	 * Stops the connection between client and server
	 */
	public void stop(){
		server.stop();
	}

	/** Deletes the account associated with the provided username
	 * @param username the username associated to the account
	 * @return true if the account has been successfully deleted, false otherwise
	 */
	public boolean deleteAccount(String username){
		return server.deleteAccount(username);
	}

	/**
	 * Get the Person associated to the current logged in user
	 * @return an instance of Person if the user is logged in, null otherwise
	 */
	protected Person getUser() {
		return user;
	}


	/**
	 * Login in with the provided username and password
	 * @param username username
	 * @param password passsword
	 * @return an instance of Person representing the user that has logged in successfully, null if the log in hasn't worked
	 */
	public Person login(String username, String password){
		return server.login(username, password);
	}

	/**Retrieves an array of Coordinates of the given city and district 
	 * where the first one is the North-West point
	 * and the second one is the South-East point
	 * @param city city
	 * @param district district
	 * @return Coordinate array with (in order) NW and SE points coordinates
	 */
	public Coordinate[] getCoordinates(String city, String district){
		Double[] pointsArray = server.getCoordinates(city, district);
		double nwLat = pointsArray[0];
		double nwLon = pointsArray[1];
		double seLat = pointsArray[2];
		double seLon = pointsArray[3];
		Coordinate nw = new Coordinate(nwLat, nwLon);
		Coordinate se = new Coordinate(seLat, seLon);
		return new Coordinate[]{nw, se};
	}

	/**
	 * Log out
	 */
	protected void logout(){
		user = null;
	}

	/**
	 * Requests an insertion of a given Person in the database
	 * @param p the person to be inserted
	 * @return true if the insertion was successful, false otherwise
	 */
	public boolean insertPerson(Person p) {
		return server.insertPerson(p);
	}

	/**
	 * Updates a given report, associating it to a user, of which the username is provided.
	 * If the operation is successfull, the user will have that report among those he's working on.
	 * @param report the report to be taken
	 * @param username the username of the user
	 * @return true if the update was successful, false otherwise
	 */
	public boolean takeReport(Report report, String username){
		return server.takeReport(report, username);
	}

	/**
	 * Closes permanently a report
	 * @param reportID the id of the report
	 * @param reportCause the initial cause for opening the report
	 * @param reportStatus the final status of the report
	 * @param username the username of that user that is closing the report
	 * @return true if operation was successful, false otherwise
	 */
	public boolean closeReport(String reportID, String reportCause, String reportStatus, String username){
		return server.closeReport(reportID, reportCause, reportStatus, username);
	}

	/**
	 * Insert the specified report in the database
	 * @param report report to be inserted
	 * @return true if operation was successful, false otherwise
	 */
	public boolean insertReport(Report report) {
		return server.insertReport(report);
	}
}
