package ships;

import javafx.scene.image.Image;

public class Destroyer extends Ship {
    public Destroyer(int x, int y) {
        super(x,y);
        size = 2;
        hit_points = 50;
        type = 4;
        sink_points = 0;
        image = new Image("ships/destroyer.jpg");
        image2 = new Image("ships/destroyer1.jpg");
    }

    @Override
    public String toString() {
        return "Destroyer " + super.toString();
    }
}
