package classes;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Properties;

/**
 * @author Mattia Nangeroni
 * @author Massimiliano Calicchio
 * @author Simone Bogni
 * The QueryExecutor class contains all the required methods for the interaction with the database.
 * An instance of this class is a connection with the database specified during the creation
 */

public class QueryExecutor {
	
private Connection con;
	
	
	protected Connection getCon() {
		return con;
	}

	private static void printSQLException(SQLException ex){
		System.err.println("SQLState:"+ ex.getSQLState());
		System.err.println("Error code:"+ ex.getErrorCode());
		System.err.println("Message:"+ ex.getMessage());
		
	}
	
	private Connection openConnection(String url,String usr, String pwd) throws SQLException{
		Properties props= new Properties();
		props.setProperty("user", usr);
		props.setProperty("password", pwd);
		
		Connection conn= DriverManager.getConnection(url, props);
		return conn;
	}
	
	/**
	 * It creates an object of class QueryExecutor and opens a connection with the DB corresponding to the parameters given as arguments
	 * @param url the URL of the db
	 * @param usr the username to use for the connection
	 * @param pwd the password to use to have access to the DB
	 * @throws SQLException
	 */
	public QueryExecutor(String url, String usr, String pwd) throws SQLException{
		con=openConnection(url,usr,pwd);
		System.out.println("Connesso al db "+con.getCatalog());
	}
	
	/**
	 * Inserts the specified Person in the database
	 * @param p Person to be inserted in the DB
	 * @throws SQLException
	 */
	public void insert_into_persons(Person p) throws SQLException {
		PreparedStatement p_stmt=null;
		try{
			p_stmt=con.prepareStatement(PredefinedSQLCode.insert_table_queries[0]);
			p_stmt.setString(1, p.getName());
			p_stmt.setString(2, p.getSurname());
			p_stmt.setString(3, p.getUserID());
			p_stmt.setString(4,p.getEmail());
			p_stmt.setString(5,p.getPassword());
			p_stmt.setString(6,p.getCity());
			p_stmt.setString(7,p.getDistrict());
			p_stmt.setDouble(8, p.getLatitude());
			p_stmt.setDouble(9, p.getLongitude());
			p_stmt.executeUpdate();
			
		}catch(SQLException e){
			QueryExecutor.printSQLException(e);
		}finally{
			  if(p_stmt!=null){
				p_stmt.close();
			  }
		 }
	}
	
	/**
	 * Inserts the specified Report in the database
	 * @param r the Report to insert
	 * @throws SQLException
	 */
	public void insert_into_reports(Report r) throws SQLException {
		PreparedStatement p_stmt=null;
		try{
			p_stmt=con.prepareStatement(PredefinedSQLCode.insert_table_queries[1]);
			p_stmt.setString(1, r.getCity());
			p_stmt.setString(2,r.getDistrict());
			p_stmt.setInt(3, r.getReportID());
			p_stmt.setString(4, r.getUserID());
			p_stmt.setDouble(5,r.getReportLatitude());
			p_stmt.setDouble(6,r.getReportLongitude());
			p_stmt.setTimestamp(7, r.getTimestamp());
			p_stmt.setString(8, r.getReportCause());
			p_stmt.setString(9, r.getReportStatus());
			p_stmt.setString(10,r.getUserWorkInProgress());
			p_stmt.setTimestamp(11, null);
			p_stmt.executeUpdate();
			
			
		}catch(SQLException e){
			QueryExecutor.printSQLException(e);
		}finally{
			if(p_stmt!=null){
				p_stmt.close();
			}
			
	    }
	}
	
	/**
	 * Erases from the DB the Person with the username equals to the one provided
	 * @param username username of the Person to erase
	 * @throws SQLException
	 */
	public void delete_from_persons(String username) throws SQLException {
		PreparedStatement p_stmt=null;
		try{
			p_stmt= con.prepareStatement(PredefinedSQLCode.delete_queries[0]);
			p_stmt.setString(1, username);
			p_stmt.executeUpdate();
		
		}catch(SQLException e){
			QueryExecutor.printSQLException(e);
		}finally{
			if(p_stmt!=null){
				p_stmt.close();
			}
		}
	}
	
	/**
	 * Erases from the database the given Report
	 * @param r Report to erase
	 * @throws SQLException
	 */
	public void delete_from_reports(Report r) throws SQLException {
		PreparedStatement p_stmt=null;
		try{
			p_stmt= con.prepareStatement(PredefinedSQLCode.delete_queries[1]);
			p_stmt.setInt(1, r.getReportID());
			p_stmt.executeUpdate();
		
		}catch(SQLException e){
			QueryExecutor.printSQLException(e);
		}finally{
			if(p_stmt!=null){
				p_stmt.close();
			}
		}
	}
	
	/**
	 * Returns an ArrayList containing all the rows available in the table Persons
	 * @return a list of Person
	 * @throws SQLException
	 */
	public ArrayList<Person> get_persons() throws SQLException {
		ArrayList<Person> listPersons = new ArrayList<>();
		Statement stmt=null;
		try{
			stmt= con.createStatement();
			ResultSet rs=stmt.executeQuery(PredefinedSQLCode.get_all_row_queries[0]);
			String name,surname,userId,eMail,password,city,district;
			Double latitude,longitude;
		
			while(rs.next()){
				name=rs.getString(1);
				surname=rs.getString(2);
				userId=rs.getString(3);
				eMail=rs.getString(4);
				password=rs.getString(5);
				city=rs.getString(6);
				district=rs.getString(7);
				latitude=rs.getDouble(8);
				longitude=rs.getDouble(9);
				Person p=new Person(name, surname, userId, eMail, password, city, district, latitude, longitude);
				listPersons.add(p);
			}
			
		}catch(SQLException e){
			e.printStackTrace();
			printSQLException(e);
		}
		finally{
			if(stmt!=null){
				stmt.close();
			}
		}
		return listPersons;
	}
	
	/**
	 * Returns the Person with the username specified
	 * @param uId username
	 * @return the Person corresponding to the given username, null if such Person has not been found
	 * @throws SQLException
	 */
	public Person get_person(String uId) throws SQLException {
		PreparedStatement p_stmt=null;
		String name,surname,userId,eMail,password,city,district;
		Double latitude,longitude;
		Person p=null;
		try{
			p_stmt= con.prepareStatement(PredefinedSQLCode.person_userId_query[0]);
			p_stmt.setString(1,uId);
			ResultSet rs = p_stmt.executeQuery();
			while(rs.next()){
				name=rs.getString(1);
				surname=rs.getString(2);
				userId=rs.getString(3);
				eMail=rs.getString(4);
				password=rs.getString(5);
				city=rs.getString(6);
				district=rs.getString(7);
				latitude=rs.getDouble(8);
				longitude=rs.getDouble(9);
				p=new Person(name, surname, userId, eMail, password, city, district, latitude, longitude);
		    }
			
		
		}catch(SQLException e){
			QueryExecutor.printSQLException(e);
		}finally{
			if(p_stmt!=null){
				p_stmt.close();
			}
		}
		return p;
	}
	
	
	/**
	 * Returns an ArrayList containing all the Reports not yet closed for a given city and district
	 * @param city city
	 * @param district district
	 * @return a list of all the Reports still open
	 * @throws SQLException
	 */
	public ArrayList<Report> get_reports_city_district(String city,String district) throws SQLException{
		ArrayList<Report> listReports=new ArrayList<>();
		PreparedStatement p_stmt=null;
		String[] stringArray=new String[13];
		double userLatitude,userLongitude;
		try{
			p_stmt= con.prepareStatement(PredefinedSQLCode.report_city_district_query[0]);
			p_stmt.setString(1, city);
			p_stmt.setString(2, district);
			ResultSet rs=p_stmt.executeQuery();
			while(rs.next()){
				userLatitude=rs.getDouble(1);
		    	userLongitude=rs.getDouble(2);
		    	stringArray[0]=rs.getString("Reports.city");
		    	stringArray[1]=rs.getString("Reports.district");
		    	stringArray[2]=String.valueOf(rs.getInt("reportId"));
		    	stringArray[3]=rs.getString("Reports.userId");
		    	stringArray[4]=String.valueOf(userLatitude);
		    	stringArray[5]=String.valueOf(userLongitude);
		    	stringArray[6]=String.valueOf(rs.getDouble("reportLatitude"));
		    	stringArray[7]=String.valueOf(rs.getDouble("reportLongitude"));
		    	stringArray[8]=String.valueOf(rs.getTimestamp("timeStamp"));
		    	stringArray[9]=rs.getString("reportCause");
		    	stringArray[10]=rs.getString("reportStatus");
		    	stringArray[11]=rs.getString("userWorkInProgress");
		    	stringArray[12]=(rs.getTimestamp("closingDate")==null) ? "" : String.valueOf(rs.getTimestamp("closingDate"));
		    	
		        Report r=Report.ReportFactory(stringArray);
		    	listReports.add(r);
				
			}
			
		}catch(SQLException e){
			e.printStackTrace();
			printSQLException(e);
		}
		finally{
			if(p_stmt!=null){
				p_stmt.close();
			}
		}
		return listReports;
		
	}
	
	
	/**
	 * Returns an ArrayList containing all the closed Reports
	 * @return a list with the closed Reports
	 * @throws SQLException
	 */
	public ArrayList<Report> get_reports_over() throws SQLException {
		ArrayList<Report> listReportsOver=new ArrayList<>();
		Statement stmt=null;
		try{
		    stmt=con.createStatement();
		    ResultSet rs=stmt.executeQuery(PredefinedSQLCode.reports_over_query[0]);
		    double userLatitude,userLongitude;
		    String[] stringArray=new String[13];
		    while(rs.next()){
		    	userLatitude=rs.getDouble(1);
		    	userLongitude=rs.getDouble(2);
		    	stringArray[0]=rs.getString("Reports.city");
		    	stringArray[1]=rs.getString("Reports.district");
		    	stringArray[2]=String.valueOf(rs.getInt("reportId"));
		    	stringArray[3]=rs.getString("Reports.userId");
		    	stringArray[4]=String.valueOf(userLatitude);
		    	stringArray[5]=String.valueOf(userLongitude);
		    	stringArray[6]=String.valueOf(rs.getDouble("reportLatitude"));
		    	stringArray[7]=String.valueOf(rs.getDouble("reportLongitude"));
		    	stringArray[8]=String.valueOf(rs.getTimestamp("timeStamp"));
		    	stringArray[9]=rs.getString("reportCause");
		    	stringArray[10]=rs.getString("reportStatus");
		    	stringArray[11]=rs.getString("userWorkInProgress");
		    	stringArray[12]=String.valueOf(rs.getTimestamp("closingDate"));
		    	
		    	Report r=Report.ReportFactory(stringArray);
		    	listReportsOver.add(r);
		    }
		    
		}catch(SQLException e){
			e.printStackTrace();
			printSQLException(e);
		}
		finally{
			if(stmt!=null){
				stmt.close();
			}
		}
		return listReportsOver;
	}
	
	/**
	 * It return an ArrayList with the Reports that are currently being worked on by a certain Person, specified by its username
	 * @param userId username of the Person whose Reports are needed 
	 * @return an ArrayList containing the Reports
	 * @throws SQLException
	 */
	public ArrayList<Report> get_reports_from_userWorkInProgress(String userId) throws SQLException{
		ArrayList<Report> reportsList=new ArrayList<>();
		PreparedStatement p_stmt=null;
		try{
			p_stmt=con.prepareStatement(PredefinedSQLCode.reports_from_userWorkInProgress_query[0]);
			p_stmt.setString(1, userId);
			ResultSet rs=p_stmt.executeQuery();
		    double userLatitude,userLongitude;
			String[] stringArray=new String[13];
			while(rs.next()){
				userLatitude=rs.getDouble(1);
		    	userLongitude=rs.getDouble(2);
		    	stringArray[0]=rs.getString("Reports.city");
		    	stringArray[1]=rs.getString("Reports.district");
		    	stringArray[2]=String.valueOf(rs.getInt("reportId"));
		    	stringArray[3]=rs.getString("Reports.userId");
		    	stringArray[4]=String.valueOf(userLatitude);
		    	stringArray[5]=String.valueOf(userLongitude);
		    	stringArray[6]=String.valueOf(rs.getDouble("reportLatitude"));
		    	stringArray[7]=String.valueOf(rs.getDouble("reportLongitude"));
		    	stringArray[8]=String.valueOf(rs.getTimestamp("timeStamp"));
		    	stringArray[9]=rs.getString("reportCause");
		    	stringArray[10]=rs.getString("reportStatus");
		    	stringArray[11]=rs.getString("userWorkInProgress");
		    	stringArray[12]=String.valueOf(rs.getTimestamp("closingDate"));
		    	
		        Report r=Report.ReportFactory(stringArray);
		        reportsList.add(r);
				
			}
			
		}catch(SQLException e){
			e.printStackTrace();
			printSQLException(e);
		}
		finally{
			if(p_stmt!=null){
				p_stmt.close();
			}
		}
		return reportsList;
	}
	
	//**************************************
	
	//update methods
	
	//**************************************
	
	/**
	 * updates the details of the Person, selected by the username, with the new values passed as parameters
	 * @param name new name, or old one if not modified
	 * @param surname new surname, or old one if not modified
	 * @param userId username of the person
	 * @param eMail new email address, or old one if not modified
	 * @param password new password, or old one if not modified
	 * @param city new city, or old one if not modified
	 * @param district new district, or old one if not modified
	 * @param latitude new latitude, or old one if not modified
	 * @param longitude new longitude, or old one if not modified
	 * @throws SQLException
	 */
	public void update_persons(String name,String surname,String userId, String eMail,String password,String city,String district,double latitude,double longitude) throws SQLException {
		PreparedStatement p_stmt=null;
		try{
			p_stmt=con.prepareStatement(PredefinedSQLCode.persons_update_query[0]);
			p_stmt.setString(1, name);
			p_stmt.setString(2, surname);
			p_stmt.setString(3,eMail);
			p_stmt.setString(4,password);
			p_stmt.setString(5,city);
			p_stmt.setString(6,district);
			p_stmt.setDouble(7, latitude);
			p_stmt.setDouble(8, longitude);
			p_stmt.setString(9, userId);
			p_stmt.executeUpdate();
			
		}catch(SQLException e){
			e.printStackTrace();
			printSQLException(e);
		}
		finally{
			if(p_stmt!=null){
				p_stmt.close();
			}
		}
	}
	
	/**
	 * updates the report specified through the reportId with the new values passed as parameters
	 * @param reportId the id of the report to be updated
	 * @param reportCause the new report cause, or old one if not modified
	 * @param reportStatus the new report status, or old one if not modified
	 * @param userWorkInProgress the username of the Person that is currently working on the Report, or old one if not modified
	 * @throws SQLException
	 */
	public void update_reports(String reportId,String reportCause,String reportStatus,String userWorkInProgress,Timestamp closingDate)throws SQLException{
		PreparedStatement p_stmt=null;
		try{
			p_stmt=con.prepareStatement(PredefinedSQLCode.reports_update_query[0]);
			p_stmt.setString(1, reportCause);
			p_stmt.setString(2, reportStatus);
			p_stmt.setString(3, userWorkInProgress);
			p_stmt.setTimestamp(4, closingDate);
			p_stmt.setString(5, reportId);
			p_stmt.executeUpdate();
			
		}catch(SQLException e){
			e.printStackTrace();
			printSQLException(e);
		}
		finally{
			if(p_stmt!=null){
				p_stmt.close();
			}
		}
	}
	
	/**
	 * updates the report specified through the reportId with the new values passed as parameters
	 * The userWorkInProgress field is not modified
	 * @param reportId reportId the id of the report to be updated
	 * @param reportCause the new report cause, or old one if not modified
	 * @param reportStatus the new report status, or old one if not modified
	 * @throws SQLException
	 */
	public void update_reports_noUser(String reportId,String reportCause,String reportStatus)throws SQLException{
		PreparedStatement p_stmt=null;
		try{
			p_stmt=con.prepareStatement(PredefinedSQLCode.reports_update_noUser_query[0]);
			p_stmt.setString(1, reportCause);
			p_stmt.setString(2, reportStatus);
			p_stmt.setString(3, reportId);
			p_stmt.executeUpdate();
			
		}catch(SQLException e){
			e.printStackTrace();
			printSQLException(e);
		}
		finally{
			if(p_stmt!=null){
				p_stmt.close();
			}
		}
	}
	
	
	
	
	//**************************************
	
	//metodi tabella Locations
	
	//**************************************
	
	/**
	 * Returns a String array containing all the cities available in the table Locations
	 * @return a String array with the cities
	 * @throws SQLException
	 */
	public String[] get_cities() throws SQLException {
		int rowCount=0;
		Statement stmt=null;
		String[] cities=null;
		try{
			stmt=con.createStatement();
			ResultSet rs=stmt.executeQuery(PredefinedSQLCode.locations_city_query[0]);
			if(rs.last()){ //se ci sono righe nel rs creo l'array
				   rowCount=rs.getRow(); //numero righe del rs
				   cities=new String[rowCount];
				}
			rs.beforeFirst();
			int i=0;
			while(rs.next()){
				cities[i]=rs.getString(1);
				i++;
			}
			
		}catch(SQLException e){
			e.printStackTrace();
			printSQLException(e);
		}
		finally{
			if(stmt!=null){
				stmt.close();
			}
		}
		return cities;
	
	}
	
	/**
	 * Returns a String array containing all the districts available for the city passes as parametes
	 * @param city city whose districts are needed
	 * @return a String array with the districts
	 * @throws SQLException
	 */
	public String[] get_district(String city) throws SQLException {
		int rowCount=0;
		String[] district=null;
		PreparedStatement p_stmt=null;
		try{
			p_stmt=con.prepareStatement(PredefinedSQLCode.locations_district_query[0]);
			p_stmt.setString(1, city);
			ResultSet rs=p_stmt.executeQuery();
			if(rs.last()){
			   rowCount=rs.getRow();
			   district=new String[rowCount];
			}
			rs.beforeFirst();
			int i=0;
			while(rs.next()){
				district[i]=rs.getString(1);
				i++;
			}
			
			
		}catch(SQLException e){
			e.printStackTrace();
			printSQLException(e);
		}
		finally{
			if(p_stmt!=null){
				p_stmt.close();
			}
		}
		return district;
	}
	
	/**
	 * Returns an array with the coordinates of the NW and SE point of the given district
	 * @param city city
	 * @param district district
	 * @return an array containing (in the order) the latitude and the longitude of the NW point and the latitude and the longitude of the SE point
	 * @throws SQLException
	 */
	public double[][] get_coordinates(String city,String district) throws SQLException {
		double[][] coordinates=null;
		PreparedStatement p_stmt=null;
		int rowCount,columnCount;
		try{
			p_stmt=con.prepareStatement(PredefinedSQLCode.locations_coordinates_query[0]);
			p_stmt.setString(1, city);
			p_stmt.setString(2, district);
			ResultSet rs=p_stmt.executeQuery();
			if(rs.last()){
			rowCount=rs.getRow();
			columnCount=rs.getMetaData().getColumnCount();
			coordinates=new double[rowCount][columnCount];
			}
			rs.beforeFirst();
			int i=0;
			while(rs.next()){
				int j=0;
				coordinates[i][j++]=rs.getDouble(1);
				coordinates[i][j++]=rs.getDouble(2);
				coordinates[i][j++]=rs.getDouble(3);
				coordinates[i][j++]=rs.getDouble(4);
				i++;
			}
			System.out.println(coordinates[0][0] + " " + coordinates[0][1] + " " +coordinates[0][2] + " " +coordinates[0][2]);
		}catch(SQLException e){
			e.printStackTrace();
			printSQLException(e);
		}
		finally{
			if(p_stmt!=null){
				p_stmt.close();
			}
		}
		return coordinates;
	}
	


	

}

