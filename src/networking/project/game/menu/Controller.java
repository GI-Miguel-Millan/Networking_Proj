package networking.project.game.menu;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import networking.project.game.Client;
import networking.project.game.Server;

import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

public class Controller implements Initializable {

    @FXML
    private Button button;

    @FXML
    private ChoiceBox<String> choiceBox;

    @FXML
    private TextField textField;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        textField.textProperty().addListener((observable, oldValue, newValue) -> buttonCheck());


        choiceBox.getItems().addAll("Host a Game", "Join a Host", "Server Only");
        choiceBox.valueProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                buttonCheck();

                if (Objects.equals(newValue, "Host a Game")) {
                    button.setText("Play Game!");
                    textField.setPromptText("Number of Players");
                } else if (Objects.equals(newValue, "Join a Host")) {
                    button.setText("Play Game!");
                    textField.setPromptText("Host IP Address");
                } else {
                    button.setText("Start Server!");
                    textField.setPromptText("Number of Players");
                }
            }
        });

        choiceBox.getSelectionModel().selectFirst();
    }

    private void buttonCheck() {
        button.setDisable(textField.getText().isEmpty());
    }

    public void inputCheck() {
        if (choiceBox.valueProperty().getValue().equals("Server Only")) { //simply start the server if input valid
            if (textField.getText().matches("[0-9]*")) {
                new Server(Integer.parseInt(textField.getText())).startServer();
            } else {
                textField.clear();
                return;
            }
            //start the server
        } else if (choiceBox.valueProperty().getValue().equals("Host a Game")) { //starts the server, and a client, if input valid
            if (textField.getText().matches("[0-9]*")) {
                new Server(Integer.parseInt(textField.getText())).startServer();
                try {
                    Thread.sleep(5000);
                }
                catch (Exception ignored0) {}
                new Client("localhost").startClient(); //Connect to self
            } else {
                textField.clear();
                return;
            }
        } else if (choiceBox.valueProperty().getValue().equals("Join a Host")) { //joins a host, if input valid
            try {
                String ip = textField.getText();

                if (ip == null || ip.isEmpty() || ip.endsWith(".")) //If the input is somehow empty or null, or has an extra ., it's no good
                    return;

                String[] octets = ip.split( "\\." ); //Break it up every .
                if (octets.length != 4) //This should give us four numbers, if not, it's no good
                    return;


                for (String s : octets) {
                    int i = Integer.parseInt(s); //Convert to int, if it can be converted
                    if ((i < 0) || (i > 255)) //Make sure each in is a no more/less than what's allowed for an 8 bit number
                        return;
                }

                //IT PASSED ALL THE CHECKS, JOIN THE HOST HERE
                new Client(ip).startClient();
            } catch (NumberFormatException nfe) {
                return; //Failed to convert String to int, so return
            }
        }

        ((Stage) button.getScene().getWindow()).close(); //Assuming all went well, close the window and enjoy the game
    }
}
