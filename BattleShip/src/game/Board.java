package game;

import exceptions.*;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.Parent;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import ships.*;
import java.util.*;
import java.util.Collection;

public class Board extends Parent{
    //this class represents the board of a player and contains the grid and its ships
    //and is also defined with the number of alive ships and whether it is the board of the player or the enemy
    private final GridPane grid ;
    private int quantity = 5;
    private Ship[] ships;
    private final boolean enemy;

    public Board(boolean enemy){
        this.enemy = enemy;
        ships = new Ship[5];
        grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(0, 10, 0, 10));

        Cell c;
        for(int i = 1; i<11; i++){
            for(int j = 1;j<11; j++) {
                c = new Cell(i,j);
                grid.add(c, i, j);
                if(enemy) c.setFill(Color.DARKBLUE);
                else c.setFill(Color.LIGHTBLUE);
            }
        }

        Label l;
        for(int i = 1;i<11;i++){
            grid.add(new Label(""+ (i-1)),0,i);
            l = new Label(""+ (i-1));
            grid.add(l ,i,0);
            GridPane.setHalignment(l, javafx.geometry.HPos.CENTER);
        }
        grid.add(new Label(),0,0);
        getChildren().add(grid);
    }

    protected Cell getCell(int x, int y) {
        //returns the cell with the given coordinates
        return (Cell) (grid.getChildren().get(y * 10 + x));
    }

    protected Cell getCell(Tuple ar){
        return getCell(ar.getX(), ar.getY());
    }

    public void addShip(int type, int x, int y, boolean hor) throws BattleShipException{
        //adds a ship on the specific section on the grid
        int[] sizes = new int[]{5,4,3,3,2};
        if(ships[type] != null) throw new InvalidCountException();
        else{
            if(possibleShip(new Tuple(x,y),sizes[type], hor)){
                Ship ship = null;
                switch (type) {
                    case 0:
                        ship = new Carrier(x,y);
                        break;
                    case 1:
                        ship = new Battleship(x,y);
                        break;
                    case 2:
                        ship = new Cruiser(x,y);
                        break;
                    case 3:
                        ship = new Submarine(x,y);
                        break;
                    case 4:
                        ship = new Destroyer(x,y);
                }
                ships[type] = ship;
                for(int i = 0; i < sizes[type]; i++){
                    Cell c;
                    if(hor){
                        c = getCell(x,y+i);
                    }
                    else{
                        c = getCell(x+i,y);
                    }
                    c.setShip(ship);
                    if(!enemy) c.setFill(new ImagePattern(ship.getImage()));
                }
            }

        }
    }

    private boolean possibleShip(Tuple ar, int size, boolean hor) throws BattleShipException{
        //returns whether a ship can be placed in this section of the grid
        if(!is_within_grid(ar,size,hor)) throw new OversizeException();
        else if(is_there_ship(ar,size,hor)) throw new OverlapTilesException();
        else {
            for (Cell f : adjacent(ar,hor,size)) {
                if (f.getShip() != null) {
                    throw new AdjacentTilesException();
                }
            }
        }
        return true;
    }

    public static boolean is_valid(Tuple ar){
        //returns whether the tuple is within the grid or not
        int x = ar.getX();
        int y = ar.getY();
        return (x < 10) && (x > -1) && (y < 10) && (y > -1);
    }

    boolean is_within_grid(Tuple ar, int size, boolean hor){
        //returns whether this section defined with beginning tuple,size and direction is inside the grid
        int x = ar.getX();
        int y = ar.getY();
        if(!hor){
            return !((x < 0) || ((x+size) > 9) || (y < 0) || (y > 9));
        }
        else{
            return !((y < 0) || ((y+size) > 9) || (x < 0) || (x > 9));
        }
    }

    private boolean is_there_ship(Tuple ar, int size, boolean hor){
        //returns whether this section contains a ship
        int x = ar.getX();
        int y = ar.getY();
        for(int i = 0;i < size; i++){
            Cell c;
            if(hor){
                c = getCell(x,y + i);
            }
            else{
                c = getCell(x + i,y);
            }
            if(c.getShip() != null) return true;

        }
        return false;
    }

    private Collection<Cell> adjacent(Tuple ar, boolean hor, int size){
        //returns all the adjacent tiles of a section on the grid
        //this section is defined with its size, whether it is vertical or horizontal
        //and its beginning tile
        Collection<Tuple> temp = new ArrayList<>(); //contains the adjacent tiles regardless the grid
        Collection<Cell> result = new ArrayList<>();//contains only thr valid adjacent tiles
        int x = ar.getX();
        int y = ar.getY();
        temp.add(new Tuple(x-1, y));
        temp.add(new Tuple( x,y-1));
        if(hor){
            temp.add(new Tuple(x+1,y));
            for(int i = 1; i < size; i++ ){
                temp.add(new Tuple(x-1,y+i));
                temp.add(new Tuple(x+1,y+i));
            }
            temp.add(new Tuple(x,y+size));
        }
        else{
            temp.add(new Tuple(x,y+1));
            for(int i = 1; i < size; i++ ){
                temp.add(new Tuple(x+i,y-1));
                temp.add(new Tuple(x+i,y+1));
            }
            temp.add(new Tuple(x+size,y));
        }
        for(Tuple pl: temp){
            if(is_valid(pl)){
                result.add(getCell(pl));
            }
        }
        return result;
    }

    public int hit(Cell x){
        //returns the points gained by shooting the cell
        int points = x.hit();
        if(x.sunk()) {
            quantity -- ;
        }
        return points;
    }

    public boolean sunk(Cell x){
        return x.sunk();
    }

    public int getQuantity(){
        return quantity;
    }

    public Collection<Ship> getShips() {
        return Arrays.asList(ships);
    }

    public boolean defeat(){
        return quantity == 0;
    }

}
