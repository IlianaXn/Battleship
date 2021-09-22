package ships;

import javafx.scene.image.Image;

public class Cruiser extends Ship{
    public Cruiser(int x, int y) {
        super(x,y);
        size = 3;
        type = 2;
        hit_points = 100;
        sink_points = 250;
        image = new Image("ships/cruise.jpg");
        image2 = new Image("ships/cruise1.jpg");
    }

    @Override
    public String toString() {
        return "Cruiser " + super.toString();
    }
}
