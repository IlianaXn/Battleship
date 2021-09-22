package exceptions;

public class OverlapTilesException extends BattleShipException{
    //this exception occurs when two ships are placed on top of each other for at least one cell
    public OverlapTilesException(){
        super("Ship on top of another ship.");
    }
}
