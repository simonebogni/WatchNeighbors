package classes;

import java.awt.Color;
import java.awt.Dimension;
import java.sql.Timestamp;
import java.util.Calendar;

import javax.swing.JPanel;

import org.openstreetmap.gui.jmapviewer.Coordinate;
import org.openstreetmap.gui.jmapviewer.JMapViewerTree;
import org.openstreetmap.gui.jmapviewer.MapMarkerDot;
import org.openstreetmap.gui.jmapviewer.MapRectangleImpl;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingNode;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * @author Mattia Nangeroni
 * @author Massimiliano Calicchio
 * @author Simone Bogni
 * 
 * The class DialogSeeWarnDialogSeeWarningsWorkInProgressings is used to show the location of the Reports that a certain user is currently working on
 */
public class DialogSeeWarningsWorkInProgress {

	private static ComboBox<String> cityInput;
	private static ComboBox<String> districtInput;
	private static TableView<Warnings> warningsTable;
	private final static ObservableList<Warnings> warningsList = FXCollections.observableArrayList();
	private static JMapViewerTree map;
	
	private static Client client;
	private static String username;

	
	
	protected static Client getClient() {
		return client;
	}

	protected static String getUsername() {
		return username;
	}

	
	/**
	 * It displays the window with the map for the location of the reports a specified user is currently working on
	 *
	 * @param username username of the user
	 * @param client the current instance of the Client in ClientGUI2
	 */
	public static void display(String username, Client client){
		DialogSeeWarningsWorkInProgress.client = client;
		DialogSeeWarningsWorkInProgress.username = username;
		Stage window = new Stage();
		window.initModality(Modality.APPLICATION_MODAL);
		window.setTitle("Warnings you are managing");
		window.getIcons().add(new Image(ClientGUI2.class.getResourceAsStream("/img/eye.gif")));
		window.setMinWidth(600);
		window.setMinHeight(600);
		window.setWidth(600);
		window.setHeight(600);
		window.setMaxWidth(600);
		window.setMaxHeight(600);

		final SwingNode swingNodeMap = new SwingNode();

		//create 2 buttons
		Button selectButton = new Button("Select location");
		selectButton.setDisable(true);
		selectButton.setOnAction(e -> {

			//GET COORDINATES AND REPORT[] FROM CLIENT PARAMETER
			String city = (String)cityInput.getSelectionModel().getSelectedItem();
			String district = (String)districtInput.getSelectionModel().getSelectedItem();
			Coordinate[] coords = client.getCoordinates(city, district);
			Report[] reports = client.getReportListUserWorkInProgress(username);

			createAndSetSwingContent(swingNodeMap, coords[0], coords[1], reports);
		});

		//create the comboboxes
		cityInput = new ComboBox<>();
		cityInput.setPromptText("Your city here...");
		String[] cities = client.getCityList();
		for (String c : cities){
			cityInput.getItems().add(c);
		}
		cityInput.setOnAction(e -> {
			districtInput.getItems().clear();
			String[] districts = client.getDistrictList(cityInput.getSelectionModel().getSelectedItem());
			for(String d : districts){
				districtInput.getItems().add(d);
			}
			districtInput.getSelectionModel().select(-1);
			selectButton.setDisable(true);
		});

		districtInput = new ComboBox<>();
		districtInput.setPromptText("Your district here...");
		districtInput.setOnAction(e -> {
			selectButton.setDisable(false);
		});

		window.setOnCloseRequest(e -> {
			e.consume();
			window.close();
		});
		HBox hbox = new HBox();
		hbox.setAlignment(Pos.CENTER);
		hbox.setPadding(new Insets(10, 30, 10, 30));
		hbox.setSpacing(30);
		hbox.getChildren().addAll(cityInput, districtInput, selectButton);

		warningsTable = new TableView<Warnings>();
		warningsTable.setEditable(false);


		HBox hbox2 = new HBox();
		hbox2.setAlignment(Pos.CENTER);
		hbox2.setPadding(new Insets(10, 30, 10, 30));
		hbox2.setSpacing(30);
		hbox2.getChildren().addAll(warningsTable);

		VBox vbox = new VBox(10);
		vbox.getChildren().addAll(swingNodeMap, hbox, hbox2);
		vbox.setAlignment(Pos.CENTER);
		Scene scene = new Scene(vbox);
		window.setScene(scene);
		window.showAndWait();

	}

	private static void createAndSetSwingContent(final SwingNode swingNode, final Coordinate NW, final Coordinate SE, final Report[] reports) {
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				map = createMapPanel(NW, SE, reports);
				map.setSize(new Dimension(600, 400));
				map.setPreferredSize(new Dimension(600, 400));
				map.setMinimumSize(new Dimension(600, 400));
				map.setMaximumSize(new Dimension(600, 400));

				JPanel mapPanel = new JPanel();
				mapPanel.setSize(600, 400);
				mapPanel.setPreferredSize(new Dimension(600, 400));
				mapPanel.setMinimumSize(new Dimension(600, 400));
				mapPanel.setMaximumSize(new Dimension(600, 400));
				mapPanel.add(map);
				swingNode.setContent(mapPanel);
			}
		});
	}

	@SuppressWarnings("unchecked")
	private static JMapViewerTree createMapPanel(Coordinate NW, Coordinate SE, Report[] reports){
		warningsList.clear();
		JMapViewerTree mapViewerTreeSelectLocation = new JMapViewerTree((String) null);
		//create boundaries markers and rectangle
		MapMarkerDot markerNW = new MapMarkerDot(Color.RED, NW.getLat(), NW.getLon());
		MapMarkerDot markerNE = new MapMarkerDot(Color.RED, NW.getLat(), SE.getLon());
		MapMarkerDot markerSE = new MapMarkerDot(Color.RED, SE.getLat(), SE.getLon());
		MapMarkerDot markerSW = new MapMarkerDot(Color.RED, SE.getLat(), NW.getLon());
		markerNW.setBackColor(Color.RED);
		markerNE.setBackColor(Color.RED);
		markerSE.setBackColor(Color.RED);
		markerSW.setBackColor(Color.RED);
		mapViewerTreeSelectLocation.getViewer().addMapMarker(markerNW);
		mapViewerTreeSelectLocation.getViewer().addMapMarker(markerNE);
		mapViewerTreeSelectLocation.getViewer().addMapMarker(markerSE);
		mapViewerTreeSelectLocation.getViewer().addMapMarker(markerSW);
		mapViewerTreeSelectLocation.getViewer().addMapRectangle(new MapRectangleImpl(NW, SE));

		//center the position and zoom
		mapViewerTreeSelectLocation.getViewer().setDisplayPosition(new Coordinate((NW.getLat()+SE.getLat())/2, (NW.getLon()+SE.getLon())/2), 14);

		//add markers for warnings
		if(reports != null){
			for(Report report : reports){
				MapMarkerDot marker = new MapMarkerDot(Color.white, report.getReportLatitude(), report.getReportLongitude());
				warningsList.add(new Warnings(marker, report));
				mapViewerTreeSelectLocation.getViewer().addMapMarker(marker);
			}
			//put columns
			warningsTable.setItems(warningsList);
			if(warningsTable.getColumns().size()==0){
				//create tableView columns
				//City column <TypeOfDataOfTheTable, TypeOfDataOfTheColumn>
				TableColumn<Warnings, String> columnCity = new TableColumn<>("City");
				columnCity.setMinWidth(200);
				columnCity.setCellValueFactory(new PropertyValueFactory<>("city"));//for the value use the name property of the object
				//District column
				TableColumn<Warnings, String> columnDistrict = new TableColumn<>("District");
				columnDistrict.setMinWidth(200);
				columnDistrict.setCellValueFactory(new PropertyValueFactory<>("district"));
				//Report ID
				TableColumn<Warnings, Integer> columnReportId = new TableColumn<>("Report ID");
				columnReportId.setMinWidth(50);
				columnReportId.setCellValueFactory(new PropertyValueFactory<>("reportID"));
				//User ID
				TableColumn<Warnings, String> columnUserId = new TableColumn<>("User ID");
				columnUserId.setMinWidth(100);
				columnUserId.setCellValueFactory(new PropertyValueFactory<>("userID"));
				//User Latitude
				TableColumn<Warnings, Double> columnUserLat = new TableColumn<>("User Lat");
				columnUserLat.setMinWidth(100);
				columnUserLat.setCellValueFactory(new PropertyValueFactory<>("userLatitude"));
				//User Longitude
				TableColumn<Warnings, Double> columnUserLon = new TableColumn<>("User Lon");
				columnUserLon.setMinWidth(100);
				columnUserLon.setCellValueFactory(new PropertyValueFactory<>("userLongitude"));
				//Report Latitude
				TableColumn<Warnings, Double> columnReportLat = new TableColumn<>("Report Lat");
				columnReportLat.setMinWidth(100);
				columnReportLat.setCellValueFactory(new PropertyValueFactory<>("reportLatitude"));
				//Report Longitude
				TableColumn<Warnings, Double> columnReportLon = new TableColumn<>("Report Lon");
				columnReportLon.setMinWidth(100);
				columnReportLon.setCellValueFactory(new PropertyValueFactory<>("reportLongitude"));
				//Report timestamp
				TableColumn<Warnings, Timestamp> columnTimestamp = new TableColumn<>("Creation date");
				columnTimestamp.setMinWidth(100);
				columnTimestamp.setCellValueFactory(new PropertyValueFactory<>("timestamp"));
				//Report cause
				TableColumn<Warnings, String> columnCause = new TableColumn<>("Report cause");
				columnCause.setMinWidth(100);
				columnCause.setCellValueFactory(new PropertyValueFactory<>("reportCause"));
				//Report status
				TableColumn<Warnings, String> columnStatus = new TableColumn<>("Report status");
				columnStatus.setMinWidth(100);
				columnStatus.setCellValueFactory(new PropertyValueFactory<>("reportStatus"));
				//User working on it
				TableColumn<Warnings, String> columnUserWorkInProgress = new TableColumn<>("User working on it");
				columnUserWorkInProgress.setMinWidth(100);
				columnUserWorkInProgress.setCellValueFactory(new PropertyValueFactory<>("userWorkInProgress"));
				//Button show
				TableColumn<Warnings, Button> columnButtonShow = new TableColumn<>("Show the marker");
				columnButtonShow.setMinWidth(50);
				columnButtonShow.setCellValueFactory(new PropertyValueFactory<>("buttonShow"));
				//Button close
				TableColumn<Warnings, Button> columnButtonClose = new TableColumn<>("Close the report");
				columnButtonClose.setMinWidth(100);
				columnButtonClose.setCellValueFactory(new PropertyValueFactory<>("buttonClose"));

				warningsTable.getColumns().addAll(columnCity, columnDistrict, columnReportId, columnUserId, columnUserLat,
						columnUserLon, columnReportLat, columnReportLon, columnTimestamp, columnCause, columnStatus,
						columnUserWorkInProgress, columnButtonShow, columnButtonClose);

			}
			resetMarkerColor();
		}

		return mapViewerTreeSelectLocation;
	}

	private static void setMarkerColor(MapMarkerDot marker, String reportCause){
		Color color = ApplicationParameters.getColor(reportCause);
		marker.setBackColor(color);
	}

	protected static void resetMarkerColor(){
		warningsTable.getItems().forEach(item -> setMarkerColor(item.getMarker(), item.getReportCause()));
	}

	public static class Warnings{
		private Report report;
		private MapMarkerDot marker;
		private String city;
		private String district;
		private int reportID;
		private String userID;
		private double userLatitude;
		private double userLongitude;
		private double reportLatitude;
		private double reportLongitude;
		private Timestamp timestamp;
		private String reportCause;
		private String reportStatus;
		private String userWorkInProgress;
		private Button buttonShow;
		private Button buttonClose;

		public Warnings(){
			this.report = null;
			this.marker = null;
			this.city = "";
			this.district = "";
			this.reportID = 0;
			this.userID = "";
			this.userLatitude = 0.0;
			this.userLongitude = 0.0;
			this.reportLatitude = 0.0;
			this.reportLongitude = 0.0;
			Calendar calendar = Calendar.getInstance();
			java.util.Date now = calendar.getTime();
			this.timestamp = new Timestamp(now.getTime());
			this.reportCause = "";
			this.reportStatus = "";
			this.userWorkInProgress = "";
			this.buttonShow = null;
			this.buttonClose = null;
		}

		public Warnings(MapMarkerDot marker, Report report){
			this.report = report;
			this.marker = marker;
			this.city = report.getCity();
			this.district = report.getDistrict();
			this.reportID = report.getReportID();
			this.userID = report.getUserID();
			this.userLatitude = report.getUserLatitude();
			this.userLongitude = report.getUserLongitude();
			this.reportLatitude = report.getReportLatitude();
			this.reportLongitude = report.getReportLongitude();
			this.timestamp = report.getTimestamp();
			this.reportCause = report.getReportCause();
			this.reportStatus = report.getReportStatus();
			this.userWorkInProgress = report.getUserWorkInProgress();
			this.buttonShow = new Button("Show");
			buttonShow.setOnAction(e -> {
				DialogSeeWarningsWorkInProgress.resetMarkerColor();
				marker.setBackColor(Color.BLACK);
				map.repaint();
			});
			buttonShow.setPrefWidth(90);
			this.buttonClose = new Button("Close");
			buttonClose.setOnAction(e -> {
				String result = AlertBox.displayCloseReport();
				if(result == null){
					AlertBox.display("Error", "Operation aborted");
				} else {
					if(closeReport(result)){
						AlertBox.display("Success", "Report closed successfully");
					} else {
						AlertBox.display("Error", "An error has occurred while processing your request");
					}
				}
			});
			buttonClose.setPrefWidth(90);
		}
		
		private boolean closeReport(String reportStatus){
			return DialogSeeWarningsWorkInProgress.getClient().closeReport(String.valueOf(report.getReportID()), report.getReportCause(), reportStatus, DialogSeeWarningsWorkInProgress.getUsername());
		}
		
		public MapMarkerDot getMarker() {
			return marker;
		}

		public String getCity() {
			return city;
		}

		public String getDistrict() {
			return district;
		}

		public int getReportID() {
			return reportID;
		}

		public String getUserID() {
			return userID;
		}

		public double getUserLatitude() {
			return userLatitude;
		}

		public double getUserLongitude() {
			return userLongitude;
		}

		public double getReportLatitude() {
			return reportLatitude;
		}

		public double getReportLongitude() {
			return reportLongitude;
		}

		public Timestamp getTimestamp() {
			return timestamp;
		}

		public String getReportCause() {
			return reportCause;
		}

		public String getReportStatus() {
			return reportStatus;
		}

		public String getUserWorkInProgress() {
			return userWorkInProgress;
		}

		public Button getButtonShow() {
			return buttonShow;
		}

		public Button getButtonClose() {
			return buttonClose;
		}
	}
}
