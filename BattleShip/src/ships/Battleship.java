package ships;

import javafx.scene.image.Image;

public class Battleship extends Ship {
    public Battleship(int x, int y) {
        super(x,y);
        size = 4;
        type = 1;
        hit_points = 250;
        sink_points = 500;
        image = new Image("ships/battle.jpg");
        image2 = new Image("ships/battle1.jpg");
    }

    @Override
    public String toString() {
        return "Battleship " + super.toString();
    }
}
