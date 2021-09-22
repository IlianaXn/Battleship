package exceptions;

public class OversizeException extends BattleShipException{
    //this exception occurs when a ship is placed on at one cell which is out of the borders of the grid
    public OversizeException(){
        super("Ship out of borders.");
    }
}
