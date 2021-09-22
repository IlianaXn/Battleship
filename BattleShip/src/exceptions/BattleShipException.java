package exceptions;

public class BattleShipException extends Exception{
    //this class will be useful as parent class of all the exceptions regarding the game
    public BattleShipException(String error_message){
        super(error_message);
    }
}
