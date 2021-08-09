package classes;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import org.openstreetmap.gui.jmapviewer.Coordinate;
import org.openstreetmap.gui.jmapviewer.JMapViewer;
import org.openstreetmap.gui.jmapviewer.JMapViewerTree;
import org.openstreetmap.gui.jmapviewer.MapMarkerDot;
import org.openstreetmap.gui.jmapviewer.MapRectangleImpl;

import javafx.embed.swing.SwingNode;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
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
 * The class DialogSelectLocation is used to display a window in which the user can show a location
 *
 */
public class DialogSelectLocation {
	
	private static Coordinate selectedCoords;
	private static MapMarkerDot positionMarker=null;
	
	/**
	 * It displays a window in which the user can select a location.
	 * The area in which to choose is determined by the coordinates of the NW and SE points.
	 * The selection of any area outside the boundaries is not allowed.
	 * @param NW the Coordinates of the NorthWest point
	 * @param SE the Coordinates of the SouthEst point
	 * @return the Coordinate representing the chosen point
	 */
	public static Coordinate display(Coordinate NW, Coordinate SE){
		Stage window = new Stage();
		window.initModality(Modality.APPLICATION_MODAL);
		window.setTitle("Select the location");
		window.getIcons().add(new Image(ClientGUI2.class.getResourceAsStream("/img/eye.gif")));
		window.setMinWidth(600);
		window.setMinHeight(600);
		window.setWidth(600);
		window.setHeight(600);
		window.setMaxWidth(600);
		window.setMaxHeight(600);
		
		final SwingNode swingNodeMap = new SwingNode();
        createAndSetSwingContent(swingNodeMap, NW, SE);
		
		//create 2 buttons
		Button selectButton = new Button("Select location");
		Button cancelButton = new Button("Cancel");
		selectButton.setOnAction(e -> {
			if(selectedCoords != null){
				window.close();
			}
		});
		cancelButton.setOnAction(e -> {
			selectedCoords = null;
			window.close();
		});
		window.setOnCloseRequest(e -> {
			e.consume();
			selectedCoords = null;
			window.close();
		});
		HBox hbox = new HBox();
		hbox.setAlignment(Pos.CENTER);
		hbox.setPadding(new Insets(10, 30, 10, 30));
		hbox.setSpacing(30);
		hbox.getChildren().addAll(selectButton, cancelButton);
		
		VBox vbox = new VBox(10);
		vbox.getChildren().addAll(swingNodeMap, hbox);
		vbox.setAlignment(Pos.CENTER);
		Scene scene = new Scene(vbox);
		window.setScene(scene);
		window.showAndWait();
		
		return selectedCoords;
	}
	
	private static void createAndSetSwingContent(final SwingNode swingNode, final Coordinate NW, final Coordinate SE) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
            	JMapViewerTree map = createMapPanel(NW, SE);
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

	private static JMapViewerTree createMapPanel(Coordinate NW, Coordinate SE){
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
		
		//add mouse listener to the map for mouse left click
		mapViewerTreeSelectLocation.getViewer().addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				
				//check if the button pressend is the left one
				if (e.getButton() == MouseEvent.BUTTON1){
					
					//retrieve reference to map, get the cardinal boundaries and the current selection coordinates
					JMapViewer mapSelectLocation = mapViewerTreeSelectLocation.getViewer();
					double westLimit = NW.getLon();
					double eastLimit = SE.getLon();
					double northLimit = NW.getLat();
					double southLimit = SE.getLat();
					double selectedPositionLon = mapSelectLocation.getPosition(e.getPoint()).getLon();
					double selectedPositionLat = mapSelectLocation.getPosition(e.getPoint()).getLat();
					
					//the selection coordinates has to be inside the rectangle to be valid
					if(selectedPositionLon < westLimit || selectedPositionLon > eastLimit
							|| selectedPositionLat < southLimit || selectedPositionLat > northLimit){
						System.out.println("The position is outside of the boundaries, hence it's not valid...");
					} else {
						//remove the previous marker, if existing, and update it
						if (positionMarker!=null){
							mapSelectLocation.removeMapMarker(positionMarker);
						}
						positionMarker = new MapMarkerDot(new Coordinate(selectedPositionLat, selectedPositionLon));
						mapSelectLocation.addMapMarker(positionMarker);
						selectedCoords = positionMarker.getCoordinate();
					}
				}
			}
		});
		return mapViewerTreeSelectLocation;
	}
}
