package game;

import javafx.scene.image.Image;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import javafx.scene.paint.Color;
import ships.Ship;
import sounds.Sounds;

public class Cell extends Rectangle {
    //this class represents a tile on the grid which has some characteristics (shot, contained ship, coordinates)
    private final Tuple tile;
    private boolean is_shot = false;
    private Ship ship = null;
    static Image fish  = new Image("game/fish.png");

    public Cell(int x, int y){
        super(30,30);
        tile = new Tuple(y-1,x-1);
        setFill(Color.WHITE);
        setStroke(Color.BLACK);
    }

    public int get_Cell_X(){
        return tile.getX() ;
    }

    public int get_Cell_Y(){
        return tile.getY() ;
    }

    public void setShip(Ship ship) {
        this.ship = ship;
    }

    public Ship getShip(){
        return ship;
    }

    public int hit(){
        //shooting a cell can have two outcomes based on the existence of a ship there
        //if there is not a ship, points gained are zero
        if(ship == null){
            is_shot = true;
            Sounds.water_drop.play();
            Sounds.water_drop.seek(Sounds.water_drop.getStartTime());
            setFill(new ImagePattern(fish));
            return 0;
        }
        //if there is ship, points gained are the hit points of the ship plus the sink points if it was also sunk
        else{
            is_shot = true;
            ship.hit();
            Sounds.gunshot.play();
            Sounds.gunshot.seek(Sounds.gunshot.getStartTime());
            setFill(new ImagePattern(ship.getImage2()));
            if(ship.is_sunk())
                return ship.getSink_points() + ship.getHit_points();
            else
                return ship.getHit_points();
        }
    }

    public boolean is_shot(){
        return is_shot;
    }

    public boolean sunk(){
        return ship != null && ship.is_sunk();
    }

    @Override
    public String toString() {
        return "Cell(" + (tile.getX()) + ", " + (tile.getY()) + ")";
    }
}
