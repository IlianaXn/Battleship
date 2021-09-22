package game;

public class Move {
    //this class represents a move of the game and it is defined with the shot cell, the points earned
    //and whether the ship there if existed was sunk or not.
    private final Cell cell;
    private final int points;
    private final boolean sink;

    public Move(Cell cell, int points, boolean sink){
        this.cell = cell;
        this.points = points;
        this.sink = sink;
    }

    @Override
    public String toString() {
        StringBuilder f = new StringBuilder().append(cell).append(", Points: ").append(points);

       if(points != 0) {
           f.append(", Ship: ");
           switch (cell.getShip().getType()) {
               case 0:
                   f.append("Carrier");
                   break;
               case 1:
                   f.append("Battleship");
                   break;
               case 2:
                   f.append("Cruiser");
                   break;
               case 3:
                   f.append("Submarine");
                   break;
               case 4:
                   f.append("Destroyer");
           }
       }
       return f.toString();
    }

    public boolean isSuccess() {
        return (points != 0);
    }

    public boolean isSunk(){ return sink;}

    public Cell getCell() {
        return cell;
    }

    public int getPoints() {
        return points;
    }
}
