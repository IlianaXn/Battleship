package ships;

import javafx.scene.image.Image;

public class Submarine extends Ship{

    public Submarine(int x, int y) {
        super(x,y);
        size = 3;
        hit_points = 100;
        type = 3;
        sink_points = 0;
        image = new Image("ships/sub.png");
        image2 = new Image("ships/sub1.png");
    }

    @Override
    public String toString() {
        return "Submarine "+ super.toString();
    }
}
