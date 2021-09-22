package exceptions;

public class InvalidCountException extends BattleShipException{
    //this exception occurs when two ships of the same type are place on the grid
    public InvalidCountException(){
        super("At least 2 ships of the same type.");
    }
}
