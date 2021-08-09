package classes;

import java.io.Serializable;

/**
 * @author Mattia Nangeroni
 * @author Massimiliano Calicchio
 * @author Simone Bogni
 * 
 * An instance of the class Person defines a user and its details
 */
public class Person implements Serializable{
	private static final long serialVersionUID = -7563457301627849233L;
	private String name;
	private String surname;
	private String userID;
	private String email;
	private String password;
	private String city;
	private String district;
	private double latitude;
	private double longitude;
	
	
	/** Constructor for the class Person
	 * @param name name of the user
	 * @param surname surname of the user
	 * @param userID username of the user
	 * @param email email address of the user
	 * @param password password of the user
	 * @param city city of the user
	 * @param district district of the user
	 * @param latitude latitude of the user
	 * @param longitude longitude of the user
	 */
	public Person(
			String name,
			String surname, 
			String userID,
			String email,
			String password,
			String city,
			String district,
			double latitude,
			double longitude){
		this.name=name;
		this.surname=surname;
		this.userID=userID;
		this.email=email;
		this.password=password;
		this.city=city;
		this.district=district;
		this.latitude=latitude;
		this.longitude=longitude;
	};
	
	/**
	 * @return the name of the user
	 */
	public String getName() {
		return name;
	}
	/**
	 * @return the surname of the user
	 */
	public String getSurname() {
		return surname;
	}
	/**
	 * @return the username of the user
	 */
	public String getUserID() {
		return userID;
	}
	/**
	 * @return the email address of the user
	 */
	public String getEmail() {
		return email;
	}
	/**
	 * @return the password of the user
	 */
	public String getPassword() {
		return password;
	}
	/**
	 * @return the city of the user
	 */
	public String getCity() {
		return city;
	}
	/**
	 * @return the district of the user
	 */
	public String getDistrict() {
		return district;
	}
	/**
	 * @return the latitude of the user
	 */
	public double getLatitude() {
		return latitude;
	}
	/**
	 * @return the longitude of the user
	 */
	public double getLongitude() {
		return longitude;
	}
}
