package game;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import java.util.Collection;

public class EnumBox{
    //this class represents a popup window which lists in a readable way the items of a collection,
    //which is passed as argument
    private final static Background backGround = new Background(new BackgroundImage(new Image("game/background_sea.jpg"),
            BackgroundRepeat.NO_REPEAT,
            BackgroundRepeat.NO_REPEAT,
            BackgroundPosition.CENTER,
            new BackgroundSize(100, 100, true, true, true, true)));
    private final static Image icon = new Image("game/battleship.jpg");

    public static void display (Collection data, String title, String what_to_show) {
        Stage window = new Stage();
        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle(title);
        window.getIcons().add(icon);
        window.setWidth(350);
        window.setHeight(300);
        window.setResizable(false);

        Label label = new Label(what_to_show);
        label.setTextFill(Color.WHITE);
        label.setWrapText(true);

        VBox all = new VBox(20,label);

        Background for_label= new Background(new BackgroundFill(Color.CYAN,new CornerRadii(5.0), new Insets(-5.0)));

        if(data.isEmpty()){
            Label for_object = new Label("Nothing to show yet.");
            for_object.setBackground(for_label);
            all.getChildren().add(for_object);
        }
        else {
            for (Object x : data) {
                Label for_object = new Label(x.toString());
                for_object.setBackground(for_label);
                all.getChildren().add(for_object);
            }
        }

        all.setAlignment(Pos.CENTER);
        all.setBackground(backGround);
        Scene scene = new Scene(all,350,300);
        window.setScene(scene);
        window.show();

    }
}