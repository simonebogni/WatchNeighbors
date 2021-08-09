package classes;


import org.apache.commons.validator.routines.EmailValidator;

/*import java.sql.SQLException;
import java.util.ArrayList;
 */

import org.controlsfx.glyphfont.Glyph;
import org.openstreetmap.gui.jmapviewer.Coordinate;

import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.Separator;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * @author Mattia Nangeroni
 * @author Massimiliano Calicchio
 * @author Simone Bogni
 * It defines the main GUI of the application
 */
public class ClientGUI2 extends Application {

	private Client client = new Client();
	private Stage window;
	private VBox loginContent;
	private VBox registerContent;
	private VBox detailsContent;
	private TabPane tabPane;
	private Tab tabLogin;
	private Tab tabRegistration;
	private Tab tabDetails;

	//Login fields
	private TextField usernameInputLogin;
	private PasswordField pswInputLogin;

	//Registration fields
	private TextField usernameInputRegistration;
	private PasswordField password1InputRegistration;
	private PasswordField password2InputRegistration;
	private TextField emailInputRegistration;
	private TextField nameInputRegistration;
	private TextField surnameInputRegistration;
	private ComboBox<String> cityInputRegistration;
	private ComboBox<String> districtInputRegistration;
	private TextField latitudeInputRegistration;
	private TextField longitudeInputRegistration;
	private Button selectLocationButtonRegistration;

	//Details fields
	private ComboBox<String> cityInputDetails;
	private ComboBox<String> districtInputDetails;
	private TextField usernameInputDetails;
	private TextField latitudeInputDetails;
	private TextField longitudeInputDetails;
	private Button selectLocationButtonDetails;
	private TextField emailInputDetails;
	private TextField nameInputDetails;
	private TextField surnameInputDetails;
	
	//parameters to check if the details have been modified
	private String originalName, originalSurname, originalEmail, originalCity, originalDistrict, originalLat, originalLon, originalPassword;


	/* (non java-doc)
	 * @see javafx.application.Application#start(javafx.stage.Stage)
	 */
	@Override
	public void start(Stage primaryStage) throws Exception {

		window = primaryStage;
		window.setTitle("Watch Neighbors 2.0");
		window.getIcons().add(new Image(ClientGUI2.class.getResourceAsStream("/img/eye.gif")));
		window.setOnCloseRequest(e->{
			e.consume();
			boolean answer = ConfirmBox.display("Confirm exit", "Are you sure you want to close the application?");
			if (answer){
				client.stop();
				window.close();
			}
		});

		//TabPane
		tabPane = new TabPane();
		//Tab Login
		tabLogin = new Tab();
		tabLogin.setText("Login");
		VBox logCont = getLoginTabContent();
		tabLogin.setContent(logCont);
		tabLogin.setClosable(false);
		//Tab Registration
		tabRegistration = new Tab();
		tabRegistration.setText("Registration");
		VBox regCont = getRegistrationTabContent();
		tabRegistration.setContent(regCont);
		tabRegistration.setClosable(false);
		//Tab Details
		tabDetails = new Tab();
		tabDetails.setText("Details");
		tabDetails.setDisable(true);
		VBox detCont = getDetailsTabContent();
		tabDetails.setContent(detCont);
		tabDetails.setClosable(false);
		//Add every tab to the TabPane
		tabPane.getTabs().addAll(tabLogin, tabRegistration, tabDetails);

		Scene scene = new Scene(tabPane);
		window.setScene(scene);
		window.show();
		
		String[] cities = client.getCityList();
		for(String s : cities){
			System.out.println("City: "+s);
			String[] districts = client.getDistrictList(s);
			int num = 1;
			for(String d : districts){
				System.out.println("District "+num+": "+d);
				num++;
			}
		}
	}

	private VBox getLoginTabContent() {
		if (loginContent != null){
			return loginContent;
		}
		Insets insets = new Insets(10, 10, 10, 10);
		VBox vbox = new VBox();
		Label labelTitle = new Label("Welcome in Watch Neighbors");
		labelTitle.setPadding(insets);
		Label labelSubtitle = new Label("Please insert username and password to login");
		labelSubtitle.setPadding(insets);
		//GridPane
		GridPane grid = new GridPane();
		//In Insets the argument is double varargs that represent the dimensions at the sides
		grid.setPadding(insets);
		//set the vertical and horizontal gap between cells
		grid.setVgap(8);
		grid.setHgap(10);
		//Separators
		Separator sep1 = new Separator();
		GridPane.setConstraints(sep1, 0, 0, 2, 1);
		//Name label
		Label nameLabel = new Label("Username:");
		GridPane.setConstraints(nameLabel, 0, 1);
		usernameInputLogin = new TextField();
		usernameInputLogin.setPromptText("Your username here...");
		GridPane.setConstraints(usernameInputLogin, 1, 1);
		//Name label
		Label pswLabel = new Label("Password:");
		GridPane.setConstraints(pswLabel, 0, 2);
		pswInputLogin = new PasswordField();
		pswInputLogin.setPromptText("Your password here...");
		GridPane.setConstraints(pswInputLogin, 1, 2);
		//Separator
		Separator sep2 = new Separator();
		GridPane.setConstraints(sep2, 0, 3, 2, 1);
		//Result label
		Label resultLabel = new Label();
		resultLabel.setPrefWidth(400);
		//Button login
		Button loginButton = new Button("Log in", new Glyph("FontAwesome", "KEY"));
		GridPane.setConstraints(loginButton, 0, 4, 2, 1);
		loginButton.setPrefWidth(400);
		loginButton.setOnAction(e -> {
			resultLabel.setText("");
			if(usernameInputLogin.getText().equals("") || pswInputLogin.getText().equals("")){
				//prompt invalid credentials
				resultLabel.setText("You need to fill both the username and the password.");
			} else {
				try {
					Person user = client.login(usernameInputLogin.getText(), pswInputLogin.getText());
					if(user!=null){
						tabLogin.setDisable(true);
						tabDetails.setDisable(false);
						tabRegistration.setDisable(true);
						tabPane.getSelectionModel().select(tabDetails);
						emptyLoginTab();
						emptyRegistrationTab();
						populateDetailsTab(user);
					} else {
						//prompt invalid credentials
						resultLabel.setText("Username and/or passwords are invalid.");
					}

				} catch (Exception e2) {
					AlertBox.display("ERROR", "Network error, please try again later");
					e2.printStackTrace();
				}

			}
		});
		//Button login
		Button seeWarningsButton = new Button("See warnings", new Glyph("FontAwesome", "EYE"));
		seeWarningsButton.setPrefWidth(400);
		seeWarningsButton.setOnAction(e -> seeWarningsButtonClicked(false));
		GridPane.setConstraints(seeWarningsButton, 0, 5, 2, 1);
		GridPane.setConstraints(resultLabel, 0, 6, 2, 1);

		//insert everything in the gridPane
		grid.getChildren().addAll(sep1, nameLabel, usernameInputLogin, pswLabel, pswInputLogin, sep2, loginButton, seeWarningsButton, resultLabel);
		//Add nodes to the VBox
		vbox.getChildren().addAll(labelTitle, labelSubtitle, grid);
		vbox.setMinWidth(400);
		loginContent = vbox;
		return loginContent;
	}

	private VBox getRegistrationTabContent() {
		if (registerContent != null){
			return registerContent;
		}
		Insets insets = new Insets(10, 10, 10, 10);
		VBox vbox = new VBox();
		Label labelTitle = new Label("Join Watch Neighbors today!");
		labelTitle.setPadding(insets);
		Label labelSubtitle = new Label("Please fill in your details:");
		labelSubtitle.setPadding(insets);
		//GridPane
		GridPane grid = new GridPane();
		//In Insets the argument is double varargs that represent the dimensions at the sides
		grid.setPadding(insets);
		//set the vertical and horizontal gap between cells
		grid.setVgap(8);
		grid.setHgap(10);
		int row = 0;
		//Separators
		Separator sep1 = new Separator();
		GridPane.setConstraints(sep1, 0, row++, 2, 1);
		//Username label
		Label usernameLabel = new Label("Username:");
		GridPane.setConstraints(usernameLabel, 0, row);
		usernameInputRegistration = new TextField();
		ClientGUI2.addTextLimiter(usernameInputRegistration, 30);
		usernameInputRegistration.setPromptText("Your username here...");
		GridPane.setConstraints(usernameInputRegistration, 1, row++);
		//Password label
		Label passwordLabel1 = new Label("Password:");
		GridPane.setConstraints(passwordLabel1, 0, row);
		password1InputRegistration = new PasswordField();
		ClientGUI2.addTextLimiter(password1InputRegistration, 30);
		password1InputRegistration.setPromptText("Your password here...");
		GridPane.setConstraints(password1InputRegistration, 1, row++);
		//Password convalidation label
		Label passwordLabel2 = new Label("Confirm password:");
		GridPane.setConstraints(passwordLabel2, 0, row);
		password2InputRegistration = new PasswordField();
		ClientGUI2.addTextLimiter(password2InputRegistration, 30);
		password2InputRegistration.setPromptText("Repeat the password here...");
		GridPane.setConstraints(password2InputRegistration, 1, row++);
		//Email label
		Label emailLabel = new Label("Email:");
		GridPane.setConstraints(emailLabel, 0, row);
		emailInputRegistration = new TextField();
		ClientGUI2.addTextLimiter(emailInputRegistration, 30);
		emailInputRegistration.setPromptText("Your email here...");
		GridPane.setConstraints(emailInputRegistration, 1, row++);
		//Name label
		Label nameLabel = new Label("Name:");
		GridPane.setConstraints(nameLabel, 0, row);
		nameInputRegistration = new TextField();
		ClientGUI2.addTextLimiter(nameInputRegistration, 30);
		nameInputRegistration.setPromptText("Your name here...");
		GridPane.setConstraints(nameInputRegistration, 1, row++);
		//Surname label
		Label surnameLabel = new Label("Surname:");
		GridPane.setConstraints(surnameLabel, 0, row);
		surnameInputRegistration = new TextField();
		ClientGUI2.addTextLimiter(surnameInputRegistration, 30);
		surnameInputRegistration.setPromptText("Your surname here...");
		GridPane.setConstraints(surnameInputRegistration, 1, row++);
		//City label
		Label cityLabel = new Label("City:");
		GridPane.setConstraints(cityLabel, 0, row);
		//City input
		cityInputRegistration = new ComboBox<>();
		cityInputRegistration.setPromptText("Your city here...");
		String[] cities = client.getCityList();
		for (String c : cities){
			cityInputRegistration.getItems().add(c);
		}

		GridPane.setConstraints(cityInputRegistration, 1, row++);
		//District label
		Label districtLabel = new Label("District:");
		GridPane.setConstraints(districtLabel, 0, row);
		//District input
		districtInputRegistration = new ComboBox<>();
		districtInputRegistration.setPromptText("Your district here...");
		districtInputRegistration.setOnAction(e -> {
			selectLocationButtonRegistration.setDisable(false);
			latitudeInputRegistration.setText("");
			longitudeInputRegistration.setText("");
		});

		cityInputRegistration.setOnAction(e -> {
			String location = cityInputRegistration.getSelectionModel().getSelectedItem();
			//Prevents NullPointerException caused by emptyRegistrationTab()
			if(location != null){
				districtInputRegistration.getItems().clear();
				String[] districts = client.getDistrictList(location);
				for(String d : districts){
					districtInputRegistration.getItems().add(d);
				}
				districtInputRegistration.getSelectionModel().select(-1);
				selectLocationButtonRegistration.setDisable(true);
				latitudeInputRegistration.setText("");
				latitudeInputRegistration.setText("");
			}
		});

		GridPane.setConstraints(districtInputRegistration, 1, row++);
		//Latitude label
		Label latitudeLabel = new Label("Latitude:");
		GridPane.setConstraints(latitudeLabel, 0, row);
		//Latitude input
		latitudeInputRegistration = new TextField();
		latitudeInputRegistration.setPromptText("Your latitude here...");
		latitudeInputRegistration.setEditable(false);
		latitudeInputRegistration.setDisable(true);
		GridPane.setConstraints(latitudeInputRegistration, 1, row++);
		//Longitude label
		Label longitudeLabel = new Label("Longitude:");
		GridPane.setConstraints(longitudeLabel, 0, row);
		//Longitude input
		longitudeInputRegistration = new TextField();
		longitudeInputRegistration.setPromptText("Your longitude here...");
		longitudeInputRegistration.setEditable(false);
		longitudeInputRegistration.setDisable(true);
		GridPane.setConstraints(longitudeInputRegistration, 1, row++);
		//Button
		selectLocationButtonRegistration = new Button("Select the location", new Glyph("FontAwesome", "MAP_MARKER"));
		GridPane.setConstraints(selectLocationButtonRegistration, 0, row++, 2, 1);
		selectLocationButtonRegistration.setPrefWidth(400);
		selectLocationButtonRegistration.setDisable(true);
		selectLocationButtonRegistration.setOnAction(e -> selectLocationButtonClick("Registration"));
		//Separator
		Separator sep2 = new Separator();
		GridPane.setConstraints(sep2, 0, row++, 2, 1);
		//create account Button
		Button createButton = new Button("Create account", new Glyph("FontAwesome", "CHILD"));
		createButton.setOnAction(e -> register());
		GridPane.setConstraints(createButton, 0, row++, 2, 1);
		createButton.setPrefWidth(400);

		//insert everything in the gridPane
		grid.getChildren().addAll(sep1, usernameLabel, usernameInputRegistration, password1InputRegistration, passwordLabel1,
				password2InputRegistration, passwordLabel2, emailLabel, emailInputRegistration,
				nameLabel, nameInputRegistration, surnameLabel, surnameInputRegistration, cityLabel, cityInputRegistration,
				districtLabel, districtInputRegistration, latitudeLabel, latitudeInputRegistration, longitudeLabel, longitudeInputRegistration,
				selectLocationButtonRegistration, sep2, createButton);
		//Add nodes to the VBox
		vbox.getChildren().addAll(labelTitle, labelSubtitle, grid);
		vbox.setMinWidth(400);
		registerContent = vbox;
		return registerContent;
	}

	private boolean register() {
		String message = "Can't create your account:\n\n";
		boolean error = false;

		String username = usernameInputRegistration.getText();
		String password1 = password1InputRegistration.getText();
		String password2 = password2InputRegistration.getText();
		String email = emailInputRegistration.getText();
		String name = nameInputRegistration.getText();
		String surname = surnameInputRegistration.getText();
		String city = cityInputRegistration.getSelectionModel().getSelectedItem();
		String district = districtInputRegistration.getSelectionModel().getSelectedItem();
		String lat = latitudeInputRegistration.getText();
		String lon = longitudeInputRegistration.getText();

		if(username.equals("")){
			message += "Username is missing.\n";
			error = true;
		} else if(client.verifyUsernamePresence(username)){
			//check availability of username
			message += "The selected username is already taken.\n";
			error = true;
		}
		if(password1.equals("")){
			message += "The password in the 1st field is missing.\n";
			error = true;
		}
		if(password2.equals("")){
			message += "The password in the 2nd field is missing.\n";
			error = true;
		}
		if(!password2.equals(password1)){
			message += "The passwords you have inserted are not matching.\n";
			error = true;
		}
		if(email.equals("")){
			message += "The mail address is missing.\n";
			error = true;
		} else {
			//email validation
			EmailValidator validator = EmailValidator.getInstance();
			if(!validator.isValid(email)){
				message += "The mail has an invalid address.\n";
				error = true;
			}
		}
		if(name.equals("")){
			message += "The name is missing.\n";
			error = true;
		}
		if(surname.equals("")){
			message += "The surname is missing.\n";
			error = true;
		}
		if(city == null){
			message += "The city is missing.\n";
			error = true;
		}
		if(district == null){
			message += "The district is missing.\n";
			error = true;
		}
		if(lat.equals("")){
			message += "The latitude is missing.\n";
			error = true;
		}
		if(lon.equals("")){
			message += "The longitude is missing.\n";
			error = true;
		}

		if(error){
			AlertBox.display("ERROR", message);
		} else {
			Person p = new Person(name, surname, username, email, password1, city, district, Double.parseDouble(lat), Double.parseDouble(lon));
			if(client.insertPerson(p)){
				emptyRegistrationTab();
				AlertBox.display("SUCCESS", "Account created successfully!\n\nWe have sent you an email to the address "+email+".");
			} else {
				AlertBox.display("ERROR", "Unable to create your account, please try again later.");
			}	
		}
		return !error;
	}

	private VBox getDetailsTabContent() {
		if (detailsContent != null){
			return detailsContent;
		}
		Insets insets = new Insets(10, 10, 10, 10);
		VBox vbox = new VBox();
		Label labelTitle = new Label("Welcome in Watch Neighbors");
		labelTitle.setPadding(insets);
		Label labelSubtitle = new Label("Here you can find your details:");
		labelSubtitle.setPadding(insets);
		//GridPane
		GridPane grid = new GridPane();
		//In Insets the argument is double varargs that represent the dimensions at the sides
		grid.setPadding(insets);
		//set the vertical and horizontal gap between cells
		grid.setVgap(8);
		grid.setHgap(10);
		int row = 0;
		//Separators
		Separator sep1 = new Separator();
		GridPane.setConstraints(sep1, 0, row++, 2, 1);
		//Username label
		Label usernameLabel = new Label("Username:");
		GridPane.setConstraints(usernameLabel, 0, row);
		usernameInputDetails = new TextField();
		ClientGUI2.addTextLimiter(usernameInputDetails, 30);;
		usernameInputDetails.setEditable(false);
		GridPane.setConstraints(usernameInputDetails, 1, row++);
		//Email label
		Label emailLabel = new Label("Email:");
		GridPane.setConstraints(emailLabel, 0, row);
		emailInputDetails = new TextField();
		ClientGUI2.addTextLimiter(emailInputDetails, 30);
		emailInputDetails.setPromptText("Your email here...");
		GridPane.setConstraints(emailInputDetails, 1, row++);
		//Name label
		Label nameLabel = new Label("Name:");
		GridPane.setConstraints(nameLabel, 0, row);
		nameInputDetails = new TextField();
		ClientGUI2.addTextLimiter(nameInputDetails, 30);
		nameInputDetails.setPromptText("Your name here...");
		GridPane.setConstraints(nameInputDetails, 1, row++);
		//Surname label
		Label surnameLabel = new Label("Surname:");
		GridPane.setConstraints(surnameLabel, 0, row);
		surnameInputDetails = new TextField();
		ClientGUI2.addTextLimiter(surnameInputDetails, 30);
		surnameInputDetails.setPromptText("Your surname here...");
		GridPane.setConstraints(surnameInputDetails, 1, row++);
		//City label
		Label cityLabel = new Label("City:");
		GridPane.setConstraints(cityLabel, 0, row);
		//City input
		cityInputDetails = new ComboBox<>();
		cityInputDetails.setPromptText("Your city here...");
		String[] cities = client.getCityList();
		for (String c : cities){
			cityInputDetails.getItems().add(c);
		}
		cityInputDetails.setOnAction(e -> {
			String selection = cityInputDetails.getSelectionModel().getSelectedItem();
			if(selection != null){
				districtInputDetails.getItems().clear();
				String[] districts = client.getDistrictList(selection);
				for(String d : districts){
					districtInputDetails.getItems().add(d);
				}
				districtInputDetails.getSelectionModel().select(-1);
				selectLocationButtonDetails.setDisable(true);
				latitudeInputDetails.setText("");
				longitudeInputDetails.setText("");	
			}

		});
		GridPane.setConstraints(cityInputDetails, 1, row++);
		//District label
		Label districtLabel = new Label("District:");
		GridPane.setConstraints(districtLabel, 0, row);
		//District input
		districtInputDetails = new ComboBox<>();
		districtInputDetails.setPromptText("Your district here...");
		districtInputDetails.setOnAction(e -> {
			selectLocationButtonDetails.setDisable(false);
			latitudeInputDetails.setText("");
			longitudeInputDetails.setText("");
		});
		GridPane.setConstraints(districtInputDetails, 1, row++);
		//Latitude label
		Label latitudeLabel = new Label("Latitude:");
		GridPane.setConstraints(latitudeLabel, 0, row);
		//Latitude input
		latitudeInputDetails = new TextField();
		latitudeInputDetails.setPromptText("Your latitude here...");
		latitudeInputDetails.setEditable(false);
		latitudeInputDetails.setDisable(true);
		GridPane.setConstraints(latitudeInputDetails, 1, row++);
		//Longitude label
		Label longitudeLabel = new Label("Longitude:");
		GridPane.setConstraints(longitudeLabel, 0, row);
		//Longitude input
		longitudeInputDetails = new TextField();
		longitudeInputDetails.setPromptText("Your longitude here...");
		longitudeInputDetails.setEditable(false);
		longitudeInputDetails.setDisable(true);
		GridPane.setConstraints(longitudeInputDetails, 1, row++);
		//Button
		selectLocationButtonDetails = new Button("Select the location", new Glyph("FontAwesome", "MAP_MARKER"));
		GridPane.setConstraints(selectLocationButtonDetails, 0, row++, 2, 1);
		selectLocationButtonDetails.setPrefWidth(400);
		selectLocationButtonDetails.setDisable(true);
		selectLocationButtonDetails.setOnAction(e -> selectLocationButtonClick("Details"));
		//Separator
		Separator sep2 = new Separator();
		GridPane.setConstraints(sep2, 0, row++, 2, 1);

		//create warning Button
		Button createWarningButton = new Button("Create a new warning", new Glyph("FontAwesome", "EDIT"));
		createWarningButton.setOnAction(e -> createWarningButtonClicked());
		GridPane.setConstraints(createWarningButton, 0, row++, 2, 1);
		createWarningButton.setPrefWidth(400);

		//see warnings Button
		Button seeWarningsButton = new Button("See all the available warnings", new Glyph("FontAwesome", "EYE"));
		seeWarningsButton.setOnAction(e -> seeWarningsButtonClicked(true));
		GridPane.setConstraints(seeWarningsButton, 0, row++, 2, 1);
		seeWarningsButton.setPrefWidth(400);
		//see the warnings that the user is working on
		Button seeOwnWorkInProgressButton = new Button("See warnings you are working on", new Glyph("FontAwesome", "EYE"));
		seeOwnWorkInProgressButton.setOnAction(e -> seeOwnWorkInProgressButtonClicked());
		GridPane.setConstraints(seeOwnWorkInProgressButton, 0, row++, 2, 1);
		seeOwnWorkInProgressButton.setPrefWidth(400);
		//Separator
		Separator sep3 = new Separator();
		GridPane.setConstraints(sep3, 0, row++, 2, 1);
		//modify Button
		Button modifyButton = new Button("Modify account");
		modifyButton.setOnAction(e->modifyAccountButtonClicked());
		GridPane.setConstraints(modifyButton, 0, row++, 2, 1);
		modifyButton.setPrefWidth(400);
		//delete account Button
		Button deleteAccountButton = new Button("Delete account");
		GridPane.setConstraints(deleteAccountButton, 0, row++, 2, 1);
		deleteAccountButton.setPrefWidth(400);
		deleteAccountButton.setOnAction(e -> deleteAccountClicked());
		//Separator
		Separator sep4 = new Separator();
		GridPane.setConstraints(sep4, 0, row++, 2, 1);
		//logout Button
		Button logoutButton = new Button("Log out");
		logoutButton.setOnAction(e -> logOut());
		GridPane.setConstraints(logoutButton, 0, row++, 2, 1);
		logoutButton.setPrefWidth(400);
		//insert everything in the gridPane
		grid.getChildren().addAll(sep1, usernameLabel, usernameInputDetails, emailLabel, emailInputDetails, nameLabel, nameInputDetails, surnameLabel, surnameInputDetails, cityLabel, cityInputDetails, districtLabel, districtInputDetails, latitudeLabel, latitudeInputDetails, longitudeLabel, longitudeInputDetails, selectLocationButtonDetails, sep2,
				createWarningButton, seeWarningsButton, seeOwnWorkInProgressButton, sep3, modifyButton, deleteAccountButton, sep4, logoutButton);
		//Add nodes to the VBox
		vbox.getChildren().addAll(labelTitle, labelSubtitle, grid);
		vbox.setMinWidth(400);
		detailsContent = vbox;
		return detailsContent;
	}

	private void modifyAccountButtonClicked() {
		boolean modified = false;
		String message = "";
		if(nameInputDetails.getText().equals("")){
			modified = true;
			message += "Name can't be empty\n";
		} else if(!originalName.equals(nameInputDetails.getText())){
			modified = true;
		}
		
		if(surnameInputDetails.getText().equals("")){
			modified = true;
			message += "Surname can't be empty\n";
		} else if(!originalSurname.equals(surnameInputDetails.getText())){
			modified = true;
		}
		
		EmailValidator validator = EmailValidator.getInstance();
		if(emailInputDetails.getText().equals("")){
			modified = true;
			message += "Email address can't be empty\n";
		} else if(!validator.isValid(emailInputDetails.getText())){
			modified = true;
			message += "Email address not valid\n";
		} else if(!originalEmail.equals(emailInputDetails.getText())){
			modified = true;
		}
		
		if(cityInputDetails.getSelectionModel().getSelectedIndex()<0){
			modified = true;
			message += "City not selected\n";
		} else if(!cityInputDetails.getSelectionModel().getSelectedItem().equals(originalCity)){
			modified = true;
		}
		
		if(districtInputDetails.getSelectionModel().getSelectedIndex()<0){
			modified = true;
			message += "District not selected\n";
		} else if(!districtInputDetails.getSelectionModel().getSelectedItem().equals(originalDistrict)){
			modified = true;
		}
		
		if(latitudeInputDetails.getText().equals("")){
			modified = true;
			message += "Latitude not selected\n";
		} else if(!latitudeInputDetails.getText().equals(originalLat)){
			modified = true;
		}
		
		if(longitudeInputDetails.getText().equals("")){
			modified = true;
			message += "Longitude not selected\n";
		} else if(!longitudeInputDetails.getText().equals(originalLon)){
			modified = true;
		}
		
		if(modified){
			if(message.length()>0){
				//errors
				AlertBox.display("ERROR", "There are some errors in your data.\nPlease correct the following:\n\n"+(message.substring(0, message.length()-1)));
			} else {
				//no errors
				originalName = nameInputDetails.getText();
				originalSurname = surnameInputDetails.getText();
				originalEmail = emailInputDetails.getText();
				originalCity = cityInputDetails.getSelectionModel().getSelectedItem();
				originalDistrict = districtInputDetails.getSelectionModel().getSelectedItem();
				originalLat = latitudeInputDetails.getText();
				originalLon = longitudeInputDetails.getText();
				if(client.updatePerson(originalName, originalSurname, usernameInputDetails.getText(), 
						originalEmail, originalPassword, originalCity,
						originalDistrict, Double.parseDouble(originalLat), Double.parseDouble(originalLon))){
					//update successfull
					AlertBox.display("Success", "Your account details have been successfully modified.");
				} else {
					AlertBox.display("Error", "Sorry, it appears there has been an error processing your request.\nYour details have not been changed.");
				}
			}
		} else {
			AlertBox.display("No modification", "You haven't modified any data so far.");
		}
	}

	private void createWarningButtonClicked() {
		Report reportToBeInserted = AlertBox.displayCreateReport(usernameInputDetails.getText(), Double.parseDouble(latitudeInputDetails.getText()), Double.parseDouble(longitudeInputDetails.getText()), client);
		if(reportToBeInserted!=null){
			if(client.insertReport(reportToBeInserted)){
				AlertBox.display("Success", "Warning created successfully");
			} else {
				AlertBox.display("Error", "Sorry, couldn't process ypur request");
			}
		} else {
			AlertBox.display("Error", "Sorry, couldn't process ypur request");
		}
	}

	private void seeOwnWorkInProgressButtonClicked() {
		//show only what the user is contributing atm
		DialogSeeWarningsWorkInProgress.display(usernameInputDetails.getText(), client);
	}

	private void logOut(){
		client.logout();
		tabLogin.setDisable(false);
		tabDetails.setDisable(true);
		tabRegistration.setDisable(false);
		tabPane.getSelectionModel().select(tabLogin);
		emptyDetailsTab();
	}

	private void deleteAccountClicked() {
		boolean answer = DeleteAccountBox.display(usernameInputDetails.getText());
		if(answer){
			//delete account
			if(client.deleteAccount(usernameInputDetails.getText())){
				//if successfully deleted
				AlertBox.display("Success", "Your account has been successfully deleted.");
				logOut();
			}
		} else {
			AlertBox.display("Operation not completed", "Account deletion aborted.");
		}
	}

	private void seeWarningsButtonClicked(boolean loggedIn) {
		if(loggedIn){
			DialogSeeWarnings.display(loggedIn, client, usernameInputDetails.getText());
		} else {
			DialogSeeWarnings.display(loggedIn, client, null);
		}
	}

	private void selectLocationButtonClick(String tab) {
		Coordinate nw = null, se = null;
		TextField longitudeInput = null, latitudeInput = null;
		Coordinate[] coord;
		switch (tab){
		case "Registration":
			//nw = ApplicationParameters.getCoordinates((String)cityInputRegistration.getSelectionModel().getSelectedItem(), (String)districtInputRegistration.getSelectionModel().getSelectedItem())[0];
			//se = ApplicationParameters.getCoordinates((String)cityInputRegistration.getSelectionModel().getSelectedItem(), (String)districtInputRegistration.getSelectionModel().getSelectedItem())[1];
			coord = client.getCoordinates((String)cityInputRegistration.getSelectionModel().getSelectedItem(), (String)districtInputRegistration.getSelectionModel().getSelectedItem());
			nw = coord[0];
			se = coord[1];
			latitudeInput = latitudeInputRegistration;
			longitudeInput = longitudeInputRegistration;
			break;
		case "Details":
			//nw = ApplicationParameters.getCoordinates((String)cityInputDetails.getSelectionModel().getSelectedItem(), (String)districtInputDetails.getSelectionModel().getSelectedItem())[0];
			//se = ApplicationParameters.getCoordinates((String)cityInputDetails.getSelectionModel().getSelectedItem(), (String)districtInputDetails.getSelectionModel().getSelectedItem())[1];
			coord = client.getCoordinates((String)cityInputDetails.getSelectionModel().getSelectedItem(), (String)districtInputDetails.getSelectionModel().getSelectedItem());
			nw = coord[0];
			se = coord[1];
			latitudeInput = latitudeInputDetails;
			longitudeInput = longitudeInputDetails;
			break;
		}
		System.out.println("NW is "+nw+"and SE is "+se);
		if(nw != null && se != null){
			Coordinate coords = DialogSelectLocation.display(nw, se);
			if (coords!=null){
				latitudeInput.setText(Double.toString(coords.getLat()));
				longitudeInput.setText(Double.toString(coords.getLon()));
			}
		}
	}

	private void populateDetailsTab(Person p){
		nameInputDetails.setText(p.getName());
		originalName = p.getName();
		surnameInputDetails.setText(p.getSurname());
		originalSurname = p.getSurname();
		usernameInputDetails.setText(p.getUserID());
		emailInputDetails.setText(p.getEmail());
		originalEmail = p.getEmail();
		cityInputDetails.getSelectionModel().select(p.getCity());
		originalCity = p.getCity();
		districtInputDetails.getSelectionModel().select(p.getDistrict());
		originalDistrict = p.getDistrict();
		latitudeInputDetails.setText(Double.toString(p.getLatitude()));
		originalLat = Double.toString(p.getLatitude());
		longitudeInputDetails.setText(Double.toString(p.getLongitude()));
		originalLon = Double.toString(p.getLongitude());
		originalPassword = p.getPassword();
		selectLocationButtonDetails.setDisable(false);
	}

	private void emptyLoginTab(){
		usernameInputLogin.setText("");
		pswInputLogin.setText("");
	}

	private void emptyDetailsTab(){
		nameInputDetails.setText("");
		surnameInputDetails.setText("");
		usernameInputDetails.setText("");
		emailInputDetails.setText("");
		districtInputDetails.getSelectionModel().select(-1);
		cityInputDetails.getSelectionModel().select(-1);
		latitudeInputDetails.setText("");
		longitudeInputDetails.setText("");
		selectLocationButtonDetails.setDisable(true);

		originalCity = "";
		originalDistrict = "";
		originalEmail = "";
		originalLat = "";
		originalLon = "";
		originalName = "";
		originalSurname = "";
		originalPassword = "";
	}

	private void emptyRegistrationTab(){
		nameInputRegistration.setText("");
		surnameInputRegistration.setText("");
		usernameInputRegistration.setText("");
		password1InputRegistration.clear();
		password2InputRegistration.clear();
		emailInputRegistration.setText("");
		districtInputRegistration.getSelectionModel().select(-1);
		cityInputRegistration.getSelectionModel().select(-1);
		latitudeInputRegistration.setText("");
		longitudeInputRegistration.setText("");
		selectLocationButtonRegistration.setDisable(true);
	}
	
	private static void addTextLimiter(final TextField tf, final int maxLength) {
	    tf.textProperty().addListener(new ChangeListener<String>() {
	        @Override
	        public void changed(final ObservableValue<? extends String> ov, final String oldValue, final String newValue) {
	            if (tf.getText().length() > maxLength) {
	                String s = tf.getText().substring(0, maxLength);
	                tf.setText(s);
	            }
	        }
	    });
	}


	/**
	 * Method to launch the application
	 * @param args unused parameter
	 */
	public static void main(String[] args) {
		launch(args);
	}

}
