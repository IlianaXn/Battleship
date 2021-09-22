package ships;

import javafx.scene.image.Image;

public class Carrier extends Ship {
    public Carrier(int x, int y) {
        super(x,y);
        size = 5;
        type = 0;
        hit_points = 350;
        sink_points = 1000;
        image = new Image("ships/carrier.jpg");
        image2 = new Image("ships/carrier1.jpg");
    }

    @Override
    public String toString() {
        return "Carrier " + super.toString();
    }
}
