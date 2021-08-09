package classes;

import org.openstreetmap.gui.jmapviewer.Coordinate;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * @author Mattia Nangeroni
 * @author Massimiliano Calicchio
 * @author Simone Bogni
 * 
 * A class used to create alert boxes.
 * 
 */
public class AlertBox {

	private static boolean operationAborted = false;
	private static Report reportCreated = null;

	/**It displays a box with an alert
	 * @param title The title to give to the alert box
	 * @param message The message to write in the alert box
	 */
	public static void display(String title, String message){
		Stage window = new Stage();
		window.initModality(Modality.APPLICATION_MODAL);
		window.setTitle(title);
		window.setMinWidth(420);
		Label label = new Label();
		label.setText(message);
		label.setPadding(new Insets(10, 10, 10, 10));
		window.setOnCloseRequest(e -> {
			e.consume();
			window.close();
		});
		VBox layout = new VBox(10);
		layout.getChildren().add(label);
		layout.setAlignment(Pos.CENTER);
		Scene scene = new Scene(layout);
		window.setScene(scene);
		window.getIcons().add(new Image(ClientGUI2.class.getResourceAsStream("/img/eye.gif")));
		window.showAndWait();
	}

	
	/** It displays an alert box to select the status for the report to close
	 * @return the status selected
	 */
	public static String displayCloseReport(){
		operationAborted = false;
		Stage window = new Stage();
		window.initModality(Modality.APPLICATION_MODAL);
		window.setTitle("Close report");
		window.setMinWidth(420);
		Label label = new Label();
		label.setText("Choose the closing status for the report");
		label.setPadding(new Insets(10, 10, 10, 10));
		ComboBox<String> reportStatuses = new ComboBox<String>();
		reportStatuses.setPromptText("Report status");
		String[] repS = ApplicationParameters.getReportStatus();
		for(String s : repS){
			reportStatuses.getItems().add(s);
		}
		window.setOnCloseRequest(e -> {
			operationAborted = true;
			e.consume();
			window.close();
		});

		Button select = new Button();
		select.setOnAction(e -> {
			if(reportStatuses.getSelectionModel().getSelectedItem()==null){
				AlertBox.display("Error", "Status not selected");
			} else {
				e.consume();
				window.close();
			}
		});
		VBox layout = new VBox(10);
		layout.getChildren().addAll(label, reportStatuses, select);
		layout.setAlignment(Pos.CENTER);
		Scene scene = new Scene(layout);
		window.setScene(scene);
		window.getIcons().add(new Image(ClientGUI2.class.getResourceAsStream("/img/eye.gif")));
		window.showAndWait();
		if(operationAborted){
			return null;
		}
		return reportStatuses.getSelectionModel().getSelectedItem();
	}

	/** It displays an alert box in which to put the details of the report to create
	 * @param userID username of the user creating the report
	 * @param userLatitude latitude of the user creating the report
	 * @param userLongitude longitude of the user creating the report
	 * @param client the instance of client currently used
	 * @return the newly created report
	 */
	public static Report displayCreateReport(String userID, double userLatitude, double userLongitude, Client client){
		reportCreated = null;
		operationAborted = false;
		
		double labelWidth = 132.0;
		
		Stage window = new Stage();
		window.initModality(Modality.APPLICATION_MODAL);
		window.setTitle("Open new report");
		window.setMinWidth(420);
		window.setOnCloseRequest(e -> {
			operationAborted = true;
			e.consume();
			window.close();
		});

		HBox hbox1 = new HBox(10);
		Label label = new Label();
		label.setText("What has happened?");
		label.setPrefWidth(labelWidth);;
		ComboBox<String> reportCauses = new ComboBox<String>();
		reportCauses.setPromptText("Report causes");
		String[] repS = ApplicationParameters.getReportCauses();
		for(String s : repS){
			reportCauses.getItems().add(s);
		}
		reportCauses.setMaxWidth(Double.MAX_VALUE);
		hbox1.getChildren().addAll(label, reportCauses);
		HBox.setHgrow(reportCauses, Priority.SOMETIMES);

		HBox hbox2 = new HBox(10);
		Label labelCity = new Label();
		labelCity.setText("City");
		labelCity.setPrefWidth(labelWidth);
		//City input
		ComboBox<String> cityInput = new ComboBox<>();
		cityInput.setPromptText("Choose city...");
		cityInput.getItems().addAll("Gavirate", "Varese");
		cityInput.setMaxWidth(Double.MAX_VALUE);
		hbox2.getChildren().addAll(labelCity, cityInput);
		HBox.setHgrow(cityInput, Priority.SOMETIMES);
		
		HBox hbox3 = new HBox(10);
		Label labelDistrict = new Label("District");
		labelDistrict.setPrefWidth(labelWidth);
		ComboBox<String> districtInput = new ComboBox<>();
		districtInput.setPromptText("Choose district...");
		districtInput.setMaxWidth(Double.MAX_VALUE);
		hbox3.getChildren().addAll(labelDistrict, districtInput);
		HBox.setHgrow(districtInput, Priority.SOMETIMES);
		
		HBox hbox4 = new HBox(10);
		Label labelLat = new Label("Latitude");
		labelLat.setPrefWidth(labelWidth);
		TextField textLat = new TextField("");
		textLat.setEditable(false);
		textLat.setMaxWidth(Double.MAX_VALUE);
		hbox4.getChildren().addAll(labelLat, textLat);
		HBox.setHgrow(textLat, Priority.SOMETIMES);

		HBox hbox5 = new HBox(10);
		Label labelLon = new Label("Longitude");
		labelLon.setPrefWidth(labelWidth);
		TextField textLon = new TextField("");
		textLon.setEditable(false);
		textLon.setMaxWidth(Double.MAX_VALUE);
		hbox5.getChildren().addAll(labelLon, textLon);
		HBox.setHgrow(textLon, Priority.SOMETIMES);

		Button selectLocation = new Button("Select location");
		
		
		cityInput.setOnAction(e -> {
			String selection = cityInput.getSelectionModel().getSelectedItem();
			if(selection != null){
				switch(cityInput.getSelectionModel().getSelectedItem()){
				case "Gavirate":
					districtInput.getItems().clear();
					districtInput.getItems().addAll("Gavirate centro", "Fignano", "Oltrona al Lago", "Voltorre");
					break;
				case "Varese":
					districtInput.getItems().clear();
					districtInput.getItems().addAll("Avigno", "Calcinate del pesce", "Masnago", "Schiranna", "Varese centro");
					break;
				default:
					break;
				}
				districtInput.getSelectionModel().select(-1);
				selectLocation.setDisable(true);
				textLat.setText("");
				textLon.setText("");	
			}

		});
		
		districtInput.setOnAction(e -> {
			selectLocation.setDisable(false);
			textLat.setText("");
			textLon.setText("");
		});
		
		selectLocation.setOnAction(e -> {
			Coordinate nw = null, se = null;
			Coordinate[] coord = client.getCoordinates((String)cityInput.getSelectionModel().getSelectedItem(), (String)districtInput.getSelectionModel().getSelectedItem());
			nw = coord[0];
			se = coord[1];
			if(nw != null && se != null){
				Coordinate coords = DialogSelectLocation.display(nw, se);
				if (coords!=null){
					textLat.setText(Double.toString(coords.getLat()));
					textLon.setText(Double.toString(coords.getLon()));
				}
			}
		});

		Button create = new Button("Create a new warning");
		create.setOnAction(e -> {
			if(reportCauses.getSelectionModel().getSelectedItem()==null){
				AlertBox.display("Error", "Status not selected");
			} else if(textLat.getText().equals("")||textLon.getText().equals("")) {
				AlertBox.display("Error", "Coordinates not selected");
			} else {
				
				reportCreated = new Report(
						cityInput.getSelectionModel().getSelectedItem(),
						districtInput.getSelectionModel().getSelectedItem(),
						userID,
						userLatitude,
						userLongitude,
						Double.parseDouble(textLat.getText()),
						Double.parseDouble(textLon.getText()),
						reportCauses.getSelectionModel().getSelectedItem());
				e.consume();
				window.close();
			}
		});
		
		VBox layout = new VBox(10);
		layout.setPadding(new Insets(10));
		layout.getChildren().addAll(hbox1, hbox2, hbox3, hbox4, hbox5, selectLocation, create);
		layout.setAlignment(Pos.CENTER);
		
		Scene scene = new Scene(layout);
		window.setScene(scene);
		window.getIcons().add(new Image(ClientGUI2.class.getResourceAsStream("/img/eye.gif")));
		window.showAndWait();
		
		return reportCreated;
	}

}
