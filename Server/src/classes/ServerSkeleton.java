package classes;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 * @author Mattia Nangeroni
 * @author Massimiliano Calicchio
 * @author Simone Bogni
 * 
 * An instance of the class ServerSkeleton represent the bridge between the server and the client side
 */
public class ServerSkeleton implements ClientServerInterface, Runnable {
	private Socket socket;
	private ObjectOutputStream objOut;
	private ObjectInputStream objIn;
	private RealServer server = new RealServer();

	/** Constructor for the class ServerSocket, the bridge between the server and the client side
	 * @param socket the socket shared with the client
	 */
	public ServerSkeleton(Socket socket){
		this.socket=socket;
		try{

			objOut = new ObjectOutputStream(socket.getOutputStream());
			objIn = new ObjectInputStream(socket.getInputStream());
			//iStream = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			//oStream = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);
		} catch (IOException ioe){
		}
	}


	/* (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {
		try{
			while(!socket.isClosed()){
				Command command = (Command) objIn.readObject();
				String commandString = command.getCommand();
				switch(commandString){
				case "<verifyUsername>":
					verifyUsernamePresence(command.getString1());
					break;
				case "<getCityList>":
					getCityList();
					break;
				case "<getDistrictList>":
					getDistrictList(command.getString1());
					break;
				case "<getReportList>":
					getReportList(command.getString1(), command.getString2());
					break;
				case "<getReportListUserWorkInProgress>":
					getReportListUserWorkInProgress(command.getString1());
					break;
				case "<end>":
					stop();
					break;
				case "<getCoordinates>":
					getCoordinates(command.getString1(), command.getString2());
					break;
				case "<deleteAccount>":
					deleteAccount(command.getString1());
					break;
				case "<login>":
					login(command.getString1(), command.getString2());
					break;
				case "<insertPerson>":
					insertPerson(command.getPerson1());
					break;
				case "<takeReport>":
					takeReport(command.getReport1(), command.getString1());
					break;
				case "<closeReport>":
					closeReport(command.getString1(), command.getString2(), command.getString3(), command.getString3());
					break;
				case "<insertReport>":
					insertReport(command.getReport1());
					break;
				case "<updatePerson>":
					updatePerson(command.getString1(), command.getString2(), command.getString3(), command.getString4(), command.getString5(), command.getString6(), command.getString7(), command.getDouble1(), command.getDouble2());
					break;
				default:
					System.out.println("Operation not recognised: "+command.getCommand());
					break;
				}
			}
		} catch (Exception e){
			e.printStackTrace();
			stop();
		}
	}

	/* (non-Javadoc)
	 * @see classes.ClientServerInterface#verifyUsernamePresence(java.lang.String)
	 */
	@Override
	public boolean verifyUsernamePresence(String username) {
		Boolean verification = server.verifyUsernamePresence(username);
		try {
			objOut.writeObject(verification);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return verification;
	}

	/* (non-Javadoc)
	 * @see classes.ClientServerInterface#getCityList()
	 */
	@Override
	public String[] getCityList() {
		String[] cities = server.getCityList();
		System.out.println("ServerSkeleton: get cities "+cities.toString());
		try {
			objOut.writeObject(cities);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return cities;
	}

	/* (non-Javadoc)
	 * @see classes.ClientServerInterface#getDistrictList(java.lang.String)
	 */
	@Override
	public String[] getDistrictList(String city) {
		String[] districts = server.getDistrictList(city);
		try {
			objOut.writeObject(districts);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return districts;
	}

	/* (non-Javadoc)
	 * @see classes.ClientServerInterface#getReportList(java.lang.String, java.lang.String)
	 */
	@Override
	public Report[] getReportList(String city, String district) {
		Report[] reports = server.getReportList(city, district);
		try{
			if (reports.length==0){
				objOut.writeObject(null);
			} else {
				objOut.writeObject(reports);
			}
		} catch(IOException e){
			e.printStackTrace();
		}
		return reports;
	}

	/* (non-Javadoc)
	 * @see classes.ClientServerInterface#stop()
	 */
	@Override
	public void stop() {
		server.stop();
		try{
			socket.close();
		} catch(IOException e){}
	}


	/* (non-Javadoc)
	 * @see classes.ClientServerInterface#getCoordinates(java.lang.String, java.lang.String)
	 */
	@Override
	public Double[] getCoordinates(String city, String district) {
		Double[] coords = server.getCoordinates(city, district);
		System.out.println(coords.toString());
		try {
			objOut.writeObject(coords);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return coords;
	}


	/* (non-Javadoc)
	 * @see classes.ClientServerInterface#getReportListUserWorkInProgress(java.lang.String)
	 */
	@Override
	public Report[] getReportListUserWorkInProgress(String username) {
		Report[] reports = server.getReportListUserWorkInProgress(username);
		try{
			if (reports.length==0){
				objOut.writeObject(null);
			} else {
				objOut.writeObject(reports);
			}
		} catch(IOException e){
			e.printStackTrace();
		}
		return reports;
	}


	/* (non-Javadoc)
	 * @see classes.ClientServerInterface#deleteAccount(java.lang.String)
	 */
	@Override
	public boolean deleteAccount(String username) {
		Boolean verification = server.deleteAccount(username);
		try {
			objOut.writeObject(verification);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return verification;
	}


	/* (non-Javadoc)
	 * @see classes.ClientServerInterface#login(java.lang.String, java.lang.String)
	 */
	@Override
	public Person login(String username, String password) {
		Person verification = server.login(username, password);
		try {
			objOut.writeObject(verification);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return verification;
	}


	/* (non-Javadoc)
	 * @see classes.ClientServerInterface#insertPerson(classes.Person)
	 */
	@Override
	public boolean insertPerson(Person p) {
		Boolean verification = server.insertPerson(p);
		try {
			objOut.writeObject(verification);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return verification;
	}


	/* (non-Javadoc)
	 * @see classes.ClientServerInterface#takeReport(classes.Report, java.lang.String)
	 */
	@Override
	public boolean takeReport(Report report, String username) {
		Boolean verification = server.takeReport(report, username);
		try {
			objOut.writeObject(verification);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return verification;
	}


	/* (non-Javadoc)
	 * @see classes.ClientServerInterface#closeReport(java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public boolean closeReport(String reportID, String reportCause, String reportStatus, String username) {
		Boolean verification = server.closeReport(reportID, reportCause, reportStatus, username);
		try {
			objOut.writeObject(verification);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return verification;
	}


	/* (non-Javadoc)
	 * @see classes.ClientServerInterface#insertReport(classes.Report)
	 */
	@Override
	public boolean insertReport(Report report) {
		Boolean verification = server.insertReport(report);
		try {
			objOut.writeObject(verification);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return verification;
	}


	/* (non-Javadoc)
	 * @see classes.ClientServerInterface#updatePerson(java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.Double, java.lang.Double)
	 */
	@Override
	public boolean updatePerson(String name, String surname, String userId, String eMail, String password, String city,
			String district, Double latitude, Double longitude) {
		Boolean verification = server.updatePerson(name, surname, userId, eMail, password, city, district, latitude, longitude);
		try {
			objOut.writeObject(verification);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return verification;
	}

}
