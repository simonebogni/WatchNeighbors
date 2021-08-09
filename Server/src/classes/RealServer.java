package classes;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;

/**
 * @author Mattia Nangeroni
 * @author Massimiliano Calicchio
 * @author Simone Bogni
 *
 * The RealServer constitutes the real server that answers to a specific client.
 */
public class RealServer implements ClientServerInterface {
	private QueryExecutor queryExecutor;
	
	/**
	 * Constructor for the class RealServer
	 */
	public RealServer(){
		try {
			queryExecutor = new QueryExecutor(ApplicationParameters.getDbName(), ApplicationParameters.getDbUser(), ApplicationParameters.getDbPsw());
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/* (non-Javadoc)
	 * @see classes.ClientServerInterface#verifyUsernamePresence(java.lang.String)
	 */
	@Override
	public boolean verifyUsernamePresence(String username) {
		Person person;
		try {
			person = queryExecutor.get_person(username);
			if(person == null){
				return false;
			}
			return true;
		} catch (SQLException e) {
			// In case of exception is better to return true, so the user won't be able to use the UserId
			e.printStackTrace();
			return true;
		}
	}

	/* (non-Javadoc)
	 * @see classes.ClientServerInterface#getCityList()
	 */
	@Override
	public String[] getCityList() {
		try {
			String[] cities = queryExecutor.get_cities();
			return cities;
		} catch (SQLException e) {
			e.printStackTrace();
			return new String[]{};
		}
	}

	/* (non-Javadoc)
	 * @see classes.ClientServerInterface#getDistrictList(java.lang.String)
	 */
	@Override
	public String[] getDistrictList(String city) {
		try {
			String[] districts = queryExecutor.get_district(city);
			return districts;
		} catch (SQLException e) {
			e.printStackTrace();
			return new String[]{};
		}
	}

	/* (non-Javadoc)
	 * @see classes.ClientServerInterface#getReportList(java.lang.String, java.lang.String)
	 */
	@Override
	public Report[] getReportList(String city, String district) {
		try {
			ArrayList<Report> reports = queryExecutor.get_reports_city_district(city, district);
			return reports.stream().toArray(Report[]::new);
		} catch (SQLException e) {
			e.printStackTrace();
			return new Report[]{};
		}
	}

	/* (non-Javadoc)
	 * @see classes.ClientServerInterface#stop()
	 */
	@Override
	public void stop() {
		System.out.println("Stopping real server...");

	}
	
	private boolean sendOutWelcomeMail(Person person){
		final String name = person.getName();
		final String surname = person.getSurname();
		final String email = person.getEmail();
		final String userID = person.getUserID();
		final String city = person.getCity();
		final String district = person.getDistrict();
		final double lat = person.getLatitude();
		final double lon = person.getLongitude();
		final String message = "<html>"
				+ "<div>"
				+ "<div style='display:flex;justify-content:center;align-items:center;border: 2px solid OrangeRed; padding: 10px 40px; background: yellow;border-radius: 25px;'>"
				+ "<span><img src='https://simonebogni.thecompletewebhosting.com/websites/neighbours_logo.gif' alt='logo' style='vertical-align: middle'>"
				+ "<h1 style='display:inline;color:green;font-family:\"Comic Sans MS\"'>&nbsp;WatchNeighbors</h1></span></div>"
				+ "<div style='border: 2px solid OrangeRed; padding: 10px 40px; background: yellow;border-radius: 25px;'>"
				+ "<strong><h2 style='color:blue;font-family:\"Comic Sans MS\"'>Welcome in WatchNeighbors!</h2></strong><br>"
				+ "<p style='font-family:\"Comic Sans MS\";color:OrangeRed;'>Hi <strong>"+name+" "+surname+"</strong>,<br>"
				+ "we are very happy that you have decided to join the WatchNeighbors program.</p>"
				+ "<p style='font-family:\"Lucida Sans Unicode\";color:OrangeRed;'>Following here, you'll find the details of your newly created account:</p>"
				+ "<p  style='font-family:\"Lucida Sans Unicode\";color:OrangeRed;'>"
				+ "<ul  style='font-family: \"Lucida Sans Unicode\";color:OrangeRed;'>"
				+ "<li><strong>Name</strong> : "+name+"</li>"
				+ "<li><strong>Surname</strong> : "+surname+"</li>"
				+ "<li><strong>Username</strong> : "+userID+"</li>"
				+ "<li><strong>Email address</strong> : "+email+"</li>"
				+ "<li><strong>City</strong> : "+city+"</li>"
				+ "<li><strong>District</strong> : "+district+"</li>"
				+ "<li><strong>Latitude</strong> : "+lat+"</li>"
				+ "<li><strong>Longitude</strong> : "+lon+"</li>"
				+ "</ul>"
				+ "</p>"
				+ "<p style='font-family:\"Lucida Sans Unicode\";color:OrangeRed;'>Thank you for making your neighborhood a better place.</p><br>"
				+ "<p style='font-family:\"Lucida Sans Unicode\";color:blue;'><i>The WatchNeighbors' staff</i><p>"
				+ "</div>"
				+ "</div>"
				+ "</html>";
		//true if mail sent successfully, false otherwise
		return MailSender.sendMail(email, "Welcome to WatchNeighbors!", message, true);
	}

	/* (non-Javadoc)
	 * @see classes.ClientServerInterface#getCoordinates(java.lang.String, java.lang.String)
	 */
	@Override
	public Double[] getCoordinates(String city, String district) {
		try {
			double[][] pointsArray = queryExecutor.get_coordinates(city, district);
			Double point1 = pointsArray[0][0];
			Double point2 = pointsArray[0][1];
			Double point3 = pointsArray[0][2];
			Double point4 = pointsArray[0][3];
			return new Double[]{point1, point2, point3, point4};
		} catch (SQLException e) {
			e.printStackTrace();
			return new Double[]{0.0, 0.0, 0.0, 0.0};
		}
	}

	/* (non-Javadoc)
	 * @see classes.ClientServerInterface#getReportListUserWorkInProgress(java.lang.String)
	 */
	@Override
	public Report[] getReportListUserWorkInProgress(String username) {
		try {
			ArrayList<Report> reports = queryExecutor.get_reports_from_userWorkInProgress(username);
			return reports.stream().toArray(Report[]::new);
		} catch (SQLException e) {
			e.printStackTrace();
			return new Report[]{};
		}
	}

	/* (non-Javadoc)
	 * @see classes.ClientServerInterface#deleteAccount(java.lang.String)
	 */
	@Override
	public boolean deleteAccount(String username) {
		try {
			queryExecutor.delete_from_persons(username);
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	/* (non-Javadoc)
	 * @see classes.ClientServerInterface#login(java.lang.String, java.lang.String)
	 */
	@Override
	public Person login(String username, String password) {
		Person person;
		try {
			person = queryExecutor.get_person(username);
			if(person != null && (person.getPassword().equals(password) && person.getUserID().equals(username))){
				
			} else {
				//set person to null in case the password is wrong
				person = null;
			}
		} catch (SQLException e) {
			person = null;
			e.printStackTrace();
		}
		
		return person;
	}

	/* (non-Javadoc)
	 * @see classes.ClientServerInterface#insertPerson(classes.Person)
	 */
	@Override
	public boolean insertPerson(Person p) {
		try {
			queryExecutor.insert_into_persons(p);
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
		if(!sendOutWelcomeMail(p)){
			System.out.println("Unable to send mail to "+p.getEmail());
		}
		return true;
	}

	/* (non-Javadoc)
	 * @see classes.ClientServerInterface#takeReport(classes.Report, java.lang.String)
	 */
	@Override
	public boolean takeReport(Report report, String username) {
		try {
			queryExecutor.update_reports(String.valueOf(report.getReportID()), report.getReportCause(), "Taken", username, null);;
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	/* (non-Javadoc)
	 * @see classes.ClientServerInterface#closeReport(java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public boolean closeReport(String reportID, String reportCause, String reportStatus, String username) {
		try {
			queryExecutor.update_reports(reportID, reportCause, reportStatus, username, Timestamp.from(Instant.now()));;
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	/* (non-Javadoc)
	 * @see classes.ClientServerInterface#insertReport(classes.Report)
	 */
	@Override
	public boolean insertReport(Report report) {
		try {
			queryExecutor.insert_into_reports(report);
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	/* (non-Javadoc)
	 * @see classes.ClientServerInterface#updatePerson(java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.Double, java.lang.Double)
	 */
	@Override
	public boolean updatePerson(String name, String surname, String userId, String eMail, String password, String city,
			String district, Double latitude, Double longitude) {
		try {
			queryExecutor.update_persons(name, surname, userId, eMail, password, city, district, latitude, longitude);;
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

}
