package classes;

/**
 * @author Mattia Nangeroni
 * @author Massimiliano Calicchio
 * @author Simone Bogni
 * 
 * The class PredefinedSQLCode contains the predefined queries to be run on the database
 *
 */
public class PredefinedSQLCode {
	
	/**
	 * It contains the queries to delete from Persons (index 0) and from Reports (index 1)
	 */
	public static final String delete_queries[]={
			"delete from Persons where userId=?",
			"delete from Reports where reportId=?",
		};
	
	/**
	 * It contains the queries to insert into Persons (index 0) and into Reports (inded 1)
	 */
	public static final String insert_table_queries[]={
			"insert into Persons (name,surname,userId,eMail,password,city,district,latitude,longitude) values (?,?,?,?,?,?,?,?,?)",
			"insert into Reports (city,district,reportId,userId,reportLatitude,reportLongitude,timeStamp,reportCause,reportStatus,userWorkInProgress,closingDate) values (?,?,?,?,?,?,?,?,?,?,?)",
	};
	
	/**
	 * It contains queries to get all the rows from the tables Persons (index 0) and Reports (index 1)
	 */
	public static final String get_all_row_queries[]={
			"select * from Persons",
			"select * from Reports",
	};
	
	/**
	 * It contains the query (index 0) to select the Reports that have not yet been closed
	 */
	public static final String report_city_district_query[]={  //è da verificare
			"SELECT latitude,longitude,Reports.city,Reports.district,reportId,Reports.userId,reportLatitude,reportLongitude,"
			+ "timeStamp,reportCause,reportStatus,userWorkInProgress,closingDate"
			+ " FROM Persons,Reports "
			+ "WHERE Reports.city=? AND Reports.district=? AND closingDate is null "
			+ "AND Persons.userId=Reports.userId"
	};
	
	/**
	 * It contains the query (index 0) to select all the Reports that have been closed
	 */
	public static final String reports_over_query[]={
		"select latitude,longitude,Reports.city,Reports.district,reportId,Reports.userId,reportLatitude,reportLongitude,"
		+ "timeStamp,reportCause,reportStatus,userWorkInProgress,closingDate" 
		+ " from Persons,Reports where closingDate is not null AND Persons.userId=Reports.userId"	
	};
	
	/**
	 * It contains the query (index 0) to select all the Reports a certain user is working on
	 */
	public static final String reports_from_userWorkInProgress_query[]={
			"select latitude,longitude,Reports.city,Reports.district,reportId,Reports.userId,reportLatitude,reportLongitude,"
			+ "timeStamp,reportCause,reportStatus,userWorkInProgress,closingDate" 
			+ " from Persons,Reports where closingDate is null AND Persons.userId=Reports.userId AND Reports.userWorkInProgress=?"	
		};
	
	/**
	 * It contains the query (index 0) to update a Person
	 */
	public static final String persons_update_query[]={
			"update Persons set name=?,surname=?,eMail=?,password=?,city=?,district=?,latitude=?,longitude=? "
			+ "where userId=?"
	};
	
	/**
	 * It contains the query (index 0) to obtain all the details of a specific Person
	 */
	public static final String person_userId_query[]={
			"select * from Persons where userId=?"
	};
	
	/**
	 * It contains the query (index 0) to update the reportCause, reportStatus, userWorkInProgress and closingDate of a Report
	 */
	public static final String reports_update_query[]={
		"update Reports set reportCause=?,reportStatus=?,userWorkInProgress=?,closingDate=? "
		+ "where reportId=?"	
	};
	
	/**
	 * It contains the query (index 0) to update the reportCause and reportStatus of a Report
	 */
	public static final String reports_update_noUser_query[]={
			"update Reports set reportCause=?,reportStatus=? where reportId=?"	
	};
	
	
	
	
	//Locations query 
	
	public static final String locations_city_query[]={
			"select distinct city from Locations"
	};
	
	public static final String locations_district_query[]={
			"select district from Locations where city=?"
	};
	
	public static final String locations_coordinates_query[]={
			"select nwLatitude,nwLongitude,seLatitude,seLongitude FROM Locations WHERE city=? AND district=?"
	};
	
	

}
