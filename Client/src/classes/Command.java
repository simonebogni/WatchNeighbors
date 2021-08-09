package classes;

import java.io.Serializable;

/**
 * @author Mattia Nangeroni
 * @author Massimiliano Calicchio
 * @author Simone Bogni
 *
 * An instance of the class Command represents a command to be sent from the client to the server.
 * The class provides 7 parameters of type String, 2 parameters of type Double, 1 parameter of type Person and 1 parameter of type Report in order to store the data.
 */
public class Command implements Serializable {
	
	private static final long serialVersionUID = -6043229137921320098L;
	private String command;
	private String string1, string2, string3, string4, string5, string6, string7;
	private Double double1, double2;
	private Person person1;
	private Report report1;
	/**
	 * Constructor for the class Command.
	 * @param command The message that indicates the type of command sent
	 * @param string1 If present, first parameter of type String
	 * @param string2 If present, second parameter of type String
	 * @param string3 If present, third parameter of type String
	 * @param string4 If present, fourth parameter of type String
	 * @param person1 If present, first parameter of type Person
	 * @param report1 If present, first parameter of type Report
	 */
	public Command(String command, String string1, String string2, String string3, String string4, Person person1,
			Report report1, String string5, String string6, String string7, Double double1, Double double2) {
		super();
		this.command = command;
		this.string1 = string1;
		this.string2 = string2;
		this.string3 = string3;
		this.string4 = string4;
		this.string5 = string5;
		this.string6 = string6;
		this.string7 = string7;
		this.double1 = double1;
		this.double2 = double2;
		this.person1 = person1;
		this.report1 = report1;
	}
	protected String getCommand() {
		return command;
	}
	protected void setCommand(String command) {
		this.command = command;
	}
	protected String getString1() {
		return string1;
	}
	protected void setString1(String string1) {
		this.string1 = string1;
	}
	protected String getString2() {
		return string2;
	}
	protected void setString2(String string2) {
		this.string2 = string2;
	}
	protected String getString3() {
		return string3;
	}
	protected void setString3(String string3) {
		this.string3 = string3;
	}
	protected String getString4() {
		return string4;
	}
	protected void setString4(String string4) {
		this.string4 = string4;
	}
	protected Person getPerson1() {
		return person1;
	}
	protected void setPerson1(Person person1) {
		this.person1 = person1;
	}
	protected Report getReport1() {
		return report1;
	}
	protected void setReport1(Report report1) {
		this.report1 = report1;
	}
	protected String getString5() {
		return string5;
	}
	protected void setString5(String string5) {
		this.string5 = string5;
	}
	protected String getString6() {
		return string6;
	}
	protected void setString6(String string6) {
		this.string6 = string6;
	}
	protected String getString7() {
		return string7;
	}
	protected void setString7(String string7) {
		this.string7 = string7;
	}
	protected Double getDouble1() {
		return double1;
	}
	protected void setDouble1(Double double1) {
		this.double1 = double1;
	}
	protected Double getDouble2() {
		return double2;
	}
	protected void setDouble2(Double double2) {
		this.double2 = double2;
	}
	
	
	
}
