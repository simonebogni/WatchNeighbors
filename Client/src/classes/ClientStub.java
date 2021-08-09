package classes;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;

public class ClientStub implements ClientServerInterface {
	private ObjectOutputStream objOut;
	private ObjectInputStream objIn;
	private Socket socket;

	/** Constructor for the class ClientStub, the link between the client and the server side
	 * @throws Exception
	 */
	public ClientStub() throws Exception{
		InetAddress addr = InetAddress.getByName(ApplicationParameters.getServerAddress());
		System.out.println("server address = "+addr);
		socket = new Socket(addr, ApplicationParameters.getPort());

		//crea gli stream di input/output della socket
		System.out.println("socket = "+socket);
		objOut = new ObjectOutputStream(socket.getOutputStream());
		objIn = new ObjectInputStream(socket.getInputStream());
		}

	/* (non-Javadoc)
	 * @see classes.ClientServerInterface#verifyUsernamePresence(java.lang.String)
	 */
	@Override
	public boolean verifyUsernamePresence(String username) {
		Command command = new Command("<verifyUsername>", username, null, null, null, null, null, null, null, null, null, null);
		try {
			objOut.writeObject(command);
			return (Boolean) objIn.readObject();
		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
		}
		return false;
	}

	/* (non-Javadoc)
	 * @see classes.ClientServerInterface#getCityList()
	 */
	@Override
	public String[] getCityList() {
		Command command = new Command("<getCityList>", null, null, null, null, null, null, null, null, null, null, null);
		try {
			objOut.writeObject(command);
			return (String[]) objIn.readObject();
		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
			System.out.println("ClientStub: exceptionReached"+e.getClass().toString());
			return new String[]{""};
		}
	}

	/* (non-Javadoc)
	 * @see classes.ClientServerInterface#getDistrictList(java.lang.String)
	 */
	@Override
	public String[] getDistrictList(String city) {
		Command command = new Command("<getDistrictList>", city, null, null, null, null, null, null, null, null, null, null);
		try {
			objOut.writeObject(command);
			return (String[]) objIn.readObject();
		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
			return new String[]{""};
		}
	}

	/* (non-Javadoc)
	 * @see classes.ClientServerInterface#getReportList(java.lang.String, java.lang.String)
	 */
	@Override
	public Report[] getReportList(String city, String district) {
		Command command = new Command("<getReportList>", city, district, null, null, null, null, null, null, null, null, null);
		try {
			objOut.writeObject(command);
			return (Report[]) objIn.readObject();
		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
			return null;
		}
	}

	/* (non-Javadoc)
	 * @see classes.ClientServerInterface#stop()
	 */
	@Override
	public void stop() {
		System.out.println("closing...");
		Command command = new Command("<end>", null, null, null, null, null, null, null, null, null, null, null);
		try {
			objOut.writeObject(command);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		try {
			socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/* (non-Javadoc)
	 * @see classes.ClientServerInterface#getCoordinates(java.lang.String, java.lang.String)
	 */
	@Override
	public Double[] getCoordinates(String city, String district) {
		Command command = new Command("<getCoordinates>", city, district, null, null, null, null, null, null, null, null, null);
		try {
			objOut.writeObject(command);
			return (Double[]) objIn.readObject();
		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
			System.out.println("ClientStub: exception reached "+e.getClass().toString());
			return new Double[]{0.0, 0.0, 0.0, 0.0};
		}
	}

	/* (non-Javadoc)
	 * @see classes.ClientServerInterface#getReportListUserWorkInProgress(java.lang.String)
	 */
	@Override
	public Report[] getReportListUserWorkInProgress(String username) {
		Command command = new Command("<getReportListUserWorkInProgress>", username, null, null, null, null, null, null, null, null, null, null);
		try {
			objOut.writeObject(command);
			return (Report[]) objIn.readObject();
		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
			return null;
		}
	}

	/* (non-Javadoc)
	 * @see classes.ClientServerInterface#deleteAccount(java.lang.String)
	 */
	@Override
	public boolean deleteAccount(String username) {
		Command command = new Command("<deleteAccount>", username, null, null, null, null, null, null, null, null, null, null);
		try {
			objOut.writeObject(command);
			return (Boolean) objIn.readObject();
		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
		}
		return false;
	}

	/* (non-Javadoc)
	 * @see classes.ClientServerInterface#login(java.lang.String, java.lang.String)
	 */
	@Override
	public Person login(String username, String password) {
		Command command = new Command("<login>", username, password, null, null, null, null, null, null, null, null, null);
		try {
			objOut.writeObject(command);
			return (Person) objIn.readObject();
		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}

	/* (non-Javadoc)
	 * @see classes.ClientServerInterface#insertPerson(classes.Person)
	 */
	@Override
	public boolean insertPerson(Person p) {
		Command command = new Command("<insertPerson>", null, null, null, null, p, null, null, null, null, null, null);
		try {
			objOut.writeObject(command);
			return (Boolean) objIn.readObject();
		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
		}
		return false;
	}

	/* (non-Javadoc)
	 * @see classes.ClientServerInterface#takeReport(classes.Report, java.lang.String)
	 */
	@Override
	public boolean takeReport(Report report, String username) {
		Command command = new Command("<takeReport>", username, null, null, null, null, report, null, null, null, null, null);
		try {
			objOut.writeObject(command);
			return (Boolean) objIn.readObject();
		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
		}
		return false;
	}

	/* (non-Javadoc)
	 * @see classes.ClientServerInterface#closeReport(java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public boolean closeReport(String reportID, String reportCause, String reportStatus, String username) {
		Command command = new Command("<closeReport>", reportID, reportCause, reportStatus, username, null, null, null, null, null, null, null);
		try {
			objOut.writeObject(command);
			return (Boolean) objIn.readObject();
		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
		}
		return false;
	}

	/* (non-Javadoc)
	 * @see classes.ClientServerInterface#insertReport(classes.Report)
	 */
	@Override
	public boolean insertReport(Report report) {
		Command command = new Command("<insertReport>", null, null, null, null, null, report, null, null, null, null, null);
		try {
			objOut.writeObject(command);
			return (Boolean) objIn.readObject();
		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
		}
		return false;
	}

	/* (non-Javadoc)
	 * @see classes.ClientServerInterface#updatePerson(java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.Double, java.lang.Double)
	 */
	@Override
	public boolean updatePerson(String name, String surname, String userId, String eMail, String password, String city,
			String district, Double latitude, Double longitude) {
		Command command = new Command("<updatePerson>", name, surname, userId, eMail, null, null, password, city, district, latitude, longitude);
		try {
			objOut.writeObject(command);
			return (Boolean) objIn.readObject();
		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
		}
		return false;
	}
}
