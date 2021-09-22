package game;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class AlertBox{
    //this class represents a popup window as a result of clicking on a button whose purpose is
    //either to start a new session or terminate the current one, and return the decision of the user
    private static boolean answer;
    private static Image clapboard = new Image("game/clapboard.jpg");

    public static boolean handle_exit(String title, String message) {
        Stage window = new Stage();
        window.setResizable(false);
        window.getIcons().add(clapboard);
        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle(title);
        window.setWidth(320);
        window.setHeight(200);

        Label label = new Label(message);
        label.setWrapText(true);

        Button ok_button = new Button("Yes");
        ok_button.setOnAction(e->{answer = true;window.close();});

        Button no_button = new Button("No");
        no_button.setOnAction(e ->{answer = false;window.close();});

        window.setOnCloseRequest(e->{answer = false;window.close();}); //it is assumed that when no button is pressed
                                                                       //the answer is by default negative

        HBox down = new HBox(20,ok_button,no_button);

        down.setAlignment(Pos.CENTER);

        VBox all = new VBox(30,label,down);
        all.setAlignment(Pos.CENTER);
        all.setPadding(new Insets(10, 10, 10, 25));
        Scene scene = new Scene(all,320,200);
        window.setScene(scene);
        window.showAndWait();
        return answer;
    }
}
