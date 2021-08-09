package classes;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
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
 * Creates a modal pop-up with a message and two buttons for "Yes" and "No"
 */
public class ConfirmBox {
	private static boolean answer;
	
	/**
	 * It displays the modal pop-up
	 * @param title the title to give to the pop-up window
	 * @param message the message to be shown in the pop-up window
	 * @return true if "Yes" has been selected, false if "No" has been selected or if the window has been closed
	 */
	public static boolean display(String title, String message){
		Stage window = new Stage();
		window.initModality(Modality.APPLICATION_MODAL);
		window.setTitle(title);
		window.setMinWidth(250);
		Label label = new Label();
		label.setText(message);
		label.setPadding(new Insets(10, 10, 10, 10));
		
		HBox hbox = new HBox();
		hbox.setAlignment(Pos.CENTER);
		hbox.setPadding(new Insets(10, 30, 10, 30));
		hbox.setSpacing(30);
		//create 2 buttons
		Button yesButton = new Button("Yes");
		yesButton.setMinWidth(90);
		Button noButton = new Button("No");
		noButton.setMinWidth(90);
		yesButton.setOnAction(e -> {
			answer = true;
			window.close();
		});
		noButton.setOnAction(e -> {
			answer = false;
			window.close();
		});
		window.setOnCloseRequest(e -> {
			e.consume();
			answer = false;
			window.close();
		});
		hbox.getChildren().addAll(yesButton, noButton);
		VBox layout = new VBox(10);
		layout.getChildren().addAll(label, hbox);
		layout.setAlignment(Pos.CENTER);
		Scene scene = new Scene(layout);
		window.setScene(scene);
		window.getIcons().add(new Image(ClientGUI2.class.getResourceAsStream("/img/eye.gif")));
		window.showAndWait();
		
		return answer;
	}

}
