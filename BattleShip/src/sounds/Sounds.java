package sounds;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import java.io.File;

public class Sounds {
    //several sounds that are used by our application
    public static MediaPlayer water_drop = new MediaPlayer(new Media(new File("src/sounds/water_drop.mp3").toURI().toString()));
    public static MediaPlayer gunshot = new MediaPlayer(new Media(new File("src/sounds/gunshot.mp3").toURI().toString()));
    public static MediaPlayer success = new MediaPlayer(new Media(new File("src/sounds/success.mp3").toURI().toString()));
    public static MediaPlayer failure = new MediaPlayer(new Media(new File("src/sounds/failure.mp3").toURI().toString()));
}
