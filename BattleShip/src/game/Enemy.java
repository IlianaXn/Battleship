package game;

import java.util.*;

public class Enemy extends Player {
    //this class represents the enemy, here is the computer, which needs to add some features to the parent
    //player class, in order to simulate the predictions regarding the next moves.

    private boolean success = false, hor = false, pattern = false;
    //indicate if we have a successful move in recent past
    //whether the ship is predicted to be placed horizontal or vertical
    //if we have found a pattern, basically if we have at least two successful moves and already determined direction of ship
    private Cell last; //represents last successfully shot cel;
    private boolean[][] hit = new boolean[10][10];
    //represents the shot or not to be shot cells on the grid
    private Queue<Tuple> possible_next_move  = new ArrayDeque<>();
    //predicted possible next moves based on previous shots

    public Tuple make_move(){
        int x, y;
        if(possible_next_move.isEmpty()) {      //if there is not any predicted moves, pick random
                                                //but ensure it can be shot
            do {
                 x = (int) Math.floor((Math.random()) * 10);
                 y =  (int) Math.floor((Math.random()) * 10);
            } while(hit[x][y]);
            return new Tuple(x,y);
        }
        else return possible_next_move.remove();
    }

    @Override
    public void addMove(Move x){
        super.addMove(x);
        hit[x.getCell().get_Cell_X()][x.getCell().get_Cell_Y()] = true;
        if(x.isSuccess()) {
            if (!pattern) {
                if (success) {
                    pattern = true;
                    hor = (last.get_Cell_X() == x.getCell().get_Cell_X());
                    possible_next_move.clear();
                    //ensure that no adjacent tiles to the ship will be shot in the future
                    if(hor){
                        if(last.get_Cell_X() > 0) {
                            hit[last.get_Cell_X() - 1][last.get_Cell_Y()] = true;
                            hit[x.getCell().get_Cell_X() - 1][x.getCell().get_Cell_Y()] = true;
                        }
                        if(last.get_Cell_X() < 9){
                            hit[last.get_Cell_X() + 1][last.get_Cell_Y()] = true;
                            hit[x.getCell().get_Cell_X() + 1][x.getCell().get_Cell_Y()] = true;
                        }
                        //because the right cell of the previous successful shot will be first examined,
                        //add to the next move the left cell in case the right cell was the second successful shot
                        Collection<Tuple> temp = new ArrayList<>();
                        temp.add(new Tuple(last.get_Cell_X(),last.get_Cell_Y() - 1));
                        add_to_next_moves(temp);
                    }
                    else{
                        if(last.get_Cell_Y() > 0) {
                            hit[last.get_Cell_X()][last.get_Cell_Y() - 1] = true;
                            hit[x.getCell().get_Cell_X()][x.getCell().get_Cell_Y() - 1] = true;
                        }
                        if(last.get_Cell_Y() < 9){
                            hit[last.get_Cell_X()][last.get_Cell_Y() + 1] = true;
                            hit[x.getCell().get_Cell_X()][x.getCell().get_Cell_Y() + 1] = true;
                        }
                        //because the upper cell of the previous successful shot will be first examined,
                        //add to the next move the below cell in case the upper cell was the second successful shot
                        Collection<Tuple> temp = new ArrayList<>();
                        temp.add(new Tuple(last.get_Cell_X() + 1,last.get_Cell_Y()));
                        add_to_next_moves(temp);
                    }
                }
                else {
                    success = true;
                }

            }
            else {
                //ensure that no adjacent tiles to the ship will be shot in the future
                if(hor){
                    if(x.getCell().get_Cell_X() > 0) {
                        hit[x.getCell().get_Cell_X() - 1][x.getCell().get_Cell_Y()] = true;
                    }
                    if(x.getCell().get_Cell_X() < 9){
                        hit[x.getCell().get_Cell_X() + 1][x.getCell().get_Cell_Y()] = true;
                    }
                }
                else{
                    if(x.getCell().get_Cell_Y() > 0) {
                        hit[x.getCell().get_Cell_X()][x.getCell().get_Cell_Y() - 1] = true;
                    }
                    if(x.getCell().get_Cell_Y() < 9){
                        hit[x.getCell().get_Cell_X()][x.getCell().get_Cell_Y() + 1] = true;
                    }
                }
            }
            if(x.isSunk()) {
                //if the ship was sunk, we begin a whole new search for a new cell to shoot
                success = false;
                pattern = false;
                //ensure that no adjacent tiles to the ship will be shot in the future
                neighbours(x.getCell());
                for(Tuple t: possible_next_move){
                    hit[t.getX()][t.getY()] = true;
                }
                possible_next_move.clear();
            }
            else {
                //else determine neighbours based on pattern
                neighbours(x.getCell());
            }
            last = x.getCell();
        }
        else{
            if(possible_next_move.isEmpty()){
                success = false;
                pattern = false;
            }
        }
    }

    private void neighbours(Cell a){
        //returns the neighbours of the cell in order to be our next targets
        //if there isn't a pattern the neighbours are the usual 4 neighbours
        //else they are constrained by the direction of the ship
        int x = a.get_Cell_X();
        int y = a.get_Cell_Y();
        Collection<Tuple> to_add = new ArrayList<>();
        if(!pattern) {
            possible_next_move.clear();
            to_add.add(new Tuple(x - 1, y));
            to_add.add(new Tuple(x + 1, y));
            to_add.add(new Tuple(x, y + 1));
            to_add.add(new Tuple(x, y - 1));
        }
        else{
            if(hor){
                to_add.add(new Tuple(x, y + 1));
                to_add.add(new Tuple(x, y - 1));
            }
            else{
                to_add.add(new Tuple(x - 1, y));
                to_add.add(new Tuple(x + 1, y));
            }
        }
        add_to_next_moves(to_add);
    }

    private void add_to_next_moves(Collection<Tuple> to_add){
        //adds to the possible_next_move the tuples that are inside the grid and also can be shot
        for(Tuple x: to_add)
            if(Board.is_valid(x) && (!hit[x.getX()][x.getY()])) possible_next_move.add(x);
    }

    public boolean done(){
        //returns whether the game is over
        return so_far_moves == 40;
    }
}
