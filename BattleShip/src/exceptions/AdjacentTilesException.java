package exceptions;

public class AdjacentTilesException extends BattleShipException{
    //this exception indicates that two ships are placed next to each other for at least one cell
    public AdjacentTilesException(){
        super("Ship next to another ship.");
    }
}
