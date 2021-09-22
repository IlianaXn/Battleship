package game;

import java.util.ArrayDeque;
import java.util.Queue;

public class Player {
    //this class represents the player of the game and contains their 5 most recent moves, their points
    //their total and successful shots.
    protected Queue<Move> last_five = new ArrayDeque<>();
    protected int so_far_successful, so_far_moves;
    protected int points;

    /**
     * Adds a move made by the player to their collections of moves. This method
     * increments the number of concluded moves, and if the move was successful,
     * it also increments the number of concluded moves with successful outcome
     * and increases the points of the player by the points gained of the specific
     * move. It is ensured that the size of the collection of moves doesn't become
     * greater than 5 by keeping the 5 most recent moves.
     * @param move the move concluded by the player and to be added to their
     *             collection. It is ensured that it is not <code>null</code>
     */
    public void addMove(Move move){
        if(move.isSuccess()) {so_far_successful++; points = points + move.getPoints();}
        so_far_moves++;
        if(last_five.size() == 5){
            last_five.remove();
        }
        last_five.add(move);
    }

    /**
     * Computes the success rate of concluded moves by the player.
     * with accuracy of 2 decimal digits.
     * @return the success rate of concluded moves by the player
     */
    public double getPercentage(){
        double v = (double) so_far_successful / so_far_moves;
        return Math.round(v * 100.0);
    }

    /**
     * Gets the points gained by the player.
     * @return the points gained by the player
     */
    public int getPoints() {
        return points;
    }

    /**
     * Gets the collection of the 5 most recently concluded moves
     * by the player.
     * @return the collection of the 5 most recently concluded moves
     *         by the player
     */
    public Queue<Move> getLast_five() {
        return last_five;
    }
}
