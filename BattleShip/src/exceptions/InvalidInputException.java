package exceptions;

public class InvalidInputException extends BattleShipException {
    //this exception occurs when there is invalid data in a file
    public InvalidInputException(String error_message){
        super("Invalid Input: " + error_message);
    }
}
