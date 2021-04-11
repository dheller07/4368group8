package com.company;

import java.util.Random;

public class RandomPolicy {

    private int i, j, x, a, b, c, d, e, f;
    private Random random_generator;

    //Function carries out one step of PRandom policy based on current world state. Q is not needed for this policy.
    public void pRandom(){
        int rand_int = random_generator.nextInt();
        //pickup block if agent at (3, 5) or (4, 3) AND pickup is valid
        if (pickupValid()){
            pickup();
        }
        //dropoff block if agent at (1, 1), (1, 5), (3, 3), or (5, 5) AND dropoff is valid
        else if (dropoffValid()){
            dropoff();
        }
        //go east or south at random if agent is at (1, 1) but dropoff is not valid
        else if (i == 1 && j == 1){
            if (rand_int % 2 == 0){
                east();
            }
            else{
                south();
            }
        }
        //go south or west at random if agent is at (1, 5) but dropoff is not valid
        else if (i == 1 && j == 5){
            if (rand_int % 2 == 0){
                south();
            }
            else{
                west();
            }
        }
        //go north or east at random if agent is at (5, 1)
        else if (i == 5 && j == 1){
            if (rand_int % 2 == 0){
                north();
            }
            else{
                east();
            }
        }
        //go north or west at random if agent is at (5, 5) but dropoff is not valid
        else if (i == 5 && j == 5){
            if (rand_int % 2 == 0){
                north();
            }
            else{
                west();
            }
        }
        //go east, south, or west at random if agent is at north edge
        else if (i == 1){
            if (rand_int % 3 == 0){
                east();
            }
            else if (rand_int % 3 == 1){
                south();
            }
            else{
                west();
            }
        }
        //go north, east, or west at random if agent is at south edge
        else if (i == 5){
            if (rand_int % 3 == 0){
                north();
            }
            else if (rand_int % 3 == 1){
                east();
            }
            else{
                west();
            }
        }
        //go east, south, or west at random if agent is at west edge
        else if (j == 1){
            if (rand_int % 3 == 0){
                east();
            }
            else if (rand_int % 3 == 1){
                south();
            }
            else{
                west();
            }
        }
        //go north, south, or east at random if agent is at east edge
        else if (j == 5){
            if (rand_int % 3 == 0){
                north();
            }
            else if (rand_int % 3 == 1){
                south();
            }
            else{
                east();
            }
        }
        //go north, east, south, or west at random if agent is not at an edge
        else{
            if (rand_int % 4 == 0){
                north();
            }
            else if (rand_int % 4 == 1){
                east();
            }
            else if (rand_int % 4 == 2){
                south();
            }
            else{
                west();
            }
        }
    }

    //function returns true if dropoff() is a valid operator and false otherwise
    public boolean dropoffValid() {
        if (x == 0){
            return false;
        }
        else{
            if (i == 1 && j == 1){
                if (a < 4){
                    return true;
                }
                else{
                    return false;
                }
            }
            else if (i == 1 && j == 5){
                if (b < 4){
                    return true;
                }
                else{
                    return false;
                }
            }
            else if (i == 3 && j == 3){
                if (c < 4){
                    return true;
                }
                else{
                    return false;
                }
            }
            else if (i == 5 && j == 5){
                if (f < 4){
                    return true;
                }
                else{
                    return false;
                }
            }
            else{
                return false;
            }
        }
    }

    //function returns true if pickup() is a valid operator and false otherwise
    public boolean pickupValid() {
        if (x == 1){
            return false;
        }
        else{
            if (i == 3 && j == 5){
                if (d > 0){
                    return true;
                }
                else{
                    return false;
                }
            }
            else if (i == 4 && j == 2){
                if (e > 0){
                    return true;
                }
                else{
                    return false;
                }
            }
            else{
                return false;
            }
        }
    }

    public void pickup(){
        //TODO: Function stub
    }

    public void dropoff(){
        //TODO: Function stub
    }

    public void north(){
        //TODO: Function stub
    }

    public void south(){
        //TODO: Function stub
    }

    public void east(){
        //TODO: Function stub
    }

    public void west(){
        //TODO: Function stub
    }
}
