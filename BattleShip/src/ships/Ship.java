package ships;

import javafx.scene.Parent;
import javafx.scene.image.Image;

public abstract class Ship extends Parent {
    //represents a ship whose specific characteristics are determined by the subclasses
    protected int size;
    protected int shots;
    protected int hit_points;
    protected int sink_points;
    protected int type;
    protected Image image, image2;
    private final int x,y; //the coordinates of ship on the grid

    public Ship(int x,int y){
        this.x = x;
        this.y = y;
    }

    public int getType() {
        return type;
    }
    public Image getImage(){
        return image;
    }
    public Image getImage2(){
        return image2;
    }
    public int getHit_points(){
        return hit_points;
    }
    public int getSink_points(){
        return sink_points;
    }
    public void hit(){
        shots++;
    }
    private boolean is_shot(){
        return shots > 0;
    }
    public boolean is_sunk(){
        return size == shots;
    }
    @Override
    public String toString() {
        return "Ship is" + ((is_sunk())?" sunk":(is_shot())?" shot":" intact");
    }
}
