package classes;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
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
 * The class DeleteAccountBox allows the user to delete its account
 */
class DeleteAccountBox {
	private static boolean answer;
	
	/**
	 * It displays window in which the user can confirm the deletion of the account
	 * @param username the username of the user
	 * @return true if the account has been successfully deleted, false otherwise
	 */
	public static boolean display(String username){
		Stage window = new Stage();
		window.initModality(Modality.APPLICATION_MODAL);
		window.setTitle("Account deletion - confirmation");
		window.setMinWidth(420);
		
		VBox layout = new VBox(10);
		
		Label label = new Label();
		label.setText("In order to DELETE the account, please input your username.\nPlease note that the effects are irreversible.");
		label.setPadding(new Insets(0, 0, 5, 0));
		
		
		HBox hbox = new HBox();
		hbox.setAlignment(Pos.CENTER);
		hbox.setPadding(new Insets(5, 0, 5, 0));
		
		TextField usernameField = new TextField("");
		usernameField.setPromptText("Username");
		usernameField.setMaxWidth(Double.MAX_VALUE);
		HBox.setHgrow(usernameField, Priority.SOMETIMES);
		hbox.getChildren().add(usernameField);
		
		Button deleteButton = new Button("Delete");
		deleteButton.setMinWidth(90);
		deleteButton.setPadding(new Insets(5, 0, 0, 0));
		deleteButton.setMinHeight(20);
		deleteButton.setOnAction(e -> {
			if(usernameField.getText().equals("")){
				AlertBox.display("Error", "In order to delete your account you have to type in username.");
			} else if(usernameField.getText().equals(username)){
				answer = true;
				window.close();
			} else {
				AlertBox.display("Error", "Incorrect username");
			}
			
		});
		window.setOnCloseRequest(e -> {
			e.consume();
			answer = false;
			window.close();
		});
		
		
		layout.getChildren().addAll(label, hbox, deleteButton);
		layout.setAlignment(Pos.CENTER);
		layout.setPadding(new Insets(10));
		layout.setMinHeight(140);
		Scene scene = new Scene(layout);
		window.setScene(scene);
		window.getIcons().add(new Image(ClientGUI2.class.getResourceAsStream("/img/eye.gif")));
		window.showAndWait();
		return answer;
	}

}
